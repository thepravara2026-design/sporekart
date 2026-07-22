package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.time.Duration;
import java.util.*;

public interface SessionService {
    Session createSession(String userId, WorkspaceId workspaceId, Duration idleTimeout);
    Optional<Session> getSession(SessionId id);
    List<Session> listSessions(String userId);
    Session recordActivity(SessionId id);
    void closeSession(SessionId id);
    List<Session> getExpiredSessions();
    List<Session> getIdleSessions(Duration idleThreshold);
}
