import React from 'react';

export interface FormLayoutProps {
  children: React.ReactNode;
  columns?: 1 | 2;
  maxWidth?: string;
  className?: string;
  style?: React.CSSProperties;
  onSubmit?: (e: React.FormEvent) => void;
  noValidate?: boolean;
  id?: string;
  'aria-label'?: string;
}

export const FormLayout: React.FC<FormLayoutProps> = ({
  children,
  columns = 1,
  maxWidth,
  className = '',
  style,
  onSubmit,
  noValidate = false,
  id,
  'aria-label': ariaLabel,
}) => {
  const resolvedMaxWidth = maxWidth ?? 'var(--container-data)';

  const baseStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-component-gap)',
    width: '100%',
    maxWidth: resolvedMaxWidth,
    marginInline: 'auto',
    containerType: 'inline-size',
    ...style,
  };

  const gridStyle: React.CSSProperties = columns === 2
    ? {
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: 'var(--space-component-gap)',
      }
    : {
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-component-gap)',
      };

  return (
    <>
      <form
        id={id}
        className={`sk-form-layout ${className}`}
        style={baseStyle}
        onSubmit={onSubmit}
        noValidate={noValidate}
        aria-label={ariaLabel}
      >
        <div className="sk-form-layout__grid" style={gridStyle}>
          {children}
        </div>
      </form>
      <style>{`
        @container (max-width: 768px) {
          .sk-form-layout__grid {
            grid-template-columns: 1fr !important;
          }
        }
      `}</style>
    </>
  );
};

FormLayout.displayName = 'FormLayout';

export default FormLayout;
