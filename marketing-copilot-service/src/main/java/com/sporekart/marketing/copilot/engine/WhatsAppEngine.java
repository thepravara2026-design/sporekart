package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.WhatsAppMessage;
import com.sporekart.marketing.copilot.dto.WhatsAppRequest;
import com.sporekart.marketing.copilot.dto.WhatsAppResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WhatsAppEngine {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppEngine.class);

    public WhatsAppResponse generateMessage(WhatsAppRequest request) {
        log.info("Generating WhatsApp message type: {} content: {}", request.messageType(), request.content());
        var messageId = UUID.randomUUID().toString();
        var templateName = request.messageType().toLowerCase() + "_template";
        var body = formatMessage(request);
        return new WhatsAppResponse(messageId, templateName, body, request.messageType(),
            request.recipientSegment() != null ? request.recipientSegment() : "all");
    }

    public WhatsAppMessage createWhatsAppMessage(WhatsAppRequest request) {
        log.debug("Creating WhatsApp message record");
        return new WhatsAppMessage(UUID.randomUUID().toString(),
            request.messageType().toLowerCase() + "_template",
            List.of(request.content()),
            formatMessage(request),
            null,
            WhatsAppMessage.MessageType.valueOf(request.messageType().toUpperCase()),
            request.recipientSegment() != null ? request.recipientSegment() : "all",
            LocalDateTime.now().plusDays(1), request.campaignId());
    }

    public List<String> personalizeMessage(String customerName, String content) {
        log.debug("Personalizing WhatsApp message for: {}", customerName);
        return List.of(
            "Hello " + customerName + "! " + content,
            "Hi " + customerName + ", " + content.toLowerCase(),
            "Dear " + customerName + ", " + content
        );
    }

    public List<String> getTemplateSuggestions(String messageType) {
        log.debug("Getting template suggestions for type: {}", messageType);
        return switch (messageType.toLowerCase()) {
            case "promotional" -> List.of("offer_announcement", "new_arrival", "flash_sale");
            case "transactional" -> List.of("order_confirmation", "shipping_update", "delivery_notification");
            case "educational" -> List.of("tip_of_the_day", "how_to_guide", "product_care");
            case "reengagement" -> List.of("we_miss_you", "special_offer_back", "new_for_you");
            default -> List.of("general_update", "announcement");
        };
    }

    private String formatMessage(WhatsAppRequest request) {
        return "[" + request.messageType().toUpperCase() + "] " + request.content()
            + (request.callToAction() != null ? "\n\n" + request.callToAction() : "")
            + "\n\n- SporeKart Team";
    }
}
