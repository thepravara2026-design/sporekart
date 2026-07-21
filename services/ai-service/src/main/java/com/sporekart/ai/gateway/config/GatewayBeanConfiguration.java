package com.sporekart.ai.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GatewayConfigProperties.class)
@ComponentScan(basePackages = "com.sporekart.ai.gateway")
public class GatewayBeanConfiguration {
}
