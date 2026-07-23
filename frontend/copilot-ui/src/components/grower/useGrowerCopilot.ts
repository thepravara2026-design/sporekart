import { useState, useCallback } from 'react';
import type {
  SpawnRecommendation,
  SubstrateRecommendation,
  DiseaseInfo,
  YieldPrediction,
  WeatherData,
  BusinessPlan,
  KnowledgeArticle,
  ChatMessage,
  CropHealth,
} from './types/grower';

interface CopilotState {
  messages: ChatMessage[];
  spawnRecommendations: SpawnRecommendation[];
  substrateRecommendations: SubstrateRecommendation[];
  diseaseDiagnoses: DiseaseInfo[];
  yieldPredictions: YieldPrediction[];
  weather: WeatherData | null;
  forecast: WeatherData[];
  businessPlans: BusinessPlan[];
  knowledgeResults: KnowledgeArticle[];
  cropHealth: CropHealth | null;
  loading: boolean;
  streaming: boolean;
  error: string | null;
}

const initialState: CopilotState = {
  messages: [],
  spawnRecommendations: [],
  substrateRecommendations: [],
  diseaseDiagnoses: [],
  yieldPredictions: [],
  weather: null,
  forecast: [],
  businessPlans: [],
  knowledgeResults: [],
  cropHealth: null,
  loading: false,
  streaming: false,
  error: null,
};

