import React, { forwardRef, InputHTMLAttributes, useState, useCallback, useRef, useEffect, useMemo } from 'react';

export interface OtpInputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'size' | 'value' | 'onChange'> {
  length?: number;
  value?: string;
  onChange?: (value: string) => void;
  onComplete?: (value: string) => void;
  disabled?: boolean;
  error?: boolean;
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  helperText?: string;
}

export const OtpInput = forwardRef<HTMLInputElement, OtpInputProps>(
  (
    {
      length = 6,
      value,
      onChange,
      onComplete,
      disabled = false,
      error = false,
      size: otpSize = 'md',
      label,
      helperText,
      className = '',
      id: externalId,
      ...props
    },
    ref
  ) => {
    const generatedId = useRef(`sk-otp-${Math.random().toString(36).substr(2, 9)}`);
    const containerId = externalId || generatedId.current;
    const helperId = `${containerId}-helper`;

    const isControlled = value !== undefined;
    const [internalValues, setInternalValues] = useState<string[]>(() =>
      Array.from({ length }, () => '')
    );
    const inputRefs = useRef<(HTMLInputElement | null)[]>([]);

    const currentValues = useMemo(() => {
      if (isControlled) {
        const vals: string[] = Array.from({ length }, () => '');
        const str = String(value || '');
        for (let i = 0; i < Math.min(str.length, length); i++) {
          vals[i] = str[i] || '';
        }
        return vals;
      }
      return internalValues;
    }, [isControlled, value, internalValues, length]);

    useEffect(() => {
      if (inputRefs.current[0]) {
        inputRefs.current[0].focus();
      }
    }, []);

    const emitChange = useCallback(
      (vals: string[]) => {
        const joined = vals.join('');
        if (!isControlled) {
          setInternalValues(vals);
        }
        onChange?.(joined);
        if (joined.length === length) {
          onComplete?.(joined);
        }
      },
      [isControlled, length, onChange, onComplete]
    );

    const handleChange = useCallback(
      (e: React.ChangeEvent<HTMLInputElement>, idx: number) => {
        if (disabled) return;
        const target = e.target;
        let char = target.value;

        char = char.replace(/[^0-9a-zA-Z]/g, '');

        if (char.length > 1) {
          return;
        }

        const newValues = [...currentValues];
        newValues[idx] = char;
        emitChange(newValues);

        if (char && idx < length - 1) {
          inputRefs.current[idx + 1]?.focus();
        }
      },
      [disabled, currentValues, length, emitChange]
    );

    const handleKeyDown = useCallback(
      (e: React.KeyboardEvent<HTMLInputElement>, index: number) => {
        if (disabled) return;

        if (e.key === 'Backspace') {
          e.preventDefault();
          const newValues = [...currentValues];
          if (newValues[index] === '' && index > 0) {
            newValues[index - 1] = '';
            emitChange(newValues);
            inputRefs.current[index - 1]?.focus();
          } else {
            newValues[index] = '';
            emitChange(newValues);
          }
        }

        if (e.key === 'ArrowLeft' && index > 0) {
          e.preventDefault();
          inputRefs.current[index - 1]?.focus();
        }

        if (e.key === 'ArrowRight' && index < length - 1) {
          e.preventDefault();
          inputRefs.current[index + 1]?.focus();
        }

        if (e.key === 'Home') {
          e.preventDefault();
          inputRefs.current[0]?.focus();
        }

        if (e.key === 'End') {
          e.preventDefault();
          inputRefs.current[length - 1]?.focus();
        }

        props.onKeyDown?.(e);
      },
      [disabled, currentValues, length, emitChange, props.onKeyDown]
    );

    const handlePaste = useCallback(
      (e: React.ClipboardEvent<HTMLInputElement>) => {
        if (disabled) return;
        e.preventDefault();
        const pastedData = e.clipboardData
          .getData('text/plain')
          .replace(/[^0-9a-zA-Z]/g, '')
          .slice(0, length);

        const newValues: string[] = Array.from({ length }, () => '');
        for (let i = 0; i < pastedData.length; i++) {
          newValues[i] = pastedData[i];
        }

        emitChange(newValues);

        const nextFocusIndex = Math.min(pastedData.length, length - 1);
        inputRefs.current[nextFocusIndex]?.focus();

        props.onPaste?.(e);
      },
      [disabled, length, emitChange, props.onPaste]
    );

    const handleFocus = useCallback(
      (e: React.FocusEvent<HTMLInputElement>, _index: number) => {
        e.currentTarget.select();
        props.onFocus?.(e as unknown as React.FocusEvent<HTMLInputElement>);
      },
      [props.onFocus]
    );

    const boxSize = {
      sm: '36px',
      md: '44px',
      lg: '52px',
    };

    const fontSize = {
      sm: 'var(--text-h4)',
      md: 'var(--text-otp)',
      lg: 'var(--text-otp)',
    };

    const wrapperStyles = `
      display: inline-flex;
      flex-direction: column;
      gap: var(--space-stack-xs);
    `;

    const labelStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-label);
      font-weight: var(--weight-medium);
      line-height: var(--leading-normal);
      color: var(--color-text-primary);
      letter-spacing: var(--tracking-normal);
    `;

    const borderColor = error
      ? 'var(--color-border-error)'
      : 'var(--color-border-default)';

    const inputBoxStyles = `
      display: flex;
      align-items: center;
      justify-content: center;
      width: ${boxSize[otpSize]};
      height: ${boxSize[otpSize]};
      border: var(--border-width-thin) solid ${borderColor};
      border-radius: var(--radius-input);
      background: ${disabled
        ? 'var(--color-bg-surface-default)'
        : 'var(--color-bg-surface-default)'
      };
      font-family: var(--font-family-sans);
      font-size: ${fontSize[otpSize]};
      font-weight: var(--weight-semibold);
      line-height: 1;
      text-align: center;
      color: var(--color-text-primary);
      letter-spacing: var(--tracking-otp);
      outline: none;
      transition: border-color var(--duration-fast) var(--easing-standard),
                  box-shadow var(--duration-fast) var(--easing-standard);
      caret-color: var(--color-text-primary);
      ${disabled ? 'opacity: var(--opacity-disabled); cursor: not-allowed;' : ''}
    `;

    const helperStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      line-height: var(--leading-normal);
      color: ${error ? 'var(--color-text-error)' : 'var(--color-text-secondary)'};
    `;

    const setInputRef = useCallback(
      (el: HTMLInputElement | null, index: number) => {
        inputRefs.current[index] = el;
        if (index === 0 && ref) {
          if (typeof ref === 'function') {
            ref(el);
          } else {
            (ref as React.MutableRefObject<HTMLInputElement | null>).current = el;
          }
        }
      },
      [ref]
    );

    return (
      <div
        className="sk-otp-wrapper"
        style={wrapperStyles as React.CSSProperties}
      >
        {label && (
          <label id={`${containerId}-label`} style={labelStyles as React.CSSProperties}>
            {label}
          </label>
        )}

        <div
          className={`sk-otp-inputs${error ? ' sk-otp-inputs--error' : ''}`}
          style={{
            display: 'flex',
            gap: 'var(--space-inline-sm)',
            alignItems: 'center',
          }}
          role="group"
          aria-labelledby={label ? `${containerId}-label` : undefined}
          aria-label={!label ? `OTP input, ${length} digits` : undefined}
        >
          {currentValues.map((digit, index) => (
            <input
              key={index}
              ref={(el) => setInputRef(el, index)}
              id={`${containerId}-${index}`}
              type="text"
              inputMode="numeric"
              autoComplete="one-time-code"
              className={`sk-otp-input${className ? ` ${className}` : ''}`}
              style={inputBoxStyles as React.CSSProperties}
              value={digit}
              onChange={(e) => handleChange(e, index)}
              onKeyDown={(e) => handleKeyDown(e, index)}
              onFocus={(e) => handleFocus(e, index)}
              onPaste={handlePaste}
              disabled={disabled}
              aria-label={`Digit ${index + 1}`}
              maxLength={1}
              {...props}
            />
          ))}
        </div>

        {helperText && (
          <span
            id={helperId}
            className="sk-otp__helper"
            style={helperStyles as React.CSSProperties}
          >
            {helperText}
          </span>
        )}
      </div>
    );
  }
);

OtpInput.displayName = 'OtpInput';

export default OtpInput;
