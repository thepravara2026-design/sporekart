package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.CollaborationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CollaborationEngineTest {

    private CollaborationEngine collaborationEngine;

    @BeforeEach
    void setUp() {
        collaborationEngine = new CollaborationEngine();
    }

    @Test
    void initiateCollaboration_ShouldCreateCollaborationRequest() {
        CollaborationRequest request = collaborationEngine.initiateCollaboration(
                "copilot-1", List.of("copilot-2", "copilot-3"), "How to improve yield?");

        assertNotNull(request.collaborationId());
        assertEquals(CollaborationRequest.STATUS_PENDING, request.status());
        assertEquals("copilot-1", request.primaryCopilotId());
        assertThat(request.collaboratingCopilotIds()).containsExactly("copilot-2", "copilot-3");
    }

    @Test
    void addCollaboratingCopilot_ShouldAddCopilotToRequest() {
        CollaborationRequest request = collaborationEngine.initiateCollaboration(
                "copilot-1", List.of("copilot-2"), "Test query");
        collaborationEngine.addCollaboratingCopilot(request.collaborationId(), "copilot-4");

        CollaborationRequest updated = collaborationEngine.getCollaboration(request.collaborationId());
        assertThat(updated.collaboratingCopilotIds()).contains("copilot-4");
    }

    @Test
    void submitPartialResponse_ShouldCollectResponses() {
        CollaborationRequest request = collaborationEngine.initiateCollaboration(
                "copilot-1", List.of("copilot-2", "copilot-3"), "Query");
        collaborationEngine.submitPartialResponse(request.collaborationId(), "copilot-2", "Response from copilot-2");
        collaborationEngine.submitPartialResponse(request.collaborationId(), "copilot-3", "Response from copilot-3");

        CollaborationRequest updated = collaborationEngine.getCollaboration(request.collaborationId());
        assertThat(updated.partialResponses()).hasSize(2);
    }

    @Test
    void getMergedResponse_ShouldCombineResponses() {
        CollaborationRequest request = collaborationEngine.initiateCollaboration(
                "copilot-1", List.of("copilot-2"), "Query");
        collaborationEngine.submitPartialResponse(request.collaborationId(), "copilot-2", "Partial response");

        String merged = collaborationEngine.getMergedResponse(request.collaborationId());
        assertNotNull(merged);
        assertThat(merged).contains("Partial response");
    }

    @Test
    void cancelCollaboration_ShouldCancelRequest() {
        CollaborationRequest request = collaborationEngine.initiateCollaboration(
                "copilot-1", List.of("copilot-2"), "Query");
        collaborationEngine.cancelCollaboration(request.collaborationId());

        CollaborationRequest cancelled = collaborationEngine.getCollaboration(request.collaborationId());
        assertEquals(CollaborationRequest.STATUS_FAILED, cancelled.status());
    }

    @Test
    void getAllActiveCollaborations_ShouldReturnActiveRequests() {
        collaborationEngine.initiateCollaboration("copilot-1", List.of("copilot-2"), "Query 1");
        collaborationEngine.initiateCollaboration("copilot-3", List.of("copilot-4"), "Query 2");

        List<CollaborationRequest> active = collaborationEngine.getAllActiveCollaborations();
        assertThat(active).hasSize(2);
    }

    @Test
    void getAllActiveCollaborations_ShouldExcludeCompleted() {
        CollaborationRequest request = collaborationEngine.initiateCollaboration("copilot-1", List.of("copilot-2"), "Query");
        collaborationEngine.cancelCollaboration(request.collaborationId());

        List<CollaborationRequest> active = collaborationEngine.getAllActiveCollaborations();
        assertThat(active).isEmpty();
    }
}
