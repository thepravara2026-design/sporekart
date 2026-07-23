import React from 'react';
import type { ChatMessage } from './types/workspace';

interface CopilotMessageBubbleProps {
  message: ChatMessage;
  copilotColorMap?: Record<string, string>;
  onSuggestionClick?: (action: string, payload?: Record<string, unknown>) => void;
  style?: React.CSSProperties;
}

const DEFAULT_COLORS: Record<string, string> = {
  sales: '#6366f1',
  support: '#0ea5e9',
  analytics: '#f59e0b',
  training: '#10b981',
  general: '#8b5cf6',
};

function getColor(message: ChatMessage, colorMap?: Record<string, string>): string {
  if (message.copilotId && colorMap?.[message.copilotId]) return colorMap[message.copilotId];
  return '#6366f1';
}

function formatTimestamp(ts: string): string {
  try {
    return new Date(ts).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  } catch {
    return '';
  }
}

function renderMarkdown(text: string): string {
  let html = text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');

  html = html
    .replace(/```(\w*)\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    .replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener">$1</a>')
    .replace(/\n/g, '<br />');

  return html;
}

export default function CopilotMessageBubble({
  message,
  copilotColorMap,
  onSuggestionClick,
  style,
}: CopilotMessageBubbleProps) {
  const isUser = message.role === 'user';
  const color = getColor(message, copilotColorMap);

  const wrapperStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: isUser ? 'flex-end' : 'flex-start',
    marginBottom: '12px',
    ...style,
  };

  const bubbleStyle: React.CSSProperties = {
    maxWidth: '75%',
    padding: '10px 14px',
    borderRadius: isUser ? '16px 16px 4px 16px' : '16px 16px 16px 4px',
    background: isUser
      ? 'var(--cp-user-bg, #e0e7ff)'
      : 'var(--cp-surface, #fff)',
    border: isUser ? 'none' : '1px solid var(--cp-border, #e2e8f0)',
    color: 'var(--cp-text, #1e293b)',
    fontSize: '14px',
    lineHeight: 1.5,
    position: 'relative',
  };

  return (
    <div style={wrapperStyle}>
      {!isUser && message.copilotName && (
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
          marginBottom: '4px',
          marginLeft: '4px',
        }}>
          <span style={{
            display: 'inline-block',
            width: '8px',
            height: '8px',
            borderRadius: '50%',
            background: color,
          }} />
          <span style={{
            fontSize: '12px',
            fontWeight: 600,
            color,
          }}>
            {message.copilotName}
          </span>
        </div>
      )}

      {message.handoff && (
        <div style={{
          fontSize: '11px',
          color: '#f59e0b',
          marginBottom: '4px',
          fontWeight: 500,
        }}>
          ↪ Handoff
        </div>
      )}

      <div style={bubbleStyle}>
        <div
          dangerouslySetInnerHTML={{ __html: renderMarkdown(message.content) }}
          style={{ wordBreak: 'break-word' }}
        />

        {message.suggestions && message.suggestions.length > 0 && (
          <div style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: '6px',
            marginTop: '10px',
            paddingTop: '10px',
            borderTop: '1px solid var(--cp-border, #e2e8f0)',
          }}>
            {message.suggestions.map((s, i) => (
              <button
                key={i}
                onClick={() => onSuggestionClick?.(s.action, s.payload)}
                style={{
                  padding: '4px 12px',
                  borderRadius: '16px',
                  border: `1px solid ${color}`,
                  background: 'transparent',
                  color,
                  fontSize: '12px',
                  cursor: 'pointer',
                  whiteSpace: 'nowrap',
                }}
              >
                {s.label}
              </button>
            ))}
          </div>
        )}

        <div style={{
          fontSize: '11px',
          color: 'var(--cp-text-secondary, #94a3b8)',
          marginTop: '6px',
          textAlign: 'right',
        }}>
          {formatTimestamp(message.timestamp)}
        </div>
      </div>

      {!isUser && message.collaborationResponses && message.collaborationResponses.length > 0 && (
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '4px',
          marginTop: '6px',
          marginLeft: '12px',
          width: '70%',
        }}>
          {message.collaborationResponses.map((cr, i) => (
            <div
              key={i}
              style={{
                padding: '6px 10px',
                borderRadius: '8px',
                background: 'var(--cp-surface-3, #f8fafc)',
                border: '1px solid var(--cp-border, #e2e8f0)',
                fontSize: '12px',
                borderLeft: `3px solid ${getColor(message, copilotColorMap)}`,
              }}
            >
              <div style={{ fontWeight: 600, fontSize: '11px', color: 'var(--cp-text-secondary, #64748b)', marginBottom: '2px' }}>
                {cr.copilotName} · {cr.status}
              </div>
              <div style={{ color: 'var(--cp-text, #1e293b)' }}>{cr.message}</div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
