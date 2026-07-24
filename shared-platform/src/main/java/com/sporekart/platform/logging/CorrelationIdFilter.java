package com.sporekart.platform.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        LoggingContext.initialize();

        String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        LoggingContext.setCorrelationId(correlationId);

        String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
        if (requestId != null) {
            LoggingContext.setTraceId(requestId);
        }

        httpResponse.setHeader(CORRELATION_ID_HEADER, LoggingContext.getCorrelationId());
        httpResponse.setHeader(REQUEST_ID_HEADER, LoggingContext.getRequestId());

        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.debug("Request {} {} completed in {}ms",
                    httpRequest.getMethod(), httpRequest.getRequestURI(), duration);
            LoggingContext.clear();
        }
    }
}
