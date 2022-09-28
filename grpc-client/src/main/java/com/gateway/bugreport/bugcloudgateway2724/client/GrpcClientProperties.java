package com.gateway.bugreport.bugcloudgateway2724.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "grpc.client")
public record GrpcClientProperties(String host, int port) {
}
