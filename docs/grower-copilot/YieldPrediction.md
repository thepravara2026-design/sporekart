# Yield Prediction Engine

## Species-Specific Yield Formulas

Yield predictions use species-specific baseline yields adjusted by environmental, material, and management factors.

### White Button Mushroom (_Agaricus bisporus_)

```
Y_btn = B_btn × E_temp × E_hum × C_comp × H_stage
```

| Variable | Description | Default |
|----------|-------------|---------|
| `B_btn` | Baseline: 25–30 kg/m² per cycle (6–8 flushes) | 28 kg/m² |
| `E_temp` | Temperature efficiency: 20–26°C optimal (1.0); -0.05 per °C outside range | 1.0 |
| `E_hum` | Humidity efficiency: 80–90% optimal (1.0); -0.03 per 5% outside range | 1.0 |
| `C_comp` | Compost quality factor: 0.7 (poor) to 1.2 (excellent) | 1.0 |
| `H_stage` | Harvest stage factor: flush 1 (0.35), flush 2 (0.25), flush 3 (0.18), flush 4+ (0.22 total) | Varies |

### Oyster Mushroom (_Pleurotus ostreatus_)

```
Y_oys = B_oys × E_temp × E_hum × S_bag × T_spawn
```

| Variable | Description | Default |
|----------|-------------|---------|
| `B_oys` | Baseline: 1.5–2.5 kg per 5 kg bag | 2.0 kg/bag |
| `E_temp` | Temperature efficiency: 20–28°C optimal | 1.0 |
| `E_hum` | Humidity efficiency: 70–85% optimal | 1.0 |
| `S_bag` | Substrate bag quality: 0.6–1.2 | 1.0 |
| `T_spawn` | Spawn run quality: 0.7–1.1 | 1.0 |

### Shiitake (_Lentinula edodes_)

```
Y_shi = B_shi × L_age × E_temp × E_hum × W_log
```

| Variable | Description | Default |
|----------|-------------|---------|
| `B_shi` | Baseline: 0.5–1.5 kg per log per year | 1.0 kg/log/yr |
| `L_age` | Log age factor: Year 2 (1.0), Year 3 (0.85), Year 4 (0.65) | 1.0 |
| `W_log` | Log weight factor: kg/10 | Varies |

### Milky Mushroom (_Calocybe indica_)

```
Y_mil = B_mil × E_temp × C_bed × W_water × H_vent
```

| Variable | Description | Default |
|----------|-------------|---------|
| `B_mil` | Baseline: 400–600 g per bed | 500 g/bed |
| `E_temp` | Temperature efficiency: 25–35°C optimal | 1.0 |
| `C_bed` | Bed casing quality: 0.6–1.2 | 1.0 |
| `W_water` | Water management: 0.7–1.1 | 1.0 |
| `H_vent` | Ventilation factor: 0.7–1.1 | 1.0 |

### Paddy Straw Mushroom (_Volvariella volvacea_)

```
Y_pad = B_pad × E_temp × E_hum × S_straw × T_compost
```

| Variable | Description | Default |
|----------|-------------|---------|
| `B_pad` | Baseline: 2–4 kg per 10 kg straw | 3 kg/batch |
| `E_temp` | Temperature efficiency: 30–38°C optimal | 1.0 |
| `E_hum` | Humidity efficiency: 75–90% optimal | 1.0 |
| `S_straw` | Straw quality factor: 0.6–1.2 | 1.0 |

## Efficiency Calculation

```
Efficiency = ActualYield / PredictedYield × 100

Benchmarks:
- >90%    Excellent
- 75–90%  Good
- 60–75%  Average
- <60%    Needs improvement
```

## Revenue / Cost Estimation

```
Revenue = Yield_kg × Price_per_kg
Cost = Spawn_cost + Substrate_cost + Labor_cost + Utility_cost + Other_cost
NetProfit = Revenue - Cost
ROI = (NetProfit / Cost) × 100
```

Default price inputs sourced from:
- National Horticulture Board (NHB) monthly bulletin
- State-level Agricultural Marketing Board reports
- Local mandi prices (when available)

## Risk Scoring

Composite risk score (0–100) incorporating:

| Factor | Weight | Description |
|--------|--------|-------------|
| Disease Risk | 30% | Proximity to known outbreaks (from DiseaseEngine) |
| Weather Risk | 25% | Forecast anomalies (from WeatherEngine) |
| Price Volatility | 15% | Historical price variability for species |
| Input Quality | 15% | Spawn/substrate quality score |
| Grower Experience | 10% | Historical success rate (grower profile) |
| Seasonality | 5% | Seasonal suitability for species |

**Risk Levels:**
- 0–30: Low (green)
- 31–55: Moderate (amber)
- 56–75: High (orange)
- 76–100: Critical (red)

## Harvest Window Prediction

```
Days_to_first_harvest = Baseline_days × (1 + ∑(Deviation_penalties))

First_flush_window:
  White button:  18–24 days after casing
  Oyster:        14–21 days after spawning
  Shiitake:      7–14 days after log soaking
  Milky:         18–22 days after casing
  Paddy straw:   8–12 days after spawning
```

Window adjustments based on:
- **Temperature deviation:** +1 day per 2°C below optimal
- **Humidity deviation:** +1 day per 10% below optimal
- **Substrate quality:** +2–5 days for poor quality
- **Seasonal baseline shift:** +0–7 days based on season
