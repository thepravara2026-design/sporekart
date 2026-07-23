package com.sporekart.grower.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sporekart.grower.copilot")
public class GrowerCopilotConfig {

    private int defaultSpawnRate = 5;
    private String defaultRegion = "temperate";
    private List<String> climateTypes = List.of("temperate", "tropical", "arid", "continental");
    private int maxCultivationCycles = 12;
    private String aiModel = "gpt-4o";

    public int getDefaultSpawnRate() { return defaultSpawnRate; }
    public void setDefaultSpawnRate(int v) { this.defaultSpawnRate = v; }
    public String getDefaultRegion() { return defaultRegion; }
    public void setDefaultRegion(String v) { this.defaultRegion = v; }
    public List<String> getClimateTypes() { return climateTypes; }
    public void setClimateTypes(List<String> v) { this.climateTypes = v; }
    public int getMaxCultivationCycles() { return maxCultivationCycles; }
    public void setMaxCultivationCycles(int v) { this.maxCultivationCycles = v; }
    public String getAiModel() { return aiModel; }
    public void setAiModel(String v) { this.aiModel = v; }
}
