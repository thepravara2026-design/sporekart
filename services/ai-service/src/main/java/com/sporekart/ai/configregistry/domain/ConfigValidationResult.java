package com.sporekart.ai.configregistry.domain;

import java.time.Instant;
import java.util.List;

public record ConfigValidationResult(boolean valid, List<String> errors, List<String> warnings, Instant validatedAt) {}
