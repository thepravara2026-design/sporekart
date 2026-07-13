import React from 'react';
import { useChartResize } from './useChartResize';
import { ChartSkeleton } from './ChartSkeleton';
import { EmptyState } from '../../display/EmptyState';

export interface ChartContainerProps {
  title?: string;
  subtitle?: string;
  children?: React.ReactNode;
  loading?: boolean;
  empty?: boolean;
  error?: boolean;
  errorMessage?: string;
  skeleton?: React.ReactNode;
  className?: string;
  style?: React.CSSProperties;
  height?: string | number;
  onExport?: () => void;
  actions?: React.ReactNode;
}

export const ChartContainer: React.FC<ChartContainerProps> = ({
  title,
  subtitle,
  children,
  loading = false,
  empty = false,
  error = false,
  errorMessage,
  skeleton,
  className = '',
  style,
  height = 400,
  onExport,
  actions,
}) => {
  const { containerRef } = useChartResize();

  const wrapperStyle: React.CSSProperties = {
    background: 'var(--color-bg-surface-default)',
    borderRadius: 'var(--radius-card)',
    padding: 'var(--space-4)',
    boxShadow: 'var(--shadow-1)',
    display: 'flex',
    flexDirection: 'column',
    width: '100%',
    ...style,
  };

  const headerStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    justifyContent: 'space-between',
    marginBottom: 'var(--space-3)',
  };

  const titleGroupStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-1)',
  };

  const titleStyle: React.CSSProperties = {
    fontSize: 'var(--text-h5)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const subtitleStyle: React.CSSProperties = {
    fontSize: 'var(--text-body-sm)',
    color: 'var(--color-text-secondary)',
    margin: 0,
  };

  const chartAreaStyle: React.CSSProperties = {
    position: 'relative',
    width: '100%',
    height,
    minHeight: 200,
    overflow: 'hidden',
  };

  const errorStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100%',
    color: 'var(--color-text-danger)',
    fontSize: 'var(--text-body)',
  };

  const renderBody = () => {
    if (loading) {
      return skeleton || <ChartSkeleton height={height} />;
    }
    if (error) {
      return <div style={errorStyle}>{errorMessage || 'An error occurred while loading the chart.'}</div>;
    }
    if (empty) {
      return <EmptyState type="noData" compact />;
    }
    return <div ref={containerRef as React.RefObject<HTMLDivElement>} style={{ width: '100%', height: '100%' }}>{children}</div>;
  };

  return (
    <div className={className} style={wrapperStyle}>
      {(title || subtitle || actions || onExport) && (
        <div style={headerStyle}>
          <div style={titleGroupStyle}>
            {title && <h3 style={titleStyle}>{title}</h3>}
            {subtitle && <p style={subtitleStyle}>{subtitle}</p>}
          </div>
          {(actions || onExport) && (
            <div style={{ display: 'flex', gap: 'var(--space-2)', alignItems: 'center' }}>
              {actions}
            </div>
          )}
        </div>
      )}
      <div style={chartAreaStyle}>
        {renderBody()}
      </div>
    </div>
  );
};

ChartContainer.displayName = 'ChartContainer';
export default ChartContainer;
