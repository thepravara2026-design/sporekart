import React, { useState, useRef, useEffect, useCallback } from 'react';
import type { SelectOption } from './Select';

export interface MultiSelectProps {
  options: SelectOption[];
  values?: string[];
  onChange?: (values: string[]) => void;
  placeholder?: string;
  label?: string;
  error?: string;
  helperText?: string;
  required?: boolean;
  disabled?: boolean;
  fullWidth?: boolean;
  size?: 'sm' | 'md' | 'lg';
  maxDisplay?: number;
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

const CheckIcon: React.FC = () => (
  <svg width="14" height="14" viewBox="0 0 14 14" fill="none" aria-hidden="true">
    <path d="M2 7l3.5 3.5L12 3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const CloseIcon: React.FC = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none" aria-hidden="true">
    <path d="M9 3L3 9M3 3l6 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const sizeMap = {
  sm: { height: 'var(--input-height-sm)', fontSize: 'var(--text-body-sm)', paddingX: 'var(--space-inline-sm)', chipSize: 20 },
  md: { height: 'var(--input-height-md)', fontSize: 'var(--text-body)', paddingX: 'var(--space-inline-md)', chipSize: 24 },
  lg: { height: 'var(--input-height-lg)', fontSize: 'var(--text-body-lg)', paddingX: 'var(--space-inline-lg)', chipSize: 28 },
};

export const MultiSelect: React.FC<MultiSelectProps> = ({
  options,
  values = [],
  onChange,
  placeholder = 'Select...',
  label,
  error,
  helperText,
  required = false,
  disabled = false,
  fullWidth = false,
  size: multiSize = 'md',
  maxDisplay = 3,
  className = '',
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [focusedIndex, setFocusedIndex] = useState(-1);
  const containerRef = useRef<HTMLDivElement>(null);
  const listRef = useRef<HTMLUListElement>(null);
  const generatedId = useRef(`sk-ms-${Math.random().toString(36).substr(2, 9)}`);
  const triggerId = `${generatedId.current}-trigger`;
  const listboxId = `${generatedId.current}-listbox`;

  const close = useCallback(() => {
    setIsOpen(false);
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
    if (isOpen && listRef.current && focusedIndex >= 0) {
      const item = listRef.current.children[focusedIndex] as HTMLElement;
      item?.focus();
    }
  }, [focusedIndex, isOpen]);

  const selectedOptions = options.filter((opt) => values.includes(opt.value));
  const displayChips = selectedOptions.slice(0, maxDisplay);
  const remaining = selectedOptions.length - maxDisplay;

  const allSelected = options.every((opt) => values.includes(opt.value));

  const isOptionDisabled = (opt: SelectOption): boolean => {
    return opt.disabled === true;
  };

  const handleToggle = () => {
    if (disabled) return;
    setIsOpen((prev) => !prev);
    if (!isOpen) setFocusedIndex(0);
  };

  const handleSelect = (opt: SelectOption) => {
    if (isOptionDisabled(opt)) return;
    const isSelected = values.includes(opt.value);
    let newValues: string[];
    if (isSelected) {
      newValues = values.filter((v) => v !== opt.value);
    } else {
      newValues = [...values, opt.value];
    }
    onChange?.(newValues);
  };

  const handleSelectAll = () => {
    if (allSelected) {
      onChange?.([]);
    } else {
      onChange?.(options.filter((opt) => !isOptionDisabled(opt)).map((opt) => opt.value));
    }
  };

  const handleRemoveChip = (e: React.MouseEvent, optValue: string) => {
    e.stopPropagation();
    if (disabled) return;
    onChange?.(values.filter((v) => v !== optValue));
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (disabled) return;

    switch (e.key) {
      case 'Enter':
      case ' ':
        e.preventDefault();
        if (!isOpen) {
          setIsOpen(true);
          setFocusedIndex(0);
        } else if (focusedIndex === 0) {
          handleSelectAll();
        } else if (focusedIndex > 0) {
          const opt = options[focusedIndex - 1];
          if (opt) handleSelect(opt);
        }
        break;
      case 'ArrowDown':
        e.preventDefault();
        if (!isOpen) {
          setIsOpen(true);
          setFocusedIndex(0);
        } else {
          setFocusedIndex((prev) => Math.min(prev + 1, options.length));
        }
        break;
      case 'ArrowUp':
        e.preventDefault();
        if (isOpen) {
          setFocusedIndex((prev) => (prev > 0 ? prev - 1 : 0));
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

  const sz = sizeMap[multiSize];

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
    flexWrap: 'wrap',
    gap: 'var(--space-inline-xs)',
    minHeight: sz.height,
    padding: `var(--space-1) ${sz.paddingX}`,
    background: disabled ? 'var(--color-bg-surface-default)' : 'var(--color-bg-surface-default)',
    border: 'var(--border-width-thin) solid ' + (error ? 'var(--color-border-error)' : 'var(--color-border-default)'),
    borderRadius: 'var(--radius-input)',
    cursor: disabled ? 'not-allowed' : 'pointer',
    opacity: disabled ? 'var(--opacity-disabled)' : 1,
    transition: 'border-color var(--duration-fast) var(--easing-standard)',
    outline: 'none',
    width: '100%',
    boxSizing: 'border-box',
  };

  const chipStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 2,
    height: sz.chipSize,
    padding: '0 var(--space-inline-xs)',
    background: 'var(--color-bg-primary-weak)',
    borderRadius: 'var(--radius-tag)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-bg-primary-default)',
    whiteSpace: 'nowrap',
  };

  const chipLabelStyle: React.CSSProperties = {
    maxWidth: 100,
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  };

  const chipRemoveStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    border: 'none',
    background: 'none',
    cursor: disabled ? 'not-allowed' : 'pointer',
    padding: 0,
    color: 'inherit',
    fontSize: 0,
  };

  const placeholderStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: 'var(--color-text-disabled)',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
    flex: 1,
  };

  const chevronStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    color: 'var(--color-text-secondary)',
    flexShrink: 0,
    marginLeft: 'auto',
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
    maxHeight: 280,
    overflowY: 'auto',
    display: isOpen ? 'block' : 'none',
  };

