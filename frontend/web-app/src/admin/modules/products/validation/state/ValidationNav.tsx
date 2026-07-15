import React from 'react';
import type { ValidationSectionId } from '../types';
import { VALIDATION_SECTION_LABELS, VALIDATION_SECTION_ICONS } from '../types';

interface ValidationNavProps { section: ValidationSectionId; onSectionChange: (s: ValidationSectionId) => void }
const sections: ValidationSectionId[] = ['overview', 'validation', 'compliance', 'completeness', 'seo', 'media', 'pricing', 'variants', 'packaging', 'marketplace', 'accessibility', 'publishing', 'history', 'reports', 'settings', 'help'];
const base: React.CSSProperties = { display: 'flex', alignItems: 'center', gap: 10, padding: '8px 12px', borderRadius: 'var(--radius-sm)', cursor: 'pointer', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', border: 'none', background: 'none', width: '100%', textAlign: 'left', transition: 'background 0.15s, color 0.15s' };
const active: React.CSSProperties = { ...base, background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-primary)', fontWeight: 600 };
const Icons: Record<string, string> = { dashboard: '📊', 'check-circle': '✅', shield: '🛡️', percent: '📊', search: '🔍', image: '🖼️', dollar: '💰', layers: '📦', box: '📋', 'shopping-cart': '🛒', accessibility: '♿', send: '📤', clock: '🕐', 'file-text': '📄', gear: '⚙️', 'help-circle': '❓' };

export const ValidationNav: React.FC<ValidationNavProps> = React.memo(({ section, onSectionChange }) => (
  <nav style={{ display: 'flex', flexDirection: 'column', gap: 2, padding: 'var(--space-component-gap)' }} aria-label="Validation sections">
    {sections.map((s) => (
      <button key={s} onClick={() => onSectionChange(s)} style={section === s ? active : base}
        aria-current={section === s ? 'page' : undefined}
        onMouseEnter={(e) => { if (section !== s) e.currentTarget.style.background = 'var(--color-bg-surface-raised)'; }}
        onMouseLeave={(e) => { if (section !== s) e.currentTarget.style.background = 'none'; }}>
        <span style={{ width: 20, fontSize: 14, textAlign: 'center', opacity: 0.7 }} aria-hidden="true">{Icons[VALIDATION_SECTION_ICONS[s]] ?? '•'}</span>
        {VALIDATION_SECTION_LABELS[s]}
      </button>
    ))}
  </nav>
));
