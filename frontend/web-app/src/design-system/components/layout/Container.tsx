import React from 'react';

export interface ContainerProps {
  children: React.ReactNode;
  size?: 'sm' | 'md' | 'lg' | 'full' | 'prose' | 'data';
  className?: string;
  as?: 'div' | 'section' | 'article' | 'main';
}

const maxWidthMap: Record<string, string> = {
  sm: '640px',
  md: '768px',
  lg: '1024px',
  full: '100%',
  prose: 'var(--container-prose)',
  data: 'var(--container-data)',
};

export const Container: React.FC<ContainerProps> = ({
  children,
  size = 'lg',
  className = '',
  as: Tag = 'div',
}) => {
  const style: React.CSSProperties = {
    width: '100%',
    maxWidth: maxWidthMap[size],
    marginLeft: 'auto',
    marginRight: 'auto',
    paddingLeft: 'var(--space-page-x)',
    paddingRight: 'var(--space-page-x)',
  };

  return (
    <Tag className={`sk-container sk-container--${size} ${className}`.trim()} style={style}>
      {children}
    </Tag>
  );
};

Container.displayName = 'Container';
export default Container;
