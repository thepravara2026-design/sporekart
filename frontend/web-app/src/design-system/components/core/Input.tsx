import React, { forwardRef, InputHTMLAttributes, useState, useCallback, useRef } from 'react';

export interface InputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'size' | 'type' | 'prefix'> {
  type?: 'text' | 'email' | 'tel' | 'url' | 'number' | 'search' | 'password';
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  helperText?: string;
  error?: string;
  success?: boolean;
  warning?: string;
  required?: boolean;
  disabled?: boolean;
  readOnly?: boolean;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
  characterLimit?: number;
  fullWidth?: boolean;
  loading?: boolean;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  (
    {
      type = 'text',
      size: inputSize = 'md',
      label,
      helperText,
      error,
      success = false,
      warning,
      required = false,
      disabled = false,
      readOnly = false,
      prefix,
      suffix,
      characterLimit,
      fullWidth = false,
      loading = false,
      className = '',
      id: externalId,
      value,
      onChange,
      ...props
    },
    ref
  ) => {
    const generatedId = useRef(`sk-input-${Math.random().toString(36).substr(2, 9)}`);
    const inputId = externalId || generatedId.current;
    const helperId = `${inputId}-helper`;
    const errorId = `${inputId}-error`;

    const [inputValue, setInputValue] = useState<string>(
      typeof value === 'string' ? value : ''
    );

    const isControlled = value !== undefined;

    const currentLength = isControlled ? String(value).length : inputValue.length;

    const handleChange = useCallback(
      (e: React.ChangeEvent<HTMLInputElement>) => {
        if (disabled || readOnly || loading) return;
        if (!isControlled) {
          setInputValue(e.target.value);
        }
        onChange?.(e);
      },
      [disabled, readOnly, loading, isControlled, onChange]
    );

    const describedBy = [
      helperText ? helperId : '',
      error ? errorId : '',
    ]
      .filter(Boolean)
      .join(' ') || undefined;

    const inputHeight = {
      sm: 'var(--input-height-sm)',
      md: 'var(--input-height-md)',
      lg: 'var(--input-height-lg)',
    };

    const fontSize = {
      sm: 'var(--text-body-sm)',
      md: 'var(--text-body)',
      lg: 'var(--text-body-lg)',
    };

    const paddingX = {
      sm: 'var(--space-inline-sm)',
      md: 'var(--space-inline-md)',
      lg: 'var(--space-inline-lg)',
    };

    const wrapperStyles = `
      display: inline-flex;
      flex-direction: column;
      gap: var(--space-stack-xs);
      ${fullWidth ? 'width: 100%;' : ''}
    `;

    const labelStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-label);
      font-weight: var(--weight-medium);
      line-height: var(--leading-normal);
      color: var(--color-text-primary);
      letter-spacing: var(--tracking-normal);
    `;

    const requiredIndicator = `
      color: var(--color-text-error);
      margin-left: 2px;
    `;

    const optionalLabel = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      font-weight: var(--weight-normal);
      color: var(--color-text-secondary);
      margin-left: var(--space-inline-xs);
    `;

    let borderColor = 'var(--color-border-default)';
    let outlineColor = 'var(--color-focus-ring)';
    if (error) {
      borderColor = 'var(--color-border-error)';
      outlineColor = 'var(--color-border-error)';
    } else if (success) {
      borderColor = 'var(--color-border-success)';
      outlineColor = 'var(--color-border-success)';
    } else if (warning) {
      borderColor = 'var(--color-border-warning)';
      outlineColor = 'var(--color-border-warning)';
    }

    const inputContainerStyles = `
      display: flex;
      align-items: center;
      gap: var(--space-inline-sm);
      height: ${inputHeight[inputSize]};
      padding: 0 ${paddingX[inputSize]};
      background: ${disabled
        ? 'var(--color-bg-surface-default)'
        : 'var(--color-bg-surface-default)'
      };
      border: var(--border-width-thin) solid ${borderColor};
      border-radius: var(--radius-input);
      transition: border-color var(--duration-fast) var(--easing-standard),
                  box-shadow var(--duration-fast) var(--easing-standard);
      ${disabled ? 'opacity: var(--opacity-disabled); cursor: not-allowed;' : ''}
      ${readOnly ? 'cursor: default;' : ''}
      ${fullWidth ? 'width: 100%;' : ''}
    `;

    const inputStyles = `
      flex: 1;
      min-width: 0;
      height: 100%;
      border: none;
      outline: none;
      background: transparent;
      font-family: var(--font-family-sans);
      font-size: ${fontSize[inputSize]};
      font-weight: var(--weight-normal);
      line-height: var(--leading-normal);
      color: var(--color-text-primary);
      letter-spacing: var(--tracking-normal);
      padding: 0;
      margin: 0;
      ${disabled ? 'cursor: not-allowed;' : ''}
    `;

