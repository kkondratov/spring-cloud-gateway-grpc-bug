package com.gateway.bugreport.bugcloudgateway2724.http;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;

public class ResponseDecorator extends ServerHttpResponseDecorator {

    public ResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }
}
