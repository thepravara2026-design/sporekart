package com.sporekart.prompt.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class KafkaConfig {

    @Bean
    public NewTopic promptEventsTopic() {
        return new NewTopic("prompt-events", 1, (short) 1);
    }
}
