package com.gateway.bugreport.bugcloudgateway2724.grpc;


import com.gateway.bugreport.ReactorSimpleGrpcServiceGrpc;
import com.gateway.bugreport.SimpleRequestMessage;
import com.gateway.bugreport.SimpleResponseMessage;
import org.lognet.springboot.grpc.GRpcService;
import reactor.core.publisher.Mono;

import static java.util.UUID.randomUUID;

@GRpcService
public class SimpleGrpcService extends ReactorSimpleGrpcServiceGrpc.SimpleGrpcServiceImplBase {

    @Override
    public Mono<SimpleResponseMessage> simpleGrpcRequest(Mono<SimpleRequestMessage> request) {
        return Mono.just(SimpleResponseMessage.newBuilder()
                .setId(randomUUID().toString())
                .setMessage("Pong")
                .build());
    }
}
