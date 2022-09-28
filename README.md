# spring-cloud-gateway-grpc-bug

This repository is an example that describes a bug in spring-cloud-gateway when working with custom filters that implement `ServerHttpResponseDecorator`


## Project structure
This project contains three main modules:
- Root: spring-cloud-gateway
- grpc-server
- grpc-client

### spring-cloud-gateway
This is an implementation of gateway to reproduce the buggy behaviour.
The gateway is configured to route traffic for GRPC requests based on the header to localhost:8082
The gateway has two spring profiles: 
- `filter` this profile will apply a custom filter which reproduces the bug
- `no-filter` this profile will apply the same routing without the custom filter where the bug is not present

### gRPC server
Simple gRPC server that servers one endpoint to pong a request

### gRPC client
Simple gRPC client with two configs:
- `direct` this profile will directly do calls to the gRPC server
- `gateway` this profile will call the gRPC server via the gateway.

The client is a command line runner, so it does a single request and terminates


## How to reproduce the bug

1. Build the project
`./gradlew clean build`

2. Start the gateway and the grpc server
`./gradlew :bootRunWithFilter`
`./gradlew :grpc-server:bootRun`

3. Run the client with direct profile to validate grpc server running:
`./gradlew :grpc-client:bootRunDirect`. You should see a log with `message: "Pong"`

4. Run the client with gateway profile to display the bug
`./gradlew :grpc-client:bootRunGateway`. This request will fail and will log the error in the gateway boot run logs.

5. Validate that this only occurs with the filter: 
Rerun `./gradlew :bootRunNoFilter`. This will start the application without the filter and route it to the grpc-server.
Validate with `./gradlew :grpc-client:bootRunGateway`

   
## Bug Explanation
The issue arises when a gRPC call is routed through gateway. 
The `GRPCResponseHeadersFilter` will try to unwrap the response and cast it to `AbstractServerHttpResponse`.
This will fail due to the fact that the underlying response being an instance of `ServerHttpResponseDecorator`.

The ideal solution is: GRPCResponseHeadersFilter being unwrapped until the actual response is found just as in `NettyRoutingFilter`.
Or instance of checks to get the native response from the Decorator.