import React, { forwardRef, ButtonHTMLAttributes } from 'react';

export interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'destructive' | 'success' | 'warning' | 'link';
  size?: 'sm' | 'md' | 'lg';
  fullWidth?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  loading?: boolean;
  disabled?: boolean;
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      variant = 'primary',
      size = 'md',
      fullWidth = false,
      leftIcon,
      rightIcon,
      loading = false,
      disabled = false,
      className = '',
      children,
      type = 'button',
      ...props
    },
    ref
  ) => {
    const baseStyles = `
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: var(--space-inline-sm);
      font-family: var(--font-family-sans);
      font-weight: var(--weight-semibold);
      font-size: var(--text-button);
      line-height: var(--leading-normal);
      letter-spacing: var(--tracking-wide);
      border: var(--border-width-thin) solid transparent;
      border-radius: var(--radius-btn);
      cursor: pointer;
      transition: all var(--duration-fast) var(--easing-standard);
      white-space: nowrap;
      user-select: none;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
    `;

    const sizeStyles = {
      sm: 'padding: 6px 12px; height: 36px; font-size: 0.8125rem; gap: var(--space-inline-xs);',
      md: 'padding: 9px 16px; height: 40px; font-size: var(--text-button); gap: var(--space-inline-sm);',
      lg: 'padding: 11px 24px; height: 48px; font-size: 1rem; gap: var(--space-inline-md);',
    };

    const widthStyles = fullWidth ? 'width: 100%;' : '';

    const disabledStyles = disabled ? `
      opacity: var(--opacity-disabled);
      cursor: not-allowed;
      pointer-events: none;
    ` : '';

    const loadingStyles = loading ? `
      opacity: 0.7;
      cursor: wait;
      pointer-events: none;
    ` : '';

    const variantStyles = {
      primary: `
        background: var(--color-bg-primary-default);
        color: var(--color-text-on-primary);
        border-color: var(--color-bg-primary-default);
      `,
      secondary: `
        background: var(--color-bg-surface-default);
        color: var(--color-text-primary);
        border-color: var(--color-border-default);
      `,
      outline: `
        background: transparent;
        color: var(--color-primary);
        border-color: var(--color-primary);
      `,
      ghost: `
        background: transparent;
        color: var(--color-primary);
        border-color: transparent;
      `,
      destructive: `
        background: var(--color-danger);
        color: white;
        border-color: var(--color-danger);
      `,
      success: `
        background: var(--color-success);
        color: white;
        border-color: var(--color-success);
      `,
      warning: `
        background: var(--color-warning);
        color: var(--color-neutral-900);
        border-color: var(--color-warning);
      `,
      link: `
        background: transparent;
        color: var(--color-primary);
        border-color: transparent;
        padding: 0;
        height: auto;
        text-decoration: underline;
        text-underline-offset: 2px;
      `,
    };

    const focusStyles = `
      outline: none;
      box-shadow: 0 0 0 3px var(--color-focus-ring);
      outline-offset: var(--focus-ring-offset);
    `;

    const iconSize = {
      sm: '16px',
      md: '20px',
      lg: '24px',
    };

    const style = `
      ${baseStyles}
      ${sizeStyles[size]}
      ${widthStyles}
      ${disabledStyles}
      ${loadingStyles}
      ${variantStyles[variant]}
      ${className}
    `;

    const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
      if (disabled || loading) {
        e.preventDefault();
        e.stopPropagation();
        return;
      }
      props.onClick?.(e);
    };

    return (
      <button
        ref={ref}
        type={type}
        disabled={disabled || loading}
        className="sk-btn"
        style={style as React.CSSProperties}
        onClick={handleClick}
        onMouseDown={props.onMouseDown}
        onMouseUp={props.onMouseUp}
        onFocus={(e) => {
          e.currentTarget.style.cssText += focusStyles;
          props.onFocus?.(e);
        }}
        onBlur={props.onBlur}
        aria-disabled={disabled || loading}
        aria-busy={loading}
        {...props}
      >
        {loading && (
          <span
            className="sk-btn__spinner"
            style={{
              width: iconSize[size],
              height: iconSize[size],
              border: '2px solid currentColor',
              borderRightColor: 'transparent',
              borderRadius: '50%',
              animation: 'sk-spin 0.8s linear infinite',
              flexShrink: 0,
            }}
            aria-hidden="true"
          />
        )}
        {!loading && leftIcon && (
          <span className="sk-btn__icon sk-btn__icon--left" style={{ fontSize: iconSize[size] }}>
            {leftIcon}
          </span>
        )}
        <span className="sk-btn__text">{children}</span>
        {!loading && rightIcon && (
          <span className="sk-btn__icon sk-btn__icon--right" style={{ fontSize: iconSize[size] }}>
            {rightIcon}
          </span>
        )}
        <style>{`@keyframes sk-spin { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }`}</style>
      </button>
    )
  }
);

Button.displayName = 'Button';

export default Button;