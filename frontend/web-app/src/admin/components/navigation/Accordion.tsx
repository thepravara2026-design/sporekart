import React, { useState, useCallback } from 'react';

export interface AccordionItem {
  id: string;
  title: string;
  content: React.ReactNode;
  icon?: React.ReactNode;
  disabled?: boolean;
}

export interface AccordionProps {
  items: AccordionItem[];
  allowMultiple?: boolean;
  defaultOpenIds?: string[];
  className?: string;
  style?: React.CSSProperties;
}

const itemStyle: React.CSSProperties = {
  borderBottom: '1px solid var(--color-border-default)',
};

const triggerStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 'var(--space-inline-sm)',
  width: '100%',
  padding: 'var(--space-stack-sm) var(--space-page-x)',
  border: 'none',
  background: 'transparent',
  color: 'var(--color-text-primary)',
  fontFamily: 'var(--font-family-sans)',
  fontSize: 'var(--text-body)',
  fontWeight: 'var(--weight-medium)',
  cursor: 'pointer',
  textAlign: 'left',
  transition: 'background var(--duration-fast) var(--easing-standard)',
};

const chevronStyle: React.CSSProperties = {
  marginLeft: 'auto',
  transition: 'transform var(--duration-normal) var(--easing-standard)',
  flexShrink: 0,
  fontSize: 12,
  color: 'var(--color-text-secondary)',
};

const contentStyle: React.CSSProperties = {
  overflow: 'hidden',
  transition: 'max-height var(--duration-normal) var(--easing-standard), opacity var(--duration-normal) var(--easing-standard)',
};

const innerStyle: React.CSSProperties = {
  padding: '0 var(--space-page-x) var(--space-stack-md)',
  fontSize: 'var(--text-body)',
  color: 'var(--color-text-secondary)',
};

export const Accordion: React.FC<AccordionProps> = ({
  items,
  allowMultiple = false,
  defaultOpenIds = [],
  className = '',
  style,
}) => {
  const [openIds, setOpenIds] = useState<string[]>(defaultOpenIds);

  const toggle = useCallback((id: string) => {
    setOpenIds((prev) => {
      if (prev.includes(id)) return prev.filter((i) => i !== id);
      if (allowMultiple) return [...prev, id];
      return [id];
    });
  }, [allowMultiple]);

  return (
    <div className={className} style={{ borderTop: '1px solid var(--color-border-default)', ...style }}>
      {items.map((item) => {
        const isOpen = openIds.includes(item.id);
        return (
          <div key={item.id} style={itemStyle}>
            <button
              type="button"
              style={triggerStyle}
              onClick={() => !item.disabled && toggle(item.id)}
              aria-expanded={isOpen}
              aria-controls={`accordion-${item.id}`}
              disabled={item.disabled}
              onMouseEnter={(e) => (e.currentTarget.style.background = 'var(--color-bg-background)')}
              onMouseLeave={(e) => (e.currentTarget.style.background = 'transparent')}
            >
              {item.icon && <span style={{ flexShrink: 0, display: 'inline-flex' }}>{item.icon}</span>}
              <span>{item.title}</span>
              <span style={{
                ...chevronStyle,
                transform: isOpen ? 'rotate(180deg)' : 'rotate(0deg)',
              }}>&#9660;</span>
            </button>
            <div
              id={`accordion-${item.id}`}
              role="region"
              style={{
                ...contentStyle,
                maxHeight: isOpen ? 1000 : 0,
                opacity: isOpen ? 1 : 0,
              }}
            >
              <div style={innerStyle}>{item.content}</div>
            </div>
          </div>
        );
      })}
    </div>
  );
};

Accordion.displayName = 'Accordion';
export default Accordion;
