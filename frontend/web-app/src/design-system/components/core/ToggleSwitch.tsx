import React, { forwardRef, useState, useRef, InputHTMLAttributes } from 'react';

export interface ToggleSwitchProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type' | 'size' | 'onChange' | 'value'> {
  checked?: boolean;
  defaultChecked?: boolean;
  onChange?: (checked: boolean) => void;
  label?: string;
  description?: string;
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  loading?: boolean;
  error?: boolean;
  success?: boolean;
}

const switchWidth = { sm: 32, md: 40, lg: 48 };
const switchHeight = { sm: 18, md: 22, lg: 26 };
const thumbSize = { sm: 14, md: 18, lg: 22 };
const thumbOffset = { sm: 2, md: 2, lg: 2 };

export const ToggleSwitch = forwardRef<HTMLInputElement, ToggleSwitchProps>(
  (
    {
      checked,
      defaultChecked = false,
      onChange,
      label,
      description,
      size = 'md',
      disabled = false,
      loading = false,
      error = false,
      success = false,
      className = '',
      id: externalId,
      ...props
    },
    ref
  ) => {
    const inputRef = useRef<HTMLInputElement | null>(null);
    const [internalChecked, setInternalChecked] = useState(defaultChecked);

    const isControlled = checked !== undefined;
    const isChecked = isControlled ? checked : internalChecked;
    const isDisabled = disabled || loading;

    const combinedRef = (el: HTMLInputElement | null) => {
      (inputRef as React.MutableRefObject<HTMLInputElement | null>).current = el;
      if (typeof ref === 'function') ref(el);
      else if (ref) (ref as React.MutableRefObject<HTMLInputElement | null>).current = el;
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      if (isDisabled) return;
      const newChecked = e.target.checked;
      if (!isControlled) {
        setInternalChecked(newChecked);
      }
      onChange?.(newChecked);
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLLabelElement>) => {
      if (e.key === ' ' || e.key === 'Enter') {
        e.preventDefault();
        if (!isDisabled && inputRef.current) {
          inputRef.current.checked = !inputRef.current.checked;
          const ev = new Event('change', { bubbles: true });
          inputRef.current.dispatchEvent(ev);
          if (!isControlled) {
            setInternalChecked(inputRef.current.checked);
          }
          onChange?.(inputRef.current.checked);
        }
      }
    };

    const sw = switchWidth[size];
    const sh = switchHeight[size];
    const th = thumbSize[size];
    const off = thumbOffset[size];

    const wrapperStyle = `
      display: inline-flex;
      flex-direction: column;
      gap: var(--space-stack-xs);
    ` as React.CSSProperties;

    const rowStyle = `
      display: inline-flex;
      align-items: center;
      gap: var(--space-inline-sm);
      cursor: ${isDisabled ? 'not-allowed' : 'pointer'};
      opacity: ${isDisabled ? 'var(--opacity-disabled)' : '1'};
    ` as React.CSSProperties;

    const trackStyle = `
      position: relative;
      width: ${sw}px;
      height: ${sh}px;
      min-width: ${sw}px;
      border-radius: ${sh}px;
      background: var(${error ? '--color-danger' : isChecked ? (success ? '--color-success' : '--color-primary') : '--color-border-strong'});
      transition: background var(--duration-fast) var(--easing-standard);
      box-sizing: border-box;
    ` as React.CSSProperties;

    const thumbStyle = `
      position: absolute;
      top: ${off}px;
      left: ${isChecked ? sw - th - off : off}px;
      width: ${th}px;
      height: ${th}px;
      border-radius: var(--radius-full);
      background: white;
      box-shadow: 0 1px 3px rgba(0,0,0,0.2);
      transition: left var(--duration-fast) var(--easing-standard);
      display: flex;
      align-items: center;
      justify-content: center;
    ` as React.CSSProperties;

    const labelStyle = `
      font-family: var(--font-family-sans);
      font-size: var(--text-body);
      color: var(--color-text-primary);
      line-height: 1.25;
      user-select: none;
    ` as React.CSSProperties;

    const descriptionStyle = `
      font-family: var(--font-family-sans);
      font-size: var(--text-body-sm);
      color: var(--color-text-secondary);
      line-height: 1.25;
      margin: 0;
    ` as React.CSSProperties;

    const id = externalId || `sk-toggle-${Math.random().toString(36).slice(2, 9)}`;

    const spinnerStyle = `
      width: ${th * 0.6}px;
      height: ${th * 0.6}px;
      border: 2px solid var(--color-primary);
      border-right-color: transparent;
      border-radius: 50%;
      animation: sk-toggle-spin 0.6s linear infinite;
    ` as React.CSSProperties;

    return (
      <div style={wrapperStyle as React.CSSProperties} className={`sk-toggle-wrapper ${className}`}>
        <label
          htmlFor={id}
          style={rowStyle as React.CSSProperties}
          onKeyDown={handleKeyDown}
          tabIndex={isDisabled ? -1 : 0}
          role="label"
        >
          <input
            ref={combinedRef}
            id={id}
            type="checkbox"
            role="switch"
            checked={isChecked}
            disabled={isDisabled}
            onChange={handleChange}
            aria-checked={isChecked}
            aria-disabled={isDisabled}
            aria-label={label}
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
            className="sk-toggle__track"
            style={trackStyle as React.CSSProperties}
            onFocus={(e) => {
              e.currentTarget.style.outline = 'var(--focus-ring-width) solid var(--focus-ring-color)';
              e.currentTarget.style.outlineOffset = 'var(--focus-ring-offset)';
            }}
            onBlur={(e) => {
              e.currentTarget.style.outline = '';
              e.currentTarget.style.outlineOffset = '';
            }}
          >
            <span
              className="sk-toggle__thumb"
              style={thumbStyle as React.CSSProperties}
            >
              {loading && <span style={spinnerStyle as React.CSSProperties} />}
            </span>
          </span>
          {label && <span style={labelStyle as React.CSSProperties}>{label}</span>}
        </label>
        {description && <p style={descriptionStyle as React.CSSProperties}>{description}</p>}
        <style>{`
          @keyframes sk-toggle-spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
          }
        `}</style>
      </div>
    );
  }
);

ToggleSwitch.displayName = 'ToggleSwitch';

export default ToggleSwitch;
