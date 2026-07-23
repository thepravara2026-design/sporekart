export interface CultivationStage { stageId: string; stageName: string; stageOrder: number; description: string; durationDays: number; optimalTempCelsius: number; optimalHumidityPercent: number; lightRequirement: string; co2Ppm: number; ventilationRequirement: string; commonIssues: string[]; }

export interface SpawnRecommendation { spawnId: string; speciesName: string; variety: string; difficulty: string; optimalTempLow: number; optimalTempHigh: number; optimalHumidityLow: number; optimalHumidityHigh: number; spawnRunDays: number; harvestDays: number; expectedYieldKg: number; climateSuitability: string; description: string; advantages: string[]; disadvantages: string[]; }

export interface SubstrateRecommendation { substrateId: string; name: string; type: string; description: string; moisturePercent: number; sterilizationMethod: string; preparationDays: number; costPerKg: number; suitableSpecies: string[]; advantages: string[]; disadvantages: string[]; }

export interface DiseaseInfo { diseaseId: string; diseaseName: string; scientificName: string; category: string; symptoms: string[]; possibleCauses: string[]; probabilityScore: number; severity: string; treatmentPlan: string; preventionMethods: string[]; scientificReferences: string[]; }

export interface YieldPrediction { predictionId: string; expectedYieldKg: number; yieldEfficiency: number; estimatedRevenue: number; productionCost: number; profitMargin: number; riskScore: number; harvestWindow: string; recommendations: string; }

export interface WeatherData { location: string; temperatureCelsius: number; humidityPercent: number; condition: string; forecastDate: string; climateRisk: string; }

export interface BusinessPlan { planId: string; speciesName: string; investmentAmount: number; expectedRevenue: number; expectedRoi: number; cycleDurationDays: number; marketInsights: string[]; pricingSuggestions: string[]; }

export interface KnowledgeArticle { articleId: string; title: string; content: string; source: string; type: string; citation: string; }

export interface ChatMessage { id: string; role: 'user' | 'assistant'; content: string; timestamp: string; suggestions?: Suggestion[]; }

export interface Suggestion { label: string; action: string; payload?: Record<string, unknown>; }

export interface CropHealth { healthScore: number; issues: string[]; recommendations: string[]; parameterDeviations: Record<string, {current: number; optimal: number; deviation: number}>; }
