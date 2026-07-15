import React from 'react';
import type { SeoSectionId } from '../types';
import { SEO_SECTION_LABELS, SEO_SECTION_ICONS } from '../types';

interface SeoNavProps { section: SeoSectionId; onSectionChange: (s: SeoSectionId) => void }
const sections: SeoSectionId[] = ['overview', 'seo', 'meta', 'structured-data', 'urls', 'publishing', 'marketplace', 'search-preview', 'social', 'ai-readiness', 'validation', 'history', 'settings', 'help'];
const base: React.CSSProperties = { display: 'flex', alignItems: 'center', gap: 10, padding: '8px 12px', borderRadius: 'var(--radius-sm)', cursor: 'pointer', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', border: 'none', background: 'none', width: '100%', textAlign: 'left', transition: 'background 0.15s, color 0.15s' };
const active: React.CSSProperties = { ...base, background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-primary)', fontWeight: 600 };
const Icons: Record<string, string> = { dashboard: '📊', search: '🔍', 'file-text': '📄', code: '</>', link: '🔗', send: '📤', 'shopping-cart': '🛒', eye: '👁️', share: '🔗', cpu: '🤖', 'check-circle': '✅', clock: '🕐', gear: '⚙️', 'help-circle': '❓' };

export const SeoNav: React.FC<SeoNavProps> = React.memo(({ section, onSectionChange }) => (
  <nav style={{ display: 'flex', flexDirection: 'column', gap: 2, padding: 'var(--space-component-gap)' }} aria-label="SEO sections">
    {sections.map((s) => (
      <button key={s} onClick={() => onSectionChange(s)} style={section === s ? active : base}
        aria-current={section === s ? 'page' : undefined}
        onMouseEnter={(e) => { if (section !== s) e.currentTarget.style.background = 'var(--color-bg-surface-raised)'; }}
        onMouseLeave={(e) => { if (section !== s) e.currentTarget.style.background = 'none'; }}>
        <span style={{ width: 20, fontSize: 14, textAlign: 'center', opacity: 0.7 }} aria-hidden="true">{Icons[SEO_SECTION_ICONS[s]] ?? '•'}</span>
        {SEO_SECTION_LABELS[s]}
      </button>
    ))}
  </nav>
));
