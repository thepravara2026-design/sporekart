package com.sporekart.executive.copilot.engine;

import com.sporekart.executive.copilot.dto.ForecastRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessForecastingEngineTest {

    private BusinessForecastingEngine engine;

    @BeforeEach
    void setUp() {
        engine = new BusinessForecastingEngine();
    }

    @Test
    void forecast_shouldReturnValidResponse() {
        var request = new ForecastRequest("revenue", "quarterly", 12);
        var response = engine.forecast(request);
        assertNotNull(response);
        assertEquals("revenue", response.forecastType());
        assertEquals(12, response.horizonMonths());
    }

    @Test
    void forecast_shouldHaveProjections() {
        var request = new ForecastRequest("revenue", "quarterly", 6);
        var response = engine.forecast(request);
        assertEquals(6, response.projectedValues().size());
        assertEquals(6, response.lowerBound().size());
        assertEquals(6, response.upperBound().size());
    }

    @Test
    void forecast_shouldHaveConfidenceScore() {
        var request = new ForecastRequest("revenue", "quarterly", 12);
        var response = engine.forecast(request);
        assertTrue(response.confidenceScore() > 0);
    }

    @Test
    void forecast_shouldHaveAssumptions() {
        var request = new ForecastRequest("revenue", "quarterly", 12);
        var response = engine.forecast(request);
        assertNotNull(response.assumptions());
        assertFalse(response.assumptions().isEmpty());
    }

    @Test
    void forecast_shouldHaveKeyRisks() {
        var request = new ForecastRequest("revenue", "quarterly", 12);
        var response = engine.forecast(request);
        assertNotNull(response.keyRisks());
        assertFalse(response.keyRisks().isEmpty());
    }

    @Test
    void forecast_orders_shouldReturnValidResponse() {
        var request = new ForecastRequest("orders", "monthly", 6);
        var response = engine.forecast(request);
        assertEquals("orders", response.forecastType());
        assertEquals(6, response.projectedValues().size());
    }

    @Test
    void forecast_customers_shouldReturnValidResponse() {
        var request = new ForecastRequest("customers", "monthly", 3);
        var response = engine.forecast(request);
        assertEquals("customers", response.forecastType());
    }

    @Test
    void forecast_inventory_shouldReturnValidResponse() {
        var request = new ForecastRequest("inventory", "monthly", 3);
        var response = engine.forecast(request);
        assertEquals("inventory", response.forecastType());
    }

    @Test
    void generateDetailedForecast_shouldReturnValidForecast() {
        var forecast = engine.generateDetailedForecast("revenue", 12);
        assertNotNull(forecast);
        assertNotNull(forecast.forecastId());
        assertNotNull(forecast.assumptions());
        assertEquals(12, forecast.projectedValues().size());
    }

    @Test
    void forecastRevenue_shouldReturnValidData() {
        var result = engine.forecastRevenue(12);
        assertNotNull(result);
        assertTrue(result.containsKey("currentMonthlyRevenue"));
        assertTrue(result.containsKey("projectedMonthlyRevenue"));
        assertTrue(result.containsKey("totalProjectedRevenue"));
    }

    @Test
    void forecastRevenue_shouldHaveGrowthRate() {
        var result = engine.forecastRevenue(6);
        assertEquals("15.2%", result.get("growthRate"));
    }

    @Test
    void forecastOrders_shouldReturnValidData() {
        var result = engine.forecastOrders(12);
        assertNotNull(result);
        assertTrue(result.containsKey("currentMonthlyOrders"));
        assertTrue(result.containsKey("projectedOrders"));
        assertTrue(result.containsKey("totalProjectedOrders"));
    }

    @Test
    void forecastOrders_shouldHaveCorrectBase() {
        var result = engine.forecastOrders(1);
        assertEquals(2500, result.get("currentMonthlyOrders"));
    }

    @Test
    void forecastOrders_shouldHaveProjectedValues() {
        var result = engine.forecastOrders(3);
        var projected = (java.util.List<Double>) result.get("projectedOrders");
        assertEquals(3, projected.size());
    }

    @Test
    void forecast_withNullHorizon_shouldDefaultTo12() {
        var request = new ForecastRequest("revenue", "annual", null);
        var response = engine.forecast(request);
        assertEquals(12, response.horizonMonths());
    }

    @Test
    void forecast_upperBound_shouldExceedValues() {
        var request = new ForecastRequest("revenue", "quarterly", 3);
        var response = engine.forecast(request);
        for (int i = 0; i < response.projectedValues().size(); i++) {
            assertTrue(response.upperBound().get(i) >= response.projectedValues().get(i));
        }
    }

    @Test
    void forecast_lowerBound_shouldBeBelowValues() {
        var request = new ForecastRequest("revenue", "quarterly", 3);
        var response = engine.forecast(request);
        for (int i = 0; i < response.projectedValues().size(); i++) {
            assertTrue(response.lowerBound().get(i) <= response.projectedValues().get(i));
        }
    }
}
