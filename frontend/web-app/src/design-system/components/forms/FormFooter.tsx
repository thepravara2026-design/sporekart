import React from 'react';

export interface FormFooterProps {
  children?: React.ReactNode;
  className?: string;
}

export const FormFooter: React.FC<FormFooterProps> = ({
  children,
  className = '',
}) => {
  const footerStyle: React.CSSProperties = {
    position: 'sticky',
    bottom: 0,
    background: 'var(--color-bg-surface-default)',
    borderTop: '1px solid var(--color-border-default)',
    padding: 'var(--space-stack-md) var(--space-0)',
    zIndex: 'var(--z-sticky)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    gap: 'var(--space-inline-md)',
    flexWrap: 'wrap',
  };

  return (
    <div
      className={`sk-form-footer ${className}`}
      style={footerStyle}
    >
      {children}
    </div>
  );
};

FormFooter.displayName = 'FormFooter';

export default FormFooter;
