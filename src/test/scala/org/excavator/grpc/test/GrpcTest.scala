package org.excavator.grpc.test

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import org.excavator.grpc.{GrpcClientApplication, GrpcServerApplication, ProductReviewRequest, Result}
import org.junit.jupiter.api._
import org.junit.jupiter.api.Assertions.assertTrue
import org.slf4j.LoggerFactory

class GrpcTest {
  val logger = LoggerFactory.getLogger(classOf[GrpcTest])

  @Test
  @DisplayName("testProductByOK")
  @RepeatedTest(1000)
  def testProductByOK() = {

    var request = ProductReviewRequest.newBuilder
      .setReview("cmonkey")
      .setProductId("0123456789")
      .setReviewerEmail("42.codemonkey at gmail.com")
      .setFiveStarRating(5)
      .build

    var response = GrpcTest.client.createReview(request)
    logger.info(s"request = ${request} response = ${response}")
    assertTrue(Result.OK == response.getStatus)
  }

  @Test
  @DisplayName("testProductByFailed_bad_language")
  @RepeatedTest(1000)
  def testProductByFailed_bad_language() = {

    val request = ProductReviewRequest.newBuilder
      .setReview("F*ck product")
      .setProductId("012345678")
      .setReviewerEmail("42.codemonkey at gmail.com")
      .setFiveStarRating(5)
      .build

    val response = GrpcTest.client.createReview(request)
    logger.info(s"request = ${request} response = ${response}")
    assertTrue(Result.FAILED_BAD_LANGUAGE == response.getStatus)
  }

  @Test
  @DisplayName("testProductByFailed_invalid_score")
  @RepeatedTest(1000)
  def testProductByFailed_invalid_socre() = {

    val request = ProductReviewRequest.newBuilder
      .setReview("Bad product!")
      .setProductId("EAN132069854")
      .setReviewerEmail("42.codemonkey at gmail.com")
      .setFiveStarRating(-5)
      .build

    val response = GrpcTest.client.createReview(request)
    logger.info(s"request = ${request} response = ${response}")
    assertTrue(Result.FAILED_INVALID_SCORE == response.getStatus)
  }

  @Test
  @DisplayName("testStream")
  @RepeatedTest(1000)
  def testStream() = {
    val fiveStarRating = 100

    val request = ProductReviewRequest.newBuilder()
      .setReview("stream product")
      .setProductId("1234567")
      .setReviewerEmail("42.codemonkey at gmail.com")
      .setFiveStarRating(fiveStarRating)
      .build()

    val atomicInteger = new AtomicInteger(0)
    GrpcTest.client.responseStream(request, atomicInteger)
    TimeUnit.SECONDS.sleep(10)

    logger.info(s"testStream next responseStream count = ${atomicInteger.get()}")
    assertTrue(fiveStarRating < atomicInteger.get())
  }
}

object GrpcTest {
  var client: GrpcClientApplication = null

  @BeforeAll
  def initServer() = {
    val host = "localhost"
    val port = 53000
    val server = new GrpcServerApplication
    server.start(port)

    client = new GrpcClientApplication(host, port)
  }

}
