// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.frontend

import viper.gobra.frontend.TaskManagerMode.{Lazy, Parallel, Sequential, TaskManagerMode}
import viper.gobra.util.{GobraExecutionContext, Violation}

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}
import scala.jdk.CollectionConverters._

object TaskManagerMode extends Enumeration {
  type TaskManagerMode = Value
  val Lazy, Sequential, Parallel = Value
}

trait Job[R] {
  private var compututationStarted = false
  private val promise: Promise[R] = Promise()
  def getFuture: Future[R] = promise.future
  protected def compute(): R

  def call(): R = {
    getFuture.value match {
      case Some(Success(res)) => return res // return already computed type-checker result
      case Some(Failure(exception)) => Violation.violation(s"Job resulted in exception: $exception")
      case _ =>
    }
    Violation.violation(!compututationStarted, s"Job $this is already on-going")
    compututationStarted = true
    val res = try {
      val res = compute()
      promise.success(res)
      res
    } catch {
      case e: Exception =>
        promise.failure(e)
        // propagate this exception for the case that `call` is executed synchronously:
        throw e
    }
    res
  }
}

trait ParallelJob[R] {
  /*
  protected def compute(): Future[R]
  private var compututationStarted: Option[Future[R]] = None
  def call(): Future[R] = {
    compututationStarted.getOrElse {
      val fut = compute()
      compututationStarted = Some(fut)
      fut
    }
  }
  */

  private var compututationStarted = false
  private val promise: Promise[R] = Promise()
  def getFuture: Future[R] = promise.future
  protected def compute(): Future[R]

  def call(executionContext: GobraExecutionContext): Future[R] = {
    getFuture.value match {
      case Some(Success(res)) => return Future.successful(res) // return already computed type-checker result
      case Some(Failure(exception)) => Violation.violation(s"Job resulted in exception: $exception")
      case _ =>
    }
    Violation.violation(!compututationStarted, s"Job $this is already on-going")
    compututationStarted = true
    compute()
      .map(res => {
        promise.success(res)
        res
      })(executionContext)
  }
}

class TaskManager[K, R](mode: TaskManagerMode) {
  require(mode != Parallel)

  private val jobs: ConcurrentMap[K, Job[R]] = new ConcurrentHashMap()

  /**
    * returns true if job has been inserted and thus was previously absent
    */
  def addIfAbsent(id: K, job: Job[R], insertOnly: Boolean = false): Unit = {
    var isAbsent = false
    // first insert job, then run it (if necessary)
    jobs.computeIfAbsent(id, _ => {
      isAbsent = true
      job
    })
    // now run it but only if it's a new job:
    if (isAbsent && !insertOnly) {
      mode match {
        case Lazy => // don't do anything as of now
        case Sequential => job.call()
      }
    }
  }
  /*
  def getFuture(id: K): Future[R] = {
    val job = jobs.get(id)
    Violation.violation(job != null, s"Task $id not found")
    job.getFuture
  }

  def getAllFutures: Iterable[(K, Future[R])] =
    jobs.asScala.toVector.map { case (key, job) => (key, job.getFuture) }
  */
  def getResult(id: K): R = {
    val job = jobs.get(id)
    Violation.violation(job != null, s"Task $id not found")
    getResultFromJob(job)
  }
  /*
  def getAllResults(executionContext: GobraExecutionContext): Iterable[R] = mode match {
    case Lazy | Sequential => jobs.values().asScala.map(getResultFromJob)
    case Parallel =>
      val futs = jobs.values().asScala.map(_.getFuture)
      implicit val executor: GobraExecutionContext = executionContext
      Await.result(Future.sequence(futs), Duration.Inf)
  }
   */
  def getAllResults: Iterable[R] =
    jobs.values().asScala.map(getResultFromJob)
  /*
  def getAllResultsWithKeys(executionContext: GobraExecutionContext): Iterable[(K, R)] = mode match {
    case Lazy | Sequential => jobs.asScala.toVector.map { case (key, job) => (key, getResultFromJob(job)) }
    case Parallel =>
      implicit val executor: GobraExecutionContext = executionContext
      val futs = jobs.asScala.toVector.map { case (key, job) => job.getFuture.map(res => (key, res)) }
      Await.result(Future.sequence(futs), Duration.Inf)
  }
   */
  def getAllResultsWithKeys: Iterable[(K, R)] =
    jobs.asScala.toVector.map { case (key, job) => (key, getResultFromJob(job)) }

  private def getResultFromJob(job: Job[R]): R = mode match {
    case Lazy => job.call() // we perform the computation now that we need the result
    case Sequential =>
      // note that we cannot await the future here as type-checking of this package might not have started yet.
      // Thus, we use `.call()` that either returns a previously calculated type-checking result or will calculate it.
      job.call()
    // case Parallel =>
    //   Await.result(job.getFuture, Duration.Inf)
  }
}

class ParallelTaskManager[K, R] {
  private val jobs: ConcurrentMap[K, ParallelJob[R]] = new ConcurrentHashMap()

  /**
    * returns true if job has been inserted and thus was previously absent
    */
  def addIfAbsent(id: K, job: ParallelJob[R], insertOnly: Boolean = false)(executionContext: GobraExecutionContext): Future[R] = {
    var isAbsent = false
    // first insert job, then run it (if necessary)
    val res = jobs.computeIfAbsent(id, _ => {
      isAbsent = true
      job
    })
    // now run it but only if it's a new job:
    if (isAbsent && !insertOnly) {
      job.call(executionContext)
    }
    // return the future (either of the inserted job or the already existing job with the same id)
    res.getFuture
  }

  def getAllResultsWithKeys(executionContext: GobraExecutionContext): Iterable[(K, R)] = {
      implicit val executor: GobraExecutionContext = executionContext
      val futs = jobs.asScala.toVector.map { case (key, job) => job.getFuture.map(res => (key, res)) }
      Await.result(Future.sequence(futs), Duration.Inf)
  }
}

class FutureManager[K, R]() {
  private val futures: ConcurrentMap[K, Future[R]] = new ConcurrentHashMap()

  def addIfAbsent(id: K, futFn: () => Future[R]): Future[R] = {
    // first insert job, then run it (if necessary)
    futures.computeIfAbsent(id, _ => futFn())
  }

  def getFuture(id: K): Future[R] = {
    val fut = futures.get(id)
    Violation.violation(fut != null, s"Task $id not found")
    fut
  }

  def getAllResults(executionContext: GobraExecutionContext): Iterable[R] = {
    implicit val executor: GobraExecutionContext = executionContext
    Await.result(Future.sequence(futures.values().asScala), Duration.Inf)
  }
}
