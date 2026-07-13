import React, { forwardRef, useState } from 'react';

export interface FloatingActionButtonProps {
  onClick?: () => void;
  label?: string;
  icon?: React.ReactNode;
  variant?: 'primary' | 'secondary' | 'destructive';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  loading?: boolean;
  position?: 'bottom-right' | 'bottom-left' | 'top-right' | 'top-left';
  ariaLabel?: string;
  tooltip?: string;
}

export const FloatingActionButton = forwardRef<HTMLButtonElement, FloatingActionButtonProps>(
  ({
    onClick,
    label: _label,
    icon = <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" width={24} height={24}><line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" /></svg>,
    variant: _variant = 'primary',
    size = 'md',
    disabled = false,
    loading = false,
    position = 'bottom-right',
    ariaLabel = 'Floating action',
    tooltip,
    ...props
  }, ref) => {
    void props;
    const [_isHovered, setIsHovered] = useState(false);

    const positionStyles: Record<string, React.CSSProperties> = {
      'bottom-right': { bottom: '24px', right: '24px' },
      'bottom-left': { bottom: '24px', left: '24px' },
      'top-right': { top: '24px', right: '24px' },
      'top-left': { top: '24px', left: '24px' },
    };

    const sizeStyles = {
      sm: { width: '40px', height: '40px', fontSize: '0.875rem' },
      md: { width: '56px', height: '56px', fontSize: '1rem' },
      lg: { width: '64px', height: '64px', fontSize: '1.125rem' },
    };

    return (
      <div
        className={`sk-fab sk-fab--${position}`}
        style={{
          position: 'fixed',
          zIndex: 100,
          ...positionStyles[position],
          ...sizeStyles[size],
        } as React.CSSProperties}
      >
        {tooltip && (
          <div className="sk-fab__tooltip" role="tooltip">
            {tooltip}
          </div>
        )}
        <button
          ref={ref as React.Ref<HTMLButtonElement>}
          className="sk-fab"
          onClick={onClick}
          disabled={disabled}
          aria-label={ariaLabel}
          aria-disabled={disabled}
          style={{
            width: '100%',
            height: '100%',
            border: 'none',
            borderRadius: '50%',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            cursor: disabled ? 'not-allowed' : 'pointer',
            opacity: disabled ? 0.5 : 1,
            boxShadow: 'var(--shadow-3)',
            transition: 'transform var(--duration-fast) var(--easing-standard), box-shadow var(--duration-fast) var(--easing-standard)',
            backgroundColor: 'var(--color-bg-primary-default)',
            color: 'var(--color-text-on-primary)',
          }}
          onMouseEnter={() => setIsHovered(true)}
          onMouseLeave={() => setIsHovered(false)}
        >
          {loading ? (
            <svg className="sk-fab__spinner" viewBox="0 0 24 24" width="24" height="24" aria-hidden="true">
              <circle cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="3" fill="none" strokeDasharray="30 70" strokeLinecap="round">
                <animateTransform attributeName="transform" type="rotate" from="0 12 12" to="360 12 12" dur="1s" repeatCount="indefinite" />
              </circle>
            </svg>
          ) : (
            <span style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              {icon}
            </span>
          )}
        </button>
      </div>
    );
  }
);

FloatingActionButton.displayName = 'FloatingActionButton';

export default FloatingActionButton;