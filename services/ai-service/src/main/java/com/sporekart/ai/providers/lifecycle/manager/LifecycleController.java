package com.sporekart.ai.providers.lifecycle.manager;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

public interface LifecycleController {
    void initialize(String providerId);
    void activate(String providerId);
    void deactivate(String providerId);
    void degrade(String providerId, String reason);
    void recover(String providerId);
    void enterMaintenance(String providerId, String reason);
    void exitMaintenance(String providerId);
    void markUnavailable(String providerId, String reason);
    void markFailed(String providerId, String error);
    void deprecate(String providerId, String reason);
    void remove(String providerId);
    LifecycleState getCurrentState(String providerId);
}
