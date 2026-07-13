import React, { useState, useRef, useEffect, useCallback } from 'react';
import type { SelectOption } from './Select';

export interface AsyncSelectProps {
  loadOptions: (search: string) => Promise<SelectOption[]>;
  value?: string;
  onChange?: (value: string) => void;
  debounceMs?: number;
  minSearchLength?: number;
  placeholder?: string;
  label?: string;
  error?: string;
  helperText?: string;
  required?: boolean;
  disabled?: boolean;
  fullWidth?: boolean;
  size?: 'sm' | 'md' | 'lg';
  noResultsMessage?: string;
  className?: string;
}

const ChevronIcon: React.FC<{ open: boolean }> = ({ open }) => (
  <svg
    width="16"
    height="16"
    viewBox="0 0 16 16"
    fill="none"
    aria-hidden="true"
    style={{
      transition: 'transform var(--duration-fast) var(--easing-standard)',
      transform: open ? 'rotate(180deg)' : 'rotate(0deg)',
    }}
  >
    <path d="M4 6L8 10L12 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const Spinner: React.FC = () => (
  <span
    style={{
      display: 'inline-block',
      width: 14,
      height: 14,
      border: '2px solid var(--color-border-default)',
      borderRightColor: 'transparent',
      borderRadius: '50%',
      animation: 'sk-async-spin 0.6s linear infinite',
      flexShrink: 0,
    }}
    aria-hidden="true"
  />
);

const sizeMap = {
  sm: { height: 'var(--input-height-sm)', fontSize: 'var(--text-body-sm)', paddingX: 'var(--space-inline-sm)' },
  md: { height: 'var(--input-height-md)', fontSize: 'var(--text-body)', paddingX: 'var(--space-inline-md)' },
  lg: { height: 'var(--input-height-lg)', fontSize: 'var(--text-body-lg)', paddingX: 'var(--space-inline-lg)' },
};

