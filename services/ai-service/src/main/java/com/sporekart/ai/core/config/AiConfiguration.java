package com.sporekart.ai.core.config;

import com.sporekart.ai.core.domain.TimestampProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    TimestampProvider timestampProvider() {
        return TimestampProvider.system();
    }
}
