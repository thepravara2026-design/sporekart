package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.HandoffRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HandoffEngineTest {

    private HandoffEngine handoffEngine;

    @BeforeEach
    void setUp() {
        handoffEngine = new HandoffEngine();
    }

    @Test
    void initiateHandoff_ShouldCreateHandoffRequest() {
        HandoffRequest request = handoffEngine.initiateHandoff(
                "sess-1", "copilot-1", "copilot-2", "needs expertise", "context summary", "user message");

        assertNotNull(request.handoffId());
        assertEquals(HandoffRequest.STATUS_PENDING, request.status());
        assertEquals("copilot-1", request.fromCopilotId());
        assertEquals("copilot-2", request.toCopilotId());
    }

    @Test
    void acceptHandoff_ShouldChangeStatusToAccepted() {
        HandoffRequest request = handoffEngine.initiateHandoff(
                "sess-1", "copilot-1", "copilot-2", "reason", "context", "msg");
        handoffEngine.acceptHandoff(request.handoffId());

        HandoffRequest accepted = handoffEngine.getHandoff(request.handoffId());
        assertEquals(HandoffRequest.STATUS_ACCEPTED, accepted.status());
    }

    @Test
    void rejectHandoff_ShouldChangeStatusToRejected() {
        HandoffRequest request = handoffEngine.initiateHandoff(
                "sess-1", "copilot-1", "copilot-2", "reason", "context", "msg");
        handoffEngine.rejectHandoff(request.handoffId());

        HandoffRequest rejected = handoffEngine.getHandoff(request.handoffId());
        assertEquals(HandoffRequest.STATUS_REJECTED, rejected.status());
    }

    @Test
    void completeHandoff_ShouldChangeStatusToCompleted() {
        HandoffRequest request = handoffEngine.initiateHandoff(
                "sess-1", "copilot-1", "copilot-2", "reason", "context", "msg");
        handoffEngine.acceptHandoff(request.handoffId());
        handoffEngine.completeHandoff(request.handoffId());

        HandoffRequest completed = handoffEngine.getHandoff(request.handoffId());
        assertEquals(HandoffRequest.STATUS_COMPLETED, completed.status());
    }

    @Test
    void getHandoffHistory_ShouldReturnAllHandoffs() {
        handoffEngine.initiateHandoff("sess-1", "copilot-1", "copilot-2", "reason", "context", "msg");
        handoffEngine.initiateHandoff("sess-1", "copilot-2", "copilot-3", "another reason", "context", "msg");

        List<HandoffRequest> history = handoffEngine.getHandoffHistory("sess-1");
        assertThat(history).hasSize(2);
    }

    @Test
    void getSuggestedHandoff_ShouldReturnRecommendedCopilot() {
        String suggested = handoffEngine.getSuggestedHandoff("sess-1", "What is the revenue?");
        assertNotNull(suggested);
    }

    @Test
    void getHandoff_ShouldReturnCorrectRequest() {
        HandoffRequest request = handoffEngine.initiateHandoff(
                "sess-1", "copilot-1", "copilot-2", "reason", "context", "msg");
        HandoffRequest found = handoffEngine.getHandoff(request.handoffId());

        assertEquals(request.handoffId(), found.handoffId());
    }
}
