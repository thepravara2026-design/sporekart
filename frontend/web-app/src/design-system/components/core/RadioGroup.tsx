import React, { forwardRef, useRef, InputHTMLAttributes } from 'react';

export interface RadioOption {
  value: string;
  label: string;
  disabled?: boolean;
  helperText?: string;
}

export interface RadioGroupProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type' | 'size' | 'onChange' | 'value'> {
  name: string;
  options: RadioOption[];
  value?: string;
  defaultValue?: string;
  onChange?: (value: string) => void;
  orientation?: 'horizontal' | 'vertical';
  label?: string;
  error?: string;
  helperText?: string;
  required?: boolean;
  disabled?: boolean;
  size?: 'sm' | 'md' | 'lg';
}

const sizeMap = { sm: 16, md: 20, lg: 24 };
const dotSizeMap = { sm: 6, md: 8, lg: 10 };

export const RadioGroup = forwardRef<HTMLDivElement, RadioGroupProps>(
  (
    {
      name,
      options,
      value,
      defaultValue,
      onChange,
      orientation = 'vertical',
      label,
      error,
      helperText,
      required = false,
      disabled = false,
      size = 'md',
      className = '',
      ...props
    },
    ref
  ) => {
    const internalRef = useRef<string>(value || defaultValue || '');

    const isControlled = value !== undefined;
    const selectedValue = isControlled ? value : internalRef.current;

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const newValue = e.target.value;
      if (!isControlled) {
        internalRef.current = newValue;
      }
      onChange?.(newValue);
    };

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
      <fieldset
        style={fieldsetStyle as React.CSSProperties}
        role="radiogroup"
        aria-label={label}
        aria-required={required}
        aria-invalid={!!error}
        aria-describedby={helperText || error ? `${name}-desc` : undefined}
      >
        {label && (
          <legend style={legendStyle as React.CSSProperties}>
            {label}
            {required && <span aria-hidden="true" style={{ color: 'var(--color-danger)', marginLeft: 2 }}>*</span>}
          </legend>
        )}
        <div ref={ref} style={groupStyle as React.CSSProperties} className={`sk-radio-group ${className}`}>
          {options.map((option) => {
            const optionPx = sizeMap[size];
            const optionDotPx = dotSizeMap[size];
            const isSelected = selectedValue === option.value;
            const isDisabled = disabled || option.disabled;
            const optionId = `${name}-${option.value}`;

            const radioWrapperStyle = `
              display: inline-flex;
              flex-direction: column;
              gap: 2px;
              opacity: ${isDisabled ? 'var(--opacity-disabled)' : '1'};
              cursor: ${isDisabled ? 'not-allowed' : 'pointer'};
            ` as React.CSSProperties;

            const rowStyle = `
              display: inline-flex;
              align-items: center;
              gap: var(--space-inline-sm);
            ` as React.CSSProperties;

            const circleStyle = `
              display: flex;
              align-items: center;
              justify-content: center;
              width: ${optionPx}px;
              height: ${optionPx}px;
              min-width: ${optionPx}px;
              border: 2px solid var(${error ? '--color-danger' : isSelected ? '--color-primary' : '--color-border-strong'});
              border-radius: var(--radius-full);
              background: var(--color-bg-surface-default);
              transition: all var(--duration-fast) var(--easing-standard);
              box-sizing: border-box;
            ` as React.CSSProperties;

            const selectedCircleStyle = `
              border-color: var(${error ? '--color-danger' : '--color-primary'});
            ` as React.CSSProperties;

            const dotStyle = `
              width: ${optionDotPx}px;
              height: ${optionDotPx}px;
              border-radius: var(--radius-full);
              background: var(${error ? '--color-danger' : '--color-primary'});
              transition: all var(--duration-fast) var(--easing-standard);
            ` as React.CSSProperties;

            const labelStyle = `
              font-family: var(--font-family-sans);
              font-size: var(--text-body);
              color: var(--color-text-primary);
              line-height: 1.25;
              user-select: none;
            ` as React.CSSProperties;

            const optionHelperStyle = `
              font-family: var(--font-family-sans);
              font-size: var(--text-body-sm);
              color: var(--color-text-secondary);
              line-height: 1.25;
              margin: 0;
              padding-left: calc(${optionPx}px + var(--space-inline-sm));
            ` as React.CSSProperties;

            return (
              <div key={option.value} style={radioWrapperStyle as React.CSSProperties}>
                <label htmlFor={optionId} style={rowStyle as React.CSSProperties} tabIndex={isDisabled ? -1 : 0}>
                  <input
                    id={optionId}
                    type="radio"
                    name={name}
                    value={option.value}
                    checked={isSelected}
                    disabled={isDisabled}
                    required={required && options.indexOf(option) === 0}
                    onChange={handleChange}
                    aria-checked={isSelected}
                    aria-disabled={isDisabled}
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
                    className="sk-radio__indicator"
                    style={{
                      ...(circleStyle as React.CSSProperties),
                      ...(isSelected ? (selectedCircleStyle as React.CSSProperties) : {}),
                    }}
                    onFocus={(e) => {
                      if (document.activeElement === document.getElementById(optionId)) {
                        e.currentTarget.style.outline = 'var(--focus-ring-width) solid var(--focus-ring-color)';
                        e.currentTarget.style.outlineOffset = 'var(--focus-ring-offset)';
                      }
                    }}
                    onBlur={(e) => {
                      e.currentTarget.style.outline = '';
                      e.currentTarget.style.outlineOffset = '';
                    }}
                  >
                    {isSelected && <span style={dotStyle as React.CSSProperties} />}
                  </span>
                  <span style={labelStyle as React.CSSProperties}>{option.label}</span>
                </label>
                {option.helperText && <p style={optionHelperStyle as React.CSSProperties}>{option.helperText}</p>}
              </div>
            );
          })}
        </div>
        {error && <p id={`${name}-desc`} style={errorStyle as React.CSSProperties} role="alert">{error}</p>}
        {helperText && !error && <p id={`${name}-desc`} style={helperStyle as React.CSSProperties}>{helperText}</p>}
      </fieldset>
    );
  }
);

RadioGroup.displayName = 'RadioGroup';

export default RadioGroup;
