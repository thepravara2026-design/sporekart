import React from 'react';

export interface PanelContainerProps {
  title?: string;
  description?: string;
  children?: React.ReactNode;
  actions?: React.ReactNode;
  footer?: React.ReactNode;
  variant?: 'default' | 'elevated' | 'bordered';
  padding?: 'sm' | 'md' | 'lg';
  className?: string;
  style?: React.CSSProperties;
}

const variantMap: Record<string, React.CSSProperties> = {
  default: {
    background: 'var(--color-bg-surface-default)',
    border: '1px solid var(--color-border-default)',
  },
  elevated: {
    background: 'var(--color-bg-surface-default)',
    boxShadow: 'var(--shadow-sm)',
    border: '1px solid var(--color-border-default)',
  },
  bordered: {
    background: 'var(--color-bg-background)',
    border: '2px solid var(--color-border-default)',
  },
};

const paddingMap: Record<string, React.CSSProperties> = {
  sm: { padding: 'var(--space-stack-sm) var(--space-page-x)' },
  md: { padding: 'var(--space-stack-md) var(--space-page-x)' },
  lg: { padding: 'var(--space-stack-lg) var(--space-page-x)' },
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'flex-start',
  gap: 'var(--space-inline-md)',
  marginBottom: 'var(--space-stack-md)',
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h4)',
  fontWeight: 'var(--weight-bold)',
  color: 'var(--color-text-primary)',
  margin: 0,
};

const descStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
  margin: '2px 0 0',
};

const footerStyle: React.CSSProperties = {
  marginTop: 'var(--space-stack-md)',
  paddingTop: 'var(--space-stack-sm)',
  borderTop: '1px solid var(--color-border-default)',
  fontSize: 'var(--text-caption)',
  color: 'var(--color-text-secondary)',
};

export const PanelContainer: React.FC<PanelContainerProps> = ({
  title,
  description,
  children,
  actions,
  footer,
  variant = 'default',
  padding = 'md',
  className = '',
  style,
}) => {
  return (
    <div
      className={className}
      style={{
        borderRadius: 'var(--radius-card)',
        display: 'flex',
        flexDirection: 'column',
        ...variantMap[variant],
        ...paddingMap[padding],
        ...style,
      }}
    >
      {(title || actions) && (
        <div style={headerStyle}>
          <div>
            {title && <h3 style={titleStyle}>{title}</h3>}
            {description && <p style={descStyle}>{description}</p>}
          </div>
          {actions && <div style={{ flexShrink: 0, display: 'flex', gap: 'var(--space-inline-xs)' }}>{actions}</div>}
        </div>
      )}
      <div style={{ flex: 1 }}>{children}</div>
      {footer && <div style={footerStyle}>{footer}</div>}
    </div>
  );
};

PanelContainer.displayName = 'PanelContainer';
export default PanelContainer;
