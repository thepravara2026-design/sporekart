package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;

public interface SessionRepository {
    Optional<Session> findById(SessionId id);
    List<Session> findByUserId(String userId);
    List<Session> findByWorkspaceId(WorkspaceId workspaceId);
    List<Session> findByStatus(String status);
    List<Session> findAll();
    Session save(Session session);
    void delete(SessionId id);
    boolean exists(SessionId id);
}
