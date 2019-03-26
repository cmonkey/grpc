package org.excavator.grpc

import io.grpc.{ManagedChannel, ManagedChannelBuilder}

class GrpcClientApplication {

  def start() = {
    val channel = ManagedChannelBuilder.forAddress("localhost", 53000).usePlaintext.build

    val client = ProductGrpc.newBlockingStub(channel)

    val review = ProductReviewRequest.newBuilder.setReview("cmonkey").setProductId("0123456789").setReviewerEmail("42.codemonkey at gmail.com").setFiveStarRating(5).build

    var result = client.createOrUpdateReview(review)

    System.out.println("result of posing review ; " + result.getStatus.name)

    val badLangReview = ProductReviewRequest.newBuilder.setReview("F*ck product").setProductId("012345678").setReviewerEmail("42.codemonkey at gmail.com").setFiveStarRating(5).build

    result = client.createOrUpdateReview(badLangReview)

    System.out.println("result of posting review: " + result.getStatus.name)

    val invalidRatingReview = ProductReviewRequest.newBuilder.setReview("Bad product!").setProductId("EAN132069854").setReviewerEmail("42.codemonkey at gmail.com").setFiveStarRating(-5).build

    result = client.createOrUpdateReview(invalidRatingReview)

    System.out.println("Result of posting review: " + result.getStatus.name)
  }

}
