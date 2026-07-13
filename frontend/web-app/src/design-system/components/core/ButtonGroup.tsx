import React, { forwardRef } from 'react';

export interface ButtonGroupProps {
  children: React.ReactNode;
  ariaLabel?: string;
  orientation?: 'horizontal' | 'vertical';
  className?: string;
}

export const ButtonGroup = forwardRef<HTMLDivElement, ButtonGroupProps>(
  ({ children, ariaLabel = 'Button group', orientation = 'horizontal', className = '' }, ref) => {

    const containerStyles = `
      display: inline-flex;
      ${orientation === 'vertical' ? 'flex-direction: column;' : ''}
      gap: var(--space-inline-sm);
      ${className}
    `;

    return (
      <div
        ref={ref}
        role="group"
        aria-label={ariaLabel}
        className="sk-btn-group"
        style={containerStyles as React.CSSProperties}
      >
        {React.Children.map(children, (child) => {
          if (!React.isValidElement(child)) return child;
          return React.cloneElement(child as React.ReactElement<any>, {
            className: (child.props.className || '') + ' sk-btn-group__item',
          });
        })}
      </div>
    );
  }
);

ButtonGroup.displayName = 'ButtonGroup';

export default ButtonGroup;