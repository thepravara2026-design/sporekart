import React from 'react';
import { useScopedStyle } from '../useScopedStyle';

export interface CardProps {
  children?: React.ReactNode;
  variant?: 'default' | 'elevated' | 'outlined' | 'ghost';
  padding?: 'none' | 'sm' | 'md' | 'lg';
  className?: string;
  onClick?: () => void;
  hoverable?: boolean;
  disabled?: boolean;
  loading?: boolean;
  error?: string;
  as?: 'div' | 'article' | 'section';
  'aria-label'?: string;
}

export const Card: React.FC<CardProps> = ({
  children,
  variant = 'default',
  padding = 'md',
  className = '',
  onClick,
  hoverable = false,
  disabled = false,
  loading = false,
  error,
  as: Component = 'article',
  'aria-label': ariaLabel,
}) => {
  const baseStyles = `
    display: flex;
    flex-direction: column;
    background: var(--color-bg-surface-default);
    border-radius: var(--radius-card);
    border: var(--border-width-thin) solid var(--color-border-default);
    transition: all var(--duration-fast) var(--easing-standard);
    position: relative;
    overflow: hidden;
    font-family: var(--font-family-sans);
    color: var(--color-text-primary);
  `;

  const variantStyles: Record<string, string> = {
    default: `
      background: var(--color-bg-surface-default);
      box-shadow: none;
    `,
    elevated: `
      background: var(--color-bg-surface-default);
      box-shadow: var(--shadow-1);
      border-color: transparent;
    `,
    outlined: `
      background: transparent;
      box-shadow: none;
      border-color: var(--color-border-default);
    `,
    ghost: `
      background: transparent;
      box-shadow: none;
      border-color: transparent;
    `,
  };

  const paddingStyles: Record<string, string> = {
    none: 'padding: 0;',
    sm: 'padding: var(--space-3);',
    md: 'padding: var(--space-4);',
    lg: 'padding: var(--space-6);',
  };

  const hoverableStyles = hoverable
    ? `
      cursor: pointer;
      &:hover {
        box-shadow: var(--shadow-2);
        transform: translateY(-1px);
      }
    `
    : '';

  const disabledStyles = disabled
    ? `opacity: var(--opacity-disabled); pointer-events: none;`
    : '';

  const loadingStyles = loading
    ? `position: relative;`
    : '';

  const errorStyles = error
    ? `border-color: var(--color-border-error);`
    : '';

  const clickableStyles = onClick && !disabled && !loading
    ? `cursor: pointer;`
    : '';

  const style = `
    ${baseStyles}
    ${variantStyles[variant]}
    ${paddingStyles[padding]}
    ${hoverableStyles}
    ${disabledStyles}
    ${loadingStyles}
    ${errorStyles}
    ${clickableStyles}
  `;

  const { className: scopedClass, styleEl } = useScopedStyle(style);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (onClick && !disabled && !loading && (e.key === 'Enter' || e.key === ' ')) {
      e.preventDefault();
      onClick();
    }
  };

  const showSkeleton = loading && !error;

  return (
    <>
      {styleEl}
      <Component
        className={`sk-card ${scopedClass} ${className}`.trim()}
        onClick={!disabled && !loading ? onClick : undefined}
      onKeyDown={onClick ? handleKeyDown : undefined}
      tabIndex={onClick ? 0 : undefined}
      role={onClick ? 'button' : undefined}
      aria-label={ariaLabel}
      aria-disabled={disabled}
      aria-busy={loading}
    >
      {showSkeleton && (
        <div
          className="sk-card__skeleton"
          style={{
            position: 'absolute',
            inset: 0,
            background: 'var(--color-bg-skeleton-base)',
            zIndex: 1,
            pointerEvents: 'none',
          }}
        >
          <div
            style={{
              height: '100%',
              width: '40%',
              background: 'linear-gradient(90deg, var(--color-bg-skeleton-base) 0%, var(--color-bg-skeleton-highlight) 50%, var(--color-bg-skeleton-base) 100%)',
              backgroundSize: '200% 100%',
              animation: 'sk-shimmer 1.5s ease-in-out infinite',
            }}
          />
          <style>{`@keyframes sk-shimmer { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }`}</style>
        </div>
      )}
      {error && (
        <div
          className="sk-card__error"
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-inline-sm)',
            padding: 'var(--space-2) var(--space-3)',
            background: 'var(--color-danger-50)',
            color: 'var(--color-text-danger)',
            fontSize: 'var(--text-caption)',
            borderRadius: 'var(--radius-xs)',
            marginBottom: 'var(--space-stack-sm)',
          }}
        >
          <span aria-hidden="true" style={{ fontWeight: 'var(--weight-bold)' }}>!</span>
          <span>{error}</span>
        </div>
      )}
      {!showSkeleton && children}
    </Component>
    </>
  );
};

Card.displayName = 'Card';

export default Card;
