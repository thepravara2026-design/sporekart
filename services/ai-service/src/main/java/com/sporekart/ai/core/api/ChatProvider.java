package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiMessage;
import java.util.List;

public interface ChatProvider {
    String chat(List<AiMessage> messages, String model);
    boolean supportsStreaming();
    boolean supportsFunctionCalling();
}
