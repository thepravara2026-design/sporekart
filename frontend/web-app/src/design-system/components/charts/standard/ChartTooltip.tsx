import React from 'react';
import { createPortal } from 'react-dom';

export interface ChartTooltipProps {
  open: boolean;
  x: number;
  y: number;
  title?: string;
  children?: React.ReactNode;
  className?: string;
  style?: React.CSSProperties;
}

export const ChartTooltip: React.FC<ChartTooltipProps> = ({
  open,
  x,
  y,
  title,
  children,
  className = '',
  style,
}) => {
  if (!open) return null;

  const tooltipStyle: React.CSSProperties = {
    position: 'fixed',
    left: x + 12,
    top: y - 12,
    background: 'var(--color-bg-surface-overlay)',
    border: '1px solid var(--color-border-default)',
    borderRadius: 'var(--radius-tooltip)',
    boxShadow: 'var(--shadow-2)',
    padding: 'var(--space-2) var(--space-3)',
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-primary)',
    pointerEvents: 'none',
    zIndex: 'var(--z-tooltip)',
    whiteSpace: 'nowrap',
    ...style,
  };

  const titleStyle: React.CSSProperties = {
    fontWeight: 'var(--weight-semibold)',
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    marginBottom: 'var(--space-1)',
  };

  return createPortal(
    <div className={className} style={tooltipStyle} role="tooltip">
      {title && <div style={titleStyle}>{title}</div>}
      {children}
    </div>,
    document.body
  );
};

ChartTooltip.displayName = 'ChartTooltip';
export default ChartTooltip;
