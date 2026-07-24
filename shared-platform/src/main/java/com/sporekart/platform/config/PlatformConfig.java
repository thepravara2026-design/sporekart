package com.sporekart.platform.config;

import com.sporekart.platform.error.GlobalExceptionHandler;
import com.sporekart.platform.logging.CorrelationIdFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GlobalExceptionHandler.class, CorrelationIdFilter.class})
public class PlatformConfig {

    @Bean
    @ConditionalOnMissingBean
    public PlatformConfig platformConfig() {
        return new PlatformConfig();
    }
}
