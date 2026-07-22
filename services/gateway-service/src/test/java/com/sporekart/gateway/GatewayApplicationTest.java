package com.sporekart.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class GatewayApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void gatewayBeanExists() {
        assertThat(context.containsBean("gatewayConfig")).isTrue();
    }

    @Test
    void serviceRegistryExists() {
        assertThat(context.containsBean("serviceRegistry")).isTrue();
    }

    @Test
    void authenticationFilterExists() {
        assertThat(context.containsBean("authenticationFilter")).isTrue();
    }

    @Test
    void authorizationFilterExists() {
        assertThat(context.containsBean("authorizationFilter")).isTrue();
    }

    @Test
    void correlationIdFilterExists() {
        assertThat(context.containsBean("correlationIdFilter")).isTrue();
    }

    @Test
    void loggingFilterExists() {
        assertThat(context.containsBean("loggingFilter")).isTrue();
    }

    @Test
    void metricsRecorderExists() {
        assertThat(context.containsBean("metricsRecorder")).isTrue();
    }

    @Test
    void aggregatedHealthIndicatorExists() {
        assertThat(context.containsBean("aggregatedHealthIndicator")).isTrue();
    }

    @Test
    void configValidatorExists() {
        assertThat(context.containsBean("configValidator")).isTrue();
    }

    @Test
    void gatewayBootstrapperExists() {
        assertThat(context.containsBean("gatewayBootstrapper")).isTrue();
    }
}
