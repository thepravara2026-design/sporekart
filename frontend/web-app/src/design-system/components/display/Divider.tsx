import React from 'react';

export interface DividerProps {
  orientation?: 'horizontal' | 'vertical';
  variant?: 'solid' | 'dashed' | 'dotted';
  label?: string;
  labelPosition?: 'left' | 'center' | 'right';
  className?: string;
}

export const Divider: React.FC<DividerProps> = ({
  orientation = 'horizontal',
  variant = 'solid',
  label,
  labelPosition = 'center',
  className = '',
}) => {
  const borderStyle = variant === 'dotted' ? 'dotted' : variant;

  if (orientation === 'vertical') {
    return (
      <div
        className={`sk-divider sk-divider--vertical ${className}`}
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          height: '100%',
          minHeight: '1em',
          alignSelf: 'stretch',
        }}
        role="separator"
        aria-orientation="vertical"
      >
        <span
          style={{
            width: 0,
            height: '100%',
            borderLeft: 'var(--border-width-thin) ' + borderStyle + ' var(--color-border-default)',
          }}
        />
      </div>
    );
  }

  if (label) {
    return (
      <div
        className={`sk-divider sk-divider--with-label ${className}`}
        style={{
          display: 'flex',
          alignItems: 'center',
          width: '100%',
          gap: 'var(--space-inline-sm)',
          position: 'relative',
        }}
        role="separator"
        aria-orientation="horizontal"
      >
        <span
          style={{
            flex: labelPosition === 'left' ? '0 0 0' : 1,
            height: 0,
            borderTop: 'var(--border-width-thin) ' + borderStyle + ' var(--color-border-default)',
          }}
        />
        <span
          className="sk-divider__label"
          style={{
            fontSize: 'var(--text-caption)',
            color: 'var(--color-text-secondary)',
            fontWeight: 'var(--weight-medium)',
            whiteSpace: 'nowrap',
            padding: '0 var(--space-inline-xs)',
          }}
        >
          {label}
        </span>
        <span
          style={{
            flex: labelPosition === 'right' ? '0 0 0' : 1,
            height: 0,
            borderTop: 'var(--border-width-thin) ' + borderStyle + ' var(--color-border-default)',
          }}
        />
      </div>
    );
  }

  return (
    <div
      className={`sk-divider ${className}`}
      style={{
        width: '100%',
        height: 0,
        borderTop: 'var(--border-width-thin) ' + borderStyle + ' var(--color-border-default)',
      }}
      role="separator"
      aria-orientation="horizontal"
    />
  );
};

Divider.displayName = 'Divider';

export default Divider;
