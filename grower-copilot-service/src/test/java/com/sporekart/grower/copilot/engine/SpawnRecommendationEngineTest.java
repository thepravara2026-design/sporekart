package com.sporekart.grower.copilot.engine;

import com.sporekart.grower.copilot.domain.SpawnRecommendation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SpawnRecommendationEngineTest {

    private SpawnRecommendationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new SpawnRecommendationEngine();
    }

    @Test
    void recommendSpawn_ByQuery_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawn("oyster");
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).speciesName()).toLowerCase().contains("oyster");
    }

    @Test
    void recommendSpawn_ByQueryButton_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawn("button");
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).speciesName()).toLowerCase().contains("button");
    }

    @Test
    void recommendSpawn_ByQueryShiitake_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawn("shiitake");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSpawn_ByQueryMilky_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawn("milky");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSpawn_ByQueryUnknown_ShouldReturnEmpty() {
        List<SpawnRecommendation> results = engine.recommendSpawn("nonexistent");
        assertThat(results).isEmpty();
    }

    @Test
    void recommendSpawn_ByDifficulty_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawnByDifficulty("easy");
        assertThat(results).isNotEmpty();
        results.forEach(r -> assertThat(r.difficulty()).isEqualToIgnoringCase("easy"));
    }

    @Test
    void recommendSpawn_ByDifficultyIntermediate_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawnByDifficulty("intermediate");
        assertThat(results).isNotEmpty();
        results.forEach(r -> assertThat(r.difficulty()).isEqualToIgnoringCase("intermediate"));
    }

    @Test
    void recommendSpawn_ByDifficultyExpert_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawnByDifficulty("expert");
        assertThat(results).isNotEmpty();
        results.forEach(r -> assertThat(r.difficulty()).isEqualToIgnoringCase("expert"));
    }

    @Test
    void recommendSpawn_ByDifficultyUnknown_ShouldReturnEmpty() {
        List<SpawnRecommendation> results = engine.recommendSpawnByDifficulty("impossible");
        assertThat(results).isEmpty();
    }

    @Test
    void recommendSpawn_ByClimate_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawnByClimate("tropical");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSpawn_ByClimateTemperate_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawnByClimate("temperate");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSpawn_ByClimateArid_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawnByClimate("arid");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSpawn_ByClimateContinental_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawnByClimate("continental");
        assertThat(results).isNotEmpty();
    }

    @Test
    void recommendSpawn_ByClimateUnknown_ShouldReturnEmpty() {
        List<SpawnRecommendation> results = engine.recommendSpawnByClimate("unknown");
        assertThat(results).isEmpty();
    }

    @Test
    void getAllSpawns_ShouldReturnAll() {
        List<SpawnRecommendation> all = engine.getAllSpawns();
        assertThat(all).isNotEmpty();
        assertThat(all.size()).isGreaterThan(3);
    }

    @Test
    void getSpawnById_ShouldReturnSpawn() {
        SpawnRecommendation spawn = engine.getSpawnById("SPAWN-001");
        assertThat(spawn).isNotNull();
        assertThat(spawn.spawnId()).isEqualTo("SPAWN-001");
    }

    @Test
    void getSpawnById_WithInvalidId_ShouldReturnNull() {
        SpawnRecommendation spawn = engine.getSpawnById("INVALID");
        assertThat(spawn).isNull();
    }

    @Test
    void compareSpawns_ShouldReturnComparison() {
        String comparison = engine.compareSpawns("SPAWN-001", "SPAWN-002");
        assertThat(comparison).isNotBlank();
    }

    @Test
    void compareSpawns_WithSameId_ShouldReturnComparison() {
        String comparison = engine.compareSpawns("SPAWN-001", "SPAWN-001");
        assertThat(comparison).isNotBlank();
    }

    @Test
    void compareSpawns_WithInvalidId_ShouldReturnDefault() {
        String comparison = engine.compareSpawns("INVALID", "SPAWN-001");
        assertThat(comparison).isNotBlank();
    }

    @Test
    void getSpeciesDetails_ShouldReturnDetails() {
        String details = engine.getSpeciesDetails("Oyster");
        assertThat(details).isNotBlank();
        assertThat(details).containsIgnoringCase("oyster");
    }

    @Test
    void getSpeciesDetails_ForButton_ShouldReturnDetails() {
        String details = engine.getSpeciesDetails("Button");
        assertThat(details).isNotBlank();
    }

    @Test
    void getSpeciesDetails_ForShiitake_ShouldReturnDetails() {
        String details = engine.getSpeciesDetails("Shiitake");
        assertThat(details).isNotBlank();
    }

    @Test
    void getSpeciesDetails_ForUnknown_ShouldReturnDefault() {
        String details = engine.getSpeciesDetails("Unknown");
        assertThat(details).isNotBlank();
    }

    @Test
    void recommendSpawn_ByQueryPaddyStraw_ShouldReturnRecommendations() {
        List<SpawnRecommendation> results = engine.recommendSpawn("paddy straw");
        assertThat(results).isNotEmpty();
    }

    @Test
    void getAllSpawns_ShouldContainExpectedSpecies() {
        List<SpawnRecommendation> all = engine.getAllSpawns();
        List<String> species = all.stream().map(SpawnRecommendation::speciesName).toList();
        assertThat(species).contains("Oyster");
    }
}
