package com.sporekart.ai.core.domain;

import java.time.Clock;
import java.time.OffsetDateTime;

public interface TimestampProvider {
    OffsetDateTime now();
    Clock getClock();

    static TimestampProvider system() {
        return new TimestampProvider() {
            @Override
            public OffsetDateTime now() { return OffsetDateTime.now(); }
            @Override
            public Clock getClock() { return Clock.systemUTC(); }
        };
    }
}
