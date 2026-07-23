package com.sporekart.admin.engine;

import com.sporekart.admin.domain.OperationalAlert;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AlertEngine {

    public List<OperationalAlert> getActiveAlerts() {
        List<OperationalAlert> alerts = new ArrayList<>();
        alerts.addAll(checkInventoryAlerts());
        alerts.addAll(checkRevenueAlerts());
        alerts.add(new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.INFO,
            "New Admin User Created",
            "3 new admin users were onboarded this week",
            "user_onboarding",
            null,
            3.0,
            OffsetDateTime.now(),
            "Verify all new users have completed orientation"
        ));
        return alerts;
    }

    public List<OperationalAlert> checkInventoryAlerts() {
        List<OperationalAlert> alerts = new ArrayList<>();
        alerts.add(new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.WARNING,
            "Low Stock Alert",
            "18 products are below minimum threshold",
            "inventory_level",
            50.0,
            23.0,
            OffsetDateTime.now(),
            "Review reorder points and expedite pending purchase orders"
        ));
        alerts.add(new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.CRITICAL,
            "Out of Stock: Organic Tomatoes",
            "Organic Tomatoes has been out of stock for 2 days",
            "inventory_level",
            30.0,
            0.0,
            OffsetDateTime.now(),
            "Place urgent replenishment order with preferred supplier"
        ));
        return alerts;
    }

    public List<OperationalAlert> checkRevenueAlerts() {
        List<OperationalAlert> alerts = new ArrayList<>();
        alerts.add(new OperationalAlert(
            UUID.randomUUID().toString(),
            OperationalAlert.AlertType.WARNING,
            "Revenue Dip Detected",
            "Today's revenue is 15% below the 7-day average",
            "daily_revenue",
            50000.0,
            42500.0,
            OffsetDateTime.now(),
            "Analyze traffic sources and identify drop-off points in conversion funnel"
        ));
        return alerts;
    }
}
