package com.sporekart.ai.semantic.domain;

import java.util.List;

public record SemanticVector(
        String id,
        List<Double> values,
        int dimensions) {
}
