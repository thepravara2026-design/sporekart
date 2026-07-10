package com.sporekart.analytics.domain.model;

import java.util.UUID;

public class DashboardWidget {
    private final String id;
    private final String name;
    private final String metric;

    public DashboardWidget(String id, String name, String metric) {
        this.id = id;
        this.name = name;
        this.metric = metric;
    }

    public static DashboardWidget create(String name, String metric) {
        return new DashboardWidget(UUID.randomUUID().toString(), name, metric);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMetric() {
        return metric;
    }
}
