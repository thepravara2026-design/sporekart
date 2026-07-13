import React, { forwardRef } from 'react';

export interface LinkProps extends React.AnchorHTMLAttributes<HTMLAnchorElement> {
  variant?: 'inline' | 'navigation' | 'external' | 'text' | 'disabled';
  size?: 'sm' | 'md' | 'lg';
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  underline?: 'always' | 'hover' | 'never';
  disabled?: boolean;
}

export const Link = forwardRef<HTMLAnchorElement, LinkProps>(
  ({
    children,
    variant = 'inline',
    size = 'md',
    leftIcon,
    rightIcon,
    underline = 'hover',
    disabled = false,
    className = '',
    onClick,
    ...props
  }, ref) => {
    const isExternal = props.href && (props.href.startsWith('http://') || props.href.startsWith('https://')) && !props.href.includes(window.location.host);
    const resolvedVariant = isExternal ? 'external' : (props as any).variant || 'inline';

    const handleClick = (e: React.MouseEvent<HTMLAnchorElement>) => {
      if (disabled) {
        e.preventDefault();
        return;
      }
      if (isExternal && props.target === '_blank') {
        window.open(props.href, '_blank', 'noopener,noreferrer');
        e.preventDefault();
      }
      onClick?.(e as any);
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLAnchorElement>) => {
      if (e.key === 'Enter' || e.key === ' ') {
        if (!disabled) {
          e.preventDefault();
          (props as any).onClick?.(e);
        }
      }
    };

    const baseStyles = `
      display: inline-flex;
      align-items: center;
      gap: var(--space-inline-sm);
      text-decoration: none;
      font-family: var(--font-family-sans);
      font-weight: var(--weight-medium);
      cursor: ${disabled ? 'not-allowed' : 'pointer'};
      opacity: ${disabled ? 0.5 : 1};
      pointer-events: ${disabled ? 'none' : 'auto'};
      transition: color var(--duration-fast) var(--easing-standard);
    `;

    const variantStyles = {
      inline: `
        color: var(--color-text-primary);
        &:hover { color: var(--color-primary); }
        &:focus-visible { outline: var(--focus-ring-width) solid var(--focus-ring-color); outline-offset: var(--focus-ring-offset); }
      `,
      navigation: `
        color: var(--color-text-secondary);
        padding: var(--space-inline-xs) var(--space-inline-sm);
        border-radius: var(--radius-sm);
        &:hover {
          color: var(--color-text-primary);
          background: var(--color-bg-primary-weak);
        }
        &:focus-visible { outline: var(--focus-ring-width) solid var(--focus-ring-color); outline-offset: var(--focus-ring-offset); }
      `,
      external: `
        color: var(--color-primary);
        &:hover { text-decoration: underline; }
        &:focus-visible { outline: var(--focus-ring-width) solid var(--focus-ring-color); outline-offset: var(--focus-ring-offset); }
      `,
      text: `
        color: var(--color-text-secondary);
        font-weight: var(--weight-normal);
        &:hover { color: var(--color-primary); }
        &:focus-visible { outline: var(--focus-ring-width) solid var(--focus-ring-color); outline-offset: var(--focus-ring-offset); }
      `,
      disabled: `
        color: var(--color-text-disabled);
        cursor: not-allowed;
        pointer-events: none;
      `,
    };

    const sizeStyles = {
      sm: 'font-size: var(--text-caption); padding: var(--space-inline-xs) 0;',
      md: 'font-size: var(--text-body); padding: var(--space-inline-xs) 0;',
      lg: 'font-size: var(--text-body-lg); padding: var(--space-inline-sm) 0;',
    };

    const underlineStyles = {
      always: 'text-decoration: underline; text-underline-offset: 2px;',
      hover: 'text-decoration: none; &:hover { text-decoration: underline; }',
      never: 'text-decoration: none;',
    };

    return (
      <a
        ref={ref}
        className={`sk-link sk-link--${resolvedVariant} sk-link--${size} ${className}`}
        style={{
          cssText: `${baseStyles} ${variantStyles[resolvedVariant as keyof typeof variantStyles]} ${sizeStyles[size]} ${underlineStyles[underline]} ${className ? '' : ''}`,
        } as React.CSSProperties}
        onClick={handleClick}
        onKeyDown={handleKeyDown}
        aria-disabled={disabled}
        tabIndex={disabled ? -1 : 0}
        {...props}
      >
        {leftIcon && <span className="sk-link__icon sk-link__icon--left" aria-hidden="true">{leftIcon}</span>}
        <span>{children}</span>
        {rightIcon && <span className="sk-link__icon sk-link__icon--right" aria-hidden="true">{rightIcon}</span>}
        {isExternal && <span className="sk-link__external-icon" aria-hidden="true">↗</span>}
      </a>
    );
  }
);

Link.displayName = 'Link';

export default Link;