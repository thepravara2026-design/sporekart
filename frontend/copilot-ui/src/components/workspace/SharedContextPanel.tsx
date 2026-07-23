import React, { useState } from 'react';
import type { ContextSnapshot } from './types/workspace';

interface SharedContextPanelProps {
  context: ContextSnapshot | null;
  loading?: boolean;
  onRefresh?: () => void;
  style?: React.CSSProperties;
}

interface SectionProps {
  title: string;
  icon: string;
  data: Record<string, unknown>;
  defaultOpen?: boolean;
}

function ContextSection({ title, icon, data, defaultOpen = false }: SectionProps) {
  const [open, setOpen] = useState(defaultOpen);
  const entries = Object.entries(data);

  return (
    <div style={{ marginBottom: '8px' }}>
      <button
        onClick={() => setOpen(o => !o)}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          width: '100%',
          padding: '8px 12px',
          border: 'none',
          borderRadius: '6px',
          background: 'var(--cp-surface-2, #f1f5f9)',
          cursor: 'pointer',
          fontSize: '13px',
          fontWeight: 600,
          color: 'var(--cp-text, #1e293b)',
          textAlign: 'left',
        }}
      >
        <span>{icon}</span>
        <span style={{ flex: 1 }}>{title}</span>
        <span style={{ fontSize: '10px', color: 'var(--cp-text-secondary, #64748b)' }}>
          {entries.length} items
        </span>
        <span>{open ? '−' : '+'}</span>
      </button>
      {open && (
        <div style={{
          padding: '8px 12px 4px',
          display: 'flex',
          flexDirection: 'column',
          gap: '4px',
        }}>
          {entries.map(([key, value]) => (
            <div key={key} style={{
              display: 'flex',
              justifyContent: 'space-between',
              fontSize: '12px',
              padding: '2px 0',
            }}>
              <span style={{ color: 'var(--cp-text-secondary, #64748b)', fontWeight: 500 }}>{key}</span>
              <span style={{ color: 'var(--cp-text, #1e293b)', maxWidth: '160px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                {String(value)}
              </span>
            </div>
          ))}
          {entries.length === 0 && (
            <div style={{ fontSize: '12px', color: 'var(--cp-text-secondary, #94a3b8)', fontStyle: 'italic' }}>
              No data
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default function SharedContextPanel({
  context,
  loading,
  onRefresh,
  style,
}: SharedContextPanelProps) {
  return (
    <div style={{
      width: '280px',
      background: 'var(--cp-surface, #fff)',
      borderLeft: '1px solid var(--cp-border, #e2e8f0)',
      display: 'flex',
      flexDirection: 'column',
      height: '100%',
      ...style,
    }}>
      <div style={{
        padding: '12px',
        borderBottom: '1px solid var(--cp-border, #e2e8f0)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
      }}>
        <div style={{ fontWeight: 600, fontSize: '14px', color: 'var(--cp-text, #1e293b)' }}>
          Shared Context
        </div>
        <button
          onClick={onRefresh}
          disabled={loading}
          style={{
            padding: '4px 10px',
            borderRadius: '6px',
            border: '1px solid var(--cp-border, #e2e8f0)',
            background: 'var(--cp-surface, #fff)',
            cursor: loading ? 'not-allowed' : 'pointer',
            fontSize: '12px',
            color: 'var(--cp-text, #1e293b)',
            opacity: loading ? 0.6 : 1,
          }}
        >
          {loading ? '...' : '↻ Refresh'}
        </button>
      </div>

      <div style={{ flex: 1, overflowY: 'auto', padding: '8px' }}>
        {context ? (
          <>
            <ContextSection
              title="User Context"
              icon="👤"
              data={context.userContext}
              defaultOpen
            />
            <ContextSection
              title="Business Context"
              icon="🏢"
              data={context.businessContext}
            />
            <ContextSection
              title="Conversation Context"
              icon="💬"
              data={context.conversationContext}
            />
            <ContextSection
              title="Knowledge Context"
              icon="📚"
              data={context.knowledgeContext}
            />
          </>
        ) : (
          <div style={{
            padding: '24px 16px',
            textAlign: 'center',
            color: 'var(--cp-text-secondary, #64748b)',
            fontSize: '13px',
          }}>
            {loading ? 'Loading context...' : 'No shared context available'}
          </div>
        )}
      </div>
    </div>
  );
}
