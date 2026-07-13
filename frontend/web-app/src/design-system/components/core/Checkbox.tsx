import React, { forwardRef, useRef, useEffect, InputHTMLAttributes } from 'react';

export interface CheckboxProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type' | 'size'> {
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  indeterminate?: boolean;
  error?: boolean;
  success?: boolean;
  disabled?: boolean;
  required?: boolean;
  helperText?: string;
}

const sizeMap = { sm: 16, md: 20, lg: 24 };
const tickStrokeWidth = { sm: 2, md: 2.5, lg: 3 };

const CheckMark = ({ size }: { size: 'sm' | 'md' | 'lg' }) => {
  const px = sizeMap[size];
  const sw = tickStrokeWidth[size];
  return (
    <svg width={px} height={px} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M6 12l4 4 8-8" stroke="currentColor" strokeWidth={sw} strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
};

const MinusIcon = ({ size }: { size: 'sm' | 'md' | 'lg' }) => {
  const px = sizeMap[size];
  const sw = tickStrokeWidth[size];
  return (
    <svg width={px} height={px} viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M6 12h12" stroke="currentColor" strokeWidth={sw} strokeLinecap="round" />
    </svg>
  );
};

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(
  (
    {
      size = 'md',
      label,
      indeterminate = false,
      error = false,
      success = false,
      disabled = false,
      required = false,
      helperText,
      className = '',
      checked,
      defaultChecked,
      onChange,
      id: externalId,
      ...props
    },
    ref
  ) => {
    const inputRef = useRef<HTMLInputElement | null>(null);
    const combinedRef = (el: HTMLInputElement | null) => {
      (inputRef as React.MutableRefObject<HTMLInputElement | null>).current = el;
      if (typeof ref === 'function') ref(el);
      else if (ref) (ref as React.MutableRefObject<HTMLInputElement | null>).current = el;
    };

    useEffect(() => {
      if (inputRef.current) {
        inputRef.current.indeterminate = indeterminate;
      }
    }, [indeterminate]);

    const px = sizeMap[size];
    const id = externalId || `sk-checkbox-${Math.random().toString(36).slice(2, 9)}`;

    const wrapperStyle = `
      display: inline-flex;
      flex-direction: column;
      gap: var(--space-stack-xs);
      opacity: ${disabled ? 'var(--opacity-disabled)' : '1'};
      cursor: ${disabled ? 'not-allowed' : 'pointer'};
    ` as React.CSSProperties;

    const rowStyle = `
      display: inline-flex;
      align-items: center;
      gap: var(--space-inline-sm);
    ` as React.CSSProperties;

    const boxStyle = `
      display: flex;
      align-items: center;
      justify-content: center;
      width: ${px}px;
      height: ${px}px;
      min-width: ${px}px;
      border: 2px solid var(${error ? '--color-danger' : success ? '--color-success' : '--color-border-strong'});
      border-radius: var(--radius-sm);
      background: var(--color-bg-surface-default);
      color: var(--color-text-on-primary);
      transition: all var(--duration-fast) var(--easing-standard);
      box-sizing: border-box;
      position: relative;
    ` as React.CSSProperties;

    const checkedBoxStyle = `
      background: var(${error ? '--color-danger' : success ? '--color-success' : '--color-primary'});
      border-color: var(${error ? '--color-danger' : success ? '--color-success' : '--color-primary'});
    ` as React.CSSProperties;

    const labelStyle = `
      font-family: var(--font-family-sans);
      font-size: var(--text-body);
      color: var(--color-text-primary);
      line-height: 1.25;
      user-select: none;
    ` as React.CSSProperties;

    const helperStyle = `
      font-family: var(--font-family-sans);
      font-size: var(--text-body-sm);
      color: var(${error ? '--color-danger' : '--color-text-secondary'});
      line-height: 1.25;
      margin: 0;
    ` as React.CSSProperties;

    const isChecked = checked !== undefined ? checked : !!defaultChecked;

    const handleKeyDown = (e: React.KeyboardEvent<HTMLLabelElement>) => {
      if (e.key === ' ' || e.key === 'Enter') {
        e.preventDefault();
        if (!disabled && inputRef.current) {
          inputRef.current.click();
        }
      }
    };

    return (
      <div style={wrapperStyle as React.CSSProperties} className={`sk-checkbox-wrapper ${className}`}>
        <label
          htmlFor={id}
          style={rowStyle as React.CSSProperties}
          onKeyDown={handleKeyDown}
          tabIndex={disabled ? -1 : 0}
          role="label"
        >
          <input
            ref={combinedRef}
            id={id}
            type="checkbox"
            disabled={disabled}
            required={required}
            checked={checked}
            defaultChecked={defaultChecked}
            onChange={onChange}
            aria-checked={indeterminate ? 'mixed' : checked !== undefined ? checked : undefined}
            aria-disabled={disabled}
            aria-required={required}
            style={{
              position: 'absolute',
              opacity: 0,
              width: 0,
              height: 0,
              margin: 0,
              padding: 0,
              pointerEvents: 'none',
            } as React.CSSProperties}
            {...props}
          />
          <span
            className="sk-checkbox__indicator"
            style={{
              ...(boxStyle as React.CSSProperties),
              ...((isChecked || indeterminate) ? (checkedBoxStyle as React.CSSProperties) : {}),
            }}
            onFocus={(e) => {
              e.currentTarget.style.outline = 'var(--focus-ring-width) solid var(--focus-ring-color)';
              e.currentTarget.style.outlineOffset = 'var(--focus-ring-offset)';
            }}
            onBlur={(e) => {
              e.currentTarget.style.outline = '';
              e.currentTarget.style.outlineOffset = '';
            }}
          >
            {indeterminate ? <MinusIcon size={size} /> : isChecked ? <CheckMark size={size} /> : null}
          </span>
          {label && (
            <span style={labelStyle as React.CSSProperties}>
              {label}
              {required && <span aria-hidden="true" style={{ color: 'var(--color-danger)', marginLeft: 2 }}>*</span>}
            </span>
          )}
        </label>
        {helperText && <p style={helperStyle as React.CSSProperties}>{helperText}</p>}
      </div>
    );
  }
);

Checkbox.displayName = 'Checkbox';

export interface CheckboxGroupProps {
  label?: string;
  children: React.ReactNode;
  orientation?: 'horizontal' | 'vertical';
  error?: string;
  helperText?: string;
  required?: boolean;
}

export const CheckboxGroup: React.FC<CheckboxGroupProps> = ({
  label,
  children,
  orientation = 'vertical',
  error,
  helperText,
  required,
}) => {
  const fieldsetStyle = `
    border: none;
    padding: 0;
    margin: 0;
  ` as React.CSSProperties;

  const legendStyle = `
    font-family: var(--font-family-sans);
    font-size: var(--text-body);
    font-weight: var(--weight-medium);
    color: var(--color-text-primary);
    margin-bottom: var(--space-stack-xs);
    padding: 0;
  ` as React.CSSProperties;

  const groupStyle = `
    display: flex;
    flex-direction: ${orientation === 'horizontal' ? 'row' : 'column'};
    gap: ${orientation === 'horizontal' ? 'var(--space-inline-md)' : 'var(--space-stack-xs)'};
    flex-wrap: ${orientation === 'horizontal' ? 'wrap' : 'nowrap'};
  ` as React.CSSProperties;

  const errorStyle = `
    font-family: var(--font-family-sans);
    font-size: var(--text-body-sm);
    color: var(--color-danger);
    margin-top: var(--space-stack-xs);
    margin-bottom: 0;
  ` as React.CSSProperties;

  const helperStyle = `
    font-family: var(--font-family-sans);
    font-size: var(--text-body-sm);
    color: var(--color-text-secondary);
    margin-top: var(--space-stack-xs);
    margin-bottom: 0;
  ` as React.CSSProperties;

  return (
    <fieldset style={fieldsetStyle as React.CSSProperties} role="group">
      {label && (
        <legend style={legendStyle as React.CSSProperties}>
          {label}
          {required && <span aria-hidden="true" style={{ color: 'var(--color-danger)', marginLeft: 2 }}>*</span>}
        </legend>
      )}
      <div style={groupStyle as React.CSSProperties}>
        {children}
      </div>
      {error && <p style={errorStyle as React.CSSProperties} role="alert">{error}</p>}
      {helperText && !error && <p style={helperStyle as React.CSSProperties}>{helperText}</p>}
    </fieldset>
  );
};

CheckboxGroup.displayName = 'CheckboxGroup';

export default Checkbox;
