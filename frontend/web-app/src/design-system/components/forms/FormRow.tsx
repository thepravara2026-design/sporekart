import React from 'react';

export interface FormRowProps {
  children: React.ReactNode;
  columns?: 1 | 2 | 3 | 'auto-fit';
  gap?: string;
  className?: string;
}

export const FormRow: React.FC<FormRowProps> = ({
  children,
  columns = 2,
  gap,
  className = '',
}) => {
  const resolvedGap = gap ?? 'var(--space-component-gap)';

  const gridTemplateColumns = columns === 'auto-fit'
    ? 'repeat(auto-fit, minmax(200px, 1fr))'
    : `repeat(${columns}, 1fr)`;

  const rowStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns,
    gap: resolvedGap,
  };

  return (
    <>
      <div
        className={`sk-form-row sk-form-row--${columns} ${className}`}
        style={rowStyle}
      >
        {children}
      </div>
      <style>{`
        @media (max-width: 640px) {
          .sk-form-row {
            grid-template-columns: 1fr !important;
          }
        }
      `}</style>
    </>
  );
};

FormRow.displayName = 'FormRow';

export default FormRow;
