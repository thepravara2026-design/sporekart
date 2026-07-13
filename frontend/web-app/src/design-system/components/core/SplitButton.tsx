import React, { forwardRef, useState, useRef, useEffect } from 'react';
import { Button } from './Button';

export interface SplitButtonProps {
  label: string;
  onClick?: () => void;
  menuItems: Array<{
    label: string;
    onClick: () => void;
    icon?: React.ReactNode;
    disabled?: boolean;
    danger?: boolean;
  }>;
  variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'destructive';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  loading?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  ariaLabel?: string;
}

export const SplitButton = forwardRef<HTMLButtonElement, SplitButtonProps>(
  ({
    label,
    onClick,
    menuItems = [],
    variant = 'primary',
    size = 'md',
    disabled = false,
    loading = false,
    leftIcon,
    rightIcon,
    ariaLabel = 'Actions',
    ...props
  }, _ref) => {
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const buttonRef = useRef<HTMLButtonElement>(null);
    useEffect(() => {
      const handleClickOutside = (event: MouseEvent) => {
        if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
          setIsOpen(false);
        }
      };

      if (isOpen) {
        document.addEventListener('mousedown', handleClickOutside);
      }

      return () => {
        document.removeEventListener('mousedown', handleClickOutside);
      };
    }, [isOpen]);

    const handleButtonClick = () => {
      if (disabled || loading) return;
      setIsOpen(!isOpen);
    };

    const handleItemClick = (item: any) => {
      if (item.disabled) return;
      item.onClick();
      setIsOpen(false);
    };

    const handleButtonKeyDown = (e: React.KeyboardEvent) => {
      if (e.key === 'ArrowDown' && !isOpen) {
        e.preventDefault();
        setIsOpen(true);
      } else if (e.key === 'Enter' || e.key === ' ') {
        e.preventDefault();
        setIsOpen(!isOpen);
      }
    };

    return (
      <div className="sk-split-btn" ref={dropdownRef}>
        <Button
          ref={buttonRef}
          variant={variant}
          size={size}
          disabled={disabled}
          loading={loading}
          leftIcon={leftIcon}
          rightIcon={<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={2} strokeLinecap="round" strokeLinejoin="round" width={16} height={16}><polyline points="6 9 12 15 18 9" /></svg>}
          onClick={handleButtonClick}
          onKeyDown={handleButtonKeyDown}
          aria-haspopup="true"
          aria-expanded={isOpen}
          aria-label={ariaLabel}
          {...props}
        >
          {label}
        </Button>

        {isOpen && (
          <div
            ref={dropdownRef}
            className="sk-split-btn__dropdown"
            role="menu"
            aria-label={ariaLabel}
          >
            {menuItems.map((item, index) => (
              <button
                key={index}
                role="menuitem"
                className={`sk-split-btn__menu-item ${item.disabled ? 'sk-disabled' : ''} ${item.danger ? 'sk-danger' : ''}`}
                onClick={() => handleItemClick(item)}
                disabled={item.disabled}
                tabIndex={-1}
              >
                {item.icon && <span className="sk-split-btn__menu-icon">{item.icon}</span>}
                <span>{item.label}</span>
              </button>
            ))}
          </div>
        )}
      </div>
    );
  }
);

SplitButton.displayName = 'SplitButton';

export default SplitButton;