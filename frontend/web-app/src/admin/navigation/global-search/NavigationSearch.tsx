import { useState, useCallback, useRef, useEffect, useId, memo } from 'react';
import { useNavigate } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import type { NavItem, RecentPage, FavoriteItem } from '../types';

interface NavigationSearchProps {
  items: NavItem[];
  recentPages?: RecentPage[];
  favorites?: FavoriteItem[];
  onSearch?: (query: string) => void;
}

export const NavigationSearch = memo(function NavigationSearch({ items, recentPages = [], favorites = [], onSearch }: NavigationSearchProps) {
  const [query, setQuery] = useState('');
  const [open, setOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(0);
  const inputRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const listboxId = useId();

  const allPages = flattenItems(items);

  const results = query
    ? allPages.filter((p) => p.label.toLowerCase().includes(query.toLowerCase()))
    : [];
  const hasOptions = results.length > 0 || (!query && (recentPages.length > 0 || favorites.length > 0));
  const activeDescendantId = hasOptions ? `${listboxId}-option-${selectedIndex}` : undefined;

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'ArrowDown') { e.preventDefault(); setSelectedIndex((prev) => Math.min(prev + 1, results.length + recentPages.length + favorites.length - 1)); }
      else if (e.key === 'ArrowUp') { e.preventDefault(); setSelectedIndex((prev) => Math.max(prev - 1, 0)); }
      else if (e.key === 'Enter') { e.preventDefault(); if (results[selectedIndex]?.href) navigate(results[selectedIndex].href!); setOpen(false); }
      else if (e.key === 'Escape') { setOpen(false); inputRef.current?.blur(); }
    },
    [results, selectedIndex, recentPages.length, favorites.length, navigate]
  );

  useEffect(() => {
    const handleOutside = (e: MouseEvent) => {
      if (inputRef.current && !inputRef.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleOutside);
    return () => document.removeEventListener('mousedown', handleOutside);
  }, []);

  const showPanel = open && (query.length > 0 || recentPages.length > 0 || favorites.length > 0);

  return (
    <div style={{ position: 'relative', width: '100%', maxWidth: 360 }}>
      <div style={{ position: 'relative' }}>
        <Icon name="search" size={16} style={{ position: 'absolute', left: 10, top: '50%', transform: 'translateY(-50%)', color: 'var(--color-text-tertiary)', pointerEvents: 'none' }} />
        <input
          ref={inputRef}
          type="text"
          value={query}
          onChange={(e) => { setQuery(e.target.value); setOpen(true); setSelectedIndex(0); onSearch?.(e.target.value); }}
          onFocus={() => setOpen(true)}
          onKeyDown={handleKeyDown}
          placeholder="Search pages & modules..."
          aria-label="Search navigation"
          role="combobox"
          aria-expanded={showPanel}
          aria-controls={listboxId}
          aria-activedescendant={activeDescendantId}
          style={{
            width: '100%',
            padding: '8px 32px 8px 34px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-lg)',
            background: 'var(--color-surface-hover)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body)',
            outline: 'none',
          }}
        />
      </div>
      {showPanel && (
        <div
          id={listboxId}
          role="listbox"
          style={{
            position: 'absolute',
            top: '100%',
            left: 0,
            right: 0,
            marginTop: 4,
            background: 'var(--color-surface)',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-lg)',
            boxShadow: 'var(--elevation-lg)',
            zIndex: 100,
            maxHeight: 400,
            overflowY: 'auto',
          }}
        >
          {!query && favorites.length > 0 && (
            <div>
              <div style={sectionHeaderStyle}>Pinned</div>
              {favorites.filter((f) => f.pinned).map((fav, i) => (
                <ResultItem id={`${listboxId}-option-${i}`} key={fav.id} label={fav.label} icon={fav.icon} onClick={() => navigate(fav.href)} selected={selectedIndex === i} />
              ))}
            </div>
          )}
          {!query && recentPages.length > 0 && (
            <div>
              <div style={sectionHeaderStyle}>Recent</div>
              {recentPages.slice(0, 5).map((r, i) => (
                <ResultItem id={`${listboxId}-option-${i + favorites.filter((f) => f.pinned).length}`} key={r.id} label={r.label} icon="clock" onClick={() => navigate(r.href)} selected={selectedIndex === i + favorites.filter((f) => f.pinned).length} />
              ))}
            </div>
          )}
          {query && results.length > 0 && (
            <div>
              <div style={sectionHeaderStyle}>Results</div>
              {results.map((r, i) => (
                <ResultItem id={`${listboxId}-option-${i}`} key={r.id} label={r.label} icon={r.icon} onClick={() => { if (r.href) navigate(r.href); setOpen(false); }} selected={selectedIndex === i} />
              ))}
            </div>
          )}
          {query && results.length === 0 && (
            <div style={{ padding: '16px', textAlign: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body)' }}>
              No results for "{query}"
            </div>
          )}
        </div>
      )}
    </div>
  );
});

function ResultItem({ label, icon, onClick, selected, id }: { label: string; icon?: string; onClick: () => void; selected: boolean; id?: string }) {
  return (
    <button
      id={id}
      role="option"
      aria-selected={selected}
      onClick={onClick}
      style={{
        display: 'flex',
        alignItems: 'center',
        gap: 8,
        width: '100%',
        padding: '8px 12px',
        border: 'none',
        borderRadius: 'var(--radius-sm)',
        background: selected ? 'var(--color-surface-hover)' : 'transparent',
        cursor: 'pointer',
        textAlign: 'left',
        color: 'var(--color-text-primary)',
        fontSize: 'var(--text-body)',
      }}
    >
      {icon && <Icon name={icon} size={14} style={{ color: 'var(--color-text-tertiary)' }} />}
      {label}
    </button>
  );
}

function flattenItems(items: NavItem[]): NavItem[] {
  const result: NavItem[] = [];
  for (const item of items) {
    if (item.href) result.push(item);
    if (item.children) result.push(...flattenItems(item.children));
  }
  return result;
}

const sectionHeaderStyle: React.CSSProperties = {
  padding: '6px 12px',
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-tertiary)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
  fontWeight: 600,
};
