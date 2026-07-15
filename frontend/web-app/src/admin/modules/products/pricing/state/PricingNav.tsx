import React from 'react';
import type { PricingSectionId } from '../types';
import { PRICING_SECTION_LABELS, PRICING_SECTION_ICONS } from '../types';

interface PricingNavProps {
  section: PricingSectionId;
  onSectionChange: (section: PricingSectionId) => void;
}

const sections: PricingSectionId[] = [
  'overview',
  'product-pricing',
  'discount-rules',
  'promotions',
  'gst',
  'hsn',
  'scheduled-pricing',
  'price-history',
  'bulk-pricing',
  'price-preview',
  'settings',
  'help',
];

const itemStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: 10,
  padding: '8px 12px',
  borderRadius: 'var(--radius-sm)',
  cursor: 'pointer',
  fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-secondary)',
  border: 'none',
  background: 'none',
  width: '100%',
  textAlign: 'left',
  transition: 'background 0.15s, color 0.15s',
};

const activeItemStyle: React.CSSProperties = {
  ...itemStyle,
  background: 'var(--color-bg-surface-raised)',
  color: 'var(--color-text-primary)',
  fontWeight: 600,
};

const navStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 2,
  padding: 'var(--space-component-gap)',
};

function NavIcon({ icon }: { icon: string }) {
  return (
    <span
      style={{
        width: 18,
        height: 18,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: 16,
        opacity: 0.7,
      }}
      aria-hidden="true"
    >
      {icon === 'dashboard' && '📊'}
      {icon === 'tag' && '🏷️'}
      {icon === 'percent' && '💯'}
      {icon === 'megaphone' && '📢'}
      {icon === 'receipt' && '🧾'}
      {icon === 'currency-rupee' && '💰'}
      {icon === 'code' && '📋'}
      {icon === 'calendar' && '📅'}
      {icon === 'clock' && '🕐'}
      {icon === 'multiple' && '📦'}
      {icon === 'eye' && '👁️'}
      {icon === 'settings' && '⚙️'}
      {icon === 'help' && '❓'}
    </span>
  );
}

export const PricingNav: React.FC<PricingNavProps> = React.memo(({ section, onSectionChange }) => {
  return (
    <nav style={navStyle} aria-label="Pricing sections">
      {sections.map((s) => (
        <button
          key={s}
          onClick={() => onSectionChange(s)}
          style={section === s ? activeItemStyle : itemStyle}
          aria-current={section === s ? 'page' : undefined}
          onMouseEnter={(e) => {
            if (section !== s) {
              e.currentTarget.style.background = 'var(--color-bg-surface-raised)';
            }
          }}
          onMouseLeave={(e) => {
            if (section !== s) {
              e.currentTarget.style.background = 'none';
            }
          }}
        >
          <NavIcon icon={PRICING_SECTION_ICONS[s]} />
          {PRICING_SECTION_LABELS[s]}
        </button>
      ))}
    </nav>
  );
});
