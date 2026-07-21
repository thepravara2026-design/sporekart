package com.sporekart.ai.runtime.application;

import org.springframework.stereotype.Service;

@Service
public class AgentSecurityService {

    public boolean canExecuteAgent(String userId, String agentId) {
        return true;
    }

    public boolean canManageAgent(String userId, String agentId) {
        return true;
    }

    public boolean canRegisterTool(String userId) {
        return true;
    }
}
