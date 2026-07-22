package com.sporekart.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sporekart.gateway")
public class GatewayConfig {

    private boolean enabled = true;
    private CorsConfig cors = new CorsConfig();
    private SecurityConfig security = new SecurityConfig();
    private RateLimiterConfig rateLimiter = new RateLimiterConfig();
    private RoutingConfig routing = new RoutingConfig();
    private SecurityHeadersConfig securityHeaders = new SecurityHeadersConfig();
    private ObservabilityConfig observability = new ObservabilityConfig();
    private Map<String, ServiceConfig> services = Map.of();

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public CorsConfig getCors() { return cors; }
    public void setCors(CorsConfig cors) { this.cors = cors; }
    public SecurityConfig getSecurity() { return security; }
    public void setSecurity(SecurityConfig security) { this.security = security; }
    public RateLimiterConfig getRateLimiter() { return rateLimiter; }
    public void setRateLimiter(RateLimiterConfig rateLimiter) { this.rateLimiter = rateLimiter; }
    public RoutingConfig getRouting() { return routing; }
    public void setRouting(RoutingConfig routing) { this.routing = routing; }
    public SecurityHeadersConfig getSecurityHeaders() { return securityHeaders; }
    public void setSecurityHeaders(SecurityHeadersConfig securityHeaders) { this.securityHeaders = securityHeaders; }
    public ObservabilityConfig getObservability() { return observability; }
    public void setObservability(ObservabilityConfig observability) { this.observability = observability; }
    public Map<String, ServiceConfig> getServices() { return services; }
    public void setServices(Map<String, ServiceConfig> services) { this.services = services; }

    public static class CorsConfig {
        private String allowedOrigins = "*";
        private String allowedMethods = "GET,POST,PUT,DELETE,PATCH,OPTIONS";
        private String allowedHeaders = "*";
        private long maxAge = 3600;

        public String getAllowedOrigins() { return allowedOrigins; }
        public void setAllowedOrigins(String v) { this.allowedOrigins = v; }
        public String getAllowedMethods() { return allowedMethods; }
        public void setAllowedMethods(String v) { this.allowedMethods = v; }
        public String getAllowedHeaders() { return allowedHeaders; }
        public void setAllowedHeaders(String v) { this.allowedHeaders = v; }
        public long getMaxAge() { return maxAge; }
        public void setMaxAge(long v) { this.maxAge = v; }
    }

    public static class SecurityConfig {
        private boolean jwtEnabled = true;
        private boolean requireAuthentication = true;
        private List<String> publicPaths = new ArrayList<>();
        private String jwtSecret;
        private String jwkSetUri;

        public boolean isJwtEnabled() { return jwtEnabled; }
        public void setJwtEnabled(boolean v) { this.jwtEnabled = v; }
        public boolean isRequireAuthentication() { return requireAuthentication; }
        public void setRequireAuthentication(boolean v) { this.requireAuthentication = v; }
        public List<String> getPublicPaths() { return publicPaths; }
        public void setPublicPaths(List<String> v) { this.publicPaths = v; }
        public String getJwtSecret() { return jwtSecret; }
        public void setJwtSecret(String v) { this.jwtSecret = v; }
        public String getJwkSetUri() { return jwkSetUri; }
        public void setJwkSetUri(String v) { this.jwkSetUri = v; }
    }

    public static class RateLimiterConfig {
        private boolean enabled = true;
        private int defaultPermitPerSecond = 100;
        private int defaultBurstCapacity = 200;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean v) { this.enabled = v; }
        public int getDefaultPermitPerSecond() { return defaultPermitPerSecond; }
        public void setDefaultPermitPerSecond(int v) { this.defaultPermitPerSecond = v; }
        public int getDefaultBurstCapacity() { return defaultBurstCapacity; }
        public void setDefaultBurstCapacity(int v) { this.defaultBurstCapacity = v; }
    }

    public static class RoutingConfig {
        private String serviceTimeout = "30s";
        private int maxHeaderSize = 8192;
        private String maxRequestSize = "10MB";

        public String getServiceTimeout() { return serviceTimeout; }
        public void setServiceTimeout(String v) { this.serviceTimeout = v; }
        public int getMaxHeaderSize() { return maxHeaderSize; }
        public void setMaxHeaderSize(int v) { this.maxHeaderSize = v; }
        public String getMaxRequestSize() { return maxRequestSize; }
        public void setMaxRequestSize(String v) { this.maxRequestSize = v; }
    }

    public static class SecurityHeadersConfig {
        private boolean hstsEnabled = true;
        private long hstsMaxAge = 31536000;
        private boolean hstsIncludeSubdomains = true;
        private boolean cspEnabled = true;
        private String cspPolicy = "default-src 'self'";
        private boolean xssProtection = true;
        private boolean contentTypeOptions = true;
        private String frameOptions = "DENY";

        public boolean isHstsEnabled() { return hstsEnabled; }
        public void setHstsEnabled(boolean v) { this.hstsEnabled = v; }
        public long getHstsMaxAge() { return hstsMaxAge; }
        public void setHstsMaxAge(long v) { this.hstsMaxAge = v; }
        public boolean isHstsIncludeSubdomains() { return hstsIncludeSubdomains; }
        public void setHstsIncludeSubdomains(boolean v) { this.hstsIncludeSubdomains = v; }
        public boolean isCspEnabled() { return cspEnabled; }
        public void setCspEnabled(boolean v) { this.cspEnabled = v; }
        public String getCspPolicy() { return cspPolicy; }
        public void setCspPolicy(String v) { this.cspPolicy = v; }
        public boolean isXssProtection() { return xssProtection; }
        public void setXssProtection(boolean v) { this.xssProtection = v; }
        public boolean isContentTypeOptions() { return contentTypeOptions; }
        public void setContentTypeOptions(boolean v) { this.contentTypeOptions = v; }
        public String getFrameOptions() { return frameOptions; }
        public void setFrameOptions(String v) { this.frameOptions = v; }
    }

    public static class ObservabilityConfig {
        private boolean metricsEnabled = true;
        private boolean tracingEnabled = true;
        private boolean auditEnabled = true;

        public boolean isMetricsEnabled() { return metricsEnabled; }
        public void setMetricsEnabled(boolean v) { this.metricsEnabled = v; }
        public boolean isTracingEnabled() { return tracingEnabled; }
        public void setTracingEnabled(boolean v) { this.tracingEnabled = v; }
        public boolean isAuditEnabled() { return auditEnabled; }
        public void setAuditEnabled(boolean v) { this.auditEnabled = v; }
    }

    public static class ServiceConfig {
        private String url;
        private String healthPath = "/actuator/health";
        private String timeout = "10s";
        private List<String> roles = List.of();

        public String getUrl() { return url; }
        public void setUrl(String v) { this.url = v; }
        public String getHealthPath() { return healthPath; }
        public void setHealthPath(String v) { this.healthPath = v; }
        public String getTimeout() { return timeout; }
        public void setTimeout(String v) { this.timeout = v; }
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> v) { this.roles = v; }
    }
}
