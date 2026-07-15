import React from 'react';
import type { VariantSectionId } from '../types';
import { VARIANT_SECTION_LABELS, VARIANT_SECTION_ICONS } from '../types';

interface VariantNavProps {
  section: VariantSectionId;
  onSectionChange: (section: VariantSectionId) => void;
}

const sections: VariantSectionId[] = [
  'overview',
  'variants',
  'attributes',
  'sku',
  'packaging',
  'specifications',
  'configurations',
  'bulk-operations',
  'preview',
  'settings',
  'help',
];

const itemBase: React.CSSProperties = {
  display: 'flex', alignItems: 'center', gap: 10, padding: '8px 12px',
  borderRadius: 'var(--radius-sm)', cursor: 'pointer', fontSize: 'var(--text-body-sm)',
  color: 'var(--color-text-secondary)', border: 'none', background: 'none',
  width: '100%', textAlign: 'left', transition: 'background 0.15s, color 0.15s',
};

const activeItem: React.CSSProperties = { ...itemBase, background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-primary)', fontWeight: 600 };

const navStyle: React.CSSProperties = { display: 'flex', flexDirection: 'column', gap: 2, padding: 'var(--space-component-gap)' };

const IconMap: Record<string, string> = {
  dashboard: '📊', layers: '🧩', list: '📋', tag: '🏷️', box: '📦',
  'file-text': '📄', settings: '⚙️', multiple: '📑', eye: '👁️', gear: '🔧', help: '❓',
};

function NavIcon({ icon }: { icon: string }) {
  return <span style={{ width: 18, height: 18, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 16, opacity: 0.7 }} aria-hidden="true">{IconMap[icon] ?? '•'}</span>;
}

export const VariantNav: React.FC<VariantNavProps> = React.memo(({ section, onSectionChange }) => {
  return (
    <nav style={navStyle} aria-label="Variant sections">
      {sections.map((s) => (
        <button
          key={s}
          onClick={() => onSectionChange(s)}
          style={section === s ? activeItem : itemBase}
          aria-current={section === s ? 'page' : undefined}
          onMouseEnter={(e) => { if (section !== s) e.currentTarget.style.background = 'var(--color-bg-surface-raised)'; }}
          onMouseLeave={(e) => { if (section !== s) e.currentTarget.style.background = 'none'; }}
        >
          <NavIcon icon={VARIANT_SECTION_ICONS[s]} />
          {VARIANT_SECTION_LABELS[s]}
        </button>
      ))}
    </nav>
  );
});
