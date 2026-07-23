package com.sporekart.grower.copilot.domain;

public record WeatherData(
    String location,
    double temperatureCelsius,
    double humidityPercent,
    double rainfallMm,
    double windSpeedKmh,
    String condition,
    String forecastDate,
    double forecastTempHigh,
    double forecastTempLow,
    double forecastHumidity,
    double forecastRainfall,
    String climateRisk,
    String weatherImpact
) {}
