package com.sporekart.grower.copilot.engine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WeatherIntegrationEngine {

    private static final Logger log = LoggerFactory.getLogger(WeatherIntegrationEngine.class);

    private final WeatherProvider weatherProvider;

    public WeatherIntegrationEngine() {
        this.weatherProvider = new SimulatedWeatherProvider();
        log.info("WeatherIntegrationEngine initialized with provider: {}", weatherProvider.providerName());
    }

    public WeatherIntegrationEngine(final WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
        log.info("WeatherIntegrationEngine initialized with provider: {}", weatherProvider.providerName());
    }

    public WeatherData getCurrentWeather(final String location) {
        log.debug("Fetching current weather for location: {}", location);
        return weatherProvider.getCurrentWeather(location);
    }

    public List<WeatherData> getForecast(final String location, final int days) {
        log.debug("Fetching {}-day forecast for location: {}", days, location);
        return weatherProvider.getForecast(location, days);
    }

    public String getWeatherImpact(final String location, final String cultivationStage) {
        log.debug("Assessing weather impact for {} at stage '{}'", location, cultivationStage);
        final WeatherData current = getCurrentWeather(location);
        final double temp = current.temperature();
        final double humidity = current.humidity();

        if ("spawning".equalsIgnoreCase(cultivationStage) || "incubation".equalsIgnoreCase(cultivationStage)) {
            if (temp < 20 || temp > 30) {
                return "HIGH RISK – Temperature " + temp + "°C is outside optimal range (20-30°C) for " + cultivationStage;
            }
            if (humidity > 85) {
                return "MODERATE RISK – High humidity may encourage contamination during " + cultivationStage;
            }
            return "FAVOURABLE – Conditions are suitable for " + cultivationStage;
        }

        if ("fruiting".equalsIgnoreCase(cultivationStage)) {
            if (temp < 22 || temp > 28) {
                return "MODERATE RISK – Temperature " + temp + "°C may reduce fruiting body formation";
            }
            if (humidity < 70) {
                return "CAUTION – Low humidity (" + humidity + "%) may affect pinhead formation";
            }
            return "FAVOURABLE – Good fruiting conditions";
        }

        return "NEUTRAL – No specific impact data for stage: " + cultivationStage;
    }

    public String getClimateRisk(final String location, final String season) {
        log.debug("Assessing climate risk for {} in {}", location, season);
        final String key = location.toLowerCase() + ":" + season.toLowerCase();
        return switch (key) {
            case "punjab:summer" -> "MODERATE – Heat waves possible; maintain shade and ventilation";
            case "punjab:winter" -> "LOW – Cool temperatures suitable for oyster mushrooms";
            case "karnataka:monsoon" -> "HIGH – Heavy rainfall and humidity spike; risk of contamination";
            case "karnataka:summer" -> "MODERATE – High temperatures require active cooling";
            case "maharashtra:monsoon" -> "HIGH – Persistent humidity >85%; strict sterilization needed";
            case "maharashtra:winter" -> "LOW – Mild temperatures ideal for button mushrooms";
            case "tamil nadu:summer" -> "HIGH – Extreme heat & humidity; use climate-controlled rooms";
            case "tamil nadu:winter" -> "MODERATE – Short cool window suitable for oyster cultivation";
            case "himachal pradesh:summer" -> "LOW – Pleasant temperatures; natural growing season";
            case "himachal pradesh:winter" -> "MODERATE – Cold may slow growth; heating may be needed";
            default -> "UNKNOWN – No climate risk data for " + location + " in " + season;
        };
    }

    public List<String> checkWeatherAlert(final String location) {
        log.debug("Checking weather alerts for location: {}", location);
        final List<String> alerts = new ArrayList<>();
        final WeatherData current = getCurrentWeather(location);

        if (current.temperature() > 35) {
            alerts.add("HEAT ALERT – Temperature " + current.temperature() + "°C exceeds safe threshold");
        }
        if (current.temperature() < 10) {
            alerts.add("COLD ALERT – Temperature " + current.temperature() + "°C may slow mycelial growth");
        }
        if (current.humidity() > 90) {
            alerts.add("HUMIDITY ALERT – " + current.humidity() + "% humidity increases contamination risk");
        }
        if (current.humidity() < 40) {
            alerts.add("LOW HUMIDITY ALERT – " + current.humidity() + "% humidity may cause substrate drying");
        }
        if (current.windSpeed() > 30) {
            alerts.add("WIND ALERT – Strong winds may spread contaminants");
        }
        if (current.rainfall() > 50) {
            alerts.add("RAIN ALERT – Heavy rainfall may affect outdoor growing structures");
        }

        if (alerts.isEmpty()) {
            alerts.add("NO ALERTS – Current conditions are within safe parameters");
        }
        return alerts;
    }

    public String getOptimalGrowingSeason(final String speciesName, final String location) {
        log.debug("Determining optimal growing season for {} in {}", speciesName, location);
        final String species = speciesName.toLowerCase();
        final String loc = location.toLowerCase();

        if (species.contains("oyster")) {
            return switch (loc) {
                case "punjab" -> "September to March (autumn through early spring)";
                case "karnataka" -> "November to February (post-monsoon cool period)";
                case "maharashtra" -> "October to March (winter season)";
                case "tamil nadu" -> "December to February (mild winter window)";
                case "himachal pradesh" -> "March to June & September to November (spring & autumn)";
                default -> "October to March (generic cool season)";
            };
        }
        if (species.contains("button")) {
            return switch (loc) {
                case "punjab" -> "October to March (requires 22-26°C substrate temperature)";
                case "karnataka" -> "Year-round in climate-controlled facilities only";
                case "maharashtra" -> "November to February (requires supplemental cooling)";
                case "tamil nadu" -> "Not recommended without climate-controlled environments";
                case "himachal pradesh" -> "April to October (temperate conditions ideal)";
                default -> "October to February (cool season with temperature management)";
            };
        }
        if (species.contains("shiitake")) {
            return switch (loc) {
                case "punjab" -> "September to November & March to May";
                case "karnataka" -> "December to February";
                case "maharashtra" -> "October to March";
                case "tamil nadu" -> "Limited window: December to January";
                case "himachal pradesh" -> "April to October (excellent natural conditions)";
                default -> "Spring and autumn seasons";
            };
        }
        if (species.contains("milky")) {
            return switch (loc) {
                case "punjab" -> "October to March (ideal for Calocybe indica)";
                case "karnataka" -> "November to March";
                case "maharashtra" -> "October to February";
                case "tamil nadu" -> "November to February";
                case "himachal pradesh" -> "April to September";
                default -> "October to March";
            };
        }
        if (species.contains("paddy") || species.contains("straw")) {
            return switch (loc) {
                case "punjab" -> "April to June & September to November";
                case "karnataka" -> "June to December (following paddy harvest)";
                case "maharashtra" -> "July to December (post-kharif season)";
                case "tamil nadu" -> "August to December (post-paddy season)";
                case "himachal pradesh" -> "May to October";
                default -> "Post-monsoon season";
            };
        }
        return "Consult local agricultural extension for specific timing in " + location;
    }

    public Map<String, Object> getHistoricalWeatherPattern(final String location, final String month) {
        log.debug("Fetching historical weather patterns for {} in {}", location, month);
        final Map<String, Object> patterns = new HashMap<>();
        final String loc = location.toLowerCase();

        switch (loc) {
            case "punjab" -> {
                patterns.put("avg_temperature_min", 7);
                patterns.put("avg_temperature_max", 40);
                patterns.put("avg_humidity", 45);
                patterns.put("annual_rainfall_mm", 650);
                patterns.put("climate_type", "Temperate / Semi-arid");
                patterns.put("notes", "Distinct seasons; hot summers, cool winters");
            }
            case "karnataka" -> {
                patterns.put("avg_temperature_min", 20);
                patterns.put("avg_temperature_max", 34);
                patterns.put("avg_humidity", 75);
                patterns.put("annual_rainfall_mm", 1200);
                patterns.put("climate_type", "Tropical");
                patterns.put("notes", "High humidity year-round; heavy monsoon Jun-Sep");
            }
            case "maharashtra" -> {
                patterns.put("avg_temperature_min", 16);
                patterns.put("avg_temperature_max", 38);
                patterns.put("avg_humidity", 65);
                patterns.put("annual_rainfall_mm", 950);
                patterns.put("climate_type", "Varied (coastal to semi-arid)");
                patterns.put("notes", "Monsoon Jun-Sep; moderate winters, hot summers");
            }
            case "tamil nadu" -> {
                patterns.put("avg_temperature_min", 22);
                patterns.put("avg_temperature_max", 37);
                patterns.put("avg_humidity", 80);
                patterns.put("annual_rainfall_mm", 900);
                patterns.put("climate_type", "Humid Tropical");
                patterns.put("notes", "Northeast monsoon Oct-Dec; consistently warm");
            }
            case "himachal pradesh" -> {
                patterns.put("avg_temperature_min", 2);
                patterns.put("avg_temperature_max", 30);
                patterns.put("avg_humidity", 55);
                patterns.put("annual_rainfall_mm", 1100);
                patterns.put("climate_type", "Cool Temperate");
                patterns.put("notes", "Snow in winter; pleasant summers; good for temperate mushrooms");
            }
            default -> {
                patterns.put("avg_temperature_min", 15);
                patterns.put("avg_temperature_max", 35);
                patterns.put("avg_humidity", 60);
                patterns.put("annual_rainfall_mm", 800);
                patterns.put("climate_type", "Unknown");
                patterns.put("notes", "No historical data available for " + location);
            }
        }

        patterns.put("month", month);
        patterns.put("location", location);
        return patterns;
    }

    public interface WeatherProvider {
        String providerName();
        WeatherData getCurrentWeather(String location);
        List<WeatherData> getForecast(String location, int days);
    }

    public record WeatherData(
        String location,
        double temperature,
        double humidity,
        double windSpeed,
        double rainfall,
        String condition,
        LocalDate date
    ) {}

    static class SimulatedWeatherProvider implements WeatherProvider {

        private static final Logger log = LoggerFactory.getLogger(SimulatedWeatherProvider.class);

        @Override
        public String providerName() {
            return "SimulatedWeatherProvider";
        }

        @Override
        public WeatherData getCurrentWeather(final String location) {
            log.debug("SimulatedWeatherProvider: fetching current weather for {}", location);
            final String loc = location.toLowerCase();
            return switch (loc) {
                case "punjab" -> new WeatherData("Punjab", 28.5, 44.0, 12.0, 0.0, "Clear", LocalDate.now());
                case "karnataka" -> new WeatherData("Karnataka", 31.2, 72.0, 8.5, 12.0, "Partly Cloudy", LocalDate.now());
                case "maharashtra" -> new WeatherData("Maharashtra", 30.0, 65.0, 15.0, 5.0, "Hazy", LocalDate.now());
                case "tamil nadu" -> new WeatherData("Tamil Nadu", 33.5, 78.0, 10.0, 0.0, "Sunny", LocalDate.now());
                case "himachal pradesh" -> new WeatherData("Himachal Pradesh", 22.0, 50.0, 6.0, 2.0, "Cloudy", LocalDate.now());
                default -> new WeatherData(location, 27.0, 60.0, 10.0, 0.0, "Fair", LocalDate.now());
            };
        }

        @Override
        public List<WeatherData> getForecast(final String location, final int days) {
            log.debug("SimulatedWeatherProvider: fetching {}-day forecast for {}", days, location);
            final List<WeatherData> forecast = new ArrayList<>();
            final WeatherData base = getCurrentWeather(location);
            for (int i = 1; i <= days; i++) {
                final double offset = (Math.sin(i * 1.5) * 3);
                forecast.add(new WeatherData(
                    location,
                    base.temperature() + offset,
                    base.humidity() + (Math.cos(i * 0.8) * 5),
                    base.windSpeed() + (Math.sin(i * 0.7) * 4),
                    Math.max(0, base.rainfall() + (Math.cos(i * 0.5) * 5)),
                    i % 2 == 0 ? "Sunny" : "Cloudy",
                    LocalDate.now().plusDays(i)
                ));
            }
            return forecast;
        }
    }
}
