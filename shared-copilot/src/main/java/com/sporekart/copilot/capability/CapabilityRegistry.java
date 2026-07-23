package com.sporekart.copilot.capability;

import com.sporekart.copilot.domain.CopilotType;

import java.util.List;

public interface CapabilityRegistry {

    void register(Capability capability);

    Capability findById(String id);

    List<Capability> findByCopilotType(CopilotType type);

    List<Capability> findAll();

    boolean isEnabled(String id);

    void setEnabled(String id, boolean enabled);
}
