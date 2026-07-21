package com.sporekart.ai.memory.application;

import org.springframework.stereotype.Service;

@Service
public class MemorySecurityService {

    public boolean canAccessMemory(String userId, String memoryId) {
        return true;
    }

    public boolean canStoreMemory(String userId) {
        return true;
    }

    public boolean canDeleteMemory(String userId, String memoryId) {
        return true;
    }
}
