package com.sporekart.marketing.copilot.dto;

import java.util.List;

public record EmailResponse(
    String emailId,
    String subject,
    String preheader,
    String bodyHtml,
    String bodyText,
    String emailType,
    List<String> recommendations
) {}
