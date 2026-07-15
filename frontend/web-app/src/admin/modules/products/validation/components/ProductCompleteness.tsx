import React, { useState, useCallback, useMemo } from 'react';

interface ProductCompletenessEntry {
  productId: string;
  productName: string;
  completionPct: number;
}

interface ProductCompletenessProps {
  entries: ProductCompletenessEntry[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const inputStyle: React.CSSProperties = { padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)', fontSize: 'var(--text-body-sm)', width: '100%', boxSizing: 'border-box' };
const rowBase: React.CSSProperties = { display: 'flex', alignItems: 'center', gap: 12, padding: '10px 12px', borderRadius: 'var(--radius-sm)', cursor: 'pointer', transition: 'background 0.15s', border: 'none', background: 'none', width: '100%', textAlign: 'left', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' };
const container: React.CSSProperties = { borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', overflow: 'hidden' };

function ProgressBar({ pct }: { pct: number }) {
  const color = pct >= 80 ? 'var(--color-accent-green)' : pct >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return (
    <div style={{ flex: 1, height: 8, borderRadius: 4, background: 'var(--color-bg-surface-raised)', overflow: 'hidden' }}>
      <div style={{ height: '100%', width: `${pct}%`, borderRadius: 4, background: color, transition: 'width 0.3s' }} />
    </div>
  );
}

export const ProductCompleteness: React.FC<ProductCompletenessProps> = React.memo(({ entries, selectedId, onSelect }) => {
  const [search, setSearch] = useState('');
  const handleSearch = useCallback((e: React.ChangeEvent<HTMLInputElement>) => setSearch(e.target.value), []);
  const handleSelect = useCallback((id: string) => onSelect(id), [onSelect]);

  const filtered = useMemo(() => {
    if (!search.trim()) return entries;
    const q = search.toLowerCase();
    return entries.filter((e) => e.productName.toLowerCase().includes(q) || e.productId.toLowerCase().includes(q));
  }, [entries, search]);

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Product Completeness</h2>
      <input
        type="text"
        placeholder="Search products..."
        value={search}
        onChange={handleSearch}
        style={inputStyle}
        aria-label="Search products"
      />
      <div style={container} role="listbox" aria-label="Product completeness list">
        {filtered.map((e) => {
          const isSelected = e.productId === selectedId;
          const rowStyle: React.CSSProperties = {
            ...rowBase,
            background: isSelected ? 'var(--color-bg-surface-raised)' : 'transparent',
            fontWeight: isSelected ? 600 : 400,
          };
          return (
            <button
              key={e.productId}
              onClick={() => handleSelect(e.productId)}
              style={rowStyle}
              role="option"
              aria-selected={isSelected}
              onMouseEnter={(ev) => { if (!isSelected) ev.currentTarget.style.background = 'var(--color-bg-surface-raised)'; }}
              onMouseLeave={(ev) => { if (!isSelected) ev.currentTarget.style.background = 'none'; }}
            >
              <span style={{ minWidth: 140, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{e.productName}</span>
              <ProgressBar pct={e.completionPct} />
              <span style={{ minWidth: 36, textAlign: 'right', fontWeight: 600, color: e.completionPct >= 80 ? 'var(--color-accent-green)' : e.completionPct >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)' }}>
                {e.completionPct}%
              </span>
            </button>
          );
        })}
        {filtered.length === 0 && (
          <div style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>No products found</div>
        )}
      </div>
    </div>
  );
});
