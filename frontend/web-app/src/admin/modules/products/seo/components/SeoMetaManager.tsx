import React from 'react';
import type { SeoEntry } from '../types';

interface SeoMetaManagerProps {
  entries: SeoEntry[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' };
const th: React.CSSProperties = { textAlign: 'left', padding: '10px 12px', borderBottom: '2px solid var(--color-border)', color: 'var(--color-text-tertiary)', fontWeight: 600, textTransform: 'uppercase', fontSize: 'var(--text-body-xs)', letterSpacing: '0.5px', whiteSpace: 'nowrap' };
const td: React.CSSProperties = { padding: '10px 12px', borderBottom: '1px solid var(--color-border)', color: 'var(--color-text-primary)' };

export const SeoMetaManager: React.FC<SeoMetaManagerProps> = React.memo(({ entries, selectedId, onSelect }) => {
  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Meta Information</h2>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={table}>
          <thead>
            <tr>
              <th style={th}>Product</th>
              <th style={th}>Meta Title</th>
              <th style={th}>Description</th>
              <th style={th}>Focus Keyword</th>
              <th style={th}>Score</th>
              <th style={th}>Status</th>
            </tr>
          </thead>
          <tbody>
            {entries.map((e) => (
              <tr key={e.id} onClick={() => onSelect(e.id)}
                style={{ cursor: 'pointer', background: e.id === selectedId ? 'var(--color-bg-surface-raised)' : undefined }}
                onMouseEnter={(ev) => { if (e.id !== selectedId) ev.currentTarget.style.background = 'var(--color-bg-surface-hover)'; }}
                onMouseLeave={(ev) => { if (e.id !== selectedId) ev.currentTarget.style.background = 'none'; }}
                role="row" aria-selected={e.id === selectedId}>
                <td style={{ ...td, fontWeight: 500 }}>{e.productName}</td>
                <td style={{ ...td, maxWidth: 250, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{e.metaTitle}</td>
                <td style={{ ...td, maxWidth: 300, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-xs)' }}>{e.metaDescription}</td>
                <td style={td}><span style={{ padding: '2px 6px', borderRadius: 6, fontSize: 'var(--text-body-xs)', background: 'var(--color-accent-blue)15', color: 'var(--color-accent-blue)' }}>{e.focusKeyword}</span></td>
                <td style={td}><ScoreBadge score={e.seoScore} /></td>
                <td style={td}><StatusBadge status={e.publishingStatus} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

function ScoreBadge({ score }: { score: number }) {
  const color = score >= 80 ? 'var(--color-accent-green)' : score >= 60 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return <span style={{ padding: '2px 8px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 600, background: `${color}20`, color }}>{score}%</span>;
}

function StatusBadge({ status }: { status: string }) {
  const colors: Record<string, string> = { published: 'var(--color-accent-green)', draft: 'var(--color-accent-yellow)', ready_seo: 'var(--color-accent-blue)', ready_publish: 'var(--color-accent-purple)', archived: 'var(--color-accent-red)', review_required: 'var(--color-accent-orange)', rejected: 'var(--color-accent-orange)' };
  return <span style={{ padding: '2px 8px', borderRadius: 10, fontSize: 'var(--text-body-xs)', fontWeight: 600, background: `${colors[status] ?? '#888'}20`, color: colors[status] ?? '#888' }}>{status}</span>;
}
