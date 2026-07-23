package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.WhatsAppRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WhatsAppEngineTest {

    @InjectMocks
    private WhatsAppEngine whatsAppEngine;

    @Test
    void testGenerateMessage() {
        var request = new WhatsAppRequest("text", "Check out our new mushroom kit!", "all", null, "Buy Now");
        var response = whatsAppEngine.generateMessage(request);
        assertNotNull(response);
        assertNotNull(response.messageId());
        assertEquals("text_template", response.templateName());
        assertTrue(response.body().contains("Check out our new mushroom kit!"));
    }

    @Test
    void testGenerateMessagePromotional() {
        var request = new WhatsAppRequest("promotional", "50% off on all spore kits", "active_buyers", "camp-1", "Shop Now");
        var response = whatsAppEngine.generateMessage(request);
        assertNotNull(response);
        assertEquals("promotional_template", response.templateName());
    }

    @Test
    void testCreateWhatsAppMessage() {
        var request = new WhatsAppRequest("text", "Hello!", "all", "camp-1", null);
        var message = whatsAppEngine.createWhatsAppMessage(request);
        assertNotNull(message);
        assertNotNull(message.id());
        assertEquals("text_template", message.templateName());
    }

    @Test
    void testPersonalizeMessage() {
        var messages = whatsAppEngine.personalizeMessage("John", "Welcome to SporeKart!");
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).contains("John"));
    }

    @Test
    void testGetTemplateSuggestionsForPromotional() {
        var templates = whatsAppEngine.getTemplateSuggestions("promotional");
        assertTrue(templates.contains("offer_announcement"));
    }

    @Test
    void testGetTemplateSuggestionsForTransactional() {
        var templates = whatsAppEngine.getTemplateSuggestions("transactional");
        assertTrue(templates.contains("order_confirmation"));
    }

    @Test
    void testGetTemplateSuggestionsForReengagement() {
        var templates = whatsAppEngine.getTemplateSuggestions("reengagement");
        assertTrue(templates.contains("we_miss_you"));
    }

    @Test
    void testGetTemplateSuggestionsDefault() {
        var templates = whatsAppEngine.getTemplateSuggestions("unknown");
        assertEquals(2, templates.size());
    }
}
