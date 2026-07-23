import { useState, useCallback } from 'react';
import { AdminDashboard, BusinessInsight, ForecastResult, OperationalAlert, PerformanceReport } from '../types/admin';
import { ReportConfig } from '../AdminReportGenerator';

interface AdminCopilotState {
  dashboard: AdminDashboard | null;
  insights: BusinessInsight[];
  forecast: ForecastResult | null;
  alerts: OperationalAlert[];
  report: PerformanceReport | null;
  loading: boolean;
  error: string | null;
}

interface AdminCopilotActions {
  getDashboard: () => Promise<void>;
  getInsights: () => Promise<void>;
  generateReport: (config: ReportConfig) => Promise<void>;
  getForecast: (metric: string, period: string, horizon: number) => Promise<void>;
  getAlerts: () => Promise<void>;
  clearError: () => void;
}

type AdminCopilotHook = AdminCopilotState & AdminCopilotActions;

const API_BASE = '/api/v1/copilot/admin';

async function fetchJson<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });
  if (!response.ok) {
    throw new Error(`API error: ${response.status} ${response.statusText}`);
  }
  return response.json();
}

const initialState: AdminCopilotState = {
  dashboard: null,
  insights: [],
  forecast: null,
  alerts: [],
  report: null,
  loading: false,
  error: null
};

export function useAdminCopilot(): AdminCopilotHook {
  const [state, setState] = useState<AdminCopilotState>(initialState);

  const setLoading = useCallback((loading: boolean) => {
    setState((prev) => ({ ...prev, loading }));
  }, []);

  const setError = useCallback((error: string | null) => {
    setState((prev) => ({ ...prev, error }));
  }, []);

  const getDashboard = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchJson<{
        dashboard: AdminDashboard;
        insights: BusinessInsight[];
        alerts: OperationalAlert[];
      }>(`${API_BASE}/dashboard`);
      setState((prev) => ({
        ...prev,
        dashboard: data.dashboard,
        insights: data.insights || [],
        alerts: data.alerts || [],
        loading: false
      }));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load dashboard');
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getInsights = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchJson<{
        summary: string;
        keyMetrics: Record<string, unknown>;
        trends: Array<Record<string, unknown>>;
        recommendations: string[];
      }>(`${API_BASE}/insights`);
      const insights: BusinessInsight[] = (data.trends || []).map((trend: Record<string, unknown>) => ({
        summary: data.summary,
        metric: String(trend.metric || ''),
        currentValue: Number(trend.currentValue || 0),
        previousValue: Number(trend.previousValue || 0),
        change: Number(trend.change || 0),
        trend: (trend.direction === 'up' ? 'up' : trend.direction === 'down' ? 'down' : 'stable') as BusinessInsight['trend'],
        severity: 'info',
        recommendation: data.recommendations?.[0] || ''
      }));
      setState((prev) => ({ ...prev, insights, loading: false }));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load insights');
      setLoading(false);
    }
  }, [setLoading, setError]);

  const generateReportAction = useCallback(async (config: ReportConfig) => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchJson<PerformanceReport>(`${API_BASE}/report`, {
        method: 'POST',
        body: JSON.stringify({
          title: config.title,
          type: config.type,
          periodFrom: config.periodFrom,
          periodTo: config.periodTo,
          metrics: config.metrics,
          format: config.format
        })
      });
      setState((prev) => ({ ...prev, report: data, loading: false }));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to generate report');
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getForecast = useCallback(async (metric: string, period: string, horizon: number) => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchJson<ForecastResult>(`${API_BASE}/forecast`, {
        method: 'POST',
        body: JSON.stringify({ metric, period, horizon })
      });
      setState((prev) => ({ ...prev, forecast: data, loading: false }));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load forecast');
      setLoading(false);
    }
  }, [setLoading, setError]);

  const getAlerts = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchJson<{ alerts: OperationalAlert[] }>(`${API_BASE}/alerts`);
      setState((prev) => ({ ...prev, alerts: data.alerts || [], loading: false }));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load alerts');
      setLoading(false);
    }
  }, [setLoading, setError]);

  const clearError = useCallback(() => {
    setError(null);
  }, [setError]);

  return {
    ...state,
    getDashboard,
    getInsights,
    generateReport: generateReportAction,
    getForecast,
    getAlerts,
    clearError
  };
}
