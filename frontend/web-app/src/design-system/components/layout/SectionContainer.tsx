import React from 'react';

export interface SectionContainerProps {
  children: React.ReactNode;
  title?: string;
  description?: string;
  className?: string;
  as?: 'section' | 'div' | 'article';
}

export const SectionContainer: React.FC<SectionContainerProps> = ({
  children,
  title,
  description,
  className = '',
  as: Tag = 'section',
}) => {
  const style: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-md)',
  };

  const titleStyle: React.CSSProperties = {
    font: 'var(--text-h3)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-primary)',
    margin: 0,
  };

  const descStyle: React.CSSProperties = {
    font: 'var(--text-body)',
    color: 'var(--color-text-secondary)',
    margin: 0,
  };

  return (
    <Tag className={`sk-section-container ${className}`.trim()} style={style}>
      {title && <h3 className="sk-section-container__title" style={titleStyle}>{title}</h3>}
      {description && <p className="sk-section-container__desc" style={descStyle}>{description}</p>}
      {children}
    </Tag>
  );
};

SectionContainer.displayName = 'SectionContainer';
export default SectionContainer;
