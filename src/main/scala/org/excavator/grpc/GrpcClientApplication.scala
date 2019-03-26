package org.excavator.grpc

import io.grpc.{ManagedChannelBuilder}
import org.slf4j.LoggerFactory

class GrpcClientApplication(host: String, port: Int) {

  val logger = LoggerFactory.getLogger(classOf[GrpcClientApplication])

  var client: ProductGrpc.ProductBlockingStub = _

  connect()

  def connect() = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext.build
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit =
        channel.shutdownNow()
    })
    client = ProductGrpc.newBlockingStub(channel)
  }

  def createReview(request: ProductReviewRequest) = {
    val response = client.createOrUpdateReview(request)

    logger.info(s"createReview response = ${response}")

    response
  }

}
