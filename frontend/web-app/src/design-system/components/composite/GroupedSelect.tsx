import React, { useState, useRef, useEffect, useCallback } from 'react';

export interface GroupedOption {
  label: string;
  options: { value: string; label: string; disabled?: boolean }[];
}

export interface GroupedSelectProps {
  groups: GroupedOption[];
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

const sizeStyles: Record<string, React.CSSProperties> = {
  sm: { height: 'var(--input-height-sm)', fontSize: 'var(--text-body-sm)', padding: '0 var(--space-inline-sm)' },
  md: { height: 'var(--input-height-md)', fontSize: 'var(--text-body)', padding: '0 var(--space-inline-md)' },
  lg: { height: 'var(--input-height-lg)', fontSize: 'var(--text-body-lg)', padding: '0 var(--space-inline-md)' },
};

const dropdownSizeStyles: Record<string, React.CSSProperties> = {
  sm: { fontSize: 'var(--text-body-sm)' },
  md: { fontSize: 'var(--text-body)' },
  lg: { fontSize: 'var(--text-body-lg)' },
};

export const GroupedSelect: React.FC<GroupedSelectProps> = ({
  groups,
  value,
  onChange,
  placeholder = 'Select...',
  label,
  error,
  warning,
  success,
  helperText,
  required,
  disabled = false,
  readOnly = false,
  fullWidth = false,
  size = 'md',
  className = '',
}) => {
  const [open, setOpen] = useState(false);
  const [focusedIndex, setFocusedIndex] = useState(-1);
  const containerRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLButtonElement>(null);

  const flatOptions = groups.flatMap((g) => g.options);
  const selectedOption = flatOptions.find((o) => o.value === value);
  const hasError = !!error;
  const hasWarning = !!warning;
  const hasSuccess = !!success;

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    if (!open) setFocusedIndex(-1);
  }, [open]);

  const handleSelect = useCallback((val: string) => {
    onChange?.(val);
    setOpen(false);
    triggerRef.current?.focus();
  }, [onChange]);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (disabled || readOnly) return;

    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      if (!open) { setOpen(true); return; }
      if (focusedIndex >= 0 && focusedIndex < flatOptions.length) {
        handleSelect(flatOptions[focusedIndex].value);
      }
    }

    if (e.key === 'Escape') {
      setOpen(false);
      triggerRef.current?.focus();
    }

    if (e.key === 'ArrowDown') {
      e.preventDefault();
      if (!open) { setOpen(true); return; }
      setFocusedIndex((prev) => Math.min(prev + 1, flatOptions.length - 1));
    }

    if (e.key === 'ArrowUp') {
      e.preventDefault();
      setFocusedIndex((prev) => Math.max(prev - 1, -1));
    }
  };

  const borderColor = hasError
    ? 'var(--color-border-error)'
    : hasSuccess
    ? 'var(--color-border-success)'
    : hasWarning
    ? 'var(--color-border-default)'
    : 'var(--color-border-default)';

  const triggerStyle: React.CSSProperties = {
    ...sizeStyles[size],
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 'var(--space-inline-sm)',
    width: fullWidth ? '100%' : undefined,
    minWidth: 180,
    backgroundColor: disabled ? 'var(--color-bg-surface-default)' : 'var(--color-bg-surface-default)',
    border: `var(--border-width-thin) solid ${borderColor}`,
    borderRadius: 'var(--radius-input)',
    cursor: disabled || readOnly ? 'not-allowed' : 'pointer',
    opacity: disabled ? 'var(--opacity-disabled)' : 1,
    color: selectedOption ? 'var(--color-text-primary)' : 'var(--color-text-disabled)',
    outline: 'none',
    fontFamily: 'var(--font-family-sans)',
    fontWeight: 'var(--weight-normal)',
    lineHeight: 'var(--leading-normal)',
    transition: 'border-color var(--duration-fast) var(--easing-standard), box-shadow var(--duration-fast) var(--easing-standard)',
  };

  const dropdownStyle: React.CSSProperties = {
    position: 'absolute',
    top: '100%',
    left: 0,
    right: 0,
    marginTop: 'var(--space-inline-xs)',
    backgroundColor: 'var(--color-bg-surface-overlay)',
    border: 'var(--border-width-thin) solid var(--color-border-default)',
    borderRadius: 'var(--radius-dropdown)',
    boxShadow: 'var(--shadow-3)',
    zIndex: 'var(--z-dropdown)',
    maxHeight: 280,
    overflowY: 'auto',
    ...dropdownSizeStyles[size],
  };

  const groupLabelStyle: React.CSSProperties = {
    padding: 'var(--space-inline-xs) var(--space-inline-md)',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-secondary)',
    textTransform: 'uppercase' as const,
    letterSpacing: 'var(--tracking-wide)',
    backgroundColor: 'var(--color-bg-background)',
    position: 'sticky' as const,
    top: 0,
    zIndex: 1,
  };

  const optionStyle = (isSelected: boolean, isFocused: boolean, optionDisabled?: boolean): React.CSSProperties => ({
    padding: 'var(--space-inline-sm) var(--space-inline-md)',
    cursor: optionDisabled ? 'not-allowed' : 'pointer',
    backgroundColor: isFocused ? 'var(--color-bg-primary-weak)' : isSelected ? 'var(--color-bg-primary-weak)' : 'transparent',
    color: optionDisabled ? 'var(--color-text-disabled)' : isSelected ? 'var(--color-primary)' : 'var(--color-text-primary)',
    fontWeight: isSelected ? 'var(--weight-medium)' : 'var(--weight-normal)',
    opacity: optionDisabled ? 'var(--opacity-disabled)' : 1,
    transition: 'background-color var(--duration-fast) var(--easing-standard)',
  });

  const chevronStyle: React.CSSProperties = {
    width: 16,
    height: 16,
    transition: 'transform var(--duration-fast) var(--easing-standard)',
    transform: open ? 'rotate(180deg)' : 'rotate(0deg)',
    flexShrink: 0,
  };

  const id = `grouped-select-${label?.toLowerCase().replace(/\s+/g, '-')}`;
  const errorId = `${id}-error`;
  const helperId = `${id}-helper`;

  let optionIndex = -1;

  return (
    <div
      ref={containerRef}
      className={className}
      style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)', width: fullWidth ? '100%' : undefined, position: 'relative' }}
    >
      {label && (
        <label
          htmlFor={id}
          style={{
            fontSize: 'var(--text-label)',
            fontWeight: 'var(--weight-medium)',
            color: disabled ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-inline-xs)',
          }}
        >
          {label}
          {required && <span style={{ color: 'var(--color-text-danger)' }} aria-hidden="true">*</span>}
        </label>
      )}
      <button
        ref={triggerRef}
        id={id}
        type="button"
        disabled={disabled || readOnly}
        onClick={() => !disabled && !readOnly && setOpen((v) => !v)}
        onKeyDown={handleKeyDown}
        style={triggerStyle}
        onFocus={(e) => {
          e.currentTarget.style.boxShadow = '0 0 0 var(--focus-ring-width) var(--focus-ring-color)';
        }}
        onBlur={(e) => {
          e.currentTarget.style.boxShadow = 'none';
        }}
        role="combobox"
        aria-expanded={open}
        aria-haspopup="listbox"
        aria-invalid={hasError}
        aria-describedby={error ? errorId : helperText ? helperId : undefined}
        aria-required={required}
        aria-label={label}
      >
        <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {selectedOption ? selectedOption.label : placeholder}
        </span>
        <svg style={chevronStyle} viewBox="0 0 16 16" fill="none" aria-hidden="true">
          <path d="M4 6l4 4 4-4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      </button>
      {open && (
        <div style={dropdownStyle} role="listbox" aria-label={label}>
          {groups.map((group) => (
            <div key={group.label}>
              <div style={groupLabelStyle}>{group.label}</div>
              {group.options.map((opt) => {
                optionIndex++;
                const idx = optionIndex;
                const isSelected = opt.value === value;
                const isFocused = idx === focusedIndex;
                return (
                  <div
                    key={opt.value}
                    role="option"
                    aria-selected={isSelected}
                    onClick={() => !opt.disabled && handleSelect(opt.value)}
                    onMouseEnter={() => setFocusedIndex(idx)}
                    style={optionStyle(isSelected, isFocused, opt.disabled)}
                  >
                    {opt.label}
                  </div>
                );
              })}
            </div>
          ))}
        </div>
      )}
      {error && <span id={errorId} style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger)' }} role="alert">{error}</span>}
      {warning && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-warning)' }}>{warning}</span>}
      {helperText && !error && <span id={helperId} style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{helperText}</span>}
    </div>
  );
};

GroupedSelect.displayName = 'GroupedSelect';

export default GroupedSelect;
