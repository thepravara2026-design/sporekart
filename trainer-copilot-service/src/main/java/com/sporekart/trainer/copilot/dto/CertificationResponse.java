package com.sporekart.trainer.copilot.dto;

import java.util.List;

import com.sporekart.trainer.copilot.domain.Certification;

public record CertificationResponse(
    Certification certification,
    String eligibility,
    List<String> recommendations
) {}
