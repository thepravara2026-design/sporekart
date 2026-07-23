import React, { useState } from 'react';
import { ProductRecommendation, CustomerOrder, KnowledgeArticle } from '../../types/customer';
import CustomerRecommendationCarousel from './CustomerRecommendationCarousel';
import OrderTrackingCard from './OrderTrackingCard';
import KnowledgeCitationCard from './KnowledgeCitationCard';
import QuickActionBar from './QuickActionBar';

interface CopilotMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  recommendations?: ProductRecommendation[];
  orders?: CustomerOrder[];
  articles?: KnowledgeArticle[];
}

interface CustomerCopilotPanelProps {
  onSendMessage?: (message: string) => void;
  onQuickAction?: (action: string) => void;
}

export default function CustomerCopilotPanel({ onSendMessage, onQuickAction }: CustomerCopilotPanelProps) {
  const [messages, setMessages] = useState<CopilotMessage[]>([
    {
      id: 'welcome',
      role: 'assistant',
      content: "Hello! I'm your Customer Copilot. How can I help you today? You can ask me about products, track orders, get growing tips, or browse our training courses.",
    },
  ]);
  const [input, setInput] = useState('');

  function handleSend() {
    if (!input.trim()) return;

    const userMsg: CopilotMessage = {
      id: `user-${Date.now()}`,
      role: 'user',
      content: input.trim(),
    };
    setMessages((prev) => [...prev, userMsg]);
    setInput('');
    onSendMessage?.(input.trim());
  }

  function handleKeyDown(e: React.KeyboardEvent) {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  }

  function handleAction(action: string) {
    const actionMessages: Record<string, string> = {
      'search-products': 'I\'d like to search for products',
      'track-order': 'Can you help me track my order?',
      'growing-tips': 'Give me some growing tips for mushrooms',
      'faqs': 'Show me frequently asked questions',
      'recommendations': 'What do you recommend for me?',
      'my-orders': 'Show my recent orders',
      'training-courses': 'What training courses are available?',
      'contact-support': 'I need to contact customer support',
    };

    const msg = actionMessages[action] || `Action: ${action}`;
    const userMsg: CopilotMessage = {
      id: `action-${Date.now()}`,
      role: 'user',
      content: msg,
    };
    setMessages((prev) => [...prev, userMsg]);
    onQuickAction?.(action);
  }

  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      height: '100%',
      maxHeight: '600px',
      border: '1px solid #e5e7eb',
      borderRadius: '12px',
      overflow: 'hidden',
      background: '#fff',
      fontFamily: 'system-ui, sans-serif',
    }}>
      <div style={{
        padding: '14px 16px',
        background: '#1e40af',
        color: '#fff',
        fontWeight: 700,
        fontSize: '15px',
        display: 'flex',
        alignItems: 'center',
        gap: '8px',
      }}>
        <span style={{ fontSize: '18px' }}>🛒</span>
        Customer Copilot
      </div>

      <div style={{
        flex: 1,
        overflowY: 'auto',
        padding: '12px',
        display: 'flex',
        flexDirection: 'column',
        gap: '12px',
        background: '#f9fafb',
      }}>
        {messages.length === 1 && (
          <QuickActionBar onAction={handleAction} />
        )}

        {messages.map((msg) => (
          <div key={msg.id}>
            <div style={{
              display: 'flex',
              justifyContent: msg.role === 'user' ? 'flex-end' : 'flex-start',
              marginBottom: '4px',
            }}>
              <div style={{
                maxWidth: '80%',
                padding: '10px 14px',
                borderRadius: msg.role === 'user' ? '16px 16px 4px 16px' : '16px 16px 16px 4px',
                background: msg.role === 'user' ? '#2563eb' : '#fff',
                color: msg.role === 'user' ? '#fff' : '#111827',
                fontSize: '14px',
                lineHeight: '1.5',
                border: msg.role === 'user' ? 'none' : '1px solid #e5e7eb',
                boxShadow: msg.role === 'user' ? 'none' : '0 1px 2px rgba(0,0,0,0.05)',
              }}>
                {msg.content}
              </div>
            </div>

            {msg.recommendations && msg.recommendations.length > 0 && (
              <div style={{ marginTop: '8px' }}>
                <CustomerRecommendationCarousel recommendations={msg.recommendations} />
              </div>
            )}

            {msg.orders && msg.orders.length > 0 && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '8px' }}>
                {msg.orders.map((order) => (
                  <OrderTrackingCard key={order.orderId} order={order} />
                ))}
              </div>
            )}

            {msg.articles && msg.articles.length > 0 && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '8px' }}>
                {msg.articles.map((article) => (
                  <KnowledgeCitationCard key={article.id} article={article} />
                ))}
              </div>
            )}
          </div>
        ))}
      </div>

      <div style={{
        padding: '10px 12px',
        borderTop: '1px solid #e5e7eb',
        display: 'flex',
        gap: '8px',
        background: '#fff',
      }}>
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Ask about products, orders, or growing tips..."
          style={{
            flex: 1,
            padding: '10px 14px',
            border: '1px solid #d1d5db',
            borderRadius: '8px',
            fontSize: '14px',
            outline: 'none',
          }}
        />
        <button
          onClick={handleSend}
          disabled={!input.trim()}
          style={{
            padding: '10px 16px',
            border: 'none',
            borderRadius: '8px',
            background: input.trim() ? '#2563eb' : '#d1d5db',
            color: '#fff',
            fontWeight: 600,
            fontSize: '14px',
            cursor: input.trim() ? 'pointer' : 'not-allowed',
          }}
        >
          Send
        </button>
      </div>
    </div>
  );
}
