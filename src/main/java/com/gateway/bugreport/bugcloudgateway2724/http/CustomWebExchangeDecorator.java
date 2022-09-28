package com.gateway.bugreport.bugcloudgateway2724.http;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class CustomWebExchangeDecorator extends ServerWebExchangeDecorator {

    protected CustomWebExchangeDecorator(ServerWebExchange delegate) {
        super(delegate);
    }

    @NotNull
    @Override
    public ServerHttpResponse getResponse() {
        return new ResponseDecorator(getDelegate().getResponse());
    }
}