  const optionListStyle: React.CSSProperties = {
    listStyle: 'none',
    margin: 0,
    padding: 'var(--space-1) 0',
  };

  const selectAllStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-2) var(--space-3)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    fontWeight: 'var(--weight-medium)',
    color: 'var(--color-text-primary)',
    cursor: disabled ? 'not-allowed' : 'pointer',
    borderBottom: 'var(--border-width-thin) solid var(--color-border-default)',
    userSelect: 'none',
  };

  const checkboxStyle = (checked: boolean): React.CSSProperties => ({
    width: 16,
    height: 16,
    borderRadius: 'var(--radius-xs)',
    border: 'var(--border-width-thin) solid var(--color-border-strong)',
    background: checked ? 'var(--color-bg-primary-default)' : 'transparent',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    color: checked ? 'var(--color-text-on-primary)' : 'transparent',
    flexShrink: 0,
    transition: 'background var(--duration-fast) var(--easing-standard)',
  });

  const getOptionStyle = (opt: SelectOption, index: number): React.CSSProperties => ({
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-2) var(--space-3)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sz.fontSize,
    color: isOptionDisabled(opt) ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
    background: index === focusedIndex - 1 ? 'var(--color-bg-surface-default)' : 'transparent',
    cursor: isOptionDisabled(opt) ? 'not-allowed' : 'pointer',
    opacity: isOptionDisabled(opt) ? 'var(--opacity-disabled)' : 1,
    outline: 'none',
    userSelect: 'none',
  });

  const remainingStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    whiteSpace: 'nowrap',
  };

  const messageStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
  };

  const errorStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-danger)',
  };

  const helperStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-secondary)',
  };

  return (
    <div
      ref={containerRef}
      className={`sk-multi-select ${className}`}
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
        aria-multiselectable="true"
        aria-label={label || placeholder}
        aria-invalid={!!error}
        tabIndex={disabled ? -1 : 0}
        style={triggerStyle}
        onClick={handleToggle}
        onKeyDown={handleKeyDown}
      >
        {selectedOptions.length === 0 && (
          <span style={placeholderStyle}>{placeholder}</span>
        )}
        {displayChips.map((opt) => (
          <span key={opt.value} style={chipStyle}>
            <span style={chipLabelStyle}>{opt.label}</span>
            <button
              type="button"
              style={chipRemoveStyle}
              onClick={(e) => handleRemoveChip(e, opt.value)}
              aria-label={`Remove ${opt.label}`}
              tabIndex={-1}
            >
              <CloseIcon />
            </button>
          </span>
        ))}
        {remaining > 0 && (
          <span style={remainingStyle}>+{remaining} more</span>
        )}
        <span style={chevronStyle}>
          <ChevronIcon open={isOpen} />
        </span>
      </div>

      <div style={dropdownStyle}>
        <div
          role="option"
          aria-selected={allSelected}
          tabIndex={-1}
          style={selectAllStyle}
          onClick={handleSelectAll}
          onMouseEnter={() => setFocusedIndex(0)}
        >
          <span style={checkboxStyle(allSelected)}>
            {allSelected && <CheckIcon />}
          </span>
          <span>Select all</span>
        </div>
        <ul
          ref={listRef}
          id={listboxId}
          role="listbox"
          aria-label={label || placeholder}
          aria-multiselectable="true"
          style={optionListStyle}
        >
          {options.map((opt, index) => {
            const isSelected = values.includes(opt.value);
            return (
              <li
                key={opt.value}
                id={`${generatedId.current}-opt-${index}`}
                role="option"
                aria-selected={isSelected}
                aria-disabled={isOptionDisabled(opt)}
                tabIndex={-1}
                style={getOptionStyle(opt, index)}
                onClick={() => handleSelect(opt)}
                onMouseEnter={() => setFocusedIndex(index + 1)}
              >
                <span style={checkboxStyle(isSelected)}>
                  {isSelected && <CheckIcon />}
                </span>
                <span>{opt.label}</span>
              </li>
            );
          })}
        </ul>
      </div>

      <div className="sk-multi-select__messages">
        {error && <span style={errorStyle} role="alert">{error}</span>}
        {!error && helperText && <span style={helperStyle}>{helperText}</span>}
      </div>
    </div>
  );
};

MultiSelect.displayName = 'MultiSelect';

export default MultiSelect;
