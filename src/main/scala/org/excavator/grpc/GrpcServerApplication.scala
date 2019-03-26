package org.excavator.grpc

import io.grpc.{Server, ServerBuilder}

class GrpcServerApplication {

  def start() = {

    val p = new ProductServiceRpc()

    val server = ServerBuilder.forPort(53000)
      .addService(p)
      .build()
      .start()

    addHook(server)
    server.awaitTermination()
  }

  private def addHook(server: Server) = {
    Runtime.getRuntime.addShutdownHook(new Thread(){
      override def run(): Unit = {
        server.shutdownNow()
      }
    })
  }

}
