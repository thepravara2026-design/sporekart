package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.domain.ChunkResult;
import com.sporekart.ai.knowledge.domain.ChunkStrategy;
import java.util.*;

public class DocumentChunkingEngine {

    public List<ChunkResult> chunk(String content, ChunkStrategy strategy,
                                   int maxChunkSize, int overlap) {
        return switch (strategy) {
            case FIXED_SIZE -> fixedSizeChunk(content, maxChunkSize, overlap);
            case SLIDING_WINDOW -> slidingWindowChunk(content, maxChunkSize, overlap);
            case SECTION -> sectionChunk(content);
            case HEADING -> headingChunk(content);
            case SEMANTIC -> semanticChunk(content);
            case HIERARCHICAL -> hierarchicalChunk(content);
        };
    }

    public int estimateTokens(String text) {
        return text.length() / 4 + 3;
    }

    private List<ChunkResult> fixedSizeChunk(String content, int size, int overlap) {
        var results = new ArrayList<ChunkResult>();
        int start = 0;
        int seq = 0;
        while (start < content.length()) {
            int end = Math.min(start + size, content.length());
            if (end < content.length() && end - start == size) {
                int lastSpace = content.lastIndexOf(' ', end);
                if (lastSpace > start) end = lastSpace;
            }
            var segment = content.substring(start, end).trim();
            if (!segment.isEmpty()) {
                results.add(new ChunkResult(null, segment, seq, estimateTokens(segment), null, null));
                seq++;
            }
            int nextStart = end - overlap;
            if (nextStart <= start) nextStart = end;
            start = nextStart;
        }
        return results;
    }

    private List<ChunkResult> slidingWindowChunk(String content, int size, int overlap) {
        var results = new ArrayList<ChunkResult>();
        int step = Math.max(1, size - overlap);
        int seq = 0;
        for (int start = 0; start < content.length(); start += step) {
            int end = Math.min(start + size, content.length());
            var segment = content.substring(start, end).trim();
            if (!segment.isEmpty()) {
                results.add(new ChunkResult(null, segment, seq, estimateTokens(segment), null, null));
                seq++;
            }
        }
        return results;
    }

    private List<ChunkResult> sectionChunk(String content) {
        var results = new ArrayList<ChunkResult>();
        var sections = content.split("\\n\\n+");
        int seq = 0;
        for (var section : sections) {
            var trimmed = section.trim();
            if (!trimmed.isEmpty()) {
                results.add(new ChunkResult(null, trimmed, seq, estimateTokens(trimmed), null, null));
                seq++;
            }
        }
        return results;
    }

    private List<ChunkResult> headingChunk(String content) {
        var results = new ArrayList<ChunkResult>();
        var lines = content.split("\\n");
        var sb = new StringBuilder();
        String currentHeading = null;
        int seq = 0;

        for (var line : lines) {
            var trimmed = line.trim();
            if (trimmed.startsWith("#")) {
                if (!sb.isEmpty()) {
                    var segment = sb.toString().trim();
                    if (!segment.isEmpty()) {
                        results.add(new ChunkResult(null, segment, seq, estimateTokens(segment), currentHeading, null));
                        seq++;
                    }
                    sb = new StringBuilder();
                }
                currentHeading = trimmed.replaceAll("#+\\s*", "");
            } else {
                if (sb.length() > 0) sb.append("\n");
                sb.append(line);
            }
        }

        var remaining = sb.toString().trim();
        if (!remaining.isEmpty()) {
            results.add(new ChunkResult(null, remaining, seq, estimateTokens(remaining), currentHeading, null));
        }

        return results;
    }

    private List<ChunkResult> semanticChunk(String content) {
        var results = new ArrayList<ChunkResult>();
        var paragraphs = content.split("\\n\\n+");
        int seq = 0;
        for (var para : paragraphs) {
            var trimmed = para.trim();
            if (!trimmed.isEmpty()) {
                results.add(new ChunkResult(null, trimmed, seq, estimateTokens(trimmed), null, null));
                seq++;
            }
        }
        return results;
    }

    private List<ChunkResult> hierarchicalChunk(String content) {
        var results = new ArrayList<ChunkResult>();
        var lines = content.split("\\n");
        var sb = new StringBuilder();
        String currentHeading = null;
        String parentHeading = null;
        int seq = 0;

        for (var line : lines) {
            var trimmed = line.trim();
            if (trimmed.startsWith("#")) {
                if (!sb.isEmpty()) {
                    var segment = sb.toString().trim();
                    if (!segment.isEmpty()) {
                        results.add(new ChunkResult(null, segment, seq, estimateTokens(segment),
                                currentHeading != null ? currentHeading : parentHeading, parentHeading));
                        seq++;
                    }
                    sb = new StringBuilder();
                }
                int level = countHashLevel(trimmed);
                parentHeading = currentHeading;
                currentHeading = trimmed.replaceAll("#+\\s*", "");
            } else {
                if (sb.length() > 0) sb.append("\n");
                sb.append(line);
            }
        }

        var remaining = sb.toString().trim();
        if (!remaining.isEmpty()) {
            results.add(new ChunkResult(null, remaining, seq, estimateTokens(remaining),
                    currentHeading != null ? currentHeading : parentHeading, parentHeading));
        }

        return results;
    }

    private int countHashLevel(String heading) {
        int count = 0;
        for (int i = 0; i < heading.length() && heading.charAt(i) == '#'; i++) {
            count++;
        }
        return count;
    }
}
