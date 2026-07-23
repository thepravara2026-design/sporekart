# Weather Abstraction Architecture

## Provider Interface

```java
public interface WeatherProvider {
    WeatherData getCurrentWeather(Location location);
    Forecast getForecast(Location location, int days);
    HistoricalData getHistoricalData(Location location, LocalDate start, LocalDate end);
    ClimateRisk assessClimateRisk(Location location, CropType cropType);
    boolean isAvailable();
}
```

## Implementations

### SimulatedWeatherProvider

Used in development and testing environments. Generates realistic weather patterns based on:

- **Seasonal profiles** — Pre-defined temperature and humidity ranges for each target region
- **Diurnal variation** — Temperature oscillation: min at 05:00, max at 14:00
- **Random perturbation** — ±3°C, ±10% humidity, 20% chance of precipitation
- **Extreme event simulation** — Configurable probability of heatwave, cold snap, heavy rain

Configuration:

```yaml
grower-copilot:
  weather:
    provider: simulated
    simulated:
      base-temperature: 28
      temperature-range: [18, 38]
      humidity-range: [60, 95]
      monsoon-start-day: 150   # June 1
      monsoon-end-day: 275     # October 1
```

### OpenMeteoProvider (planned)

REST-based integration with Open-Meteo API (free tier, no API key required).

- **API Base:** `https://api.open-meteo.com/v1/forecast`
- **Parameters:** Latitude, longitude, hourly (temperature_2m, relative_humidity_2m, precipitation)
- **Rate limit:** 10,000 requests/day (free tier)

### IMDWeatherProvider (future)

Planned integration with India Meteorological Department API:

- **API Base:** `https://api.imd.gov.in/v1/` (requires registration)
- **Data granularity:** District-level forecasts, 7-day outlook
- **Features:** Agromet advisory bulletins, rainfall probability, heatwave warnings
- **Status:** Awaiting IMD API registration approval

## Regional Data

### Supported Regions

| Region | Lat/Lon (approx) | Climate Type | Typical Monsoon |
|--------|-------------------|--------------|-----------------|
| Solan, Himachal Pradesh | 30.90°N, 77.10°E | Temperate | Jul–Sep |
| Palampur, Himachal | 32.11°N, 76.53°E | Temperate | Jul–Sep |
| Dehradun, Uttarakhand | 30.32°N, 78.03°E | Sub-temperate | Jul–Sep |
| Pune, Maharashtra | 18.52°N, 73.85°E | Tropical monsoon | Jun–Sep |
| Bengaluru, Karnataka | 12.97°N, 77.59°E | Tropical savanna | Jun–Sep |
| Bhubaneswar, Odisha | 20.30°N, 85.83°E | Tropical monsoon | Jun–Sep |
| Kochi, Kerala | 9.93°N, 76.27°E | Tropical monsoon | Jun–Sep |
| Patna, Bihar | 25.59°N, 85.14°E | Humid subtropical | Jun–Sep |

## Forecast

The `ForecastResponse` includes:

```json
{
  "location": { "lat": 30.90, "lon": 77.10, "name": "Solan, HP" },
  "current": {
    "temperature": 24.5,
    "humidity": 82,
    "precipitation": 2.3,
    "wind_speed": 5.2,
    "timestamp": "2026-07-23T10:00:00+05:30"
  },
  "daily": [
    {
      "date": "2026-07-23",
      "temp_high": 28.0,
      "temp_low": 18.0,
      "humidity_avg": 78,
      "precipitation_probability": 40,
      "condition": "partly_cloudy"
    }
  ],
  "advisory": {
    "grower_recommendation": "Monitor humidity — levels above 85% increase bacterial blotch risk.",
    "risk_level": "moderate",
    "affected_species": ["white_button", "oyster"]
  }
}
```

## Climate Risk Assessment

The climate risk assessment evaluates the suitability of a location for a given crop across three time horizons:

### Short-term (7 days)

- Forecast-based alerts for extreme weather events
- Real-time adjustments to cultivation recommendations
- Disease risk correlation (e.g., high humidity + high temp = elevated bacterial risk)

### Seasonal (3 months)

- Monsoon onset/offset prediction
- Seasonal temperature anomaly detection
- Pest and disease calendar shifts

### Long-term (1 year)

- Climate trend analysis from historical data
- Suitability scoring for species selection per region
- Infrastructure recommendations (e.g., polyhouse requirement for temperate species in tropical regions)

### Risk Scoring

```
ClimateRiskScore = (TempAnomaly × 0.3) + (RainfallAnomaly × 0.25)
                 + (ExtremeEvents × 0.25) + (HumidityStress × 0.2)

Where:
  TempAnomaly     = |avg_temp - optimal_temp| / optimal_temp_range
  RainfallAnomaly = |rainfall - ideal_rainfall| / ideal_rainfall
  ExtremeEvents   = count of extreme days in forecast / forecast_period
  HumidityStress  = |humidity - optimal_humidity| / 50
```

Score interpretation: 0–0.2 (low risk), 0.2–0.4 (moderate), 0.4–0.7 (high), 0.7+ (critical).
