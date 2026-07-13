import React from 'react';

export interface InlineProps {
  children: React.ReactNode;
  gap?: string | number;
  align?: 'start' | 'center' | 'end' | 'stretch';
  wrap?: boolean;
  className?: string;
  as?: 'div' | 'span' | 'nav';
}

function toGap(v: string | number | undefined, fallback: string): string {
  if (v === undefined) return fallback;
  return typeof v === 'number' ? `${v}px` : v;
}

export const Inline: React.FC<InlineProps> = ({
  children,
  gap,
  align = 'center',
  wrap = false,
  className = '',
  as: Tag = 'div',
}) => {
  const style: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'row',
    alignItems: align,
    gap: toGap(gap, 'var(--space-inline-md)'),
    ...(wrap ? { flexWrap: 'wrap' } : {}),
  };

  return (
    <Tag className={`sk-inline ${className}`.trim()} style={style}>
      {children}
    </Tag>
  );
};

Inline.displayName = 'Inline';
export default Inline;
