import React, { useState, useId, useCallback, useRef } from 'react';

export interface TagSelectorProps {
  label?: string;
  tags?: string[];
  onChange?: (tags: string[]) => void;
  placeholder?: string;
  suggestions?: string[];
  disabled?: boolean;
  error?: string;
  helperText?: string;
  maxTags?: number;
  className?: string;
  style?: React.CSSProperties;
}

const wrapperStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-stack-xs)',
};

const labelStyle: React.CSSProperties = {
  fontSize: 'var(--text-body)',
  fontWeight: 'var(--weight-medium)',
  color: 'var(--color-text-primary)',
};

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: 'var(--space-inline-xs)',
  padding: '6px 8px',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-input)',
  background: 'var(--color-bg-background)',
  minHeight: 40,
  cursor: 'text',
  transition: 'border-color var(--duration-fast) var(--easing-standard)',
};

const tagStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  gap: 4,
  padding: '2px 8px',
  borderRadius: 'var(--radius-tag)',
  background: 'var(--color-bg-primary-weak)',
  color: 'var(--color-primary)',
  fontSize: 'var(--text-caption)',
  fontWeight: 'var(--weight-medium)',
};

const removeBtnStyle: React.CSSProperties = {
  display: 'inline-flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: 14,
  height: 14,
  borderRadius: 'var(--radius-full)',
  border: 'none',
  background: 'transparent',
  color: 'inherit',
  cursor: 'pointer',
  fontSize: 10,
  padding: 0,
  lineHeight: 1,
  opacity: 0.7,
};

const inputStyle: React.CSSProperties = {
  flex: 1,
  minWidth: 80,
  border: 'none',
  outline: 'none',
  background: 'transparent',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-primary)',
  padding: '2px 0',
};

const suggestionsStyle: React.CSSProperties = {
  position: 'absolute',
  top: '100%',
  left: 0,
  right: 0,
  marginTop: 4,
  background: 'var(--color-bg-surface-default)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-dropdown)',
  boxShadow: 'var(--shadow-lg)',
  zIndex: 'var(--z-dropdown)',
  maxHeight: 160,
  overflowY: 'auto',
};

const suggestionItemStyle: React.CSSProperties = {
  padding: '8px 12px',
  fontSize: 'var(--text-body)',
  cursor: 'pointer',
  transition: 'background var(--duration-fast) var(--easing-standard)',
};

export const TagSelector: React.FC<TagSelectorProps> = ({
  label,
  tags = [],
  onChange,
  placeholder = 'Type and press Enter',
  suggestions = [],
  disabled = false,
  error,
  helperText,
  maxTags,
  className = '',
  style,
}) => {
  const id = useId();
  const [inputValue, setInputValue] = useState('');
  const [showSuggestions, setShowSuggestions] = useState(false);
  const [activeSuggestion, setActiveSuggestion] = useState(-1);
  const inputRef = useRef<HTMLInputElement>(null);
  const hasError = !!error;

  const addTag = useCallback((tag: string) => {
    const trimmed = tag.trim();
    if (!trimmed) return;
    if (tags.includes(trimmed)) return;
    if (maxTags !== undefined && tags.length >= maxTags) return;
    onChange?.([...tags, trimmed]);
    setInputValue('');
    setShowSuggestions(false);
    setActiveSuggestion(-1);
  }, [tags, onChange, maxTags]);

  const removeTag = useCallback((index: number) => {
    const next = tags.filter((_, i) => i !== index);
    onChange?.(next);
  }, [tags, onChange]);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const val = e.target.value;
    setInputValue(val);
    if (val && suggestions.length > 0) {
      setShowSuggestions(true);
      setActiveSuggestion(-1);
    } else {
      setShowSuggestions(false);
    }
  }, [suggestions]);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      if (activeSuggestion >= 0 && suggestions[activeSuggestion]) {
        addTag(suggestions[activeSuggestion]);
      } else if (inputValue) {
        addTag(inputValue);
      }
    } else if (e.key === 'Backspace' && !inputValue && tags.length > 0) {
      removeTag(tags.length - 1);
    } else if (e.key === 'ArrowDown') {
      e.preventDefault();
      setActiveSuggestion((prev) => Math.min(prev + 1, suggestions.length - 1));
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      setActiveSuggestion((prev) => Math.max(prev - 1, -1));
    } else if (e.key === 'Escape') {
      setShowSuggestions(false);
      setActiveSuggestion(-1);
    }
  }, [inputValue, activeSuggestion, suggestions, addTag, removeTag, tags]);

  const filtered = inputValue
    ? suggestions.filter((s) => s.toLowerCase().includes(inputValue.toLowerCase()) && !tags.includes(s))
    : suggestions.filter((s) => !tags.includes(s));

  return (
    <div className={className} style={{ ...wrapperStyle, ...style }}>
      {label && (
        <label htmlFor={id} style={labelStyle}>
          {label}
        </label>
      )}
      <div style={{ position: 'relative' }}>
        <div
          style={{
            ...containerStyle,
            borderColor: hasError ? 'var(--color-danger)' : undefined,
            opacity: disabled ? 'var(--opacity-disabled)' : undefined,
            cursor: disabled ? 'not-allowed' : 'text',
          }}
          onClick={() => !disabled && inputRef.current?.focus()}
          onFocus={() => {
            if (!hasError && !disabled) {
              (document.activeElement as HTMLElement)?.closest?.('[style*="border"]')?.setAttribute?.('style', '');
            }
          }}
        >
          {tags.map((tag, i) => (
            <span key={`${tag}-${i}`} style={tagStyle}>
              <span>{tag}</span>
              {!disabled && (
                <button
                  type="button"
                  style={removeBtnStyle}
                  onClick={(e) => { e.stopPropagation(); removeTag(i); }}
                  aria-label={`Remove ${tag}`}
                >
                  &#x2715;
                </button>
              )}
            </span>
          ))}
          {(!maxTags || tags.length < maxTags) && (
            <input
              ref={inputRef}
              id={id}
              type="text"
              value={inputValue}
              onChange={handleInputChange}
              onKeyDown={handleKeyDown}
              onFocus={() => inputValue && setShowSuggestions(true)}
              onBlur={() => setTimeout(() => setShowSuggestions(false), 150)}
              placeholder={tags.length === 0 ? placeholder : ''}
              disabled={disabled}
              aria-label={label || 'Add tag'}
              style={inputStyle}
            />
          )}
        </div>
        {showSuggestions && filtered.length > 0 && (
          <div style={suggestionsStyle} role="listbox">
            {filtered.map((s, i) => (
              <div
                key={s}
                role="option"
                aria-selected={i === activeSuggestion}
                style={{
                  ...suggestionItemStyle,
                  background: i === activeSuggestion ? 'var(--color-bg-primary-weak)' : undefined,
                }}
                onMouseDown={() => addTag(s)}
                onMouseEnter={(e) => (e.currentTarget.style.background = 'var(--color-bg-background)')}
                onMouseLeave={(e) => (e.currentTarget.style.background = 'transparent')}
              >
                {s}
              </div>
            ))}
          </div>
        )}
      </div>
      {hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-danger)' }} role="alert">{error}</span>}
      {helperText && !hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{helperText}</span>}
    </div>
  );
};

TagSelector.displayName = 'TagSelector';
export default TagSelector;
