package com.sporekart.prompt;

import com.sporekart.prompt.events.PromptEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
class PromptServiceApplicationTest {

    @Autowired
    private ApplicationContext context;

    @TestConfiguration
    static class TestMockConfig {
        @Bean
        @Primary
        public PromptEventPublisher promptEventPublisher() {
            return mock(PromptEventPublisher.class);
        }
    }

    @Test
    void contextLoads() {
        assertThat(context).isNotNull();
    }

    @Test
    void applicationStartsWithExpectedBeans() {
        assertThat(context.containsBean("promptService")).isTrue();
        assertThat(context.containsBean("promptController")).isTrue();
        assertThat(context.containsBean("promptVersionService")).isTrue();
        assertThat(context.containsBean("previewEngine")).isTrue();
        assertThat(context.containsBean("tokenEstimator")).isTrue();
        assertThat(context.containsBean("promptServiceHealthIndicator")).isTrue();
    }
}
