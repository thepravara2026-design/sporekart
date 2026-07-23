import { useState, useCallback } from 'react';
import { ProductItem, ProductRecommendation, CustomerOrder, KnowledgeArticle } from '../types/customer';

interface CustomerCopilotState {
  messages: CopilotChatMessage[];
  isProcessing: boolean;
  sessionId: string | null;
}

interface CopilotChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
  recommendations?: ProductRecommendation[];
  orders?: CustomerOrder[];
  articles?: KnowledgeArticle[];
  suggestions?: string[];
}

interface CustomerCopilotActions {
  searchProducts: (query: string) => Promise<ProductItem[]>;
  getRecommendations: (customerId: string) => Promise<ProductRecommendation[]>;
  trackOrder: (orderId: string) => Promise<CustomerOrder | null>;
  sendMessage: (content: string) => Promise<void>;
  clearMessages: () => void;
}

const API_BASE = '/api/v1/copilot/customer';

export function useCustomerCopilot(): CustomerCopilotState & CustomerCopilotActions {
  const [state, setState] = useState<CustomerCopilotState>({
    messages: [
      {
        id: 'welcome',
        role: 'assistant',
        content: "Hello! I'm your Customer Copilot. How can I help you today? You can ask me about products, track orders, get growing tips, or browse our training courses.",
        timestamp: new Date(),
        suggestions: ['Search Products', 'Track Order', 'Growing Tips', 'Recommendations'],
      },
    ],
    isProcessing: false,
    sessionId: null,
  });

  const sendMessage = useCallback(async (content: string) => {
    if (!content.trim()) return;

    const userMessage: CopilotChatMessage = {
      id: `msg-${Date.now()}`,
      role: 'user',
      content: content.trim(),
      timestamp: new Date(),
    };

    setState((prev) => ({
      ...prev,
      messages: [...prev.messages, userMessage],
      isProcessing: true,
    }));

    try {
      const response = await fetch(`${API_BASE}/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          message: content.trim(),
          sessionId: state.sessionId,
        }),
      });

      if (!response.ok) {
        throw new Error(`Chat request failed: ${response.statusText}`);
      }

      const data = await response.json();

      const assistantMessage: CopilotChatMessage = {
        id: `resp-${Date.now()}`,
        role: 'assistant',
        content: data.message || 'I understand. Let me help you with that.',
        timestamp: new Date(),
        suggestions: data.suggestions?.map((s: any) => s.label || s) || [],
      };

      setState((prev) => ({
        ...prev,
        messages: [...prev.messages, assistantMessage],
        isProcessing: false,
        sessionId: data.sessionId || prev.sessionId,
      }));
    } catch (error) {
      const errorMessage: CopilotChatMessage = {
        id: `err-${Date.now()}`,
        role: 'assistant',
        content: 'I apologize, but I encountered an error processing your request. Please try again.',
        timestamp: new Date(),
      };

      setState((prev) => ({
        ...prev,
        messages: [...prev.messages, errorMessage],
        isProcessing: false,
      }));
    }
  }, [state.sessionId]);

  const searchProducts = useCallback(async (query: string): Promise<ProductItem[]> => {
    try {
      const response = await fetch(`${API_BASE}/products`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ query, page: 0, size: 20 }),
      });

      if (!response.ok) return [];

      const data = await response.json();
      return data.products || [];
    } catch {
      return [];
    }
  }, []);

  const getRecommendations = useCallback(async (customerId: string): Promise<ProductRecommendation[]> => {
    try {
      const response = await fetch(`${API_BASE}/recommend`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ customerId, limit: 10 }),
      });

      if (!response.ok) return [];

      const data = await response.json();
      return data.recommendations || [];
    } catch {
      return [];
    }
  }, []);

  const trackOrder = useCallback(async (orderId: string): Promise<CustomerOrder | null> => {
    try {
      const response = await fetch(`${API_BASE}/history?sessionId=${encodeURIComponent(orderId)}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) return null;

      return await response.json();
    } catch {
      return null;
    }
  }, []);

  const clearMessages = useCallback(() => {
    setState({
      messages: [],
      isProcessing: false,
      sessionId: null,
    });
  }, []);

  return {
    messages: state.messages,
    isProcessing: state.isProcessing,
    sessionId: state.sessionId,
    searchProducts,
    getRecommendations,
    trackOrder,
    sendMessage,
    clearMessages,
  };
}
