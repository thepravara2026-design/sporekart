package com.sporekart.memory.retrieval;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ContextBuilderTest {

    @Autowired
    private ContextBuilder contextBuilder;

    @Test
    void shouldBuildEmptyContext() {
        var context = contextBuilder.buildContext("test", List.of());
        assertThat(context.memories()).isEmpty();
        assertThat(context.totalResults()).isZero();
    }

    @Test
    void shouldFormatEmptyPrompt() {
        var prompt = contextBuilder.formatForPrompt(List.of(), 2000);
        assertThat(prompt).isBlank();
    }
}
