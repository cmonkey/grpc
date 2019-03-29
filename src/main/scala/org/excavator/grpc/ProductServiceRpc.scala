package org.excavator.grpc

import java.util.concurrent.{Executors, ThreadFactory, TimeUnit}

import org.slf4j.LoggerFactory

class ProductServiceRpc extends ProductGrpc.ProductImplBase{
  val logger = LoggerFactory.getLogger(classOf[ProductServiceRpc])

  def createResponse(result: Result) = {
    ProductReviewResponse.newBuilder().setStatus(result).build()
  }

  override def createOrUpdateReview(request: _root_.org.excavator.grpc.ProductReviewRequest, responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.org.excavator.grpc.ProductReviewResponse]): Unit = {
    logger.info(s"createOrUpdateReview param request = ${request}")

    val fiveStarRating = request.getFiveStarRating

    val review = request.getReview

    if(fiveStarRating < 0 || fiveStarRating > 5){
      responseObserver.onNext(createResponse(Result.FAILED_INVALID_SCORE))
    }else if (review.contains("F*ck")){
      responseObserver.onNext(createResponse(Result.FAILED_BAD_LANGUAGE))
    }else{
      responseObserver.onNext(createResponse(Result.OK))
    }

    responseObserver.onCompleted()
  }

  override def getResponse(request: _root_.org.excavator.grpc.ProductReviewRequest, responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.org.excavator.grpc.ProductReviewResponse]): Unit = {
    logger.info(s"getResponse param request = ${request}")

    val fiveStarRating = request.getFiveStarRating

    for(i <- 0 until fiveStarRating){
      responseObserver.onNext(createResponse(Result.OK))
    }
    Executors.newSingleThreadScheduledExecutor((r: Runnable) => {
      val thread = new Thread(r, "schedule-next-responseStream")
      thread.setDaemon(true)
      thread
    }).scheduleWithFixedDelay(() => {
      logger.info(s"thread name = ${Thread.currentThread().getName}")
      responseObserver.onNext(createResponse(Result.OK))
    }, 1, 1, TimeUnit.MICROSECONDS)

    //responseObserver.onCompleted()

  }
}