export function useGrowerCopilot() {
  const [state, setState] = useState<CopilotState>(initialState);

  const setLoading = useCallback((v: boolean) => setState(s => ({ ...s, loading: v })), []);
  const setStreaming = useCallback((v: boolean) => setState(s => ({ ...s, streaming: v })), []);
  const setError = useCallback((e: string | null) => setState(s => ({ ...s, error: e })), []);

  const appendMessage = useCallback((msg: ChatMessage) => {
    setState(s => ({ ...s, messages: [...s.messages, msg] }));
  }, []);

  const sendMessage = useCallback(async (content: string) => {
    const userMsg: ChatMessage = {
      id: crypto.randomUUID(),
      role: 'user',
      content,
      timestamp: new Date().toISOString(),
    };
    appendMessage(userMsg);
    setLoading(true);
    setError(null);
    try {
      const assistantMsg: ChatMessage = {
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `Echo: ${content}`,
        timestamp: new Date().toISOString(),
      };
      appendMessage(assistantMsg);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'sendMessage failed');
    } finally {
      setLoading(false);
    }
  }, [appendMessage, setLoading, setError]);

  const sendStreamMessage = useCallback(async (content: string) => {
    const userMsg: ChatMessage = {
      id: crypto.randomUUID(),
      role: 'user',
      content,
      timestamp: new Date().toISOString(),
    };
    appendMessage(userMsg);
    setStreaming(true);
    setError(null);
    try {
      const assistantMsg: ChatMessage = {
        id: crypto.randomUUID(),
        role: 'assistant',
        content: `Stream echo: ${content}`,
        timestamp: new Date().toISOString(),
      };
      appendMessage(assistantMsg);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'sendStreamMessage failed');
    } finally {
      setStreaming(false);
    }
  }, [appendMessage, setStreaming, setError]);

  const recommendSpawn = useCallback(async (_params?: Record<string, unknown>) => {
    setLoading(true);
    setError(null);
    try {
      const mock: SpawnRecommendation[] = [{
        spawnId: 's1', speciesName: 'Oyster', variety: 'Blue',
        difficulty: 'Beginner', optimalTempLow: 18, optimalTempHigh: 24,
        optimalHumidityLow: 85, optimalHumidityHigh: 95,
        spawnRunDays: 14, harvestDays: 21, expectedYieldKg: 2.5,
        climateSuitability: 'Tropical', description: 'Fast-growing oyster variety.',
        advantages: ['Fast colonization', 'High yield'], disadvantages: ['Sensitive to CO₂'],
      }];
      setState(s => ({ ...s, spawnRecommendations: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'recommendSpawn failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const recommendSubstrate = useCallback(async (_params?: Record<string, unknown>) => {
    setLoading(true);
    setError(null);
    try {
      const mock: SubstrateRecommendation[] = [{
        substrateId: 'sub1', name: 'Hardwood Sawdust', type: 'Lignin-rich',
        description: 'Fine hardwood sawdust supplemented with bran.',
        moisturePercent: 60, sterilizationMethod: 'Autoclave 121°C',
        preparationDays: 2, costPerKg: 1.5,
        suitableSpecies: ['Oyster', 'Shiitake'],
        advantages: ['Widely available', 'Consistent results'], disadvantages: ['Requires sterilization'],
      }];
      setState(s => ({ ...s, substrateRecommendations: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'recommendSubstrate failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const diagnoseDisease = useCallback(async (_params?: Record<string, unknown>) => {
    setLoading(true);
    setError(null);
    try {
      const mock: DiseaseInfo[] = [{
        diseaseId: 'd1', diseaseName: 'Green Mold', scientificName: 'Trichoderma spp.',
        category: 'Fungal', symptoms: ['Green sporulation', 'Soft decay'],
        possibleCauses: ['Contaminated spawn', 'Poor hygiene'],
        probabilityScore: 85, severity: 'HIGH',
        treatmentPlan: 'Remove affected bags, apply hydrogen peroxide.',
        preventionMethods: ['Sterilize substrate', 'Maintain air flow'],
        scientificReferences: ['doi:10.xxxx/trichoderma'],
      }];
      setState(s => ({ ...s, diseaseDiagnoses: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'diagnoseDisease failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const predictYield = useCallback(async (_params?: Record<string, unknown>) => {
    setLoading(true);
    setError(null);
    try {
      const mock: YieldPrediction[] = [{
        predictionId: 'y1', expectedYieldKg: 3.2, yieldEfficiency: 0.85,
        estimatedRevenue: 3200, productionCost: 1800, profitMargin: 43.75,
        riskScore: 25, harvestWindow: '2026-08-15 – 2026-09-01',
        recommendations: 'Increase ventilation during fruiting.',
      }];
      setState(s => ({ ...s, yieldPredictions: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'predictYield failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getWeather = useCallback(async (location?: string) => {
    setLoading(true);
    setError(null);
    try {
      const mock: WeatherData = {
        location: location ?? 'Mumbai', temperatureCelsius: 28,
        humidityPercent: 82, condition: 'Partly Cloudy',
        forecastDate: new Date().toISOString(), climateRisk: 'Low',
      };
      setState(s => ({ ...s, weather: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'getWeather failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getForecast = useCallback(async (location?: string) => {
    setLoading(true);
    setError(null);
    try {
      const baseDay = new Date();
      const mock: WeatherData[] = Array.from({ length: 5 }, (_, i) => {
        const d = new Date(baseDay);
        d.setDate(d.getDate() + i + 1);
        return {
          location: location ?? 'Mumbai',
          temperatureCelsius: 26 + Math.round(Math.random() * 6),
          humidityPercent: 75 + Math.round(Math.random() * 15),
          condition: ['Sunny', 'Cloudy', 'Light Rain', 'Humid', 'Clear'][i],
          forecastDate: d.toISOString(),
          climateRisk: ['Low', 'Low', 'Moderate', 'Low', 'Low'][i],
        };
      });
      setState(s => ({ ...s, forecast: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'getForecast failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const createBusinessPlan = useCallback(async (_params?: Record<string, unknown>) => {
    setLoading(true);
    setError(null);
    try {
      const mock: BusinessPlan[] = [{
        planId: 'bp1', speciesName: 'Shiitake', investmentAmount: 50000,
        expectedRevenue: 120000, expectedRoi: 140, cycleDurationDays: 90,
        marketInsights: ['Growing demand in organic market'],
        pricingSuggestions: ['₹400/kg fresh', '₹2500/kg dried'],
      }];
      setState(s => ({ ...s, businessPlans: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'createBusinessPlan failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const searchKnowledge = useCallback(async (_query: string) => {
    setLoading(true);
    setError(null);
    try {
      const mock: KnowledgeArticle[] = [{
        articleId: 'k1', title: 'Mushroom Cultivation Basics',
        content: 'Mushrooms require humid, dark environments...',
        source: 'Research Institute', type: 'Guide', citation: 'AgriKnow 2026',
      }];
      setState(s => ({ ...s, knowledgeResults: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'searchKnowledge failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getCropHealth = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const mock: CropHealth = {
        healthScore: 78,
        issues: ['Slight yellowing on edges'],
        recommendations: ['Increase misting frequency'],
        parameterDeviations: {
          temperature: { current: 26, optimal: 24, deviation: 2 },
          humidity: { current: 80, optimal: 88, deviation: -8 },
        },
      };
      setState(s => ({ ...s, cropHealth: mock }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : 'getCropHealth failed');
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const clearError = useCallback(() => setError(null), [setError]);
  const resetState = useCallback(() => setState(initialState), []);

  return {
    ...state,
    sendMessage,
    sendStreamMessage,
    recommendSpawn,
    recommendSubstrate,
    diagnoseDisease,
    predictYield,
    getWeather,
    getForecast,
    createBusinessPlan,
    searchKnowledge,
    getCropHealth,
    clearError,
    resetState,
  };
}
