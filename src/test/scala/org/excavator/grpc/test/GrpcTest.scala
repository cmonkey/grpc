package org.excavator.grpc.test

import org.excavator.grpc.{GrpcClientApplication, GrpcServerApplication, ProductReviewRequest, Result}
import org.junit.jupiter.api.{DisplayName, Test}
import org.junit.jupiter.api.Assertions.assertTrue
import org.slf4j.LoggerFactory

class GrpcTest {
  val logger = LoggerFactory.getLogger(classOf[GrpcTest])

  @Test
  @DisplayName("testGrpcServer")
  def testGrpcServer() = {
    val host = "localhost"
    val port = 53000
    val server = new GrpcServerApplication
    server.start(port)

    val client = new GrpcClientApplication(host, port)

    var request = ProductReviewRequest.newBuilder
      .setReview("cmonkey")
      .setProductId("0123456789")
      .setReviewerEmail("42.codemonkey at gmail.com")
      .setFiveStarRating(5)
      .build

    var response = client.createReview(request)
    logger.info(s"response = ${response}")
    assertTrue(Result.OK == response.getStatus)

    request = ProductReviewRequest.newBuilder
      .setReview("F*ck product")
      .setProductId("012345678")
      .setReviewerEmail("42.codemonkey at gmail.com")
      .setFiveStarRating(5)
      .build

    response = client.createReview(request)
    logger.info(s"response = ${response}")
    assertTrue(Result.FAILED_BAD_LANGUAGE == response.getStatus)

    request = ProductReviewRequest.newBuilder
      .setReview("Bad product!")
      .setProductId("EAN132069854")
      .setReviewerEmail("42.codemonkey at gmail.com")
      .setFiveStarRating(-5)
      .build

    response = client.createReview(request)
    logger.info(s"response = ${response}")
    assertTrue(Result.FAILED_INVALID_SCORE == response.getStatus)
  }
}
