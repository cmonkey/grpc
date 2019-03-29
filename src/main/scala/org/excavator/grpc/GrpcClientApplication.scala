package org.excavator.grpc

import java.util.concurrent.atomic.AtomicInteger

import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory

class GrpcClientApplication(host: String, port: Int) {

  val logger = LoggerFactory.getLogger(classOf[GrpcClientApplication])
  val atomicInteger = new AtomicInteger(0)

  var blockingClient: ProductGrpc.ProductBlockingStub = _
  var client: ProductGrpc.ProductStub = _

  connect()

  def connect() = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext.build
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit =
        channel.shutdownNow()
    })
    blockingClient = ProductGrpc.newBlockingStub(channel)
    client = ProductGrpc.newStub(channel)
  }

  def createReview(request: ProductReviewRequest) = {
    val response = blockingClient.createOrUpdateReview(request)

    logger.info(s"createReview response = ${response}")

    response
  }

  def responseStream(request: ProductReviewRequest) = {
    client.getResponse(request, new StreamObserver[ProductReviewResponse] {
      override def onNext(value: ProductReviewResponse): Unit = {
        logger.info(s"next response = ${value.getStatus.name()}")
        atomicInteger.incrementAndGet()
      }

      override def onCompleted(): Unit = {
        logger.info(s"stream onCompleted and count = ${atomicInteger.getAndSet(0)}")
      }

      override def onError(t: Throwable): Unit = {
        logger.error(s"stream error = ${t}")
      }
    })
  }

}
