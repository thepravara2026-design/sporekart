package com.sporekart.platform.observability.tracing;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(0)
public class TraceFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TraceFilter.class);
    private static final String SERVICE_NAME = System.getProperty("spring.application.name", "unknown");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Map<String, String> headers = extractHeaders(request);
        TraceContext ctx = TraceContext.createFromHeaders(headers);
        ctx.setServiceName(SERVICE_NAME);

        Map<String, String> mdc = ctx.toLoggingContext();
        mdc.forEach(MDC::put);

        response.setHeader("X-Trace-Id", ctx.getTraceId());
        response.setHeader("X-Span-Id", ctx.getSpanId());
        response.setHeader("X-Correlation-Id", ctx.getCorrelationId());
        response.setHeader("X-Request-Id", ctx.getRequestId());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
            TraceContext.clear();
        }
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        String[] traceHeaders = {
            "X-Trace-Id", "X-Span-Id", "X-Correlation-Id", "X-Request-Id",
            "X-Parent-Span-Id", "X-Workspace-Id", "X-User-Id"
        };
        for (String header : traceHeaders) {
            String value = request.getHeader(header);
            if (value != null) {
                headers.put(header, value);
            }
        }
        return headers;
    }
}
