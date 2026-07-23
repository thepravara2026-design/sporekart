import React, { useState, useCallback } from 'react';
import type { CopilotInfo } from './types/workspace';

interface FloatingAssistantProps {
  copilots: CopilotInfo[];
  activeCopilotId?: string;
  unreadCount?: number;
  onSendMessage?: (message: string, copilotId?: string) => void;
  onToggleExpand?: (expanded: boolean) => void;
  style?: React.CSSProperties;
}

type QuickAction = { label: string; message: string };

const QUICK_ACTIONS: QuickAction[] = [
  { label: 'Help', message: 'I need help' },
  { label: 'Summarize', message: 'Summarize the current context' },
  { label: 'Status', message: 'What is my current status?' },
];

export default function FloatingAssistant({
  copilots,
  activeCopilotId,
  unreadCount = 0,
  onSendMessage,
  onToggleExpand,
  style,
}: FloatingAssistantProps) {
  const [expanded, setExpanded] = useState(false);
  const [inputValue, setInputValue] = useState('');

  const activeCopilot = copilots.find(c => c.copilotId === activeCopilotId);

  const toggle = useCallback(() => {
    setExpanded(e => {
      onToggleExpand?.(!e);
      return !e;
    });
  }, [onToggleExpand]);

  const handleSend = useCallback((message?: string) => {
    const text = message ?? inputValue;
    if (!text.trim()) return;
    onSendMessage?.(text, activeCopilotId);
    setInputValue('');
  }, [inputValue, activeCopilotId, onSendMessage]);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  }, [handleSend]);

  const minimizedStyle: React.CSSProperties = {
    position: 'fixed',
    bottom: '20px',
    right: '20px',
    zIndex: 1000,
    ...style,
  };

  const expandedStyle: React.CSSProperties = {
    position: 'fixed',
    bottom: '20px',
    right: '20px',
    zIndex: 1000,
    width: '360px',
    height: '480px',
    background: 'var(--cp-surface, #fff)',
    borderRadius: '12px',
    boxShadow: '0 8px 32px rgba(0,0,0,0.16)',
    display: 'flex',
    flexDirection: 'column',
    overflow: 'hidden',
    ...style,
  };

  if (!expanded) {
    return (
      <div style={minimizedStyle}>
        <button
          onClick={toggle}
          style={{
            width: '56px',
            height: '56px',
            borderRadius: '50%',
            border: 'none',
            background: 'var(--cp-primary, #6366f1)',
            color: '#fff',
            fontSize: '24px',
            cursor: 'pointer',
            boxShadow: '0 4px 16px rgba(99,102,241,0.4)',
            position: 'relative',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          {activeCopilot ? '💬' : '🤖'}
          {unreadCount > 0 && (
            <span style={{
              position: 'absolute',
              top: '-4px',
              right: '-4px',
              background: '#ef4444',
              color: '#fff',
              fontSize: '11px',
              fontWeight: 700,
              minWidth: '20px',
              height: '20px',
              borderRadius: '10px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              padding: '0 4px',
            }}>
              {unreadCount > 99 ? '99+' : unreadCount}
            </span>
          )}
        </button>
      </div>
    );
  }

  return (
    <div style={expandedStyle}>
      <div style={{
        padding: '12px 16px',
        background: 'var(--cp-primary, #6366f1)',
        color: '#fff',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <span>💬</span>
          <span style={{ fontWeight: 600, fontSize: '14px' }}>
            {activeCopilot?.name ?? 'Assistant'}
          </span>
        </div>
        <button
          onClick={toggle}
          style={{
            background: 'none',
            border: 'none',
            color: '#fff',
            cursor: 'pointer',
            fontSize: '18px',
            padding: '0',
            lineHeight: 1,
          }}
        >
          −
        </button>
      </div>

      <div style={{
        flex: 1,
        padding: '12px',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
        gap: '8px',
        justifyContent: 'flex-end',
      }}>
        <div style={{ fontSize: '13px', color: 'var(--cp-text-secondary, #64748b)', textAlign: 'center' }}>
          How can I help you?
        </div>
        <div style={{
          display: 'flex',
          flexWrap: 'wrap',
          gap: '4px',
          justifyContent: 'center',
        }}>
          {QUICK_ACTIONS.map(qa => (
            <button
              key={qa.label}
              onClick={() => handleSend(qa.message)}
              style={{
                padding: '4px 10px',
                borderRadius: '12px',
                border: '1px solid var(--cp-border, #e2e8f0)',
                background: 'var(--cp-surface-2, #f1f5f9)',
                fontSize: '12px',
                cursor: 'pointer',
                color: 'var(--cp-text, #1e293b)',
              }}
            >
              {qa.label}
            </button>
          ))}
        </div>
      </div>

      <div style={{
        padding: '10px 12px',
        borderTop: '1px solid var(--cp-border, #e2e8f0)',
        display: 'flex',
        gap: '8px',
      }}>
        <input
          type="text"
          value={inputValue}
          onChange={e => setInputValue(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Type a message..."
          style={{
            flex: 1,
            padding: '8px 12px',
            borderRadius: '8px',
            border: '1px solid var(--cp-border, #e2e8f0)',
            fontSize: '13px',
            outline: 'none',
            background: 'var(--cp-surface-2, #f8fafc)',
            color: 'var(--cp-text, #1e293b)',
          }}
        />
        <button
          onClick={() => handleSend()}
          style={{
            padding: '8px 14px',
            borderRadius: '8px',
            border: 'none',
            background: 'var(--cp-primary, #6366f1)',
            color: '#fff',
            fontSize: '13px',
            cursor: 'pointer',
          }}
        >
          Send
        </button>
      </div>
    </div>
  );
}
