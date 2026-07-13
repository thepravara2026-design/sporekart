import React from 'react';

export interface ContentContainerProps {
  children: React.ReactNode;
  maxWidth?: string;
  className?: string;
  as?: 'div' | 'main' | 'section' | 'article';
}

export const ContentContainer: React.FC<ContentContainerProps> = ({
  children,
  maxWidth = 'var(--container-data)',
  className = '',
  as: Tag = 'div',
}) => {
  const style: React.CSSProperties = {
    width: '100%',
    maxWidth,
    marginLeft: 'auto',
    marginRight: 'auto',
    paddingLeft: 'var(--space-page-x)',
    paddingRight: 'var(--space-page-x)',
  };

  return (
    <Tag className={`sk-content-container ${className}`.trim()} style={style}>
      {children}
    </Tag>
  );
};

ContentContainer.displayName = 'ContentContainer';
export default ContentContainer;
