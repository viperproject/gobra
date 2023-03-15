// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.frontend

import viper.gobra.frontend.TaskManagerMode.{Lazy, Parallel, Sequential, TaskManagerMode}
import viper.gobra.util.{GobraExecutionContext, Violation}

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.collection.mutable
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

class TaskManager[K, R](mode: TaskManagerMode) {
  private val jobs: ConcurrentMap[K, Job[R]] = new ConcurrentHashMap()

  /**
    * returns true if job has been inserted and thus was previously absent
    */
  def addIfAbsent(id: K, job: Job[R], insertOnly: Boolean = false)(executionContext: GobraExecutionContext): Boolean = {
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
        case Parallel => Future{ job.call() }(executionContext)
      }
    }
    isAbsent
  }

  def getFuture(id: K): Future[R] = {
    val job = jobs.get(id)
    Violation.violation(job != null, s"Task $id not found")
    job.getFuture
  }

  def getAllFutures: Iterable[(K, Future[R])] =
    jobs.asScala.toVector.map { case (key, job) => (key, job.getFuture) }

  def getResult(id: K): R = {
    val job = jobs.get(id)
    Violation.violation(job != null, s"Task $id not found")
    getResultFromJob(job)
  }

  def getAllResults(executionContext: GobraExecutionContext): Iterable[R] = mode match {
    case Lazy | Sequential => jobs.values().asScala.map(getResultFromJob)
    case Parallel =>
      val futs = jobs.values().asScala.map(_.getFuture)
      implicit val executor: GobraExecutionContext = executionContext
      Await.result(Future.sequence(futs), Duration.Inf)
  }

  def getAllResultsWithKeys(executionContext: GobraExecutionContext): Iterable[(K, R)] = mode match {
    case Lazy | Sequential => jobs.asScala.toVector.map { case (key, job) => (key, getResultFromJob(job)) }
    case Parallel =>
      implicit val executor: GobraExecutionContext = executionContext
      val futs = jobs.asScala.toVector.map { case (key, job) => job.getFuture.map(res => (key, res)) }
      Await.result(Future.sequence(futs), Duration.Inf)
  }

  private def getResultFromJob(job: Job[R]): R = mode match {
    case Lazy => job.call() // we perform the computation now that we need the result
    case Sequential =>
      // note that we cannot await the future here as type-checking of this package might not have started yet.
      // Thus, we use `.call()` that either returns a previously calculated type-checking result or will calculate it.
      job.call()
    case Parallel =>
      Await.result(job.getFuture, Duration.Inf)
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

trait DependentTask[K, R1, R2] {
  def id: K
  // private var compututationStarted = false
  private val promise1: Promise[R1] = Promise()
  def getFuture1: Future[R1] = promise1.future
  private val promise2: Promise[R2] = Promise()
  def getFuture2: Future[R2] = promise2.future
  protected def compute1(): R1
  def dependencies: Set[K]
  protected def compute2(phase1Res: R1, dependentResults: Map[K, R2]): R2

  def setup(manager: DependentTaskManager[K, R1, R2]): Unit /*= {
    dependencies.map(id => manager.addIfAbsent(id, jobFactory))
  }*/
  /*
  private def runIfNotYetRunning[T](promise: Promise[T], runnable: () => T)(executionContext: GobraExecutionContext): Future[T] = {
    promise.future.value match {
      case Some(Success(res)) => return res // return already computed type-checker result
      case Some(Failure(exception)) => Violation.violation(s"Job resulted in exception: $exception")
      case _ =>
    }
    // Violation.violation(!compututationStarted, s"Job $this is already on-going")
    // compututationStarted = true
    val res = try {
      val res = runnable()
      promise.success(res)
      res
    } catch {
      case e: Exception =>
        promise.failure(e)
        // propagate this exception for the case that `call` is executed synchronously:
        throw e
    }
    res
  }*/

  def phase1()(executionContext: GobraExecutionContext): Future[R1] = {
    Future{
      try {
        val res = compute1()
        promise1.success(res)
        res
      } catch {
        case e: Exception =>
          promise1.failure(e)
          // propagate this exception for the case that `call` is executed synchronously:
          throw e
      }
    }(executionContext)
  }

  private object phase2Lock
  private var phase2HasBeenExecuted: Boolean = false
  def phase2(manager: DependentTaskManager[K, R1, R2])(executionContext: GobraExecutionContext): Future[R2] = {
    phase2Lock.synchronized {
      if (phase2HasBeenExecuted) {
        return getFuture2
      }
      phase2HasBeenExecuted = true
      println(s"computing phase 2 for $this")
      val dependentJobs = dependencies.map(manager.getJob)
      implicit val executor: GobraExecutionContext = executionContext
      // start phase2 of each dependent job
      val futs = dependentJobs.map(job => {
        val fut = job.phase2(manager)(executionContext)
        fut.map(res => (job.id, res))
      })
      Future.sequence(futs)
        .map(dependentResults => {
          println("dependentResults are ready")
          try {
            val res = compute2(getFuture1.value.get.get, dependentResults.toMap)
            promise2.success(res)
            res
          } catch {
            case e: Exception =>
              promise2.failure(e)
              // propagate this exception for the case that `call` is executed synchronously:
              throw e
          }
        })
    }
  }
  /*
  def execute(manager: DependentTaskManager[K, R])(executionContext: GobraExecutionContext): Future[R] = {
    val jobFuts = dependencies
      .map(manager.getJob)
      .map(job =>
        job.execute(manager)(executionContext)
          .map((job.id, _))(executionContext)
      )
    implicit val executor: GobraExecutionContext = executionContext
    Future.sequence(jobFuts)
      .map(results => call(results.toMap))
  }

  private def call(dependentResults: Map[K, R]): R = {

  }
   */
}

class DependentTaskManager[K, R1, R2]() {
  // private val jobs: ConcurrentMap[K, DependentTask[K, R]] = new ConcurrentHashMap()
  private val jobs: mutable.Set[DependentTask[K, R1, R2]] = mutable.Set.empty

  private def hasJob(jobId: K): Boolean = jobs.map(_.id).contains(jobId)

  def getJob(jobId: K): DependentTask[K, R1, R2] = jobs.collectFirst { case job if job.id == jobId => job }.get

  /** returns true if job has been absent */
  // def addIfAbsent(jobId: K, jobFactory: K => DependentTask[K, R1, R2]): Boolean = {
  def addIfAbsent(job: DependentTask[K, R1, R2]): Boolean = {
    if (hasJob(job.id)) {
      false
    } else {
      // val job = jobFactory(jobId)
      jobs.add(job)
      job.setup(this)
      true
    }
  }

  def execute(/*rootId: K, jobFactory: K => DependentTask[K, R1, R2]*/)(executionContext: GobraExecutionContext): Map[K, R2] = {
    /*
    Violation.violation(hasJob(rootId), s"Job with ID $rootId not found - we expect that this job has been installed with the task manager before")
    val job = getJob(rootId)
    val res = job.execute(this)(executionContext)
    // we await this one job but since this is the root job, alle other results should be available afterwards too
    Await.result(res, Duration.Inf)
    implicit val executor: GobraExecutionContext = executionContext
    val futs = jobs.map(job => job.getFuture.map(res => (job.id, res)))
    Await.result(Future.sequence(futs), Duration.Inf).toMap
     */
    /*
    Violation.violation(hasJob(rootId), s"Job with ID $rootId not found - we expect that this job has been installed with the task manager before")
    val job = getJob(rootId)
    job.setup(this, jobFactory)
     */
    implicit val executor: GobraExecutionContext = executionContext
    val phase1Futs = jobs.map(_.phase1()(executionContext))
    Await.result(Future.sequence(phase1Futs), Duration.Inf)
    println("phase 1 done")
    val phase2Futs = jobs.map(job => job.phase2(this)(executionContext).map(res => (job.id, res)))
    val res = Await.result(Future.sequence(phase2Futs), Duration.Inf).toMap
    println("phase 2 done")
    res
  }
}
