import { useState, useCallback, useRef, useEffect } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

interface Suggestion {
  id: string;
  label: string;
  category?: string;
  icon?: string;
}

interface GlobalSearchProps {
  onSearch: (query: string) => void;
  suggestions?: Suggestion[];
  recentSearches?: string[];
  placeholder?: string;
}

export function GlobalSearch({
  onSearch,
  suggestions = [],
  recentSearches = [],
  placeholder = 'Search anything... (Ctrl+K)',
}: GlobalSearchProps) {
  const [query, setQuery] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const inputRef = useRef<HTMLInputElement>(null);
  const panelRef = useRef<HTMLDivElement>(null);

  const filteredSuggestions = query
    ? suggestions.filter((s) => s.label.toLowerCase().includes(query.toLowerCase()))
    : suggestions;

  const filteredRecent = query
    ? recentSearches.filter((r) => r.toLowerCase().includes(query.toLowerCase()))
    : recentSearches;

  const showPanel = isOpen && (filteredSuggestions.length > 0 || filteredRecent.length > 0 || query.length > 0);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const v = e.target.value;
    setQuery(v);
    setIsOpen(true);
    setSelectedIndex(-1);
  }, []);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      const totalItems = filteredSuggestions.length + filteredRecent.length;
      if (e.key === 'ArrowDown') {
        e.preventDefault();
        setSelectedIndex((prev) => (prev < totalItems - 1 ? prev + 1 : 0));
      } else if (e.key === 'ArrowUp') {
        e.preventDefault();
        setSelectedIndex((prev) => (prev > 0 ? prev - 1 : totalItems - 1));
      } else if (e.key === 'Enter') {
        e.preventDefault();
        if (selectedIndex >= 0 && selectedIndex < filteredSuggestions.length) {
          setQuery(filteredSuggestions[selectedIndex].label);
          onSearch(filteredSuggestions[selectedIndex].label);
        } else if (selectedIndex >= filteredSuggestions.length) {
          const recentIdx = selectedIndex - filteredSuggestions.length;
          if (filteredRecent[recentIdx]) {
            setQuery(filteredRecent[recentIdx]);
            onSearch(filteredRecent[recentIdx]);
          }
        } else {
          onSearch(query);
        }
        setIsOpen(false);
      } else if (e.key === 'Escape') {
        setIsOpen(false);
        inputRef.current?.blur();
      }
    },
    [filteredSuggestions, filteredRecent, selectedIndex, query, onSearch]
  );

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (panelRef.current && !panelRef.current.contains(e.target as Node) &&
          inputRef.current && !inputRef.current.contains(e.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    const handleKb = (e: KeyboardEvent) => {
      if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        inputRef.current?.focus();
        setIsOpen(true);
      }
    };
    document.addEventListener('keydown', handleKb);
    return () => document.removeEventListener('keydown', handleKb);
  }, []);

  const handleFocus = useCallback(
    (e: React.FocusEvent<HTMLInputElement>) => {
      e.target.style.borderColor = 'var(--color-primary)';
      e.target.style.boxShadow = '0 0 0 3px var(--color-primary-alpha)';
    },
    []
  );

  const handleBlur = useCallback(
    (e: React.FocusEvent<HTMLInputElement>) => {
      e.target.style.borderColor = 'var(--color-border)';
      e.target.style.boxShadow = 'none';
    },
    []
  );

  const groupedSuggestions = filteredSuggestions.reduce<Record<string, Suggestion[]>>((acc, s) => {
    const cat = s.category ?? 'General';
    if (!acc[cat]) acc[cat] = [];
    acc[cat].push(s);
    return acc;
  }, {});

  return (
    <div style={{ position: 'relative', width: '100%', maxWidth: 480 }} ref={panelRef}>
      <div
        style={{
          position: 'relative',
          display: 'flex',
          alignItems: 'center',
        }}
      >
        <span
          style={{
            position: 'absolute',
            left: 12,
            top: '50%',
            transform: 'translateY(-50%)',
            color: 'var(--color-text-tertiary)',
            display: 'flex',
            pointerEvents: 'none',
          }}
        >
          <Icon name="search" size={18} />
        </span>
        <input
          ref={inputRef}
          type="text"
          value={query}
          onChange={handleInputChange}
          onFocus={(e) => { handleFocus(e); setIsOpen(true); }}
          onBlur={handleBlur}
          onKeyDown={handleKeyDown}
          placeholder={placeholder}
          aria-label="Global search"
          role="combobox"
          aria-expanded={showPanel}
          aria-autocomplete="list"
          style={{
            width: '100%',
            padding: '10px 40px 10px 40px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-lg)',
            background: 'var(--color-surface)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body)',
            outline: 'none',
          }}
        />
        {query && (
          <button
            onClick={() => { setQuery(''); setIsOpen(true); inputRef.current?.focus(); }}
            aria-label="Clear search"
            style={{
              position: 'absolute',
              right: 8,
              top: '50%',
              transform: 'translateY(-50%)',
              background: 'none',
              border: 'none',
              cursor: 'pointer',
              color: 'var(--color-text-tertiary)',
              padding: 4,
              display: 'flex',
            }}
          >
            <Icon name="x" size={16} />
          </button>
        )}
      </div>
      {showPanel && (
        <div
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
            zIndex: 1000,
            maxHeight: 400,
            overflowY: 'auto',
          }}
        >
          {filteredRecent.length > 0 && (
            <div>
              <div
                style={{
                  padding: '8px 12px',
                  fontSize: 'var(--text-caption)',
                  color: 'var(--color-text-tertiary)',
                  textTransform: 'uppercase',
                  letterSpacing: '0.05em',
                }}
              >
                Recent Searches
              </div>
              {filteredRecent.map((r) => (
                <button
                  key={`recent-${r}`}
                  role="option"
                  aria-selected={selectedIndex === filteredSuggestions.length + filteredRecent.indexOf(r)}
                  onClick={() => { setQuery(r); onSearch(r); setIsOpen(false); }}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 8,
                    width: '100%',
                    padding: '8px 12px',
                    border: 'none',
                    background: selectedIndex === filteredSuggestions.length + filteredRecent.indexOf(r) ? 'var(--color-surface-hover)' : 'transparent',
                    cursor: 'pointer',
                    color: 'var(--color-text-secondary)',
                    fontSize: 'var(--text-body)',
                    textAlign: 'left',
                  }}
                >
                  <Icon name="clock" size={14} />
                  {r}
                </button>
              ))}
            </div>
          )}
          {Object.entries(groupedSuggestions).map(([category, items]) => (
            <div key={category}>
              <div
                style={{
                  padding: '8px 12px',
                  fontSize: 'var(--text-caption)',
                  color: 'var(--color-text-tertiary)',
                  textTransform: 'uppercase',
                  letterSpacing: '0.05em',
                }}
              >
                {category}
              </div>
              {items.map((s) => {
                const globalIdx = filteredSuggestions.indexOf(s);
                return (
                  <button
                    key={s.id}
                    role="option"
                    aria-selected={selectedIndex === globalIdx}
                    onClick={() => { setQuery(s.label); onSearch(s.label); setIsOpen(false); }}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: 8,
                      width: '100%',
                      padding: '8px 12px',
                      border: 'none',
                      background: selectedIndex === globalIdx ? 'var(--color-surface-hover)' : 'transparent',
                      cursor: 'pointer',
                      color: 'var(--color-text-primary)',
                      fontSize: 'var(--text-body)',
                      textAlign: 'left',
                    }}
                  >
                    {s.icon && <Icon name={s.icon} size={16} />}
                    <span>{s.label}</span>
                  </button>
                );
              })}
            </div>
          ))}
          {query.length > 0 && filteredSuggestions.length === 0 && filteredRecent.length === 0 && (
            <div
              style={{
                padding: '16px 12px',
                textAlign: 'center',
                color: 'var(--color-text-tertiary)',
                fontSize: 'var(--text-body)',
              }}
            >
              No results for &ldquo;{query}&rdquo;
            </div>
          )}
        </div>
      )}
    </div>
  );
}
