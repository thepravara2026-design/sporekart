import React from 'react';

export interface SectionLoaderProps {
  rows?: number;
  type?: 'card' | 'list' | 'table' | 'form';
  className?: string;
  style?: React.CSSProperties;
}

const shimmerKeyframes = `
  @keyframes sectionShimmer {
    0% { background-position: -200% 0; }
    100% { background-position: 200% 0; }
  }
`;

function ShimmerLine({ width, height = 14 }: { width?: string; height?: number }) {
  const style: React.CSSProperties = {
    width: width || '100%',
    height,
    borderRadius: 'var(--radius-sm)',
    background: `linear-gradient(90deg, var(--color-bg-skeleton-base) 25%, var(--color-bg-skeleton-highlight) 50%, var(--color-bg-skeleton-base) 75%)`,
    backgroundSize: '200% 100%',
    animation: 'sectionShimmer 1.5s ease-in-out infinite',
  };
  return <div style={style} aria-hidden="true" />;
}

function CardSkeleton() {
  const cardStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-sm)',
    padding: 'var(--space-inline-md)',
    borderRadius: 'var(--radius-card)',
    border: '1px solid var(--color-border-default)',
    backgroundColor: 'var(--color-bg-surface-default)',
  };
  return (
    <div style={cardStyle} aria-hidden="true">
      <ShimmerLine height={160} />
      <ShimmerLine width="80%" />
      <ShimmerLine />
      <ShimmerLine width="60%" />
    </div>
  );
}

function ListSkeleton() {
  const listItemStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-md)',
    padding: 'var(--space-inline-sm) 0',
  };
  return (
    <div aria-hidden="true">
      {Array.from({ length: 4 }).map((_, i) => (
        <div key={i} style={listItemStyle}>
          <div style={{ width: 40, height: 40, borderRadius: 'var(--radius-full)', background: 'var(--color-bg-skeleton-base)', flexShrink: 0 }} />
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 6 }}>
            <ShimmerLine width="70%" height={12} />
            <ShimmerLine width="40%" height={10} />
          </div>
        </div>
      ))}
    </div>
  );
}

function TableSkeleton() {
  const headerStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-md)',
    paddingBottom: 'var(--space-stack-sm)',
    borderBottom: '1px solid var(--color-border-default)',
    marginBottom: 'var(--space-stack-sm)',
  };
  const rowStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-md)',
    padding: 'var(--space-inline-sm) 0',
  };
  return (
    <div aria-hidden="true">
      <div style={headerStyle}>
        <ShimmerLine width="20%" height={14} />
        <ShimmerLine width="30%" height={14} />
        <ShimmerLine width="25%" height={14} />
        <ShimmerLine width="15%" height={14} />
      </div>
      {Array.from({ length: 4 }).map((_, i) => (
        <div key={i} style={rowStyle}>
          <ShimmerLine width="20%" height={12} />
          <ShimmerLine width="30%" height={12} />
          <ShimmerLine width="25%" height={12} />
          <ShimmerLine width="15%" height={12} />
        </div>
      ))}
    </div>
  );
}

function FormSkeleton() {
  const fieldStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
    marginBottom: 'var(--space-stack-md)',
  };
  return (
    <div aria-hidden="true">
      {Array.from({ length: 4 }).map((_, i) => (
        <div key={i} style={fieldStyle}>
          <ShimmerLine width="30%" height={12} />
          <div style={{ height: 40, borderRadius: 'var(--radius-input)', background: 'var(--color-bg-skeleton-base)' }} />
        </div>
      ))}
      <div style={{ height: 40, width: '40%', borderRadius: 'var(--radius-btn)', background: 'var(--color-bg-skeleton-base)', marginTop: 'var(--space-stack-md)' }} />
    </div>
  );
}

export const SectionLoader: React.FC<SectionLoaderProps> = ({
  rows = 3,
  type = 'card',
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-md)',
    ...style,
  };

  const renderSkeleton = () => {
    switch (type) {
      case 'card':
        return Array.from({ length: rows }).map((_, i) => <CardSkeleton key={i} />);
      case 'list':
        return <ListSkeleton />;
      case 'table':
        return <TableSkeleton />;
      case 'form':
        return <FormSkeleton />;
      default:
        return <CardSkeleton />;
    }
  };

  return (
    <div className={`sk-section-loader sk-section-loader--${type} ${className}`.trim()} style={containerStyle} role="status" aria-label="Loading content">
      <style>{shimmerKeyframes}</style>
      {renderSkeleton()}
    </div>
  );
};

SectionLoader.displayName = 'SectionLoader';
export default SectionLoader;
