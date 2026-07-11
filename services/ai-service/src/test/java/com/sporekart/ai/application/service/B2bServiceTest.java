package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.Dealer;
import com.sporekart.ai.domain.model.Quotation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class B2bServiceTest {

    @Test
    void shouldRegisterDealerAndCreateQuotation() {
        B2bService service = new B2bService();

        Dealer dealer = service.registerDealer("Dealer One", "GST123");
        Quotation quotation = service.createQuotation("Quote-1", dealer.getId());

        assertThat(dealer.getName()).isEqualTo("Dealer One");
        assertThat(quotation.getReference()).isEqualTo("Quote-1");
    }
}
