package com.sporekart.memory.consolidation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ConsolidationServiceTest {

    @Autowired
    private ConsolidationService consolidationService;

    @Test
    void shouldReturnNullWhenNoCandidates() {
        var result = consolidationService.mergeByTopic("nonexistent-topic", "default");
        assertThat(result).isNull();
    }
}
