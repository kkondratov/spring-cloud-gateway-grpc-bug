package com.gateway.bugreport.bugcloudgateway2724.http;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            final var customDecorator = new CustomWebExchangeDecorator(exchange);

            return chain.filter(customDecorator);
        };
    }

    public static class Config {
    }
}
