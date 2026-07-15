import React, { useId, useCallback } from 'react';

export interface CurrencyInputProps {
  label?: string;
  value?: number;
  onChange?: (value: number) => void;
  currency?: string;
  locale?: string;
  placeholder?: string;
  min?: number;
  max?: number;
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

const prefixStyle: React.CSSProperties = {
  padding: '10px 0 10px 12px',
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-secondary)',
  fontWeight: 'var(--weight-medium)',
  flexShrink: 0,
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
};

export const CurrencyInput: React.FC<CurrencyInputProps> = ({
  label,
  value,
  onChange,
  currency = 'USD',
  locale = 'en-US',
  placeholder,
  min,
  max,
  disabled = false,
  error,
  helperText,
  required = false,
  className = '',
  style,
}) => {
  const id = useId();
  const hasError = !!error;
  const symbol = new Intl.NumberFormat(locale, { style: 'currency', currency, minimumFractionDigits: 0, maximumFractionDigits: 0 }).format(0).replace(/[\d0,. ]/g, '').trim() || currency;
  const displayValue = value != null ? value.toString() : '';

  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const raw = e.target.value.replace(/[^0-9.]/g, '');
    if (raw === '') { onChange?.(0); return; }
    const num = parseFloat(raw);
    if (!isNaN(num)) {
      if (min !== undefined && num < min) return;
      if (max !== undefined && num > max) return;
      onChange?.(num);
    }
  }, [onChange, min, max]);

  const groupBorderColor = hasError ? 'var(--color-danger)' : undefined;

  return (
    <div className={className} style={{ ...wrapperStyle, ...style }}>
      {label && (
        <label htmlFor={id} style={labelStyle}>
          {label}{required && <span style={{ color: 'var(--color-danger)', marginLeft: 2 }}>*</span>}
        </label>
      )}
      <div style={{ ...inputGroupStyle, borderColor: groupBorderColor }}>
        <span style={prefixStyle}>{symbol}</span>
        <input
          id={id}
          type="text"
          inputMode="decimal"
          value={displayValue}
          onChange={handleChange}
          placeholder={placeholder || '0.00'}
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
      </div>
      {hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-danger)' }} role="alert">{error}</span>}
      {helperText && !hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{helperText}</span>}
    </div>
  );
};

CurrencyInput.displayName = 'CurrencyInput';
export default CurrencyInput;
