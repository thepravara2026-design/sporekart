import { useState, useRef, useCallback } from 'react';

interface UseCopilotStreamResult {
  connect: (url: string) => void;
  disconnect: () => void;
  streamChunk: string;
  isConnected: boolean;
}

export function useCopilotStream(sessionId: string): UseCopilotStreamResult {
  const [streamChunk, setStreamChunk] = useState('');
  const [isConnected, setIsConnected] = useState(false);
  const eventSourceRef = useRef<EventSource | null>(null);

  const connect = useCallback(
    (url: string) => {
      disconnect();

      const es = new EventSource(`${url}?sessionId=${encodeURIComponent(sessionId)}`);
      eventSourceRef.current = es;

      es.onopen = () => setIsConnected(true);

      es.addEventListener('chunk', (e: MessageEvent) => {
        setStreamChunk(e.data);
      });

      es.addEventListener('done', () => {
        es.close();
        setIsConnected(false);
      });

      es.onerror = () => {
        es.close();
        setIsConnected(false);
      };
    },
    [sessionId],
  );

  const disconnect = useCallback(() => {
    if (eventSourceRef.current) {
      eventSourceRef.current.close();
      eventSourceRef.current = null;
    }
    setIsConnected(false);
    setStreamChunk('');
  }, []);

  return { connect, disconnect, streamChunk, isConnected };
}
