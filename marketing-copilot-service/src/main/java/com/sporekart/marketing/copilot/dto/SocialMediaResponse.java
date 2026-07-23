package com.sporekart.marketing.copilot.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SocialMediaResponse(
    String postId,
    String platform,
    String content,
    List<String> hashtags,
    String postType,
    String callToAction,
    LocalDateTime scheduledAt,
    List<String> bestPostingTimes
) {}
