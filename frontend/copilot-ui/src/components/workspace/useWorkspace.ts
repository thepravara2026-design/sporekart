import { useState, useCallback, useRef } from 'react';
import type { ChatMessage, CopilotInfo, ContextSnapshot, HandoffRequest, WorkspaceStatus } from './types/workspace';

const FALLBACK_ERROR = 'An unexpected error occurred';

interface WorkspaceState {
  messages: ChatMessage[];
  copilots: CopilotInfo[];
  activeCopilotId: string;
  context: ContextSnapshot | null;
  loading: boolean;
  error: string | null;
}

interface UseWorkspaceReturn extends WorkspaceState {
  sendMessage: (content: string) => Promise<void>;
  sendStreamMessage: (content: string, onChunk?: (chunk: string) => void) => Promise<void>;
  fetchCopilots: () => Promise<void>;
  switchCopilot: (copilotId: string) => void;
  getContext: () => Promise<void>;
  initiateHandoff: (request: HandoffRequest) => Promise<void>;
  fetchHistory: (sessionId?: string) => Promise<void>;
  fetchStatus: () => Promise<void>;
}

export function useWorkspace(workspaceId: string): UseWorkspaceReturn {
  const [state, setState] = useState<WorkspaceState>({
    messages: [],
    copilots: [],
    activeCopilotId: '',
    context: null,
    loading: false,
    error: null,
  });

  const abortRef = useRef<AbortController | null>(null);

  const setLoading = useCallback((loading: boolean) => setState(prev => ({ ...prev, loading })), []);
  const setError = useCallback((error: string | null) => setState(prev => ({ ...prev, error })), []);

  const sendMessage = useCallback(async (content: string) => {
    if (!content.trim()) return;
    setLoading(true);
    setError(null);

    const userMsg: ChatMessage = {
      id: `msg-${Date.now()}`,
      role: 'user',
      content,
      timestamp: new Date().toISOString(),
    };

    setState(prev => ({ ...prev, messages: [...prev.messages, userMsg] }));

    try {
      abortRef.current = new AbortController();
      const res = await fetch(`/api/workspaces/${workspaceId}/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: content, copilotId: state.activeCopilotId }),
        signal: abortRef.current.signal,
      });

      if (!res.ok) throw new Error(`Request failed: ${res.statusText}`);

      const data = await res.json();
      const assistantMsg: ChatMessage = {
        id: `msg-${Date.now() + 1}`,
        role: 'assistant',
        content: data.message ?? data.content ?? '',
        copilotId: data.copilotId ?? state.activeCopilotId,
        copilotName: data.copilotName ?? '',
        timestamp: new Date().toISOString(),
        suggestions: data.suggestions,
        collaborationResponses: data.collaborationResponses,
        handoff: data.handoff,
      };

      setState(prev => ({ ...prev, messages: [...prev.messages, assistantMsg] }));
    } catch (err: unknown) {
      if (err instanceof Error && err.name === 'AbortError') return;
      setError(err instanceof Error ? err.message : FALLBACK_ERROR);
    } finally {
      setLoading(false);
    }
  }, [workspaceId, state.activeCopilotId, setLoading, setError]);

  const sendStreamMessage = useCallback(async (content: string, onChunk?: (chunk: string) => void) => {
    if (!content.trim()) return;
    setLoading(true);
    setError(null);

    const userMsg: ChatMessage = {
      id: `msg-${Date.now()}`,
      role: 'user',
      content,
      timestamp: new Date().toISOString(),
    };

    setState(prev => ({ ...prev, messages: [...prev.messages, userMsg] }));

    try {
      abortRef.current = new AbortController();
      const res = await fetch(`/api/workspaces/${workspaceId}/chat/stream`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: content, copilotId: state.activeCopilotId }),
        signal: abortRef.current.signal,
      });

      if (!res.ok) throw new Error(`Stream request failed: ${res.statusText}`);
      if (!res.body) throw new Error('Response body is null');

      const reader = res.body.getReader();
      const decoder = new TextDecoder();
      let accumulated = '';

      while (true) {
        const { done, value } = await reader.read();
        if (done) break;

        const chunk = decoder.decode(value, { stream: true });
        accumulated += chunk;
        onChunk?.(chunk);
      }

      const assistantMsg: ChatMessage = {
        id: `msg-${Date.now() + 1}`,
        role: 'assistant',
        content: accumulated,
        copilotId: state.activeCopilotId,
        timestamp: new Date().toISOString(),
      };

      setState(prev => ({ ...prev, messages: [...prev.messages, assistantMsg] }));
    } catch (err: unknown) {
      if (err instanceof Error && err.name === 'AbortError') return;
      setError(err instanceof Error ? err.message : FALLBACK_ERROR);
    } finally {
      setLoading(false);
    }
  }, [workspaceId, state.activeCopilotId, setLoading, setError]);

  const cancelStream = useCallback(() => {
    abortRef.current?.abort();
  }, []);

  const fetchCopilots = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`/api/workspaces/${workspaceId}/copilots`);
      if (!res.ok) throw new Error(`Failed to fetch copilots: ${res.statusText}`);
      const data: CopilotInfo[] = await res.json();
      setState(prev => {
        const newActive = prev.activeCopilotId || (data.length > 0 ? data[0].copilotId : '');
        return { ...prev, copilots: data, activeCopilotId: newActive };
      });
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : FALLBACK_ERROR);
    } finally {
      setLoading(false);
    }
  }, [workspaceId, setLoading, setError]);

  const switchCopilot = useCallback((copilotId: string) => {
    setState(prev => ({ ...prev, activeCopilotId: copilotId }));
  }, []);

  const getContext = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`/api/workspaces/${workspaceId}/context`);
      if (!res.ok) throw new Error(`Failed to fetch context: ${res.statusText}`);
      const data: ContextSnapshot = await res.json();
      setState(prev => ({ ...prev, context: data }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : FALLBACK_ERROR);
    } finally {
      setLoading(false);
    }
  }, [workspaceId, setLoading, setError]);

  const initiateHandoff = useCallback(async (request: HandoffRequest) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`/api/workspaces/${workspaceId}/handoff`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
      });
      if (!res.ok) throw new Error(`Handoff failed: ${res.statusText}`);
      const result = await res.json();
      if (result.copilotId) {
        setState(prev => ({ ...prev, activeCopilotId: result.copilotId }));
      }
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : FALLBACK_ERROR);
    } finally {
      setLoading(false);
    }
  }, [workspaceId, setLoading, setError]);

  const fetchHistory = useCallback(async (sessionId?: string) => {
    setLoading(true);
    setError(null);
    try {
      const url = sessionId
        ? `/api/workspaces/${workspaceId}/history?sessionId=${sessionId}`
        : `/api/workspaces/${workspaceId}/history`;
      const res = await fetch(url);
      if (!res.ok) throw new Error(`Failed to fetch history: ${res.statusText}`);
      const data: ChatMessage[] = await res.json();
      setState(prev => ({ ...prev, messages: data }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : FALLBACK_ERROR);
    } finally {
      setLoading(false);
    }
  }, [workspaceId, setLoading, setError]);

  const fetchStatus = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`/api/workspaces/${workspaceId}/status`);
      if (!res.ok) throw new Error(`Failed to fetch status: ${res.statusText}`);
      const data: WorkspaceStatus = await res.json();
      setState(prev => ({
        ...prev,
        copilots: data.availableCopilots,
        activeCopilotId: prev.activeCopilotId || data.availableCopilots[0]?.copilotId ?? '',
      }));
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : FALLBACK_ERROR);
    } finally {
      setLoading(false);
    }
  }, [workspaceId, setLoading, setError]);

  return {
    ...state,
    sendMessage,
    sendStreamMessage,
    fetchCopilots,
    switchCopilot,
    getContext,
    initiateHandoff,
    fetchHistory,
    fetchStatus,
  };
}
