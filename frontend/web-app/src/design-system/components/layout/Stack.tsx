import React from 'react';

export interface StackProps {
  children: React.ReactNode;
  gap?: string | number;
  className?: string;
  as?: 'div' | 'section' | 'article' | 'nav';
}

function toGap(v: string | number | undefined, fallback: string): string {
  if (v === undefined) return fallback;
  return typeof v === 'number' ? `${v}px` : v;
}

export const Stack: React.FC<StackProps> = ({
  children,
  gap,
  className = '',
  as: Tag = 'div',
}) => {
  const style: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: toGap(gap, 'var(--space-component-gap)'),
  };

  return (
    <Tag className={`sk-stack ${className}`.trim()} style={style}>
      {children}
    </Tag>
  );
};

Stack.displayName = 'Stack';
export default Stack;
