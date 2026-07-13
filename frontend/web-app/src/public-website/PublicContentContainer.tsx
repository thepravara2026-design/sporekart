import type { ReactNode } from 'react';

export interface PublicContentContainerProps {
  children: ReactNode;
  maxWidth?: 'sm' | 'md' | 'lg' | 'xl';
  padding?: boolean;
}

const MAX_WIDTH: Record<NonNullable<PublicContentContainerProps['maxWidth']>, string> = {
  sm: 'var(--container-sm, 640px)',
  md: 'var(--container-md, 768px)',
  lg: 'var(--container-lg, 992px)',
  xl: 'var(--container-xl, 1200px)',
};

export function PublicContentContainer({
  children,
  maxWidth = 'xl',
  padding = true,
}: PublicContentContainerProps) {
  const style: React.CSSProperties = {
    width: '100%',
    maxWidth: MAX_WIDTH[maxWidth],
    margin: '0 auto',
    padding: padding ? 'var(--space-6, 32px) var(--space-5, 24px)' : 0,
    boxSizing: 'border-box',
  };

  return <div className="sk-public-content" style={style}>{children}</div>;
}
