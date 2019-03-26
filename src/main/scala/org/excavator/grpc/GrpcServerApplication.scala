package org.excavator.grpc

import java.util.concurrent.TimeUnit

import io.grpc.{Server, ServerBuilder}

class GrpcServerApplication {

  def start(port: Int) = {

    val p = new ProductServiceRpc()

    val server = ServerBuilder.forPort(port)
      .addService(p)
      .build()
      .start()

    addHook(server)
    server.awaitTermination(10, TimeUnit.SECONDS)
  }

  private def addHook(server: Server) = {
    Runtime.getRuntime.addShutdownHook(new Thread(){
      override def run(): Unit = {
        server.shutdownNow()
      }
    })
  }

}
