import { useState, useCallback } from 'react';
import type { ExecutiveSummary, BusinessForecast, KpiEntry, BusinessInsight, DecisionRecommendation, RiskAlert, CompanyHealthScore, ChatMessage } from './types/bi';

interface BiCopilotState {
  loading: boolean;
  error: string | null;
}

export function useBiCopilot() {
  const [state, setState] = useState<BiCopilotState>({ loading: false, error: null });
  const [messages, setMessages] = useState<ChatMessage[]>([]);

  const setLoading = (loading: boolean) => setState(prev => ({ ...prev, loading }));
  const setError = (error: string | null) => setState(prev => ({ ...prev, error }));

  const sendMessage = useCallback(async (content: string): Promise<ChatMessage | null> => {
    setLoading(true); setError(null);
    try {
      const msg: ChatMessage = { id: crypto.randomUUID(), role: 'user', content, timestamp: new Date().toISOString() };
      setMessages(prev => [...prev, msg]);
      const res = await fetch('/api/bi/copilot/chat', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ message: content }) });
      if (!res.ok) throw new Error(`API error: ${res.status}`);
      const data = await res.json();
      const reply: ChatMessage = { id: crypto.randomUUID(), role: 'assistant', content: data.response, timestamp: new Date().toISOString(), suggestions: data.suggestions };
      setMessages(prev => [...prev, reply]);
      return reply;
    } catch (e) { const err = (e as Error).message; setError(err); return null; }
    finally { setLoading(false); }
  }, []);

  const sendStreamMessage = useCallback(async (content: string, onChunk: (chunk: string) => void): Promise<ChatMessage | null> => {
    setLoading(true); setError(null);
    try {
      const msg: ChatMessage = { id: crypto.randomUUID(), role: 'user', content, timestamp: new Date().toISOString() };
      setMessages(prev => [...prev, msg]);
      const res = await fetch('/api/bi/copilot/chat/stream', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ message: content }) });
      if (!res.ok) throw new Error(`API error: ${res.status}`);
      const reader = res.body!.getReader(); const decoder = new TextDecoder();
      let full = '';
      while (true) { const { done, value } = await reader.read(); if (done) break; full += decoder.decode(value, { stream: true }); onChunk(full); }
      const reply: ChatMessage = { id: crypto.randomUUID(), role: 'assistant', content: full, timestamp: new Date().toISOString() };
      setMessages(prev => [...prev, reply]);
      return reply;
    } catch (e) { const err = (e as Error).message; setError(err); return null; }
    finally { setLoading(false); }
  }, []);

  const getDashboard = useCallback(async (): Promise<ExecutiveSummary | null> => {
    setLoading(true); setError(null);
    try { const res = await fetch('/api/bi/dashboard'); if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.json(); }
    catch (e) { setError((e as Error).message); return null; } finally { setLoading(false); }
  }, []);

  const generateReport = useCallback(async (type: string, filters?: Record<string,unknown>): Promise<Blob | null> => {
    setLoading(true); setError(null);
    try {
      const res = await fetch('/api/bi/report', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ type, filters }) });
      if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.blob();
    } catch (e) { setError((e as Error).message); return null; } finally { setLoading(false); }
  }, []);

  const getForecast = useCallback(async (metric: string, periods?: number): Promise<BusinessForecast | null> => {
    setLoading(true); setError(null);
    try { const res = await fetch(`/api/bi/forecast?metric=${encodeURIComponent(metric)}&periods=${periods ?? 12}`); if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.json(); }
    catch (e) { setError((e as Error).message); return null; } finally { setLoading(false); }
  }, []);

  const getKpis = useCallback(async (domain?: string): Promise<KpiEntry[]> => {
    setLoading(true); setError(null);
    try { const res = await fetch(`/api/bi/kpis${domain ? `?domain=${encodeURIComponent(domain)}` : ''}`); if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.json(); }
    catch (e) { setError((e as Error).message); return []; } finally { setLoading(false); }
  }, []);

  const getInsights = useCallback(async (severity?: string): Promise<BusinessInsight[]> => {
    setLoading(true); setError(null);
    try { const res = await fetch(`/api/bi/insights${severity ? `?severity=${encodeURIComponent(severity)}` : ''}`); if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.json(); }
    catch (e) { setError((e as Error).message); return []; } finally { setLoading(false); }
  }, []);

  const getRecommendations = useCallback(async (priority?: string): Promise<DecisionRecommendation[]> => {
    setLoading(true); setError(null);
    try { const res = await fetch(`/api/bi/recommendations${priority ? `?priority=${encodeURIComponent(priority)}` : ''}`); if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.json(); }
    catch (e) { setError((e as Error).message); return []; } finally { setLoading(false); }
  }, []);

  const getRisks = useCallback(async (severity?: string): Promise<RiskAlert[]> => {
    setLoading(true); setError(null);
    try { const res = await fetch(`/api/bi/risks${severity ? `?severity=${encodeURIComponent(severity)}` : ''}`); if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.json(); }
    catch (e) { setError((e as Error).message); return []; } finally { setLoading(false); }
  }, []);

  const answerQuery = useCallback(async (query: string): Promise<string | null> => {
    setLoading(true); setError(null);
    try { const res = await fetch('/api/bi/query', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ query }) }); if (!res.ok) throw new Error(`API error: ${res.status}`); const d = await res.json(); return d.answer; }
    catch (e) { setError((e as Error).message); return null; } finally { setLoading(false); }
  }, []);

  const getHealthScore = useCallback(async (): Promise<CompanyHealthScore | null> => {
    setLoading(true); setError(null);
    try { const res = await fetch('/api/bi/health-score'); if (!res.ok) throw new Error(`API error: ${res.status}`); return await res.json(); }
    catch (e) { setError((e as Error).message); return null; } finally { setLoading(false); }
  }, []);

  return { loading: state.loading, error: state.error, messages, sendMessage, sendStreamMessage, getDashboard, generateReport, getForecast, getKpis, getInsights, getRecommendations, getRisks, answerQuery, getHealthScore };
}
