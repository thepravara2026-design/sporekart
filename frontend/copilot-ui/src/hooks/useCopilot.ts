import { useState, useCallback, useRef } from 'react';
import { type CopilotMessage, type Suggestion, type CopilotContext } from '../types';

interface UseCopilotResult {
  messages: CopilotMessage[];
  sendMessage: (content: string) => Promise<void>;
  isStreaming: boolean;
  suggestions: Suggestion[];
  context: CopilotContext;
  clearMessages: () => void;
}

let messageCounter = 0;

function createMessage(role: CopilotMessage['role'], content: string): CopilotMessage {
  messageCounter += 1;
  return {
    id: `msg-${Date.now()}-${messageCounter}`,
    role,
    content,
    timestamp: new Date().toISOString(),
  };
}

interface UseCopilotOptions {
  apiUrl?: string;
  context?: CopilotContext;
}

export function useCopilot(copilotType: string, options?: UseCopilotOptions): UseCopilotResult {
  const [messages, setMessages] = useState<CopilotMessage[]>([]);
  const [isStreaming, setIsStreaming] = useState(false);
  const [suggestions, setSuggestions] = useState<Suggestion[]>([]);
  const [context, setContext] = useState<CopilotContext>(options?.context ?? {});
  const abortRef = useRef<AbortController | null>(null);

  const clearMessages = useCallback(() => {
    setMessages([]);
    setSuggestions([]);
  }, []);

  const sendMessage = useCallback(
    async (content: string) => {
      if (!content.trim() || isStreaming) return;

      const userMsg = createMessage('user', content);
      setMessages((prev) => [...prev, userMsg]);

      setIsStreaming(true);

      const assistantMsg = createMessage('assistant', '');
      setMessages((prev) => [...prev, assistantMsg]);

      try {
        if (options?.apiUrl) {
          abortRef.current = new AbortController();

          const response = await fetch(`${options.apiUrl}/chat`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              copilotType,
              message: content,
              context,
              messages: messages.slice(-10),
            }),
            signal: abortRef.current.signal,
          });

          if (!response.ok) throw new Error(`API error: ${response.status}`);

          const data = await response.json();

          setMessages((prev) => {
            const updated = [...prev];
            const lastIndex = updated.length - 1;
            if (lastIndex >= 0 && updated[lastIndex].role === 'assistant') {
              updated[lastIndex] = { ...updated[lastIndex], content: data.content ?? '' };
            }
            return updated;
          });

          if (data.suggestions) {
            setSuggestions(data.suggestions);
          }
          if (data.context) {
            setContext((prev) => ({ ...prev, ...data.context }));
          }
        } else {
          await new Promise((resolve) => setTimeout(resolve, 500));

          setMessages((prev) => {
            const updated = [...prev];
            const lastIndex = updated.length - 1;
            if (lastIndex >= 0 && updated[lastIndex].role === 'assistant') {
              updated[lastIndex] = {
                ...updated[lastIndex],
                content: `Hello! I'm the ${copilotType} copilot. This is a placeholder response. In production, I'll connect to the copilot-service API.`,
              };
            }
            return updated;
          });
        }
      } catch (err) {
        if (err instanceof DOMException && err.name === 'AbortError') return;

        setMessages((prev) => {
          const updated = [...prev];
          const lastIndex = updated.length - 1;
          if (lastIndex >= 0 && updated[lastIndex].role === 'assistant') {
            updated[lastIndex] = {
              ...updated[lastIndex],
              content: `Sorry, an error occurred: ${err instanceof Error ? err.message : 'Unknown error'}`,
            };
          }
          return updated;
        });
      } finally {
        setIsStreaming(false);
        abortRef.current = null;
      }
    },
    [copilotType, context, messages, isStreaming, options?.apiUrl],
  );

  return { messages, sendMessage, isStreaming, suggestions, context, clearMessages };
}
