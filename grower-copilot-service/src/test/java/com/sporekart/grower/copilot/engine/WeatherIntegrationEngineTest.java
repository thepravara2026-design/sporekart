package com.sporekart.grower.copilot.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WeatherIntegrationEngineTest {

    private WeatherIntegrationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new WeatherIntegrationEngine();
    }

    @Test
    void getCurrentWeather_ForPunjab_ShouldReturnData() {
        WeatherIntegrationEngine.WeatherData weather = engine.getCurrentWeather("Punjab");
        assertThat(weather).isNotNull();
        assertThat(weather.location()).isEqualTo("Punjab");
        assertThat(weather.temperature()).isPositive();
    }

    @Test
    void getCurrentWeather_ForKarnataka_ShouldReturnData() {
        WeatherIntegrationEngine.WeatherData weather = engine.getCurrentWeather("Karnataka");
        assertThat(weather).isNotNull();
        assertThat(weather.humidity()).isPositive();
    }

    @Test
    void getCurrentWeather_ForMaharashtra_ShouldReturnData() {
        WeatherIntegrationEngine.WeatherData weather = engine.getCurrentWeather("Maharashtra");
        assertThat(weather).isNotNull();
    }

    @Test
    void getCurrentWeather_ForTamilNadu_ShouldReturnData() {
        WeatherIntegrationEngine.WeatherData weather = engine.getCurrentWeather("Tamil Nadu");
        assertThat(weather).isNotNull();
    }

    @Test
    void getCurrentWeather_ForHimachalPradesh_ShouldReturnData() {
        WeatherIntegrationEngine.WeatherData weather = engine.getCurrentWeather("Himachal Pradesh");
        assertThat(weather).isNotNull();
    }

    @Test
    void getCurrentWeather_ForUnknownLocation_ShouldReturnDefault() {
        WeatherIntegrationEngine.WeatherData weather = engine.getCurrentWeather("Unknown");
        assertThat(weather).isNotNull();
        assertThat(weather.temperature()).isEqualTo(27.0);
    }

    @Test
    void getForecast_ShouldReturnForecast() {
        List<WeatherIntegrationEngine.WeatherData> forecast = engine.getForecast("Punjab", 5);
        assertThat(forecast).hasSize(5);
    }

    @Test
    void getForecast_ForSevenDays_ShouldReturnSeven() {
        List<WeatherIntegrationEngine.WeatherData> forecast = engine.getForecast("Punjab", 7);
        assertThat(forecast).hasSize(7);
    }

    @Test
    void getForecast_ForSingleDay_ShouldReturnOne() {
        List<WeatherIntegrationEngine.WeatherData> forecast = engine.getForecast("Punjab", 1);
        assertThat(forecast).hasSize(1);
    }

    @Test
    void getForecast_ForDifferentLocations_ShouldVary() {
        List<WeatherIntegrationEngine.WeatherData> punjab = engine.getForecast("Punjab", 3);
        List<WeatherIntegrationEngine.WeatherData> karnataka = engine.getForecast("Karnataka", 3);
        assertThat(punjab.get(0).temperature()).isNotEqualTo(karnataka.get(0).temperature());
    }

    @Test
    void getWeatherImpact_ForSpawning_ShouldReturnAssessment() {
        String impact = engine.getWeatherImpact("Punjab", "spawning");
        assertThat(impact).isNotBlank();
    }

    @Test
    void getWeatherImpact_ForIncubation_ShouldReturnAssessment() {
        String impact = engine.getWeatherImpact("Punjab", "incubation");
        assertThat(impact).isNotBlank();
    }

    @Test
    void getWeatherImpact_ForFruiting_ShouldReturnAssessment() {
        String impact = engine.getWeatherImpact("Punjab", "fruiting");
        assertThat(impact).isNotBlank();
    }

    @Test
    void getWeatherImpact_ForUnknownStage_ShouldReturnNeutral() {
        String impact = engine.getWeatherImpact("Punjab", "unknown");
        assertThat(impact).contains("NEUTRAL");
    }

    @Test
    void getClimateRisk_ForPunjabSummer_ShouldReturnRisk() {
        String risk = engine.getClimateRisk("Punjab", "summer");
        assertThat(risk).contains("MODERATE");
    }

    @Test
    void getClimateRisk_ForKarnatakaMonsoon_ShouldReturnHigh() {
        String risk = engine.getClimateRisk("Karnataka", "monsoon");
        assertThat(risk).contains("HIGH");
    }

    @Test
    void getClimateRisk_ForMaharashtraWinter_ShouldReturnLow() {
        String risk = engine.getClimateRisk("Maharashtra", "winter");
        assertThat(risk).contains("LOW");
    }

    @Test
    void getClimateRisk_ForUnknownLocation_ShouldReturnUnknown() {
        String risk = engine.getClimateRisk("Unknown", "summer");
        assertThat(risk).contains("UNKNOWN");
    }

    @Test
    void checkWeatherAlert_WhenTemperatureHigh_ShouldReturnAlert() {
        List<String> alerts = engine.checkWeatherAlert("Tamil Nadu");
        assertThat(alerts).isNotEmpty();
    }

    @Test
    void checkWeatherAlert_ForHimachal_ShouldReturnNoAlerts() {
        List<String> alerts = engine.checkWeatherAlert("Himachal Pradesh");
        assertThat(alerts).contains("NO ALERTS");
    }

    @Test
    void checkWeatherAlert_ForKarnataka_ShouldReturnAlerts() {
        List<String> alerts = engine.checkWeatherAlert("Karnataka");
        assertThat(alerts).isNotEmpty();
    }

    @Test
    void getOptimalGrowingSeason_ForOyster_ShouldReturnSeason() {
        String season = engine.getOptimalGrowingSeason("Oyster", "Punjab");
        assertThat(season).isNotBlank();
        assertThat(season).contains("September");
    }

    @Test
    void getOptimalGrowingSeason_ForButton_ShouldReturnSeason() {
        String season = engine.getOptimalGrowingSeason("Button", "Himachal Pradesh");
        assertThat(season).isNotBlank();
    }

    @Test
    void getOptimalGrowingSeason_ForShiitake_ShouldReturnSeason() {
        String season = engine.getOptimalGrowingSeason("Shiitake", "Karnataka");
        assertThat(season).isNotBlank();
    }

    @Test
    void getOptimalGrowingSeason_ForMilky_ShouldReturnSeason() {
        String season = engine.getOptimalGrowingSeason("Milky", "Maharashtra");
        assertThat(season).isNotBlank();
    }

    @Test
    void getOptimalGrowingSeason_ForPaddyStraw_ShouldReturnSeason() {
        String season = engine.getOptimalGrowingSeason("Paddy Straw", "Tamil Nadu");
        assertThat(season).isNotBlank();
    }

    @Test
    void getOptimalGrowingSeason_ForUnknownSpecies_ShouldReturnGeneric() {
        String season = engine.getOptimalGrowingSeason("Unknown", "Punjab");
        assertThat(season).contains("Consult local agricultural extension");
    }

    @Test
    void getHistoricalWeatherPattern_ShouldReturnPattern() {
        Map<String, Object> pattern = engine.getHistoricalWeatherPattern("Punjab", "January");
        assertThat(pattern).isNotNull();
        assertThat(pattern).containsKey("avg_temperature_min");
        assertThat(pattern).containsKey("climate_type");
    }

    @Test
    void getHistoricalWeatherPattern_ForUnknown_ShouldReturnDefault() {
        Map<String, Object> pattern = engine.getHistoricalWeatherPattern("Unknown", "June");
        assertThat(pattern).isNotNull();
        assertThat(pattern.get("climate_type")).isEqualTo("Unknown");
    }

    @Test
    void getHistoricalWeatherPattern_ForTamilNadu_ShouldReturnPattern() {
        Map<String, Object> pattern = engine.getHistoricalWeatherPattern("Tamil Nadu", "October");
        assertThat(pattern).isNotNull();
        assertThat(pattern.get("climate_type")).isEqualTo("Humid Tropical");
    }
}
