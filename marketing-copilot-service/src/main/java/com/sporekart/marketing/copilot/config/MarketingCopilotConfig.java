package com.sporekart.marketing.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sporekart.marketing.copilot")
public class MarketingCopilotConfig {

    private int streamingTimeoutSeconds = 300;
    private String defaultAudience = "home-growers,commercial-growers,enthusiasts";
    private String brandVoice = "professional,creative,data-driven,persuasive,business-oriented";
    private String brandGuidelinesPath = "/etc/sporekart/brand-guidelines";
    private String seoDefaultLocale = "en_IN";
    private List<String> socialPlatforms = List.of("instagram", "facebook", "linkedin", "youtube", "threads", "pinterest", "twitter");
    private String emailFromName = "SporeKart Marketing";
    private String emailFromAddress = "marketing@sporekart.com";
    private String whatsappBusinessNumber = "+919999999999";

    public int getStreamingTimeoutSeconds() { return streamingTimeoutSeconds; }
    public void setStreamingTimeoutSeconds(int streamingTimeoutSeconds) { this.streamingTimeoutSeconds = streamingTimeoutSeconds; }
    public String getDefaultAudience() { return defaultAudience; }
    public void setDefaultAudience(String defaultAudience) { this.defaultAudience = defaultAudience; }
    public String getBrandVoice() { return brandVoice; }
    public void setBrandVoice(String brandVoice) { this.brandVoice = brandVoice; }
    public String getBrandGuidelinesPath() { return brandGuidelinesPath; }
    public void setBrandGuidelinesPath(String brandGuidelinesPath) { this.brandGuidelinesPath = brandGuidelinesPath; }
    public String getSeoDefaultLocale() { return seoDefaultLocale; }
    public void setSeoDefaultLocale(String seoDefaultLocale) { this.seoDefaultLocale = seoDefaultLocale; }
    public List<String> getSocialPlatforms() { return socialPlatforms; }
    public void setSocialPlatforms(List<String> socialPlatforms) { this.socialPlatforms = socialPlatforms; }
    public String getEmailFromName() { return emailFromName; }
    public void setEmailFromName(String emailFromName) { this.emailFromName = emailFromName; }
    public String getEmailFromAddress() { return emailFromAddress; }
    public void setEmailFromAddress(String emailFromAddress) { this.emailFromAddress = emailFromAddress; }
    public String getWhatsappBusinessNumber() { return whatsappBusinessNumber; }
    public void setWhatsappBusinessNumber(String whatsappBusinessNumber) { this.whatsappBusinessNumber = whatsappBusinessNumber; }
}
