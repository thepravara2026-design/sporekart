import React, { forwardRef, InputHTMLAttributes, useState, useCallback, useRef } from 'react';

export interface PasswordProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'size' | 'type'> {
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  placeholder?: string;
  helperText?: string;
  error?: string;
  success?: boolean;
  warning?: string;
  required?: boolean;
  disabled?: boolean;
  showStrength?: boolean;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  fullWidth?: boolean;
}

function getPasswordStrength(password: string): { score: number; label: string; color: string; percent: number } {
  if (!password) {
    return { score: 0, label: '', color: 'transparent', percent: 0 };
  }
  const length = password.length;
  let score = 0;
  if (/[a-z]/.test(password)) score++;
  if (/[A-Z]/.test(password)) score++;
  if (/[0-9]/.test(password)) score++;
  if (/[^a-zA-Z0-9]/.test(password)) score++;
  if (length >= 8) score++;

  if (length <= 3) {
    return { score: 1, label: 'Weak', color: 'var(--color-text-error)', percent: 25 };
  }
  if (length <= 6) {
    return { score: 2, label: 'Fair', color: 'var(--color-text-warning)', percent: 50 };
  }
  if (length <= 11) {
    return { score: 3, label: 'Good', color: 'var(--color-text-warning)', percent: 75 };
  }
  return { score: 4, label: 'Strong', color: 'var(--color-text-success)', percent: 100 };
}

