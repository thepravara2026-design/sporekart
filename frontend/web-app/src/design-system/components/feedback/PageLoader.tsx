import React from 'react';

export interface PageLoaderProps {
  variant?: 'default' | 'card' | 'table' | 'form';
  className?: string;
  style?: React.CSSProperties;
}

const shimmerKeyframes = `
  @keyframes pageShimmer {
    0% { background-position: -200% 0; }
    100% { background-position: 200% 0; }
  }
`;

function ShimmerBlock({ width, height, borderRadius }: { width: string; height: string; borderRadius?: string }) {
  const style: React.CSSProperties = {
    width,
    height,
    borderRadius: borderRadius || 'var(--radius-sm)',
    background: `linear-gradient(90deg, var(--color-bg-skeleton-base) 25%, var(--color-bg-skeleton-highlight) 50%, var(--color-bg-skeleton-base) 75%)`,
    backgroundSize: '200% 100%',
    animation: 'pageShimmer 1.5s ease-in-out infinite',
  };
  return <div style={style} aria-hidden="true" />;
}

function DefaultLayout() {
  const pageStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-section-gap)',
  };
  const headerStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  };
  const contentStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    gap: 'var(--space-component-gap)',
  };
  const sidebarStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-sm)',
    width: 260,
    flexShrink: 0,
  };
  return (
    <div style={pageStyle} aria-hidden="true">
      <div style={headerStyle}>
        <ShimmerBlock width="240px" height="32px" />
        <ShimmerBlock width="120px" height="36px" borderRadius="var(--radius-btn)" />
      </div>
      <ShimmerBlock width="100%" height="1px" borderRadius="0" />
      <div style={{ display: 'flex', gap: 'var(--space-section-gap)' }}>
        <div style={sidebarStyle}>
          <ShimmerBlock width="100%" height="40px" borderRadius="var(--radius-sm)" />
          <ShimmerBlock width="100%" height="40px" borderRadius="var(--radius-sm)" />
          <ShimmerBlock width="100%" height="40px" borderRadius="var(--radius-sm)" />
          <ShimmerBlock width="100%" height="40px" borderRadius="var(--radius-sm)" />
        </div>
        <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
          <div style={contentStyle}>
            <ShimmerBlock width="100%" height="120px" borderRadius="var(--radius-card)" />
            <ShimmerBlock width="100%" height="120px" borderRadius="var(--radius-card)" />
            <ShimmerBlock width="100%" height="120px" borderRadius="var(--radius-card)" />
            <ShimmerBlock width="100%" height="120px" borderRadius="var(--radius-card)" />
          </div>
          <ShimmerBlock width="60%" height="16px" />
          <ShimmerBlock width="80%" height="16px" />
          <ShimmerBlock width="40%" height="16px" />
        </div>
      </div>
    </div>
  );
}

function CardLayout() {
  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
    gap: 'var(--space-component-gap)',
  };
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
    <div style={gridStyle} aria-hidden="true">
      {Array.from({ length: 6 }).map((_, i) => (
        <div key={i} style={cardStyle}>
          <ShimmerBlock width="100%" height="140px" borderRadius="var(--radius-sm)" />
          <ShimmerBlock width="80%" height="16px" />
          <ShimmerBlock width="100%" height="14px" />
          <ShimmerBlock width="60%" height="14px" />
        </div>
      ))}
    </div>
  );
}

function TableLayout() {
  const tableStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    borderRadius: 'var(--radius-md)',
    border: '1px solid var(--color-border-default)',
    overflow: 'hidden',
  };
  const headerStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-md)',
    padding: 'var(--space-inline-md)',
    backgroundColor: 'var(--color-bg-surface-default)',
    borderBottom: '1px solid var(--color-border-default)',
  };
  const rowStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-md)',
    padding: 'var(--space-inline-md)',
    borderBottom: '1px solid var(--color-border-default)',
  };
  return (
    <div style={tableStyle} aria-hidden="true">
      <div style={headerStyle}>
        <ShimmerBlock width="15%" height="14px" />
        <ShimmerBlock width="25%" height="14px" />
        <ShimmerBlock width="20%" height="14px" />
        <ShimmerBlock width="20%" height="14px" />
        <ShimmerBlock width="10%" height="14px" />
      </div>
      {Array.from({ length: 8 }).map((_, i) => (
        <div key={i} style={rowStyle}>
          <ShimmerBlock width="15%" height="12px" />
          <ShimmerBlock width="25%" height="12px" />
          <ShimmerBlock width="20%" height="12px" />
          <ShimmerBlock width="20%" height="12px" />
          <ShimmerBlock width="10%" height="12px" />
        </div>
      ))}
    </div>
  );
}

function FormLayout() {
  const formStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-lg)',
    maxWidth: 600,
  };
  const fieldStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
  };
  const rowStyle: React.CSSProperties = {
    display: 'flex',
    gap: 'var(--space-inline-md)',
  };
  return (
    <div style={formStyle} aria-hidden="true">
      <ShimmerBlock width="50%" height="28px" />
      <ShimmerBlock width="80%" height="14px" />
      <div style={rowStyle}>
        <div style={{ ...fieldStyle, flex: 1 }}>
          <ShimmerBlock width="40%" height="12px" />
          <ShimmerBlock width="100%" height="40px" borderRadius="var(--radius-input)" />
        </div>
        <div style={{ ...fieldStyle, flex: 1 }}>
          <ShimmerBlock width="40%" height="12px" />
          <ShimmerBlock width="100%" height="40px" borderRadius="var(--radius-input)" />
        </div>
      </div>
      {Array.from({ length: 3 }).map((_, i) => (
        <div key={i} style={fieldStyle}>
          <ShimmerBlock width="30%" height="12px" />
          <ShimmerBlock width="100%" height="40px" borderRadius="var(--radius-input)" />
        </div>
      ))}
      <div style={{ display: 'flex', gap: 'var(--space-inline-md)' }}>
        <ShimmerBlock width="120px" height="40px" borderRadius="var(--radius-btn)" />
        <ShimmerBlock width="120px" height="40px" borderRadius="var(--radius-btn)" />
      </div>
    </div>
  );
}

export const PageLoader: React.FC<PageLoaderProps> = ({
  variant = 'default',
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    padding: 'var(--space-page-y) var(--space-page-x)',
    ...style,
  };

  const renderVariant = () => {
    switch (variant) {
      case 'default': return <DefaultLayout />;
      case 'card': return <CardLayout />;
      case 'table': return <TableLayout />;
      case 'form': return <FormLayout />;
      default: return <DefaultLayout />;
    }
  };

  return (
    <div className={`sk-page-loader sk-page-loader--${variant} ${className}`.trim()} style={containerStyle} role="status" aria-label="Loading page">
      <style>{shimmerKeyframes}</style>
      {renderVariant()}
    </div>
  );
};

PageLoader.displayName = 'PageLoader';
export default PageLoader;
