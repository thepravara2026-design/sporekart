package com.sporekart.alert.application.sdk;

import com.sporekart.alert.application.engine.AlertEngine;
import com.sporekart.alert.domain.model.*;
import com.sporekart.alert.domain.repository.AlertRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AlertRuntime {

    private final AlertEngine alertEngine;
    private final AlertRepositoryPort repository;

    public AlertRuntime(AlertEngine alertEngine, AlertRepositoryPort repository) {
        this.alertEngine = alertEngine; this.repository = repository;
    }

    public List<Alert> generateAlertsForCategory(AlertCategory category) {
        List<Alert> alerts = alertEngine.generateAlertsForCategory(category);
        alerts.forEach(repository::saveAlert);
        return alerts;
    }

    public Alert acknowledgeAlert(String id) {
        return repository.findAlertById(id)
                .map(a -> repository.saveAlert(a.acknowledge()))
                .orElseThrow(() -> new NoSuchElementException("Alert not found: " + id));
    }

    public Alert resolveAlert(String id) {
        return repository.findAlertById(id)
                .map(a -> repository.saveAlert(a.resolve()))
                .orElseThrow(() -> new NoSuchElementException("Alert not found: " + id));
    }

    public List<Alert> getAllAlerts() { return repository.findAllAlerts(); }

    public Optional<Alert> findById(String id) { return repository.findAlertById(id); }
}
