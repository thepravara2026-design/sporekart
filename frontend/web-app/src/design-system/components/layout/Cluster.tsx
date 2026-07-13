import React from 'react';

export interface ClusterProps {
  children: React.ReactNode;
  gap?: string | number;
  justify?: 'start' | 'center' | 'end' | 'space-between';
  align?: 'start' | 'center' | 'end';
  className?: string;
}

function toGap(v: string | number | undefined, fallback: string): string {
  if (v === undefined) return fallback;
  return typeof v === 'number' ? `${v}px` : v;
}

export const Cluster: React.FC<ClusterProps> = ({
  children,
  gap,
  justify = 'start',
  align = 'center',
  className = '',
}) => {
  const style: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: justify,
    alignItems: align,
    gap: toGap(gap, 'var(--space-component-gap)'),
  };

  return (
    <div className={`sk-cluster ${className}`.trim()} style={style}>
      {children}
    </div>
  );
};

Cluster.displayName = 'Cluster';
export default Cluster;
