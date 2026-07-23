package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.domain.*;
import com.sporekart.operations.copilot.dto.LogisticsRequest;
import com.sporekart.operations.copilot.dto.LogisticsResponse;
import com.sporekart.operations.copilot.dto.LogisticsResponse.CourierOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class LogisticsEngine {

    private static final Logger log = LoggerFactory.getLogger(LogisticsEngine.class);

    public LogisticsResponse planShipment(LogisticsRequest request) {
        log.info("Planning shipment from: {} to: {}", request.origin(), request.destination());
        var couriers = getCourierOptions(request.origin(), request.destination(), request.weight() != null ? request.weight() : 1.0);
        var estimatedCost = couriers.stream().mapToDouble(CourierOption::price).min().orElse(100.0);
        var estimatedDays = couriers.stream().mapToInt(CourierOption::estimatedDays).min().orElse(5);
        var alerts = new ArrayList<String>();
        if (couriers.stream().anyMatch(c -> c.reliability() < 85.0)) {
            alerts.add("Some couriers have reliability below 85%");
        }
        if (estimatedDays > 5) {
            alerts.add("Estimated delivery exceeds standard SLA of 5 days");
        }
        return new LogisticsResponse("Recommended: " + couriers.get(0).name(), couriers, estimatedCost, estimatedDays, alerts);
    }

    public List<CourierOption> getCourierOptions(String origin, String destination, double weight) {
        log.debug("Getting courier options from {} to {} weight: {}", origin, destination, weight);
        return List.of(
            new CourierOption("Delhivery", 85.0 + (weight * 10), 3, 94.5, "Recommended", "LOW"),
            new CourierOption("BlueDart", 120.0 + (weight * 12), 2, 96.0, "Fastest", "LOW"),
            new CourierOption("DTDC", 75.0 + (weight * 8), 4, 88.0, "Economical", "LOW"),
            new CourierOption("India Post", 45.0 + (weight * 5), 7, 72.0, "Cheapest", "MEDIUM"),
            new CourierOption("ShadowFax", 95.0 + (weight * 11), 3, 91.0, "Good balance", "LOW")
        );
    }

    public Shipment trackShipment(String trackingNumber) {
        log.debug("Tracking shipment: {}", trackingNumber);
        return new Shipment("SHP-" + trackingNumber, "ORD-001", "CR-001", "Delhivery",
            trackingNumber, "WH-MAIN", "123 Customer St", "Mumbai", "Maharashtra", "400001",
            2.5, Shipment.ShipmentStatus.IN_TRANSIT, LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(2), null, 95.0, "domestic");
    }

    public double analyzeShippingCost(String courierId, String zone, double weight) {
        log.debug("Analyzing shipping cost for courier: {} zone: {} weight: {}", courierId, zone, weight);
        var baseRate = switch (zone.toLowerCase()) {
            case "local" -> 50.0;
            case "regional" -> 80.0;
            case "national" -> 120.0;
            case "zone_a" -> 70.0;
            case "zone_b" -> 90.0;
            case "zone_c" -> 110.0;
            default -> 100.0;
        };
        return baseRate + (weight * 10);
    }

    public Map<String, Object> getRegionalPerformance(String region) {
        log.debug("Getting regional performance for: {}", region);
        var perf = new LinkedHashMap<String, Object>();
        perf.put("region", region);
        perf.put("ordersDelivered", 1250);
        perf.put("averageDeliveryDays", 3.2);
        perf.put("deliverySuccessRate", 97.8);
        perf.put("returnRate", 2.1);
        perf.put("topCourier", "Delhivery");
        return perf;
    }

    public List<String> getDeliveryDelayPredictions(String pincode) {
        log.debug("Getting delivery delay predictions for pincode: {}", pincode);
        return List.of(
            "Estimated delay: 1-2 days due to weather conditions",
            "Alternative: Use BlueDart for faster delivery to this region",
            "Peak season surcharge may apply for this pincode"
        );
    }

    public LogisticsRoute recommendRoute(String origin, String destination) {
        log.info("Recommending route from {} to {}", origin, destination);
        return new LogisticsRoute("RTE-001", origin, destination, "domestic",
            450.0, 3.0, 95.0, List.of("Delhivery", "BlueDart", "DTDC"),
            "Delhivery", 94.5);
    }
}
