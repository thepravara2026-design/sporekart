import React, { useId, useCallback } from 'react';

export interface PhoneInputProps {
  label?: string;
  value?: string;
  onChange?: (value: string) => void;
  placeholder?: string;
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

const inputStyle: React.CSSProperties = {
  width: '100%',
  padding: '10px 12px',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  lineHeight: 'var(--leading-normal)',
  color: 'var(--color-text-primary)',
  background: 'var(--color-bg-background)',
  border: '1px solid var(--color-border-default)',
  borderRadius: 'var(--radius-input)',
  outline: 'none',
  transition: 'border-color var(--duration-fast) var(--easing-standard), box-shadow var(--duration-fast) var(--easing-standard)',
  boxSizing: 'border-box',
};

export const PhoneInput: React.FC<PhoneInputProps> = ({
  label,
  value,
  onChange,
  placeholder = '+1 (555) 000-0000',
  disabled = false,
  error,
  helperText,
  required = false,
  className = '',
  style,
}) => {
  const id = useId();
  const hasError = !!error;

  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    onChange?.(e.target.value);
  }, [onChange]);

  return (
    <div className={className} style={{ ...wrapperStyle, ...style }}>
      {label && (
        <label htmlFor={id} style={labelStyle}>
          {label}{required && <span style={{ color: 'var(--color-danger)', marginLeft: 2 }}>*</span>}
        </label>
      )}
      <input
        id={id}
        type="tel"
        value={value}
        onChange={handleChange}
        placeholder={placeholder}
        disabled={disabled}
        required={required}
        aria-invalid={hasError}
        style={{
          ...inputStyle,
          borderColor: hasError ? 'var(--color-danger)' : undefined,
          boxShadow: hasError ? '0 0 0 1px var(--color-danger)' : undefined,
          opacity: disabled ? 'var(--opacity-disabled)' : undefined,
          cursor: disabled ? 'not-allowed' : undefined,
        }}
        onFocus={(e) => {
          if (!hasError && !disabled) {
            e.target.style.borderColor = 'var(--color-primary)';
            e.target.style.boxShadow = '0 0 0 1px var(--color-primary)';
          }
        }}
        onBlur={(e) => {
          if (!hasError) {
            e.target.style.borderColor = 'var(--color-border-default)';
            e.target.style.boxShadow = 'none';
          }
        }}
      />
      {hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-danger)' }} role="alert">{error}</span>}
      {helperText && !hasError && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{helperText}</span>}
    </div>
  );
};

PhoneInput.displayName = 'PhoneInput';
export default PhoneInput;