    const prefixSuffixStyles = `
      display: flex;
      align-items: center;
      flex-shrink: 0;
      color: var(--color-text-secondary);
      font-size: ${fontSize[inputSize]};
    `;

    const helperStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      line-height: var(--leading-normal);
      color: var(--color-text-secondary);
    `;

    const errorStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      line-height: var(--leading-normal);
      color: var(--color-text-error);
    `;

    const successStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      line-height: var(--leading-normal);
      color: var(--color-text-success);
    `;

    const warningStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      line-height: var(--leading-normal);
      color: var(--color-text-warning);
    `;

    const charCounterStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      line-height: var(--leading-normal);
      color: var(--color-text-secondary);
      margin-left: auto;
      white-space: nowrap;
    `;

    const spinnerSize = {
      sm: '14px',
      md: '16px',
      lg: '18px',
    };

    const focusStyles = `
      outline: none;
      box-shadow: 0 0 0 3px ${outlineColor};
      outline-offset: var(--focus-ring-offset);
    `;

    return (
      <div
        className={`sk-input-wrapper${fullWidth ? ' sk-input-wrapper--full-width' : ''}`}
        style={wrapperStyles as React.CSSProperties}
      >
        {(label || required) && (
          <label htmlFor={inputId} style={labelStyles as React.CSSProperties}>
            {label}
            {required && (
              <span style={requiredIndicator as React.CSSProperties} aria-hidden="true">
                *
              </span>
            )}
            {!required && label && (
              <span style={optionalLabel as React.CSSProperties}>
                (optional)
              </span>
            )}
          </label>
        )}

        <div
          className={`sk-input-container${error ? ' sk-input-container--error' : ''}${success ? ' sk-input-container--success' : ''}${warning ? ' sk-input-container--warning' : ''}${disabled ? ' sk-input-container--disabled' : ''}`}
          style={inputContainerStyles as React.CSSProperties}
          onFocus={(e) => {
            const container = e.currentTarget;
            container.style.cssText += focusStyles;
          }}
          onBlur={(e) => {
            const container = e.currentTarget;
            container.style.borderColor = borderColor;
            container.style.boxShadow = 'none';
          }}
        >
          {prefix && (
            <span style={prefixSuffixStyles as React.CSSProperties}>
              {prefix}
            </span>
          )}

          <input
            ref={ref}
            id={inputId}
            type={type}
            className={`sk-input${className ? ` ${className}` : ''}`}
            style={inputStyles as React.CSSProperties}
            disabled={disabled}
            readOnly={readOnly}
            required={required}
            aria-required={required || undefined}
            aria-invalid={!!error || undefined}
            aria-describedby={describedBy}
            aria-disabled={disabled || undefined}
            value={value}
            onChange={handleChange}
            {...props}
          />

          {loading && (
            <span
              className="sk-input__spinner"
              style={{
                width: spinnerSize[inputSize],
                height: spinnerSize[inputSize],
                border: '2px solid var(--color-border-default)',
                borderRightColor: 'transparent',
                borderRadius: '50%',
                animation: 'sk-spin 0.8s linear infinite',
                flexShrink: 0,
              }}
              aria-hidden="true"
            />
          )}

          {!loading && suffix && (
            <span style={prefixSuffixStyles as React.CSSProperties}>
              {suffix}
            </span>
          )}

          {characterLimit !== undefined && (
            <span
              className="sk-input__char-counter"
              style={charCounterStyles as React.CSSProperties}
              aria-live="polite"
            >
              {currentLength}/{characterLimit}
            </span>
          )}
        </div>

        <div className="sk-input__messages">
          {error && (
            <span
              id={errorId}
              className="sk-input__error"
              style={errorStyles as React.CSSProperties}
              role="alert"
            >
              {error}
            </span>
          )}
          {!error && success && (
            <span
              className="sk-input__success"
              style={successStyles as React.CSSProperties}
              role="status"
            >
              Looks good
            </span>
          )}
          {!error && !success && warning && (
            <span
              className="sk-input__warning"
              style={warningStyles as React.CSSProperties}
              role="alert"
            >
              {warning}
            </span>
          )}
          {!error && !success && !warning && helperText && (
            <span
              id={helperId}
              className="sk-input__helper"
              style={helperStyles as React.CSSProperties}
            >
              {helperText}
            </span>
          )}
        </div>

        <style>{`@keyframes sk-spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }`}</style>
      </div>
    );
  }
);

Input.displayName = 'Input';

export default Input;
