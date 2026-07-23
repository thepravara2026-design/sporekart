package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.domain.EmailCampaign;
import com.sporekart.marketing.copilot.dto.EmailRequest;
import com.sporekart.marketing.copilot.dto.EmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EmailEngine {

    private static final Logger log = LoggerFactory.getLogger(EmailEngine.class);

    public EmailResponse generateEmail(EmailRequest request) {
        log.info("Generating email: {} type: {}", request.subject(), request.emailType());
        var emailId = UUID.randomUUID().toString();
        var preheader = generatePreheader(request);
        var bodyHtml = generateHtmlBody(request);
        var bodyText = generateTextBody(request);
        var recommendations = List.of(
            "Keep subject line under 50 characters",
            "Include personalized content based on segments",
            "Add a clear primary CTA above the fold",
            "Test on mobile devices before sending",
            "Segment audience for better engagement"
        );
        return new EmailResponse(emailId, request.subject(), preheader, bodyHtml, bodyText,
            request.emailType(), recommendations);
    }

    public EmailCampaign createEmailCampaign(EmailRequest request) {
        log.debug("Creating email campaign record: {}", request.subject());
        return new EmailCampaign(UUID.randomUUID().toString(), request.subject(),
            generatePreheader(request), generateHtmlBody(request), generateTextBody(request),
            request.targetSegment() != null ? List.of(request.targetSegment()) : List.of("all"),
            EmailCampaign.EmailType.valueOf(request.emailType().toUpperCase()),
            "SporeKart Marketing", "marketing@sporekart.com", "reply@sporekart.com",
            null, List.of("header", "hero", "body", "cta", "footer"), null);
    }

    public List<String> optimizeSubjectLine(String topic, String audience) {
        log.info("Optimizing subject lines for topic: {} audience: {}", topic, audience);
        return List.of(
            "Discover the Power of " + topic,
            "Your Ultimate Guide to " + topic,
            topic + ": What Every " + audience + " Should Know",
            "Exclusive: " + topic + " Insights Just for You",
            "5 Things You Didn't Know About " + topic
        );
    }

    public List<String> segmentAudience(String campaignType) {
        log.debug("Segmenting audience for campaign type: {}", campaignType);
        return switch (campaignType.toLowerCase()) {
            case "promotional" -> List.of("active_buyers", "cart_abandoners", "window_shoppers");
            case "educational" -> List.of("beginners", "intermediate", "advanced");
            case "reengagement" -> List.of("inactive_30d", "inactive_60d", "inactive_90d");
            case "welcome" -> List.of("new_signups", "first_purchase");
            default -> List.of("all_subscribers");
        };
    }

    private String generatePreheader(EmailRequest request) {
        return "Your guide to " + request.subject().toLowerCase() + " - brought to you by SporeKart";
    }

    private String generateHtmlBody(EmailRequest request) {
        var tone = request.tone() != null ? request.tone() : "professional";
        return """
            <html><body>
            <h1>%s</h1>
            <p>Dear SporeKart Customer,</p>
            <p>We're excited to share our latest %s content with you. %s</p>
            <p>%s</p>
            <p><a href="#">Click here</a> to learn more.</p>
            <p>Best regards,<br/>SporeKart Team</p>
            </body></html>
            """.formatted(request.subject(), request.emailType(), request.callToAction() != null ? request.callToAction() : "", "This " + tone + " email was generated for " + request.subject());
    }

    private String generateTextBody(EmailRequest request) {
        return "Subject: " + request.subject() + "\n\n"
            + "Dear SporeKart Customer,\n\n"
            + "We're excited to share our latest " + request.emailType() + " content with you.\n\n"
            + (request.callToAction() != null ? request.callToAction() + "\n\n" : "")
            + "Visit SporeKart for more information.\n\n"
            + "Best regards,\nSporeKart Team";
    }
}
