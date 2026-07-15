import React, { useState, useCallback } from 'react';
import type { PublishingReadinessStatus } from '../types';

interface PublishingReadinessProps {
  entries: PublishingReadinessStatus[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const statusConfig: Record<string, { label: string; color: string; bg: string }> = {
  ready:              { label: 'Ready',            color: '#fff', bg: 'var(--color-success)' },
  needs_review:       { label: 'Needs Review',     color: '#fff', bg: 'var(--color-warning)' },
  blocked:            { label: 'Blocked',           color: '#fff', bg: 'var(--color-danger)' },
  incomplete:         { label: 'Incomplete',        color: '#000', bg: '#eab308' },
  compliance_failure: { label: 'Compliance Failure', color: '#fff', bg: '#7f1d1d' },
  awaiting_approval:  { label: 'Awaiting Approval', color: '#fff', bg: '#7c3aed' },
};

function scoreBarColor(s: number): string {
  if (s >= 80) return 'var(--color-success)';
  if (s >= 50) return 'var(--color-warning)';
  return 'var(--color-danger)';
}

export const PublishingReadiness = React.memo(function PublishingReadiness({ entries, selectedId, onSelect }: PublishingReadinessProps) {
  const [expanded, setExpanded] = useState<string | null>(null);
  const toggleExpand = useCallback((id: string) => {
    setExpanded((prev) => (prev === id ? null : id));
  }, []);

  const th: React.CSSProperties = { padding: '10px 12px', textAlign: 'left', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.5px', borderBottom: '2px solid var(--color-border)' };
  const td: React.CSSProperties = { padding: '10px 12px', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', borderBottom: '1px solid var(--color-border)' };

  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 }} aria-label="Publishing readiness status">
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Publishing Readiness</h2>
      <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }} role="grid" aria-label="Product publishing readiness">
          <thead>
            <tr>
              <th style={th}>Product</th>
              <th style={th}>Status</th>
              <th style={th}>Readiness</th>
              <th style={th}>Blockers</th>
              <th style={th}>Warnings</th>
              <th style={th}></th>
            </tr>
          </thead>
          <tbody>
            {entries.map((entry) => {
              const st = statusConfig[entry.status] ?? { label: entry.status, color: '#fff', bg: 'var(--color-text-secondary)' };
              const isSelected = entry.productId === selectedId;
              const isExpanded = expanded === entry.productId;
              return (
                <React.Fragment key={entry.productId}>
                  <tr
                    onClick={() => onSelect(entry.productId)}
                    style={{ cursor: 'pointer', background: isSelected ? 'var(--color-bg-surface-raised)' : 'transparent', transition: 'background 0.15s' }}
                    aria-selected={isSelected}
                    tabIndex={0}
                    onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') onSelect(entry.productId); }}
                  >
                    <td style={td}>{entry.productName}</td>
                    <td style={td}>
                      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, padding: '2px 8px', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-body-xs)', fontWeight: 600, color: st.color, background: st.bg }}>
                        <span style={{ width: 6, height: 6, borderRadius: '50%', background: st.color, opacity: 0.7 }} aria-hidden="true" />
                        {st.label}
                      </span>
                    </td>
                    <td style={td}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                        <div style={{ flex: 1, height: 8, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', overflow: 'hidden', maxWidth: 120 }}>
                          <div style={{ height: '100%', width: `${entry.readinessScore}%`, borderRadius: 'var(--radius-xs)', background: scoreBarColor(entry.readinessScore), transition: 'width 0.3s' }} />
                        </div>
                        <span style={{ fontSize: 'var(--text-body-xs)', fontWeight: 600, color: 'var(--color-text-secondary)', minWidth: 28 }}>{entry.readinessScore}%</span>
                      </div>
                    </td>
                    <td style={{ ...td, color: entry.blockers.length > 0 ? 'var(--color-danger)' : 'var(--color-text-tertiary)' }}>
                      {entry.blockers.length > 0 ? `${entry.blockers.length} blocker${entry.blockers.length > 1 ? 's' : ''}` : '—'}
                    </td>
                    <td style={{ ...td, color: entry.warnings.length > 0 ? 'var(--color-warning)' : 'var(--color-text-tertiary)' }}>
                      {entry.warnings.length > 0 ? `${entry.warnings.length} warning${entry.warnings.length > 1 ? 's' : ''}` : '—'}
                    </td>
                    <td style={td}>
                      <button
                        onClick={(e) => { e.stopPropagation(); toggleExpand(entry.productId); }}
                        style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', padding: '4px 8px', borderRadius: 'var(--radius-sm)' }}
                        aria-label={isExpanded ? 'Collapse details' : 'Expand details'}
                        aria-expanded={isExpanded}
                      >
                        {isExpanded ? '▲' : '▼'}
                      </button>
                    </td>
                  </tr>
                  {isExpanded && (
                    <tr>
                      <td colSpan={6} style={{ padding: '12px 16px', background: 'var(--color-bg-surface-raised)', borderBottom: '1px solid var(--color-border)' }}>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                          {entry.blockers.length > 0 && (
                            <div>
                              <div style={{ fontSize: 'var(--text-body-xs)', fontWeight: 600, color: 'var(--color-danger)', marginBottom: 4 }}>Blockers</div>
                              <ul style={{ margin: 0, padding: '0 0 0 16px', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
                                {entry.blockers.map((b, i) => <li key={i}>{b}</li>)}
                              </ul>
                            </div>
                          )}
                          {entry.warnings.length > 0 && (
                            <div>
                              <div style={{ fontSize: 'var(--text-body-xs)', fontWeight: 600, color: 'var(--color-warning)', marginBottom: 4 }}>Warnings</div>
                              <ul style={{ margin: 0, padding: '0 0 0 16px', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
                                {entry.warnings.map((w, i) => <li key={i}>{w}</li>)}
                              </ul>
                            </div>
                          )}
                          {entry.blockers.length === 0 && entry.warnings.length === 0 && (
                            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>No blockers or warnings. Product is ready to publish.</div>
                          )}
                        </div>
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
});
