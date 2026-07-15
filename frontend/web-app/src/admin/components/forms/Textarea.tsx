import React, { useId } from 'react';

export interface TextareaProps {
  label?: string;
  value?: string;
  onChange?: (value: string) => void;
  placeholder?: string;
  rows?: number;
  maxLength?: number;
  disabled?: boolean;
  readOnly?: boolean;
  error?: string;
  helperText?: string;
  required?: boolean;
  resize?: 'none' | 'vertical' | 'horizontal' | 'both';
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

const textareaBase: React.CSSProperties = {
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

const helperStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

const errorStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-danger)',
};

export const Textarea: React.FC<TextareaProps> = ({
  label,
  value,
  onChange,
  placeholder,
  rows = 4,
  maxLength,
  disabled = false,
  readOnly = false,
  error,
  helperText,
  required = false,
  resize = 'vertical',
  className = '',
  style,
}) => {
  const id = useId();
  const hasError = !!error;
  const charCount = value?.length ?? 0;

  const textareaStyle: React.CSSProperties = {
    ...textareaBase,
    resize,
    borderColor: hasError ? 'var(--color-danger)' : undefined,
    ...(disabled ? { opacity: 'var(--opacity-disabled)', cursor: 'not-allowed', background: 'var(--color-bg-surface-raised)' } : {}),
    ...(hasError ? { boxShadow: '0 0 0 1px var(--color-danger)' } : {}),
  };

  return (
    <div className={className} style={{ ...wrapperStyle, ...style }}>
      {label && (
        <label htmlFor={id} style={labelStyle}>
          {label}{required && <span style={{ color: 'var(--color-danger)', marginLeft: 2 }}>*</span>}
        </label>
      )}
      <textarea
        id={id}
        value={value}
        onChange={(e) => onChange?.(e.target.value)}
        placeholder={placeholder}
        rows={rows}
        maxLength={maxLength}
        disabled={disabled}
        readOnly={readOnly}
        required={required}
        aria-invalid={hasError}
        aria-describedby={hasError ? `${id}-error` : helperText ? `${id}-helper` : undefined}
        style={textareaStyle}
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
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <div>
          {hasError && <span id={`${id}-error`} style={errorStyle} role="alert">{error}</span>}
          {helperText && !hasError && <span id={`${id}-helper`} style={helperStyle}>{helperText}</span>}
        </div>
        {maxLength && (
          <span style={{ fontSize: 'var(--text-caption)', color: charCount >= maxLength ? 'var(--color-danger)' : 'var(--color-text-secondary)' }}>
            {charCount}/{maxLength}
          </span>
        )}
      </div>
    </div>
  );
};

Textarea.displayName = 'Textarea';
export default Textarea;
