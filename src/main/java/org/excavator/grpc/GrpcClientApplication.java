package org.excavator.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClientApplication {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 53000)
                .usePlaintext().build();

        ProductGrpc.ProductBlockingStub client = ProductGrpc.newBlockingStub(channel);

        ProductReviewRequest review = ProductReviewRequest.newBuilder()
                .setReview("cmonkey")
                .setProductId("0123456789")
                .setReviewerEmail("42.codemonkey at gmail.com")
                .setFiveStarRating(5)
                .build();

        ProductReviewResponse result = client.createOrUpdateReview(review);

        System.out.println("result of posing review ; " + result.getStatus().name());

        ProductReviewRequest badLangReview = ProductReviewRequest.newBuilder()
                .setReview("F*ck product")
                .setProductId("012345678")
                .setReviewerEmail("42.codemonkey at gmail.com")
                .setFiveStarRating(5).build();

        result = client.createOrUpdateReview(badLangReview);

        System.out.println("result of posting review: " + result.getStatus().name());

        ProductReviewRequest invalidRatingReview = ProductReviewRequest.newBuilder()
                .setReview("Bad product!")
                .setProductId("EAN132069854")
                .setReviewerEmail("42.codemonkey at gmail.com")
                .setFiveStarRating(-5)
                .build();

        result = client.createOrUpdateReview(invalidRatingReview);

        System.out.println("Result of posting review: " + result.getStatus().name());
    }
}
