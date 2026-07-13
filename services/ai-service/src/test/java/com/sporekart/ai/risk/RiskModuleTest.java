package com.sporekart.ai.risk;

import com.sporekart.ai.AiServiceApplication;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = AiServiceApplication.class)
@ActiveProfiles("test")
@Tag("module")
class RiskModuleTest {

    @Test
    void contextLoads() {
    }
}
