package com.sporekart.operations.copilot.engine;

import com.sporekart.operations.copilot.dto.LogisticsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogisticsEngineTest {

    @InjectMocks
    private LogisticsEngine logisticsEngine;

    @Test
    void testPlanShipment() {
        var request = new LogisticsRequest("Ship spawn kits", "logistics", "Mumbai", "Delhi", "110001", 2.5);
        var response = logisticsEngine.planShipment(request);
        assertNotNull(response);
        assertNotNull(response.recommendation());
        assertFalse(response.courierOptions().isEmpty());
        assertTrue(response.estimatedCost() > 0);
    }

    @Test
    void testGetCourierOptions() {
        var options = logisticsEngine.getCourierOptions("Mumbai", "Delhi", 2.5);
        assertEquals(5, options.size());
    }

    @Test
    void testTrackShipment() {
        var shipment = logisticsEngine.trackShipment("TRACK123");
        assertNotNull(shipment);
        assertEquals("TRACK123", shipment.trackingNumber());
        assertNotNull(shipment.status());
    }

    @Test
    void testAnalyzeShippingCost() {
        var cost = logisticsEngine.analyzeShippingCost("CR-001", "national", 2.5);
        assertTrue(cost > 0);
    }

    @Test
    void testGetRegionalPerformance() {
        var perf = logisticsEngine.getRegionalPerformance("West");
        assertNotNull(perf);
        assertTrue(perf.containsKey("region"));
    }

    @Test
    void testGetDeliveryDelayPredictions() {
        var predictions = logisticsEngine.getDeliveryDelayPredictions("400001");
        assertFalse(predictions.isEmpty());
    }

    @Test
    void testRecommendRoute() {
        var route = logisticsEngine.recommendRoute("Mumbai", "Delhi");
        assertNotNull(route);
        assertNotNull(route.recommendedCourier());
        assertTrue(route.distanceKm() > 0);
    }
}
