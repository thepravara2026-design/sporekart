import React, { useState, useMemo } from 'react';
import type { CopilotInfo } from './types/workspace';

interface CopilotSwitcherProps {
  copilots: CopilotInfo[];
  activeCopilotId: string;
  onSwitch: (copilotId: string) => void;
  suggestedCopilotId?: string;
  style?: React.CSSProperties;
}

const STATUS_COLORS: Record<string, string> = {
  online: 'var(--cp-status-online, #22c55e)',
  busy: 'var(--cp-status-busy, #eab308)',
  offline: 'var(--cp-status-offline, #ef4444)',
  error: 'var(--cp-status-error, #ef4444)',
};

const TYPE_ICONS: Record<string, string> = {
  sales: '🛒',
  support: '🎧',
  analytics: '📊',
  training: '📚',
  general: '🤖',
  default: '⚡',
};

function getTypeIcon(type: string): string {
  return TYPE_ICONS[type.toLowerCase()] ?? TYPE_ICONS.default;
}

function getStatusColor(status: string): string {
  return STATUS_COLORS[status.toLowerCase()] ?? STATUS_COLORS.offline;
}

function CopilotCard({ copilot, isActive, isSuggested, onSelect }: {
  copilot: CopilotInfo;
  isActive: boolean;
  isSuggested: boolean;
  onSelect: () => void;
}) {
  const cardStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: '12px',
    padding: '10px 14px',
    borderRadius: '8px',
    cursor: 'pointer',
    background: isActive
      ? 'var(--cp-primary-bg, #e0e7ff)'
      : isSuggested
        ? 'var(--cp-suggested-bg, #fef9c3)'
        : 'transparent',
    border: isActive
      ? '2px solid var(--cp-primary, #6366f1)'
      : '2px solid transparent',
    transition: 'background 0.15s, border-color 0.15s',
    userSelect: 'none',
  };

  return (
    <div
      style={cardStyle}
      onClick={onSelect}
      role="option"
      aria-selected={isActive}
      onKeyDown={e => { if (e.key === 'Enter' || e.key === ' ') onSelect(); }}
      tabIndex={0}
    >
      <div style={{
        width: '36px',
        height: '36px',
        borderRadius: '50%',
        background: 'var(--cp-surface-2, #f1f5f9)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '18px',
        flexShrink: 0,
        position: 'relative',
      }}>
        {getTypeIcon(copilot.type)}
        <span style={{
          position: 'absolute',
          bottom: '0',
          right: '0',
          width: '10px',
          height: '10px',
          borderRadius: '50%',
          background: getStatusColor(copilot.status),
          border: '2px solid var(--cp-surface, #fff)',
        }} />
      </div>
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{
          fontWeight: 600,
          fontSize: '14px',
          color: 'var(--cp-text, #1e293b)',
          whiteSpace: 'nowrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
        }}>
          {copilot.name}
        </div>
        <div style={{
          fontSize: '12px',
          color: 'var(--cp-text-secondary, #64748b)',
          textTransform: 'capitalize',
        }}>
          {copilot.type} · v{copilot.version}
        </div>
      </div>
      <div style={{
        fontSize: '12px',
        color: 'var(--cp-text-secondary, #64748b)',
        whiteSpace: 'nowrap',
      }}>
        {copilot.activeSessions} sessions
      </div>
    </div>
  );
}

export default function CopilotSwitcher({
  copilots,
  activeCopilotId,
  onSwitch,
  suggestedCopilotId,
  style,
}: CopilotSwitcherProps) {
  const [open, setOpen] = useState(false);

  const activeCopilot = useMemo(
    () => copilots.find(c => c.copilotId === activeCopilotId),
    [copilots, activeCopilotId]
  );

  return (
    <div style={{ position: 'relative', ...style }}>
      <button
        onClick={() => setOpen(o => !o)}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          padding: '8px 14px',
          borderRadius: '8px',
          border: '1px solid var(--cp-border, #e2e8f0)',
          background: 'var(--cp-surface, #fff)',
          cursor: 'pointer',
          fontSize: '14px',
          fontWeight: 500,
          color: 'var(--cp-text, #1e293b)',
        }}
      >
        <span>{activeCopilot ? getTypeIcon(activeCopilot.type) : '🤖'}</span>
        <span>{activeCopilot?.name ?? 'Select Copilot'}</span>
        <span style={{
          width: '8px',
          height: '8px',
          borderRadius: '50%',
          background: activeCopilot ? getStatusColor(activeCopilot.status) : STATUS_COLORS.offline,
          display: 'inline-block',
        }} />
        <span style={{ marginLeft: '4px', fontSize: '10px' }}>{open ? '▲' : '▼'}</span>
      </button>

      {open && (
        <>
          <div
            style={{ position: 'fixed', inset: 0, zIndex: 98 }}
            onClick={() => setOpen(false)}
          />
          <div style={{
            position: 'absolute',
            top: 'calc(100% + 6px)',
            left: 0,
            minWidth: '340px',
            background: 'var(--cp-surface, #fff)',
            border: '1px solid var(--cp-border, #e2e8f0)',
            borderRadius: '10px',
            boxShadow: '0 8px 30px rgba(0,0,0,0.12)',
            zIndex: 99,
            padding: '8px',
            display: 'flex',
            flexDirection: 'column',
            gap: '4px',
          }}>
            {copilots.map(copilot => (
              <CopilotCard
                key={copilot.copilotId}
                copilot={copilot}
                isActive={copilot.copilotId === activeCopilotId}
                isSuggested={copilot.copilotId === suggestedCopilotId}
                onSelect={() => {
                  onSwitch(copilot.copilotId);
                  setOpen(false);
                }}
              />
            ))}
            {copilots.length === 0 && (
              <div style={{ padding: '16px', textAlign: 'center', color: 'var(--cp-text-secondary, #64748b)', fontSize: '13px' }}>
                No copilots available
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}
