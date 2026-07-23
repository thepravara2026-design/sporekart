package com.sporekart.grower.copilot.dto;

import java.util.List;
import java.util.Map;

public record WeatherResponse(
    String location,
    WeatherData currentWeather,
    List<WeatherData> forecast,
    String climateRisk,
    Map<String, Object> impact
) {}
