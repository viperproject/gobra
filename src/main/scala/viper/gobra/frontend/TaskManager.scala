package viper.gobra.frontend

import viper.gobra.frontend.info.Info.TypeCheckMode
import viper.gobra.util.{GobraExecutionContext, Violation}

import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}
import scala.jdk.CollectionConverters._

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
    Violation.violation(!compututationStarted, s"Job is already on-going")
    compututationStarted = true
    val res = compute()
    promise.success(res)
    res
  }
}

class TaskManager[K, R](config: Config) {
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
      config.typeCheckMode match {
        case TypeCheckMode.Lazy => // don't do anything as of now
        case TypeCheckMode.Sequential => job.call()
        case TypeCheckMode.Parallel => Future{ job.call() }(executionContext)
      }
    }
    isAbsent
  }

  def getResult(id: K): R = {
    val job = jobs.get(id)
    Violation.violation(job != null, s"Task $id not found")
    getResultFromJob(job)
  }

  def getAllResults: Iterable[R] =
    jobs.values().asScala.map(getResultFromJob)

  def getAllResultsWithKeys: Iterable[(K, R)] =
    jobs.asScala.toVector.map { case (key, job) => (key, getResultFromJob(job)) }

  private def getResultFromJob(job: Job[R]): R = config.typeCheckMode match {
    case TypeCheckMode.Lazy => job.call() // we perform the computation now that we need the result
    case TypeCheckMode.Sequential =>
      // note that we cannot await the future here as type-checking of this package might not have started yet.
      // Thus, we use `.call()` that either returns a previously calculated type-checking result or will calculate it.
      job.call()
    case TypeCheckMode.Parallel => Await.result(job.getFuture, Duration.Inf)
  }
}
