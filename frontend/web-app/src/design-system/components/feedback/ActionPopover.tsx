import React from 'react';
import { Popover, PopoverProps } from './Popover';

export interface ActionPopoverItem {
  label: string;
  onClick: () => void;
  icon?: React.ReactNode;
  disabled?: boolean;
  destructive?: boolean;
}

export interface ActionPopoverProps extends PopoverProps {
  items: ActionPopoverItem[];
}

export const ActionPopover: React.FC<ActionPopoverProps> = ({
  items,
  style,
  ...props
}) => {
  const listStyle: React.CSSProperties = {
    listStyle: 'none',
    margin: 0,
    padding: 'var(--space-1)',
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-inline-xs)',
  };

  return (
    <Popover
      style={style}
      {...props}
    >
      <ul style={listStyle} role="menu">
        {items.map((item, index) => {
          const buttonStyle: React.CSSProperties = {
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-inline-sm)',
            width: '100%',
            padding: 'var(--space-1) var(--space-inline-sm)',
            fontSize: 'var(--text-body-sm)',
            lineHeight: 'var(--leading-normal)',
            color: item.destructive ? 'var(--color-text-danger)' : 'var(--color-text-primary)',
            background: 'transparent',
            border: 'none',
            borderRadius: 'var(--radius-xs)',
            cursor: item.disabled ? 'not-allowed' : 'pointer',
            opacity: item.disabled ? 'var(--opacity-disabled)' : 1,
            textAlign: 'left',
            fontWeight: 'var(--weight-medium)',
          };

          const itemIconStyle: React.CSSProperties = {
            width: 'var(--icon-xs)',
            height: 'var(--icon-xs)',
            flexShrink: 0,
            color: item.destructive ? 'var(--color-icon-danger)' : 'var(--color-icon-default)',
          };

          return (
            <li key={index} role="none">
              <button
                type="button"
                role="menuitem"
                style={buttonStyle}
                onClick={item.disabled ? undefined : item.onClick}
                disabled={item.disabled}
              >
                {item.icon && <span style={itemIconStyle}>{item.icon}</span>}
                <span>{item.label}</span>
              </button>
            </li>
          );
        })}
      </ul>
    </Popover>
  );
};

ActionPopover.displayName = 'ActionPopover';
export default ActionPopover;
