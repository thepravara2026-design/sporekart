package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;

public interface SummarizerService {
    Summary summarize(ConversationId conversationId, List<Message> messages);
    Summary incrementalSummarize(ConversationId conversationId, Summary previousSummary, List<Message> newMessages);
    List<Summary> getSummaries(ConversationId conversationId);
    String restoreFromSummary(Summary summary, List<Message> recentMessages);
}
