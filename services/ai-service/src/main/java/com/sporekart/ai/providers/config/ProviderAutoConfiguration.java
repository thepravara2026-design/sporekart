package com.sporekart.ai.providers.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProviderConfigProperties.class)
@ComponentScan(basePackages = "com.sporekart.ai.providers")
public class ProviderAutoConfiguration {
}
