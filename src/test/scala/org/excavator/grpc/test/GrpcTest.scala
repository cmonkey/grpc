package org.excavator.grpc.test

import org.excavator.grpc.{GrpcClientApplication, GrpcServerApplication}
import org.junit.Test

class GrpcTest {

  @Test
  @DisplayName("testGrpcServer")
  def testGrpcServer() = {
    val server = new GrpcServerApplication
    server.start()

    val client = new GrpcClientApplication
    client.start()
  }
}
