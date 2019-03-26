package org.excavator.grpc

class ProductServiceRpc extends ProductGrpc.ProductImplBase{

  def createResponse(result: Result) = {
    ProductReviewResponse.newBuilder().setStatus(result).build()
  }

  override def createOrUpdateReview(request: _root_.org.excavator.grpc.ProductReviewRequest, responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.org.excavator.grpc.ProductReviewResponse]): Unit = {

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
}