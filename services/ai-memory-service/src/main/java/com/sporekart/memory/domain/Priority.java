package com.sporekart.memory.domain;

public enum Priority {
    LOW(1),
    MEDIUM(5),
    NORMAL(5),
    HIGH(8),
    CRITICAL(10);

    private final int level;

    Priority(int level) { this.level = level; }
    public int getLevel() { return level; }
}
