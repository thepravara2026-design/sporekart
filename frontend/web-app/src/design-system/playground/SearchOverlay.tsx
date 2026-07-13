import { useState, useRef, useEffect, useCallback, useMemo } from 'react';
import { SearchEngine, SearchResult } from './catalog/searchIndex';
import { componentManifest } from './catalog/componentManifest';
import { tokenManifest } from './catalog/tokenManifest';
import { useNavigate } from 'react-router-dom';

interface SearchOverlayProps {
  isOpen: boolean;
  onClose: () => void;
}

const TYPE_ICON: Record<SearchResult['type'], string> = {
  component: '\uD83E\uDDE9',
  token: '\uD83C\uDFA8',
  doc: '\uD83D\uDCC4',
};

const GROUP_LABELS: Record<SearchResult['type'], string> = {
  component: 'Components',
  token: 'Tokens',
  doc: 'Documentation',
};

const POPULAR_SEARCHES = ['Button', 'Input', 'Card', 'Color', 'Typography'];

export default function SearchOverlay({ isOpen, onClose }: SearchOverlayProps) {
  const navigate = useNavigate();
  const inputRef = useRef<HTMLInputElement>(null);
  const [mounted, setMounted] = useState(false);
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<SearchResult[]>([]);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const [suggestions, setSuggestions] = useState<string[]>([]);

  // SearchEngine internally consumes componentManifest and tokenManifest
  void componentManifest;
  void tokenManifest;
  const engine = useMemo(() => new SearchEngine(), []);

  useEffect(() => {
    if (isOpen) {
      setMounted(true);
    } else {
      const timer = setTimeout(() => setMounted(false), 200);
      return () => clearTimeout(timer);
    }
  }, [isOpen]);

  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    }
    return () => {
      document.body.style.overflow = '';
    };
  }, [isOpen]);

  useEffect(() => {
    if (isOpen && inputRef.current) {
      inputRef.current.focus();
    }
  }, [isOpen]);

  useEffect(() => {
    if (!query.trim()) {
      setResults([]);
      setSuggestions([]);
      setSelectedIndex(-1);
      return;
    }
    const searchResults = engine.search(query);
    setResults(searchResults);
    setSuggestions(engine.getSuggestions(query));
    setSelectedIndex(-1);
  }, [query, engine]);

  useEffect(() => {
    if (selectedIndex >= 0) {
      const el = document.querySelector(`[data-search-index="${selectedIndex}"]`);
      if (el) {
        el.scrollIntoView({ block: 'nearest' });
      }
    }
  }, [selectedIndex]);

  useEffect(() => {
    if (!isOpen) return;
    const handleGlobalKey = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };
    document.addEventListener('keydown', handleGlobalKey);
    return () => document.removeEventListener('keydown', handleGlobalKey);
  }, [isOpen, onClose]);

  const handleInputKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'ArrowDown') {
        e.preventDefault();
        setSelectedIndex((prev) => (prev < results.length - 1 ? prev + 1 : prev));
        return;
      }
      if (e.key === 'ArrowUp') {
        e.preventDefault();
        setSelectedIndex((prev) => (prev > 0 ? prev - 1 : 0));
        return;
      }
      if (e.key === 'Enter' && selectedIndex >= 0 && results[selectedIndex]) {
        navigate(results[selectedIndex].url);
        onClose();
      }
    },
    [results, selectedIndex, navigate, onClose],
  );

  const handleSelect = useCallback(
    (result: SearchResult) => {
      navigate(result.url);
      onClose();
    },
    [navigate, onClose],
  );

  const handleBackdropClick = useCallback(
    (e: React.MouseEvent) => {
      if (e.target === e.currentTarget) {
        onClose();
      }
    },
    [onClose],
  );

  const handleSuggestionClick = useCallback((suggestion: string) => {
    setQuery(suggestion);
  }, []);

  const handleClearQuery = useCallback(() => {
    setQuery('');
    inputRef.current?.focus();
  }, []);

  const groupedResults = useMemo(() => {
    const groups: { type: SearchResult['type']; items: SearchResult[] }[] = [];
    const componentResults = results.filter((r) => r.type === 'component');
    const tokenResults = results.filter((r) => r.type === 'token');
    const docResults = results.filter((r) => r.type === 'doc');

    if (componentResults.length > 0) {
      groups.push({ type: 'component', items: componentResults });
    }
    if (tokenResults.length > 0) {
      groups.push({ type: 'token', items: tokenResults });
    }
    if (docResults.length > 0) {
      groups.push({ type: 'doc', items: docResults });
    }
    return groups;
  }, [results]);

  if (!mounted) return null;

  const opacity = isOpen ? 1 : 0;

  const backdropStyle: React.CSSProperties = {
    position: 'fixed',
    inset: 0,
    zIndex: 9999,
    display: 'flex',
    justifyContent: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    opacity,
    transition: 'opacity 200ms ease',
  };

  const containerStyle: React.CSSProperties = {
    width: '100%',
    maxWidth: '640px',
    marginTop: '120px',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    alignSelf: 'flex-start',
  };

  const inputWrapperStyle: React.CSSProperties = {
    width: '100%',
    position: 'relative',
  };

  const inputStyle: React.CSSProperties = {
    width: '100%',
    padding: '16px 96px 16px 20px',
    fontSize: '1.125rem',
    border: '2px solid var(--color-border-focus, #2F6F4F)',
    borderRadius: 'var(--radius-lg, 8px)',
    backgroundColor: 'var(--color-bg-surface, #FFFFFF)',
    color: 'var(--color-text-primary, #1D2B22)',
    outline: 'none',
    boxSizing: 'border-box',
  };

  const closeButtonStyle: React.CSSProperties = {
    position: 'absolute',
    right: '12px',
    top: '50%',
    transform: 'translateY(-50%)',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: '4px 8px',
    color: 'var(--color-text-secondary, #6D6D6D)',
    fontSize: '1.25rem',
    lineHeight: 1,
    borderRadius: 'var(--radius-sm, 4px)',
  };

  const clearButtonStyle: React.CSSProperties = {
    position: 'absolute',
    right: '48px',
    top: '50%',
    transform: 'translateY(-50%)',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: '4px',
    color: 'var(--color-text-secondary, #6D6D6D)',
    fontSize: '1rem',
    lineHeight: 1,
  };

  const suggestionsWrapperStyle: React.CSSProperties = {
    width: '100%',
    display: 'flex',
    gap: '8px',
    flexWrap: 'wrap',
    marginTop: '12px',
  };

  const suggestionChipStyle: React.CSSProperties = {
    padding: '6px 14px',
    borderRadius: 'var(--radius-pill, 9999px)',
    border: '1px solid var(--color-border-default, #E3E6E3)',
    backgroundColor: 'var(--color-bg-surface, #FFFFFF)',
    color: 'var(--color-text-secondary, #6D6D6D)',
    cursor: 'pointer',
    fontSize: '0.875rem',
  };

  const resultsPanelStyle: React.CSSProperties = {
    width: '100%',
    marginTop: '16px',
    backgroundColor: 'var(--color-bg-surface, #FFFFFF)',
    borderRadius: 'var(--radius-lg, 8px)',
    boxShadow: 'var(--elevation-4, 0 10px 15px rgba(29,43,34,0.1))',
    maxHeight: '60vh',
    overflowY: 'auto',
  };

  const groupHeaderStyle: React.CSSProperties = {
    padding: '12px 16px 8px',
    fontSize: '0.75rem',
    fontWeight: 600,
    textTransform: 'uppercase',
    letterSpacing: '0.05em',
    color: 'var(--color-text-secondary, #6D6D6D)',
  };

  const resultItemBaseStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    gap: '12px',
    padding: '12px 16px',
    cursor: 'pointer',
    borderBottom: '1px solid var(--color-border-default, #E3E6E3)',
  };

  const resultIconStyle: React.CSSProperties = {
    fontSize: '1.25rem',
    lineHeight: 1,
    marginTop: '2px',
    flexShrink: 0,
  };

  const resultContentStyle: React.CSSProperties = {
    flex: 1,
    minWidth: 0,
  };

  const resultTitleStyle: React.CSSProperties = {
    fontSize: '0.9375rem',
    fontWeight: 500,
    color: 'var(--color-text-primary, #1D2B22)',
    margin: 0,
  };

  const resultDescriptionStyle: React.CSSProperties = {
    fontSize: '0.8125rem',
    color: 'var(--color-text-secondary, #6D6D6D)',
    margin: '4px 0 0',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  };

  const resultCategoryStyle: React.CSSProperties = {
    display: 'inline-block',
    marginTop: '6px',
    padding: '2px 8px',
    borderRadius: 'var(--radius-sm, 4px)',
    backgroundColor: 'var(--color-neutral-100, #F7F8F7)',
    color: 'var(--color-text-secondary, #6D6D6D)',
    fontSize: '0.6875rem',
    fontWeight: 500,
  };

  const emptyStateStyle: React.CSSProperties = {
    padding: '32px 16px',
    textAlign: 'center',
  };

  const emptyTitleStyle: React.CSSProperties = {
    fontSize: '1rem',
    fontWeight: 500,
    color: 'var(--color-text-primary, #1D2B22)',
    margin: '0 0 4px',
  };

  const emptyTextStyle: React.CSSProperties = {
    fontSize: '0.875rem',
    color: 'var(--color-text-secondary, #6D6D6D)',
    margin: 0,
  };

  const popularTitleStyle: React.CSSProperties = {
    fontSize: '0.75rem',
    fontWeight: 600,
    textTransform: 'uppercase',
    letterSpacing: '0.05em',
    color: 'var(--color-text-secondary, #6D6D6D)',
    marginBottom: '8px',
  };

  const popularChipStyle: React.CSSProperties = {
    padding: '8px 16px',
    borderRadius: 'var(--radius-pill, 9999px)',
    border: '1px solid var(--color-border-default, #E3E6E3)',
    backgroundColor: 'var(--color-bg-surface, #FFFFFF)',
    color: 'var(--color-text-primary, #1D2B22)',
    cursor: 'pointer',
    fontSize: '0.875rem',
    margin: '4px',
  };

  const noResultsStyle: React.CSSProperties = {
    padding: '32px 16px',
    textAlign: 'center',
  };

  const noResultsTitleStyle: React.CSSProperties = {
    fontSize: '1rem',
    fontWeight: 500,
    color: 'var(--color-text-primary, #1D2B22)',
    margin: '0 0 4px',
  };

  const noResultsTextStyle: React.CSSProperties = {
    fontSize: '0.875rem',
    color: 'var(--color-text-secondary, #6D6D6D)',
    margin: 0,
  };

  return (
    <div style={backdropStyle} onClick={handleBackdropClick}>
      <div style={containerStyle}>
        <div style={inputWrapperStyle}>
          <input
            ref={inputRef}
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={handleInputKeyDown}
            placeholder={'Search components, tokens, documentation\u2026'}
            role="searchbox"
            aria-label="Search design system"
            style={inputStyle}
          />
          {query && (
            <button
              type="button"
              onClick={handleClearQuery}
              style={clearButtonStyle}
              aria-label="Clear search"
            >
              {'\u2715'}
            </button>
          )}
          <button
            type="button"
            onClick={onClose}
            style={closeButtonStyle}
            aria-label="Close search"
          >
            {'\u2715'}
          </button>
        </div>

        {suggestions.length > 0 && query.trim() && (
          <div style={suggestionsWrapperStyle}>
            {suggestions.map((suggestion) => (
              <button
                key={suggestion}
                type="button"
                onClick={() => handleSuggestionClick(suggestion)}
                style={suggestionChipStyle}
              >
                {suggestion}
              </button>
            ))}
          </div>
        )}

        {query.trim() && results.length > 0 && (
          <div style={resultsPanelStyle}>
            {groupedResults.map((group) => (
              <div key={group.type}>
                <div style={groupHeaderStyle}>
                  {TYPE_ICON[group.type]} {GROUP_LABELS[group.type]}
                </div>
                {group.items.map((result) => {
                  const flatIndex = results.indexOf(result);
                  const isSelected = flatIndex === selectedIndex;
                  const itemStyle: React.CSSProperties = {
                    ...resultItemBaseStyle,
                    backgroundColor: isSelected
                      ? 'var(--color-neutral-100, #F7F8F7)'
                      : 'transparent',
                  };
                  return (
                    <div
                      key={result.id}
                      data-search-index={flatIndex}
                      onClick={() => handleSelect(result)}
                      style={itemStyle}
                      role="option"
                      aria-selected={isSelected}
                    >
                      <span style={resultIconStyle}>{TYPE_ICON[result.type]}</span>
                      <div style={resultContentStyle}>
                        <p style={resultTitleStyle}>{result.title}</p>
                        <p style={resultDescriptionStyle}>{result.description}</p>
                        <span style={resultCategoryStyle}>{result.category}</span>
                      </div>
                    </div>
                  );
                })}
              </div>
            ))}
          </div>
        )}

        {query.trim() && results.length === 0 && (
          <div style={noResultsStyle}>
            <p style={noResultsTitleStyle}>No results found</p>
            <p style={noResultsTextStyle}>
              Try a different search term or browse the catalog.
            </p>
          </div>
        )}

        {!query.trim() && (
          <div style={emptyStateStyle}>
            <p style={emptyTitleStyle}>What are you looking for?</p>
            <p style={emptyTextStyle}>
              Search components, design tokens, or documentation.
            </p>
            <div style={{ marginTop: '16px' }}>
              <p style={popularTitleStyle}>Popular searches</p>
              <div>
                {POPULAR_SEARCHES.map((term) => (
                  <button
                    key={term}
                    type="button"
                    onClick={() => setQuery(term)}
                    style={popularChipStyle}
                  >
                    {term}
                  </button>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
