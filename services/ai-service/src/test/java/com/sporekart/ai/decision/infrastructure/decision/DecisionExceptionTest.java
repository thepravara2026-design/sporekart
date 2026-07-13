package com.sporekart.ai.decision.infrastructure.decision;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.decision.infrastructure.security.DecisionException;
import org.junit.jupiter.api.Test;

class DecisionExceptionTest {

    @Test
    void notFoundReturnsCorrectCodeAndStatus() {
        DecisionException ex = DecisionException.notFound("Decision not found");
        assertEquals("DEC_404", ex.getCode());
        assertEquals(404, ex.getStatus());
        assertEquals("Decision not found", ex.getMessage());
    }

    @Test
    void badRequestReturnsCorrectCodeAndStatus() {
        DecisionException ex = DecisionException.badRequest("Invalid input");
        assertEquals("DEC_400", ex.getCode());
        assertEquals(400, ex.getStatus());
        assertEquals("Invalid input", ex.getMessage());
    }

    @Test
    void evaluationFailedReturnsCorrectCodeAndStatus() {
        DecisionException ex = DecisionException.evaluationFailed("Evaluation error");
        assertEquals("DEC_500", ex.getCode());
        assertEquals(500, ex.getStatus());
        assertEquals("Evaluation error", ex.getMessage());
    }
}
