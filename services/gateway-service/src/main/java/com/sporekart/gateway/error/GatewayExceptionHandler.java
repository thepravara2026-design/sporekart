package com.sporekart.gateway.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionHandler.class);

    private final ObjectMapper objectMapper;

    public GatewayExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var request = exchange.getRequest();
        var response = exchange.getResponse();

        HttpStatus status;
        String errorCode;
        String detail;

        if (ex instanceof GatewayException ge) {
            status = ge.getHttpStatus();
            errorCode = ge.getErrorCode();
            detail = ge.getMessage();
        } else if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            errorCode = "HTTP_" + status.value();
            detail = rse.getReason();
        } else if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            errorCode = "SERVICE_NOT_FOUND";
            detail = "The requested service was not found";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode = "INTERNAL_ERROR";
            detail = "An unexpected error occurred";
        }

        if (status.is5xxServerError()) {
            log.error("Gateway error [{}] {} {}: {}", errorCode, request.getMethod(), request.getURI().getPath(), detail, ex);
        } else {
            log.warn("Gateway error [{}] {} {}: {}", errorCode, request.getMethod(), request.getURI().getPath(), detail);
        }

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        var problem = ProblemDetails.from(status, errorCode, detail, request.getURI().getPath());

        try {
            var json = objectMapper.writeValueAsBytes(problem);
            var buffer = response.bufferFactory().wrap(json);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Failed to serialize error response", e);
            return response.setComplete();
        }
    }
}
