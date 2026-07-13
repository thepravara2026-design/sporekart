import React from 'react';

export interface SplitLayoutProps {
  left: React.ReactNode;
  right: React.ReactNode;
  ratio?: '50-50' | '60-40' | '70-30' | '40-60' | '30-70';
  className?: string;
}

const ratioMap: Record<string, string> = {
  '50-50': '1fr 1fr',
  '60-40': '3fr 2fr',
  '70-30': '7fr 3fr',
  '40-60': '2fr 3fr',
  '30-70': '3fr 7fr',
};

export const SplitLayout: React.FC<SplitLayoutProps> = ({
  left,
  right,
  ratio = '50-50',
  className = '',
}) => {
  const style: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: ratioMap[ratio],
    gap: 'var(--space-component-gap)',
  };

  return (
    <div className={`sk-split-layout ${className}`.trim()} style={style}>
      <div className="sk-split-layout__left">{left}</div>
      <div className="sk-split-layout__right">{right}</div>
    </div>
  );
};

SplitLayout.displayName = 'SplitLayout';
export default SplitLayout;
