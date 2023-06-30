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

trait Job[I, R] {
  private lazy val precomputationResult: I = sequentialPrecompute()
  private var compututationStarted = false
  private val promise: Promise[R] = Promise()
  def getFuture: Future[R] = promise.future

  protected def sequentialPrecompute(): I
  protected def compute(precomputationResult: I): R

  def triggerPrecomputation(): I = {
    precomputationResult
  }

  def execute(): R = {
    getFuture.value match {
      case Some(Success(res)) => return res // return already computed type-checker result
      case Some(Failure(exception)) => Violation.violation(s"Job resulted in exception: $exception")
      case _ =>
    }
    Violation.violation(!compututationStarted, s"Job $this is already on-going")
    compututationStarted = true
    try {
      val res = compute(precomputationResult)
      promise.success(res)
      res
    } catch {
      case e: Exception =>
        promise.failure(e)
        // propagate this exception for the case that `call` is executed synchronously:
        throw e
    }
  }
}

class TaskManager[K, I, R](mode: TaskManagerMode)(implicit executor: GobraExecutionContext) {
  private val jobs: ConcurrentMap[K, Job[I, R]] = new ConcurrentHashMap()

  def addIfAbsent(id: K, job: Job[I, R]): Unit = {
    var isAbsent = false
    jobs.computeIfAbsent(id, _ => {
      isAbsent = true
      job
    })
    if (isAbsent) {
      job.triggerPrecomputation()
      mode match {
        case Sequential => job.execute()
        case Lazy => // don't do anything as of now
        case Parallel => Future{ job.execute() }
      }
    }
  }

  def getResult(id: K): Future[R] = {
    val job = jobs.get(id)
    Violation.violation(job != null, s"Task $id not found")
    mode match {
      case Lazy => job.execute() // now we need the job's result
      case _ =>
    }
    job.getFuture
  }

  def getResultBlocking(id: K): R = {
    Await.result(getResult(id), Duration.Inf)
  }

  def getAllResultsWithKeys: Future[Map[K, R]] = {
    val futs = jobs.asScala.toVector.map { case (key, job) =>
      mode match {
        case Lazy => job.execute() // now we need the job's result
        case _ =>
      }
      job.getFuture.map(res => (key, res))
    }
    Future.sequence(futs).map(_.toMap)
  }
}
