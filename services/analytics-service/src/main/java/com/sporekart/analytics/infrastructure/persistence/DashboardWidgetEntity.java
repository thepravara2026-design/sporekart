package com.sporekart.analytics.infrastructure.persistence;

import com.sporekart.analytics.domain.model.DashboardWidget;
import jakarta.persistence.*;

@Entity
@Table(name = "dashboard_widgets")
public class DashboardWidgetEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "metric", nullable = false, length = 100)
    private String metric;

    protected DashboardWidgetEntity() {}

    public DashboardWidgetEntity(String id, String name, String metric) {
        this.id = id;
        this.name = name;
        this.metric = metric;
    }

    public static DashboardWidgetEntity fromDomain(DashboardWidget widget) {
        return new DashboardWidgetEntity(widget.getId(), widget.getName(), widget.getMetric());
    }

    public DashboardWidget toDomain() {
        return new DashboardWidget(id, name, metric);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }
}