export const AsyncSelect: React.FC<AsyncSelectProps> = ({
  loadOptions,
  value,
  onChange,
  debounceMs = 300,
  minSearchLength = 0,
  placeholder = 'Search...',
  label,
  error,
  helperText,
  required = false,
  disabled = false,
  fullWidth = false,
  size: asyncSize = 'md',
  noResultsMessage = 'No results found',
  className = '',
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [search, setSearch] = useState('');
  const [options, setOptions] = useState<SelectOption[]>([]);
  const [loading, setLoading] = useState(false);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [focusedIndex, setFocusedIndex] = useState(-1);
  const containerRef = useRef<HTMLDivElement>(null);
  const listRef = useRef<HTMLUListElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);
  const debounceRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const generatedId = useRef(`sk-as-${Math.random().toString(36).substr(2, 9)}`);
  const triggerId = `${generatedId.current}-trigger`;
  const listboxId = `${generatedId.current}-listbox`;

  const selectedOption = options.find((opt) => opt.value === value);
  const isEmpty = !selectedOption;

  const close = useCallback(() => {
    setIsOpen(false);
    setSearch('');
    setOptions([]);
    setLoadError(null);
    setFocusedIndex(-1);
  }, []);

  useEffect(() => {
    if (!isOpen) return;
    const handleClickOutside = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        close();
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [isOpen, close]);

  useEffect(() => {
    if (isOpen && searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, [isOpen]);

  useEffect(() => {
    if (isOpen && listRef.current && focusedIndex >= 0) {
      const item = listRef.current.children[focusedIndex] as HTMLElement;
      item?.focus();
    }
  }, [focusedIndex, isOpen]);

  const doLoad = useCallback(async (query: string) => {
    setLoading(true);
    setLoadError(null);
    try {
      const result = await loadOptions(query);
      setOptions(result);
      setFocusedIndex(-1);
    } catch (_err) {
      setLoadError('Failed to load options. Please try again.');
      setOptions([]);
    } finally {
      setLoading(false);
    }
  }, [loadOptions]);

  useEffect(() => {
    if (!isOpen) return;
    if (search.length < minSearchLength) {
      setOptions([]);
      setLoadError(null);
      return;
    }
    if (debounceRef.current) clearTimeout(debounceRef.current);
    debounceRef.current = setTimeout(() => {
      doLoad(search);
    }, debounceMs);
    return () => {
      if (debounceRef.current) clearTimeout(debounceRef.current);
    };
  }, [search, isOpen, minSearchLength, debounceMs, doLoad]);

  const handleToggle = () => {
    if (disabled) return;
    setIsOpen((prev) => !prev);
    if (!isOpen) {
      if (minSearchLength === 0) {
        doLoad('');
      }
    }
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
  };

  const handleSelect = (opt: SelectOption) => {
    if (opt.disabled) return;
    onChange?.(opt.value);
    close();
  };

  const handleRetry = () => {
    doLoad(search);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (disabled) return;

    switch (e.key) {
      case 'Enter':
        e.preventDefault();
        if (isOpen && focusedIndex >= 0 && focusedIndex < options.length) {
          const opt = options[focusedIndex];
          if (opt && !opt.disabled) {
            onChange?.(opt.value);
            close();
          }
        } else if (!isOpen) {
          setIsOpen(true);
          if (minSearchLength === 0) doLoad('');
        }
        break;
      case 'ArrowDown':
        e.preventDefault();
        if (!isOpen) {
          setIsOpen(true);
          if (minSearchLength === 0) doLoad('');
        } else {
          setFocusedIndex((prev) => (prev < options.length - 1 ? prev + 1 : prev));
        }
        break;
      case 'ArrowUp':
        e.preventDefault();
        if (isOpen) {
          setFocusedIndex((prev) => (prev > 0 ? prev - 1 : -1));
        }
        break;
      case 'Escape':
        e.preventDefault();
        close();
        break;
      case 'Tab':
        close();
        break;
    }
  };

  const sz = sizeMap[asyncSize];

  const wrapperStyle: React.CSSProperties = {
    display: 'inline-flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
    width: fullWidth ? '100%' : undefined,
    position: 'relative',
  };

  const labelStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-label)',
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-primary)',
    lineHeight: 'var(--leading-normal)',
  };

  const requiredIndicator: React.CSSProperties = {
    color: 'var(--color-text-danger)',
    marginLeft: 2,
  };

  const triggerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 'var(--space-inline-sm)',
    height: sz.height,
    padding: `0 ${sz.paddingX}`,
    background: disabled ? 'var(--color-bg-surface-default)' : 'var(--color-bg-surface-default)',
    border: 'var(--border-width-thin) solid ' + (error ? 'var(--color-border-error)' : 'var(--color-border-default)'),
    borderRadius: 'var(--radius-input)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: isEmpty ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
    cursor: disabled ? 'not-allowed' : 'pointer',
    opacity: disabled ? 'var(--opacity-disabled)' : 1,
    transition: 'border-color var(--duration-fast) var(--easing-standard)',
    outline: 'none',
    width: '100%',
    boxSizing: 'border-box',
    userSelect: 'none',
  };

  const chevronStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    color: 'var(--color-text-secondary)',
    flexShrink: 0,
  };

  const dropdownStyle: React.CSSProperties = {
    position: 'absolute',
    top: 'calc(100% + 4px)',
    left: 0,
    right: 0,
    zIndex: 'var(--z-dropdown)',
    background: 'var(--color-bg-surface-overlay)',
    border: 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-dropdown)',
    boxShadow: 'var(--shadow-2)',
    maxHeight: 300,
    overflowY: 'auto',
    display: isOpen ? 'block' : 'none',
  };

  const searchInputStyle: React.CSSProperties = {
    width: '100%',
    border: 'none',
    borderBottom: 'var(--border-width-thin) solid var(--color-border-default)',
    padding: 'var(--space-2) var(--space-3)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: 'var(--color-text-primary)',
    background: 'transparent',
    outline: 'none',
    boxSizing: 'border-box',
  };

  const optionListStyle: React.CSSProperties = {
    listStyle: 'none',
    margin: 0,
    padding: 'var(--space-1) 0',
  };

  const getOptionStyle = (opt: SelectOption, index: number): React.CSSProperties => ({
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-2) var(--space-3)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: opt.disabled ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
    background: opt.value === value ? 'var(--color-bg-primary-weak)' : index === focusedIndex ? 'var(--color-bg-surface-default)' : 'transparent',
    cursor: opt.disabled ? 'not-allowed' : 'pointer',
    outline: 'none',
    userSelect: 'none',
  });

  const loadingStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-3)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: 'var(--color-text-secondary)',
  };

  const errorMessageStyle: React.CSSProperties = {
    padding: 'var(--space-3)',
    textAlign: 'center',
  };

  const errorTextStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: 'var(--color-text-danger)',
    margin: 0,
    marginBottom: 'var(--space-2)',
  };

  const retryBtnStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: 'var(--color-bg-primary-default)',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    padding: 0,
    textDecoration: 'underline',
  };

  const noResultsStyle: React.CSSProperties = {
    padding: 'var(--space-3)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: 'var(--color-text-secondary)',
    textAlign: 'center',
  };

  const promptStyle: React.CSSProperties = {
    padding: 'var(--space-3)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: 'var(--color-text-secondary)',
    textAlign: 'center',
  };

  const messageStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
  };

  const errorMsgStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-danger)',
  };

  const helperMsgStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-secondary)',
  };

  const renderDropdownContent = () => {
    if (search.length < minSearchLength) {
      return <div style={promptStyle}>Type at least {minSearchLength} character{minSearchLength > 1 ? 's' : ''} to search</div>;
    }
    if (loading) {
      return (
        <div style={loadingStyle}>
          <Spinner />
          <span>Loading options...</span>
        </div>
      );
    }
    if (loadError) {
      return (
        <div style={errorMessageStyle}>
          <p style={errorTextStyle}>{loadError}</p>
          <button type="button" style={retryBtnStyle} onClick={handleRetry}>
            Retry
          </button>
        </div>
      );
    }
    if (options.length === 0) {
      return <div style={noResultsStyle}>{noResultsMessage}</div>;
    }
    return (
      <ul
        ref={listRef}
        id={listboxId}
        role="listbox"
        aria-label={label || placeholder}
        style={optionListStyle}
      >
        {options.map((opt, index) => (
          <li
            key={opt.value}
            id={`${generatedId.current}-opt-${index}`}
            role="option"
            aria-selected={opt.value === value}
            aria-disabled={opt.disabled}
            tabIndex={-1}
            style={getOptionStyle(opt, index)}
            onClick={() => handleSelect(opt)}
            onMouseEnter={() => setFocusedIndex(index)}
          >
            {opt.label}
          </li>
        ))}
      </ul>
    );
  };

  return (
    <div
      ref={containerRef}
      className={`sk-async-select ${className}`}
      style={wrapperStyle}
    >
      {(label || required) && (
        <label htmlFor={triggerId} style={labelStyle}>
          {label}
          {required && <span style={requiredIndicator} aria-hidden="true">*</span>}
        </label>
      )}

      <div
        id={triggerId}
        role="combobox"
        aria-haspopup="listbox"
        aria-expanded={isOpen}
        aria-controls={listboxId}
        aria-label={label || placeholder}
        aria-required={required}
        aria-invalid={!!error}
        tabIndex={disabled ? -1 : 0}
        style={triggerStyle}
        onClick={handleToggle}
        onKeyDown={handleKeyDown}
      >
        <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {selectedOption ? selectedOption.label : placeholder}
        </span>
        <span style={chevronStyle}>
          <ChevronIcon open={isOpen} />
        </span>
      </div>

      <div style={dropdownStyle}>
        <div style={{ padding: 'var(--space-1) 0' }}>
          <input
            ref={searchInputRef}
            type="text"
            placeholder={placeholder}
            value={search}
            onChange={handleSearchChange}
            style={searchInputStyle}
            aria-label="Search options"
            tabIndex={isOpen ? 0 : -1}
          />
        </div>
        {renderDropdownContent()}
      </div>

      <div className="sk-async-select__messages">
        {error && <span style={errorMsgStyle} role="alert">{error}</span>}
        {!error && helperText && <span style={helperMsgStyle}>{helperText}</span>}
      </div>

      <style>{`
        @keyframes sk-async-spin {
          from { transform: rotate(0deg); }
          to { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
};

AsyncSelect.displayName = 'AsyncSelect';

export default AsyncSelect;
