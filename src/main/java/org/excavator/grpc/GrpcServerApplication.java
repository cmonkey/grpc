package org.excavator.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServerApplication {

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(53000)
                .addService(new ProductService())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        server.awaitTermination();
    }
}
