package com.sporekart.customer;

import com.sporekart.customer.copilot.CustomerCopilotApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CustomerCopilotApplication.class)
@ActiveProfiles("test")
class CustomerCopilotApplicationTest {

    @Test
    void contextLoads() {
    }
}
