package org.excavator.grpc;

import io.grpc.stub.StreamObserver;

/**
 * @author cmonkey
 */
public class ProductService extends ProductGrpc.ProductImplBase{

    @Override
    public void createOrUpdateReview(ProductReviewRequest request, StreamObserver<ProductReviewResponse> responseObserver) {
        if(request.getFiveStarRating() < 0 || request.getFiveStarRating() > 5){
            responseObserver.onNext(createResponse(Result.FAILED_INVALID_SCORE));
        }else if (request.getReview().contains("F*ck")){
            responseObserver.onNext(createResponse(Result.FAILED_BAD_LANGUAGE));
        }else{
            responseObserver.onNext(createResponse(Result.OK));
        }

        responseObserver.onCompleted();
    }

    private ProductReviewResponse createResponse(Result failedInvalidScore) {

        return ProductReviewResponse.newBuilder()
                .setStatus(failedInvalidScore).build();
    }
}
