import React, { useId, useCallback } from 'react';

export interface NumberInputProps {
  label?: string;
  value?: number;
  onChange?: (value: number) => void;
  placeholder?: string;
  min?: number;
  max?: number;
  step?: number;
  disabled?: boolean;
  error?: string;
  helperText?: string;
  required?: boolean;
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

const inputGroupStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-input)',
  background: 'var(--color-bg-background)',
  overflow: 'hidden',
  transition: 'border-color var(--duration-fast) var(--easing-standard)',
};

const inputStyle: React.CSSProperties = {
  flex: 1,
  padding: '10px 12px',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-primary)',
  background: 'transparent',
  border: 'none',
  outline: 'none',
  textAlign: 'center',
  width: 0,
  MozAppearance: 'textfield',
};

const stepperStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  borderLeft: '1px solid var(--color-border-default)',
};

const stepBtnStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: 28,
  height: '50%',
  border: 'none',
  background: 'transparent',
  color: 'var(--color-text-secondary)',
  cursor: 'pointer',
  fontSize: 12,
  lineHeight: 1,
  padding: 0,
  transition: 'background var(--duration-fast) var(--easing-standard)',
};

export const NumberInput: React.FC<NumberInputProps> = ({
  label,
  value,
  onChange,
  placeholder,
  min,
  max,
  step = 1,
  disabled = false,
  error,
  helperText,
  required = false,
  className = '',
  style,
}) => {
  const id = useId();
  const hasError = !!error;
  const displayValue = value ?? '';

  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const raw = e.target.value;
    if (raw === '') { onChange?.(0); return; }
    const num = parseFloat(raw);
    if (!isNaN(num)) onChange?.(num);
  }, [onChange]);

  const increment = useCallback(() => {
    const next = (value ?? 0) + step;
    if (max !== undefined && next > max) return;
    onChange?.(next);
  }, [value, step, max, onChange]);

  const decrement = useCallback(() => {
    const next = (value ?? 0) - step;
    if (min !== undefined && next < min) return;
    onChange?.(next);
  }, [value, step, min, onChange]);

  const groupBorderColor = hasError ? 'var(--color-danger)' : undefined;

  return (
    <div className={className} style={{ ...wrapperStyle, ...style }}>
      {label && (
        <label htmlFor={id} style={labelStyle}>
          {label}{required && <span style={{ color: 'var(--color-danger)', marginLeft: 2 }}>*</span>}
        </label>
      )}
      <div style={{ ...inputGroupStyle, borderColor: groupBorderColor }}>
        <input
          id={id}
          type="number"
          value={displayValue}
          onChange={handleChange}
          placeholder={placeholder}
          min={min}
          max={max}
          step={step}
          disabled={disabled}
          required={required}
          aria-invalid={hasError}
          style={{
            ...inputStyle,
            opacity: disabled ? 'var(--opacity-disabled)' : undefined,
            cursor: disabled ? 'not-allowed' : undefined,
          }}
          onFocus={(e) => {
            if (!hasError && !disabled) e.currentTarget.parentElement!.style.borderColor = 'var(--color-primary)';
          }}
          onBlur={(e) => {
            if (!hasError) e.currentTarget.parentElement!.style.borderColor = 'var(--color-border-default)';
          }}
        />
        {!disabled && (
          <div style={stepperStyle}>
            <button type="button" style={stepBtnStyle} onClick={increment} aria-label="Increment"
              onMouseEnter={(e) => (e.currentTarget.style.background = 'var(--color-bg-surface-raised)')}
              onMouseLeave={(e) => (e.currentTarget.style.background = 'transparent')}
            >&#9650;</button>
            <button type="button" style={{ ...stepBtnStyle, borderTop: '1px solid var(--color-border-default)' }} onClick={decrement} aria-label="Decrement"
              onMouseEnter={(e) => (e.currentTarget.style.background = 'var(--color-bg-surface-raised)')}
              onMouseLeave={(e) => (e.currentTarget.style.background = 'transparent')}
            >&#9660;</button>
          </div>
        )}
      </div>
      {hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-danger)' }} role="alert">{error}</span>}
      {helperText && !hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{helperText}</span>}
    </div>
  );
};

NumberInput.displayName = 'NumberInput';
export default NumberInput;
