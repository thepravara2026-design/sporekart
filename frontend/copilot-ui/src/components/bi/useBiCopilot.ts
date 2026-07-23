import { useState, useCallback } from 'react';
import type {
  RevenueMetrics,
  CustomerAnalytics,
  TrainingAnalytics,
  CultivationAnalytics,
  BusinessInsight,
  TrendDataPoint,
  AnomalyAlert,
  ForecastResult,
  CrossCopilotMetric,
  CustomerSegment,
  DashboardData,
  ChatMessage,
} from './types/bi';

export interface BiCopilotState {
  loading: boolean;
  error: string | null;
  revenueMetrics: RevenueMetrics | null;
  customerAnalytics: CustomerAnalytics | null;
  trainingAnalytics: TrainingAnalytics | null;
  cultivationAnalytics: CultivationAnalytics | null;
  insights: BusinessInsight[];
  trends: TrendDataPoint[];
  anomalies: AnomalyAlert[];
  forecast: ForecastResult | null;
  crossCopilotMetrics: CrossCopilotMetric[];
  segments: CustomerSegment[];
  dashboard: DashboardData | null;
  messages: ChatMessage[];
  streaming: boolean;
}

const initialState: BiCopilotState = {
  loading: false,
  error: null,
  revenueMetrics: null,
  customerAnalytics: null,
  trainingAnalytics: null,
  cultivationAnalytics: null,
  insights: [],
  trends: [],
  anomalies: [],
  forecast: null,
  crossCopilotMetrics: [],
  segments: [],
  dashboard: null,
  messages: [],
  streaming: false,
};

export function useBiCopilot() {
  const [state, setState] = useState<BiCopilotState>(initialState);

  const setLoading = useCallback((loading: boolean) => {
    setState(prev => ({ ...prev, loading, error: loading ? null : prev.error }));
  }, []);

  const setError = useCallback((error: string | null) => {
    setState(prev => ({ ...prev, error, loading: false }));
  }, []);

  const queryData = useCallback(async <T,>(endpoint: string): Promise<T | null> => {
    setLoading(true);
    try {
      const response = await fetch(`/api/bi/${endpoint}`);
      if (!response.ok) throw new Error(`Failed to fetch ${endpoint}`);
      const data = await response.json();
      return data as T;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Unknown error';
      setError(message);
      return null;
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getDashboard = useCallback(async (dashboardType?: string) => {
    const params = dashboardType ? `?type=${dashboardType}` : '';
    const data = await queryData<DashboardData>(`dashboard${params}`);
    if (data) setState(prev => ({ ...prev, dashboard: data }));
    return data;
  }, [queryData]);

  const generateReport = useCallback(async (reportConfig: Record<string, unknown>) => {
    setLoading(true);
    try {
      const response = await fetch('/api/bi/reports', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(reportConfig),
      });
      if (!response.ok) throw new Error('Failed to generate report');
      const blob = await response.blob();
      return blob;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Unknown error';
      setError(message);
      return null;
    } finally {
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getInsights = useCallback(async () => {
    const data = await queryData<BusinessInsight[]>('insights');
    if (data) setState(prev => ({ ...prev, insights: data }));
    return data;
  }, [queryData]);

  const getTrends = useCallback(async (metric?: string) => {
    const params = metric ? `?metric=${metric}` : '';
    const data = await queryData<TrendDataPoint[]>(`trends${params}`);
    if (data) setState(prev => ({ ...prev, trends: data }));
    return data;
  }, [queryData]);

  const getAnomalies = useCallback(async () => {
    const data = await queryData<AnomalyAlert[]>('anomalies');
    if (data) setState(prev => ({ ...prev, anomalies: data }));
    return data;
  }, [queryData]);

  const forecast = useCallback(async (metric: string, periods: number) => {
    const data = await queryData<ForecastResult>(`forecast?metric=${metric}&periods=${periods}`);
    if (data) setState(prev => ({ ...prev, forecast: data }));
    return data;
  }, [queryData]);

  const getCrossCopilotMetrics = useCallback(async () => {
    const data = await queryData<CrossCopilotMetric[]>('cross-copilot');
    if (data) setState(prev => ({ ...prev, crossCopilotMetrics: data }));
    return data;
  }, [queryData]);

  const getSegments = useCallback(async () => {
    const data = await queryData<CustomerSegment[]>('segments');
    if (data) setState(prev => ({ ...prev, segments: data }));
    return data;
  }, [queryData]);

  const sendMessage = useCallback(async (content: string) => {
    const userMessage: ChatMessage = {
      id: crypto.randomUUID(),
      role: 'user',
      content,
      timestamp: new Date().toISOString(),
    };
    setState(prev => ({ ...prev, messages: [...prev.messages, userMessage], loading: true }));
    try {
      const response = await fetch('/api/bi/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: content }),
      });
      if (!response.ok) throw new Error('Failed to send message');
      const data = await response.json();
      const assistantMessage: ChatMessage = {
        id: crypto.randomUUID(),
        role: 'assistant',
        content: data.response,
        timestamp: new Date().toISOString(),
        suggestions: data.suggestions,
      };
      setState(prev => ({ ...prev, messages: [...prev.messages, assistantMessage], loading: false }));
      return assistantMessage;
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Unknown error';
      setError(message);
      return null;
    }
  }, [setError]);

  const sendStreamMessage = useCallback(async (content: string) => {
    const userMessage: ChatMessage = {
      id: crypto.randomUUID(),
      role: 'user',
      content,
      timestamp: new Date().toISOString(),
    };
    setState(prev => ({
      ...prev,
      messages: [...prev.messages, userMessage],
      streaming: true,
    }));
    try {
      const response = await fetch('/api/bi/chat/stream', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: content }),
      });
      if (!response.ok) throw new Error('Failed to stream message');
      const reader = response.body?.getReader();
      if (!reader) throw new Error('No reader available');

      const assistantId = crypto.randomUUID();
      const assistantMessage: ChatMessage = {
        id: assistantId,
        role: 'assistant',
        content: '',
        timestamp: new Date().toISOString(),
      };
      setState(prev => ({ ...prev, messages: [...prev.messages, assistantMessage] }));

      const decoder = new TextDecoder();
      let accumulated = '';

      while (true) {
        const { done, value } = await reader.read();
        if (done) break;
        const chunk = decoder.decode(value, { stream: true });
        accumulated += chunk;
        setState(prev => ({
          ...prev,
          messages: prev.messages.map(m =>
            m.id === assistantId ? { ...m, content: accumulated } : m
          ),
        }));
      }

      setState(prev => ({
        ...prev,
        streaming: false,
        messages: prev.messages.map(m =>
          m.id === assistantId ? { ...m, content: accumulated } : m
        ),
      }));
    } catch (err) {
      const message = err instanceof Error ? err.message : 'Unknown error';
      setError(message);
      setState(prev => ({ ...prev, streaming: false }));
    }
  }, [setError]);

  return {
    ...state,
    queryData,
    getDashboard,
    generateReport,
    getInsights,
    getTrends,
    getAnomalies,
    forecast,
    getCrossCopilotMetrics,
    getSegments,
    sendMessage,
    sendStreamMessage,
  };
}
