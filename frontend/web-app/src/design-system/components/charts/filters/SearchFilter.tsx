import React, { useState, useCallback, useRef, useEffect } from 'react';

export interface SearchFilterProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  debounceMs?: number;
  className?: string;
  style?: React.CSSProperties;
}

export const SearchFilter: React.FC<SearchFilterProps> = ({
  value,
  onChange,
  placeholder = 'Search...',
  debounceMs = 300,
  className = '',
  style,
}) => {
  const [localValue, setLocalValue] = useState(value);
  const timeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    setLocalValue(value);
  }, [value]);

  const handleChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const newValue = e.target.value;
      setLocalValue(newValue);
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
      timeoutRef.current = setTimeout(() => {
        onChange(newValue);
      }, debounceMs);
    },
    [onChange, debounceMs]
  );

  const handleClear = useCallback(() => {
    setLocalValue('');
    if (timeoutRef.current) clearTimeout(timeoutRef.current);
    onChange('');
  }, [onChange]);

  useEffect(() => {
    return () => {
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
    };
  }, []);

  const containerStyle: React.CSSProperties = {
    position: 'relative',
    display: 'inline-flex',
    alignItems: 'center',
    width: '100%',
    ...style,
  };

  const inputStyle: React.CSSProperties = {
    width: '100%',
    height: 'var(--input-height-md)',
    padding: '0 var(--space-8) 0 var(--space-8)',
    border: 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-input)',
    background: 'var(--color-bg-surface-default)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-body)',
    color: 'var(--color-text-primary)',
    boxSizing: 'border-box',
    outline: 'none',
  };

  const iconStyle: React.CSSProperties = {
    position: 'absolute',
    left: 'var(--space-2)',
    color: 'var(--color-text-secondary)',
    fontSize: 'var(--text-body)',
    pointerEvents: 'none',
    display: 'flex',
    alignItems: 'center',
  };

  const clearBtnStyle: React.CSSProperties = {
    position: 'absolute',
    right: 'var(--space-2)',
    background: 'transparent',
    border: 'none',
    cursor: 'pointer',
    color: 'var(--color-text-disabled)',
    fontSize: 'var(--text-body)',
    padding: 0,
    display: 'inline-flex',
    alignItems: 'center',
    justifyContent: 'center',
    width: 20,
    height: 20,
    borderRadius: 'var(--radius-full)',
  };

  return (
    <div
      className={`sk-search-filter ${className}`}
      style={containerStyle}
    >
      <span style={iconStyle} aria-hidden="true">
        {'\u2315'}
      </span>
      <input
        type="search"
        value={localValue}
        onChange={handleChange}
        placeholder={placeholder}
        style={inputStyle}
        aria-label={placeholder}
      />
      {localValue && (
        <button
          style={clearBtnStyle}
          onClick={handleClear}
          aria-label="Clear search"
          type="button"
        >
          {'\u2715'}
        </button>
      )}
    </div>
  );
};

SearchFilter.displayName = 'SearchFilter';

export default SearchFilter;
