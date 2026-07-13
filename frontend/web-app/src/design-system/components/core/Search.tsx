import React, { forwardRef, InputHTMLAttributes, useState, useCallback, useRef } from 'react';

export interface SearchProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'size'> {
  size?: 'sm' | 'md' | 'lg';
  label?: string;
  placeholder?: string;
  loading?: boolean;
  disabled?: boolean;
  error?: string;
  value?: string;
  defaultValue?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onClear?: () => void;
  onSearch?: (value: string) => void;
  fullWidth?: boolean;
}

export const Search = forwardRef<HTMLInputElement, SearchProps>(
  (
    {
      size: searchSize = 'md',
      label,
      placeholder = 'Search\u2026',
      loading = false,
      disabled = false,
      error,
      value,
      defaultValue = '',
      onChange,
      onClear,
      onSearch,
      fullWidth = false,
      className = '',
      id: externalId,
      ...props
    },
    ref
  ) => {
    const generatedId = useRef(`sk-search-${Math.random().toString(36).substr(2, 9)}`);
    const inputId = externalId || generatedId.current;
    const errorId = `${inputId}-error`;

    const isControlled = value !== undefined;
    const [internalValue, setInternalValue] = useState<string>(defaultValue);
    const inputRef = useRef<HTMLInputElement | null>(null);

    const currentValue = isControlled ? value : internalValue;

    const handleChange = useCallback(
      (e: React.ChangeEvent<HTMLInputElement>) => {
        if (disabled || loading) return;
        if (!isControlled) {
          setInternalValue(e.target.value);
        }
        onChange?.(e);
      },
      [disabled, loading, isControlled, onChange]
    );

    const handleClear = useCallback(() => {
      if (disabled || loading) return;
      if (!isControlled) {
        setInternalValue('');
      }
      onClear?.();
      if (inputRef.current) {
        inputRef.current.focus();
      }
    }, [disabled, loading, isControlled, onClear]);

    const handleKeyDown = useCallback(
      (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
          onSearch?.(currentValue);
        }
        if (e.key === 'Escape' && currentValue) {
          handleClear();
        }
        props.onKeyDown?.(e);
      },
      [currentValue, onSearch, handleClear, props.onKeyDown]
    );

    const hasValue = currentValue.length > 0;

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

    const spinnerSize = {
      sm: '14px',
      md: '16px',
      lg: '18px',
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

    const borderColor = error
      ? 'var(--color-border-error)'
      : 'var(--color-border-default)';

    const outlineColor = error
      ? 'var(--color-border-error)'
      : 'var(--color-focus-ring)';

    const containerStyles = `
      display: flex;
      align-items: center;
      gap: var(--space-inline-sm);
      height: ${inputHeight[searchSize]};
      padding: 0 ${paddingX[searchSize]};
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
      font-size: ${fontSize[searchSize]};
      font-weight: var(--weight-normal);
      line-height: var(--leading-normal);
      color: var(--color-text-primary);
      letter-spacing: var(--tracking-normal);
      padding: 0;
      margin: 0;
      ${disabled ? 'cursor: not-allowed;' : ''}
    `;

    const iconStyles = `
      display: flex;
      align-items: center;
      flex-shrink: 0;
      color: var(--color-text-secondary);
    `;

    const clearButtonStyles = `
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: ${iconSize[searchSize]};
      height: ${iconSize[searchSize]};
      border: none;
      background: none;
      cursor: pointer;
      padding: 0;
      color: var(--color-text-secondary);
      border-radius: var(--radius-xs);
      transition: color var(--duration-fast) var(--easing-standard);
      flex-shrink: 0;
    `;

    const errorStyles = `
      font-family: var(--font-family-sans);
      font-size: var(--text-caption);
      line-height: var(--leading-normal);
      color: var(--color-text-error);
    `;

    const focusStyles = `
      outline: none;
      box-shadow: 0 0 0 3px ${outlineColor};
      outline-offset: var(--focus-ring-offset);
    `;

    const setRef = useCallback(
      (node: HTMLInputElement | null) => {
        inputRef.current = node;
        if (typeof ref === 'function') {
          ref(node);
        } else if (ref) {
          (ref as React.MutableRefObject<HTMLInputElement | null>).current = node;
        }
      },
      [ref]
    );

    return (
      <div
        className={`sk-search-wrapper${fullWidth ? ' sk-search-wrapper--full-width' : ''}`}
        style={wrapperStyles as React.CSSProperties}
      >
        {label && (
          <label htmlFor={inputId} style={labelStyles as React.CSSProperties}>
            {label}
          </label>
        )}

        <div
          className={`sk-search-container${error ? ' sk-search-container--error' : ''}${disabled ? ' sk-search-container--disabled' : ''}`}
          style={containerStyles as React.CSSProperties}
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
          {loading ? (
            <span
              className="sk-search__spinner"
              style={{
                width: spinnerSize[searchSize],
                height: spinnerSize[searchSize],
                border: '2px solid var(--color-border-default)',
                borderRightColor: 'transparent',
                borderRadius: '50%',
                animation: 'sk-spin 0.8s linear infinite',
                flexShrink: 0,
              }}
              aria-hidden="true"
            />
          ) : (
            <span
              className="sk-search__icon"
              style={{
                ...(iconStyles as React.CSSProperties),
                width: iconSize[searchSize],
                height: iconSize[searchSize],
              }}
              aria-hidden="true"
            >
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth={2}
                strokeLinecap="round"
                strokeLinejoin="round"
                width={iconSize[searchSize]}
                height={iconSize[searchSize]}
              >
                <circle cx="11" cy="11" r="8" />
                <line x1="21" y1="21" x2="16.65" y2="16.65" />
              </svg>
            </span>
          )}

          <input
            ref={setRef}
            id={inputId}
            type="search"
            className={`sk-search${className ? ` ${className}` : ''}`}
            style={inputStyles as React.CSSProperties}
            placeholder={placeholder}
            disabled={disabled}
            aria-invalid={!!error || undefined}
            aria-describedby={error ? errorId : undefined}
            aria-disabled={disabled || undefined}
            value={value}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
            role="searchbox"
            {...props}
          />

          {!loading && hasValue && (
            <button
              type="button"
              className="sk-search__clear"
              style={clearButtonStyles as React.CSSProperties}
              onClick={handleClear}
              aria-label="Clear search"
              tabIndex={0}
            >
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth={2}
                strokeLinecap="round"
                strokeLinejoin="round"
                width={iconSize[searchSize]}
                height={iconSize[searchSize]}
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          )}
        </div>

        {error && (
          <span
            id={errorId}
            className="sk-search__error"
            style={errorStyles as React.CSSProperties}
            role="alert"
          >
            {error}
          </span>
        )}

        <style>{`@keyframes sk-spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }`}</style>
      </div>
    );
  }
);

Search.displayName = 'Search';

export default Search;
