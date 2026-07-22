package com.sporekart.memory.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class WorkspaceFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        var workspace = httpRequest.getHeader("X-Workspace");
        if (workspace == null || workspace.isBlank()) {
            workspace = "default";
        }

        log.debug("Request workspace: {}", workspace);
        httpRequest.setAttribute("workspace", workspace);

        var tenantId = httpRequest.getHeader("X-Tenant-Id");
        if (tenantId != null) {
            httpRequest.setAttribute("tenantId", tenantId);
        }

        chain.doFilter(request, response);
    }
}
