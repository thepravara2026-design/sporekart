import React, { useState, useRef, useEffect, useCallback } from 'react';

export interface SelectOption {
  value: string;
  label: string;
  disabled?: boolean;
}

export interface SelectProps {
  options: SelectOption[];
  value?: string;
  onChange?: (value: string) => void;
  placeholder?: string;
  label?: string;
  error?: string;
  warning?: string;
  success?: boolean;
  helperText?: string;
  required?: boolean;
  disabled?: boolean;
  readOnly?: boolean;
  fullWidth?: boolean;
  size?: 'sm' | 'md' | 'lg';
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
    <path
      d="M4 6L8 10L12 6"
      stroke="currentColor"
      strokeWidth="1.5"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

const sizeMap = {
  sm: { height: 'var(--input-height-sm)', fontSize: 'var(--text-body-sm)', paddingX: 'var(--space-inline-sm)' },
  md: { height: 'var(--input-height-md)', fontSize: 'var(--text-body)', paddingX: 'var(--space-inline-md)' },
  lg: { height: 'var(--input-height-lg)', fontSize: 'var(--text-body-lg)', paddingX: 'var(--space-inline-lg)' },
};

export const Select: React.FC<SelectProps> = ({
  options,
  value,
  onChange,
  placeholder = 'Select...',
  label,
  error,
  warning,
  success = false,
  helperText,
  required = false,
  disabled = false,
  readOnly = false,
  fullWidth = false,
  size: selectSize = 'md',
  className = '',
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [focusedIndex, setFocusedIndex] = useState(-1);
  const containerRef = useRef<HTMLDivElement>(null);
  const listRef = useRef<HTMLUListElement>(null);
  const generatedId = useRef(`sk-select-${Math.random().toString(36).substr(2, 9)}`);
  const triggerId = `${generatedId.current}-trigger`;
  const listboxId = `${generatedId.current}-listbox`;

  const selectedOption = options.find((opt) => opt.value === value);
  const isEmpty = !selectedOption;

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

  const handleToggle = () => {
    if (disabled || readOnly) return;
    setIsOpen((prev) => !prev);
    if (!isOpen) setFocusedIndex(options.findIndex((opt) => opt.value === value));
  };

  const handleSelect = (opt: SelectOption) => {
    if (opt.disabled) return;
    onChange?.(opt.value);
    close();
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (disabled || readOnly) return;

    switch (e.key) {
      case 'Enter':
      case ' ':
        e.preventDefault();
        if (!isOpen) {
          setIsOpen(true);
          setFocusedIndex(options.findIndex((opt) => opt.value === value));
        } else if (focusedIndex >= 0) {
          const opt = options[focusedIndex];
          if (opt && !opt.disabled) {
            onChange?.(opt.value);
            close();
          }
        }
        break;
      case 'ArrowDown':
        e.preventDefault();
        if (!isOpen) {
          setIsOpen(true);
          setFocusedIndex(0);
        } else {
          setFocusedIndex((prev) => {
            const next = prev + 1;
            return next < options.length ? next : prev;
          });
        }
        break;
      case 'ArrowUp':
        e.preventDefault();
        if (isOpen) {
          setFocusedIndex((prev) => (prev > 0 ? prev - 1 : prev));
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

  const sizeStyle = sizeMap[selectSize];

  let borderColor = 'var(--color-border-default)';
  if (error) borderColor = 'var(--color-border-error)';
  else if (success) borderColor = 'var(--color-border-success)';
  else if (warning) borderColor = 'var(--color-border-warning)';

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
    height: sizeStyle.height,
    padding: `0 ${sizeStyle.paddingX}`,
    background: disabled ? 'var(--color-bg-surface-default)' : 'var(--color-bg-surface-default)',
    border: 'var(--border-width-thin) solid ' + borderColor,
    borderRadius: 'var(--radius-input)',
    fontFamily: 'var(--font-family-sans)',
    fontSize: sizeStyle.fontSize,
    color: isEmpty ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
    cursor: (disabled || readOnly) ? 'not-allowed' : 'pointer',
    opacity: disabled ? 'var(--opacity-disabled)' : 1,
    transition: 'border-color var(--duration-fast) var(--easing-standard), box-shadow var(--duration-fast) var(--easing-standard)',
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
    maxHeight: 240,
    overflowY: 'auto',
    display: isOpen ? 'block' : 'none',
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
    fontSize: sizeStyle.fontSize,
    color: opt.disabled ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
    background: opt.value === value ? 'var(--color-bg-primary-weak)' : index === focusedIndex ? 'var(--color-bg-surface-default)' : 'transparent',
    cursor: opt.disabled ? 'not-allowed' : 'pointer',
    opacity: opt.disabled ? 'var(--opacity-disabled)' : 1,
    outline: 'none',
    userSelect: 'none',
  });

  const messageStyle: React.CSSProperties = {
    fontFamily: 'var(--font-family-sans)',
    fontSize: 'var(--text-caption)',
    lineHeight: 'var(--leading-normal)',
  };

  const errorStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-danger)',
  };

  const successStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-success)',
  };

  const warningStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-warning)',
  };

  const helperStyle: React.CSSProperties = {
    ...messageStyle,
    color: 'var(--color-text-secondary)',
  };

  return (
    <div
      ref={containerRef}
      className={`sk-select ${className}`}
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
        aria-activedescendant={focusedIndex >= 0 ? `${generatedId.current}-opt-${focusedIndex}` : undefined}
        aria-label={label || placeholder}
        aria-required={required}
        aria-invalid={!!error}
        tabIndex={disabled ? -1 : 0}
        style={triggerStyle}
        onClick={handleToggle}
        onKeyDown={handleKeyDown}
        onFocus={(e) => {
          if (!isOpen) return;
          e.currentTarget.style.borderColor = 'var(--color-border-focus)';
          e.currentTarget.style.boxShadow = '0 0 0 3px var(--color-focus-ring)';
        }}
        onBlur={(e) => {
          e.currentTarget.style.borderColor = borderColor;
          e.currentTarget.style.boxShadow = 'none';
        }}
      >
        <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {selectedOption ? selectedOption.label : placeholder}
        </span>
        <span style={chevronStyle}>
          <ChevronIcon open={isOpen} />
        </span>
      </div>

      <div style={dropdownStyle}>
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
      </div>

      <div className="sk-select__messages">
        {error && <span style={errorStyle} role="alert">{error}</span>}
        {!error && success && <span style={successStyle} role="status">Looks good</span>}
        {!error && !success && warning && <span style={warningStyle} role="alert">{warning}</span>}
        {!error && !success && !warning && helperText && <span style={helperStyle}>{helperText}</span>}
      </div>
    </div>
  );
};

Select.displayName = 'Select';

export default Select;
