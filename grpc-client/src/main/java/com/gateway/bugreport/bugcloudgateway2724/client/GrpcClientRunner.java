package com.gateway.bugreport.bugcloudgateway2724.client;

import com.gateway.bugreport.ReactorSimpleGrpcServiceGrpc;
import com.gateway.bugreport.SimpleRequestMessage;
import com.gateway.bugreport.bugcloudgateway2724.BugCloudGatewayGrpcClient;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
@EnableConfigurationProperties(GrpcClientProperties.class)
public class GrpcClientRunner implements CommandLineRunner {

    private static final Logger LOG = LogManager.getLogger(BugCloudGatewayGrpcClient.class);

    private final GrpcClientProperties properties;

    private final Resource certChain;

    private final Resource privateKey;

    public GrpcClientRunner(GrpcClientProperties properties,
                            @Value("${classpath:localhost.crt}") Resource certChain,
                            @Value("${classpath:localhost.key}") Resource privateKey) {
        this.properties = properties;
        this.certChain = certChain;
        this.privateKey = privateKey;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Making request to host {} and port {}", properties.host(), properties.port());

        final var channelBuilder = ManagedChannelBuilder.forAddress(properties.host(), properties.port());
        final var channel = ((NettyChannelBuilder)channelBuilder)
                .useTransportSecurity()
                .sslContext(GrpcSslContexts.forClient()
                        .trustManager(certChain.getFile())
                        .keyManager(certChain.getFile(), privateKey.getFile())
                        .build())
                .build();

        final var client = ReactorSimpleGrpcServiceGrpc.newReactorStub(channel);

        final var response = client.simpleGrpcRequest(SimpleRequestMessage.newBuilder()
                .setId(randomUUID().toString())
                .setMessage("Ping")
                .build()).block();

        LOG.info("Response: {}", response);

    }
}
