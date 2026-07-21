package com.sporekart.ai.shared.interfaces;

public interface BaseService {
    boolean isEnabled();
    boolean healthCheck();
    String getServiceName();
    String getVersion();
}
