package com.sporekart.ai.providers.client;

import com.sporekart.ai.gateway.contract.request.GatewayRequest;
import com.sporekart.ai.gateway.contract.response.GatewayResponse;

public interface GatewayClientBridge {
    GatewayResponse executeThroughGateway(GatewayRequest request);
    boolean isGatewayConnected();
}
