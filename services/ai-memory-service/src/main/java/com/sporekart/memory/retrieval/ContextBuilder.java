package com.sporekart.memory.retrieval;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContextBuilder {

    public RetrievalContext buildContext(String query, List<RetrievedMemory> memories) {
        var startTime = System.currentTimeMillis();

        var contextSummary = memories.isEmpty()
                ? "No relevant memories found."
                : String.format("Found %d relevant memories for query.", memories.size());

        return new RetrievalContext(
                query,
                memories,
                memories.size(),
                System.currentTimeMillis() - startTime,
                contextSummary
        );
    }

    public String formatForPrompt(List<RetrievedMemory> memories, int maxTokens) {
        if (memories.isEmpty()) return "";

        var sb = new StringBuilder();
        sb.append("## Relevant Context from Memory\n\n");
        for (int i = 0; i < memories.size(); i++) {
            var m = memories.get(i);
            var entry = String.format("### Memory %d (relevance: %.2f)\n**Title:** %s\n**Type:** %s\n**Source:** %s\n**Content:**\n%s\n\n",
                    i + 1, m.relevanceScore(), m.title(), m.memoryType(), m.source(), m.content());
            if (sb.length() + entry.length() <= maxTokens * 4) {
                sb.append(entry);
            }
        }
        return sb.toString().trim();
    }
}