export const Password = forwardRef<HTMLInputElement, PasswordProps>(
  (
    {
      size: passwordSize = 'md',
      label,
      placeholder = 'Enter password',
      helperText,
      error,
      success = false,
      warning,
      required = false,
      disabled = false,
      showStrength = false,
      value,
      onChange,
      fullWidth = false,
      className = '',
      id: externalId,
      ...props
    },
    ref
  ) => {
    const generatedId = useRef(`sk-password-${Math.random().toString(36).substr(2, 9)}`);
    const inputId = externalId || generatedId.current;
    const helperId = `${inputId}-helper`;
    const errorId = `${inputId}-error`;

    const [showPassword, setShowPassword] = useState(false);
    const isControlled = value !== undefined;
    const [internalValue, setInternalValue] = useState<string>('');

    const currentValue = isControlled ? (value || '') : internalValue;
    const strength = getPasswordStrength(currentValue);

    const handleChange = useCallback(
      (e: React.ChangeEvent<HTMLInputElement>) => {
        if (disabled) return;
        if (!isControlled) {
          setInternalValue(e.target.value);
        }
        onChange?.(e);
      },
      [disabled, isControlled, onChange]
    );

    const toggleVisibility = useCallback(() => {
      if (disabled) return;
      setShowPassword((prev) => !prev);
    }, [disabled]);

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

    const iconSize = {
      sm: '16px',
      md: '18px',
      lg: '20px',
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
    if (error) {
      borderColor = 'var(--color-border-error)';
    } else if (success) {
      borderColor = 'var(--color-border-success)';
    } else if (warning) {
      borderColor = 'var(--color-border-warning)';
    }

    const containerStyles = `
      display: flex;
      align-items: center;
      gap: var(--space-inline-sm);
      height: ${inputHeight[passwordSize]};
      padding: 0 ${paddingX[passwordSize]};
      background: ${disabled
        ? 'var(--color-bg-surface-default)'
        : 'var(--color-bg-surface-default)'
      };
      border: var(--border-width-thin) solid ${borderColor};
      border-radius: var(--radius-input);
      transition: border-color var(--duration-fast) var(--easing-standard),
                  box-shadow var(--duration-fast) var(--easing-standard);
      ${disabled ? 'opacity: var(--opacity-disabled); cursor: not-allowed;' : ''}
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
      font-size: ${fontSize[passwordSize]};
      font-weight: var(--weight-normal);
      line-height: var(--leading-normal);
      color: var(--color-text-primary);
      letter-spacing: var(--tracking-normal);
      padding: 0;
      margin: 0;
      ${disabled ? 'cursor: not-allowed;' : ''}
    `;

    const toggleButtonStyles = `
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: ${iconSize[passwordSize]};
      height: ${iconSize[passwordSize]};
      border: none;
      background: none;
      cursor: pointer;
      padding: 0;
      color: var(--color-text-secondary);
      border-radius: var(--radius-xs);
      transition: color var(--duration-fast) var(--easing-standard);
      flex-shrink: 0;
      ${disabled ? 'opacity: var(--opacity-disabled); cursor: not-allowed;' : ''}
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

    const strengthContainerStyles = `
      display: flex;
      align-items: center;
      gap: var(--space-inline-sm);
      margin-top: var(--space-stack-xs);
    `;

    const strengthBarTrackStyles = `
      flex: 1;
      height: 4px;
      background: var(--color-bg-surface-default);
      border-radius: var(--radius-xs);
      overflow: hidden;
    `;

    const strengthLabelStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      font-weight: var(--weight-medium);
      line-height: var(--leading-normal);
      white-space: nowrap;
    `;

    const focusRingCss = `
      .sk-password-container:focus-within {
        outline: none;
        box-shadow: 0 0 0 3px var(--color-focus-ring);
        outline-offset: var(--focus-ring-offset);
      }
      .sk-password-container--error:focus-within {
        box-shadow: 0 0 0 3px var(--color-border-error);
      }
      .sk-password-container--success:focus-within {
        box-shadow: 0 0 0 3px var(--color-border-success);
      }
      .sk-password-container--warning:focus-within {
        box-shadow: 0 0 0 3px var(--color-border-warning);
      }
    `;

    return (
      <div
        className={`sk-password-wrapper${fullWidth ? ' sk-password-wrapper--full-width' : ''}`}
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
          className={`sk-password-container${error ? ' sk-password-container--error' : ''}${success ? ' sk-password-container--success' : ''}${warning ? ' sk-password-container--warning' : ''}${disabled ? ' sk-password-container--disabled' : ''}`}
          style={containerStyles as React.CSSProperties}
        >
          <input
            ref={ref}
            id={inputId}
            type={showPassword ? 'text' : 'password'}
            className={`sk-password${className ? ` ${className}` : ''}`}
            style={inputStyles as React.CSSProperties}
            placeholder={placeholder}
            disabled={disabled}
            required={required}
            aria-required={required || undefined}
            aria-invalid={!!error || undefined}
            aria-describedby={describedBy}
            aria-disabled={disabled || undefined}
            value={value}
            onChange={handleChange}
            autoComplete={props.autoComplete || (required ? 'new-password' : 'current-password')}
            {...props}
          />

          <button
            type="button"
            className="sk-password__toggle"
            style={toggleButtonStyles as React.CSSProperties}
            onClick={toggleVisibility}
            disabled={disabled}
            aria-label={showPassword ? 'Hide password' : 'Show password'}
            aria-pressed={showPassword}
            tabIndex={0}
          >
            {showPassword ? (
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth={2}
                strokeLinecap="round"
                strokeLinejoin="round"
                width={iconSize[passwordSize]}
                height={iconSize[passwordSize]}
              >
                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94" />
                <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19" />
                <line x1="1" y1="1" x2="23" y2="23" />
                <path d="M14.12 14.12a3 3 0 1 1-4.24-4.24" />
              </svg>
            ) : (
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth={2}
                strokeLinecap="round"
                strokeLinejoin="round"
                width={iconSize[passwordSize]}
                height={iconSize[passwordSize]}
              >
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                <circle cx="12" cy="12" r="3" />
              </svg>
            )}
          </button>
        </div>

        <div className="sk-password__messages">
          {error && (
            <span
              id={errorId}
              className="sk-password__error"
              style={errorStyles as React.CSSProperties}
              role="alert"
            >
              {error}
            </span>
          )}
          {!error && success && (
            <span
              className="sk-password__success"
              style={successStyles as React.CSSProperties}
              role="status"
            >
              Looks good
            </span>
          )}
          {!error && !success && warning && (
            <span
              className="sk-password__warning"
              style={warningStyles as React.CSSProperties}
              role="alert"
            >
              {warning}
            </span>
          )}
          {!error && !success && !warning && helperText && (
            <span
              id={helperId}
              className="sk-password__helper"
              style={helperStyles as React.CSSProperties}
            >
              {helperText}
            </span>
          )}
        </div>

        {showStrength && currentValue.length > 0 && (
          <div
            className="sk-password__strength"
            style={strengthContainerStyles as React.CSSProperties}
          >
            <div
              className="sk-password__strength-track"
              style={strengthBarTrackStyles as React.CSSProperties}
            >
              <div
                className="sk-password__strength-bar"
                style={{
                  width: `${strength.percent}%`,
                  height: '100%',
                  background: strength.color,
                  borderRadius: 'var(--radius-xs)',
                  transition: 'width var(--duration-fast) var(--easing-standard), background var(--duration-fast) var(--easing-standard)',
                }}
                role="progressbar"
                aria-valuenow={strength.score}
                aria-valuemin={0}
                aria-valuemax={4}
                aria-label={`Password strength: ${strength.label}`}
              />
            </div>
            <span
              className="sk-password__strength-label"
              style={{
                ...(strengthLabelStyles as React.CSSProperties),
                color: strength.color,
              }}
            >
              {strength.label}
            </span>
          </div>
        )}
        <style>{focusRingCss}</style>
      </div>
    );
  }
);

Password.displayName = 'Password';

export default Password;
