package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.ContextSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContextBrokerTest {

    private ContextBroker contextBroker;

    @BeforeEach
    void setUp() {
        contextBroker = new ContextBroker();
    }

    @Test
    void buildContextSnapshot_ShouldReturnSnapshot() {
        ContextSnapshot snapshot = contextBroker.buildContextSnapshot("sess-1", "user-1");
        assertNotNull(snapshot);
        assertEquals("sess-1", snapshot.sessionId());
        assertEquals("user-1", snapshot.userId());
    }

    @Test
    void updateUserContext_ShouldUpdateUserContext() {
        contextBroker.updateUserContext("sess-1", Map.of("name", "John", "role", "grower"));
        Map<String, Object> userContext = contextBroker.getContext("sess-1", "userContext");
        assertEquals("John", userContext.get("name"));
    }

    @Test
    void updateConversationContext_ShouldUpdateConversationContext() {
        contextBroker.updateConversationContext("sess-1", Map.of("lastIntent", "order inquiry", "turnCount", 5));
        Map<String, Object> convContext = contextBroker.getContext("sess-1", "conversationContext");
        assertEquals("order inquiry", convContext.get("lastIntent"));
    }

    @Test
    void updateBusinessContext_ShouldUpdateBusinessContext() {
        contextBroker.updateBusinessContext("sess-1", Map.of("activeOrderId", "ORD-123", "region", "North"));
        Map<String, Object> bizContext = contextBroker.getContext("sess-1", "businessContext");
        assertEquals("ORD-123", bizContext.get("activeOrderId"));
    }

    @Test
    void getContext_ShouldReturnContextByNamespace() {
        contextBroker.updateUserContext("sess-1", Map.of("email", "test@example.com"));
        Map<String, Object> ctx = contextBroker.getContext("sess-1", "userContext");
        assertThat(ctx).containsKey("email");
    }

    @Test
    void getFullContext_ShouldReturnAllContexts() {
        contextBroker.updateUserContext("sess-1", Map.of("name", "John"));
        contextBroker.updateBusinessContext("sess-1", Map.of("org", "SporeKart"));

        ContextSnapshot full = contextBroker.getFullContext("sess-1");
        assertThat(full.userContext()).isNotEmpty();
        assertThat(full.businessContext()).isNotEmpty();
    }

    @Test
    void mergeContext_ShouldMergeData() {
        contextBroker.updateUserContext("sess-1", Map.of("name", "John"));
        contextBroker.mergeContext("sess-1", Map.of("userContext", Map.of("age", 30)));

        Map<String, Object> userContext = contextBroker.getContext("sess-1", "userContext");
        assertEquals("John", userContext.get("name"));
        assertEquals(30, userContext.get("age"));
    }

    @Test
    void clearContext_ShouldRemoveAllContext() {
        contextBroker.updateUserContext("sess-1", Map.of("name", "John"));
        contextBroker.clearContext("sess-1");

        ContextSnapshot full = contextBroker.getFullContext("sess-1");
        assertThat(full.userContext()).isEmpty();
        assertThat(full.businessContext()).isEmpty();
        assertThat(full.conversationContext()).isEmpty();
    }

    @Test
    void exportContext_ShouldProduceImportableFormat() {
        contextBroker.updateUserContext("sess-1", Map.of("name", "John"));
        String exported = contextBroker.exportContext("sess-1");
        assertNotNull(exported);

        contextBroker.clearContext("sess-1");
        contextBroker.importContext("sess-1", exported);
        Map<String, Object> userContext = contextBroker.getContext("sess-1", "userContext");
        assertEquals("John", userContext.get("name"));
    }

    @Test
    void getActiveContexts_ShouldReturnAllActive() {
        contextBroker.buildContextSnapshot("sess-1", "user-1");
        contextBroker.buildContextSnapshot("sess-2", "user-2");

        List<String> active = contextBroker.getActiveContexts();
        assertThat(active).contains("sess-1", "sess-2");
    }
}
