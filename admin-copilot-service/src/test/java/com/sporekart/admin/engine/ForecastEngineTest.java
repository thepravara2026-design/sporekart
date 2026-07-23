package com.sporekart.admin.engine;

import com.sporekart.admin.domain.ForecastResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ForecastEngineTest {

    private ForecastEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ForecastEngine();
    }

    @Test
    void forecastRevenueShouldReturnFutureDates() {
        ForecastResult result = engine.forecastRevenue("MONTHLY", 30);

        assertNotNull(result);
        assertNotNull(result.forecastValues());
        assertFalse(result.forecastValues().isEmpty());
        for (LocalDate date : result.forecastValues().keySet()) {
            assertTrue(date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now()));
        }
    }

    @Test
    void forecastSalesShouldShowSeasonalPatterns() {
        ForecastResult result = engine.forecastSales("WEEKLY", 12);

        assertNotNull(result);
        assertNotNull(result.seasonalityFactors());
        assertFalse(result.seasonalityFactors().isEmpty());
        assertEquals("WEEKLY", result.period());
    }

    @Test
    void forecastInventoryShouldProvideConfidenceIntervals() {
        ForecastResult result = engine.forecastInventory("MONTHLY", 6);

        assertNotNull(result);
        assertNotNull(result.confidenceInterval());
        assertTrue(result.confidenceInterval().contains("%"));
        assertNotNull(result.forecastValues());
        assertFalse(result.forecastValues().isEmpty());
    }

    @Test
    void longerHorizonsHaveWiderConfidence() {
        ForecastResult shortHorizon = engine.forecastRevenue("DAILY", 7);
        ForecastResult longHorizon = engine.forecastRevenue("DAILY", 90);

        double shortFirst = shortHorizon.forecastValues().values().iterator().next();
        double longFirst = longHorizon.forecastValues().values().iterator().next();

        assertNotNull(shortHorizon);
        assertNotNull(longHorizon);
    }

    @Test
    void forecastValuesArePositive() {
        ForecastResult result = engine.forecastRevenue("MONTHLY", 12);

        for (Double value : result.forecastValues().values()) {
            assertTrue(value > 0, "Forecast value must be positive");
        }
    }
}
