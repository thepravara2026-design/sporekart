import React, { useState, useMemo } from 'react';
import type { ChatMessage } from './types/workspace';

interface ConversationSidebarProps {
  messages: ChatMessage[];
  activeSessionId?: string;
  onSelectMessage?: (messageId: string) => void;
  onClearConversation?: () => void;
  onExport?: () => void;
  style?: React.CSSProperties;
}

function groupByDate(messages: ChatMessage[]): Map<string, ChatMessage[]> {
  const groups = new Map<string, ChatMessage[]>();
  for (const msg of messages) {
    const date = msg.timestamp ? new Date(msg.timestamp).toLocaleDateString() : 'Unknown';
    const existing = groups.get(date);
    if (existing) existing.push(msg);
    else groups.set(date, [msg]);
  }
  return groups;
}

export default function ConversationSidebar({
  messages,
  activeSessionId,
  onSelectMessage,
  onClearConversation,
  onExport,
  style,
}: ConversationSidebarProps) {
  const [search, setSearch] = useState('');

  const filteredMessages = useMemo(() => {
    if (!search.trim()) return messages;
    const q = search.toLowerCase();
    return messages.filter(m => m.content.toLowerCase().includes(q));
  }, [messages, search]);

  const grouped = useMemo(() => groupByDate(filteredMessages), [filteredMessages]);

  return (
    <div style={{
      width: '280px',
      background: 'var(--cp-surface, #fff)',
      borderRight: '1px solid var(--cp-border, #e2e8f0)',
      display: 'flex',
      flexDirection: 'column',
      height: '100%',
      ...style,
    }}>
      <div style={{
        padding: '12px',
        borderBottom: '1px solid var(--cp-border, #e2e8f0)',
      }}>
        <div style={{ fontWeight: 600, fontSize: '14px', marginBottom: '8px', color: 'var(--cp-text, #1e293b)' }}>
          Conversation History
        </div>
        <input
          type="text"
          placeholder="Search conversations..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          style={{
            width: '100%',
            padding: '6px 10px',
            borderRadius: '6px',
            border: '1px solid var(--cp-border, #e2e8f0)',
            fontSize: '13px',
            outline: 'none',
            boxSizing: 'border-box',
            background: 'var(--cp-surface-2, #f8fafc)',
            color: 'var(--cp-text, #1e293b)',
          }}
        />
      </div>

      <div style={{ flex: 1, overflowY: 'auto', padding: '8px' }}>
        {Array.from(grouped.entries()).map(([date, msgs]) => (
          <div key={date} style={{ marginBottom: '12px' }}>
            <div style={{
              fontSize: '11px',
              fontWeight: 600,
              color: 'var(--cp-text-secondary, #64748b)',
              textTransform: 'uppercase',
              marginBottom: '4px',
              padding: '0 4px',
            }}>
              {date === new Date().toLocaleDateString() ? 'Today' : date}
            </div>
            {msgs.map(msg => (
              <div
                key={msg.id}
                onClick={() => onSelectMessage?.(msg.id)}
                style={{
                  padding: '6px 8px',
                  borderRadius: '6px',
                  cursor: 'pointer',
                  fontSize: '12px',
                  color: 'var(--cp-text, #1e293b)',
                  background: msg.id === activeSessionId ? 'var(--cp-primary-bg, #e0e7ff)' : 'transparent',
                  marginBottom: '2px',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '6px',
                }}
              >
                <span style={{ flexShrink: 0 }}>
                  {msg.role === 'user' ? '👤' : '🤖'}
                </span>
                <span style={{
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap',
                  flex: 1,
                }}>
                  {msg.content.slice(0, 60)}
                </span>
                <span style={{ fontSize: '10px', color: 'var(--cp-text-secondary, #94a3b8)', flexShrink: 0 }}>
                  {msg.timestamp ? new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : ''}
                </span>
              </div>
            ))}
          </div>
        ))}
        {filteredMessages.length === 0 && (
          <div style={{ padding: '16px', textAlign: 'center', color: 'var(--cp-text-secondary, #64748b)', fontSize: '13px' }}>
            {search ? 'No matching messages' : 'No conversations yet'}
          </div>
        )}
      </div>

      <div style={{
        padding: '10px 12px',
        borderTop: '1px solid var(--cp-border, #e2e8f0)',
        display: 'flex',
        gap: '8px',
      }}>
        <button
          onClick={onClearConversation}
          style={{
            flex: 1,
            padding: '6px',
            borderRadius: '6px',
            border: '1px solid var(--cp-border, #e2e8f0)',
            background: 'var(--cp-surface, #fff)',
            fontSize: '12px',
            cursor: 'pointer',
            color: 'var(--cp-text, #1e293b)',
          }}
        >
          Clear
        </button>
        <button
          onClick={onExport}
          style={{
            flex: 1,
            padding: '6px',
            borderRadius: '6px',
            border: '1px solid var(--cp-border, #e2e8f0)',
            background: 'var(--cp-surface, #fff)',
            fontSize: '12px',
            cursor: 'pointer',
            color: 'var(--cp-text, #1e293b)',
          }}
        >
          Export
        </button>
      </div>
    </div>
  );
}
