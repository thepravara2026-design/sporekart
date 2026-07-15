import React from 'react';
import type { AnalyticsSectionId } from '../types';
import { ANALYTICS_SECTION_LABELS, ANALYTICS_SECTION_ICONS } from '../types';

interface AnalyticsNavProps { section: AnalyticsSectionId; onSectionChange: (s: AnalyticsSectionId) => void }
const sections: AnalyticsSectionId[] = ['overview', 'catalog', 'products', 'categories', 'brands', 'variants', 'pricing', 'seo', 'marketplace', 'publishing', 'validation', 'compliance', 'reports', 'insights', 'settings', 'help'];
const base: React.CSSProperties = { display: 'flex', alignItems: 'center', gap: 10, padding: '8px 12px', borderRadius: 'var(--radius-sm)', cursor: 'pointer', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', border: 'none', background: 'none', width: '100%', textAlign: 'left', transition: 'background 0.15s, color 0.15s' };
const active: React.CSSProperties = { ...base, background: 'var(--color-bg-surface-raised)', color: 'var(--color-text-primary)', fontWeight: 600 };
const Icons: Record<string, string> = { dashboard: '📊', database: '🗄️', package: '📦', folder: '📁', tag: '🏷️', layers: '🧩', dollar: '💰', search: '🔍', 'shopping-cart': '🛒', send: '📤', 'check-circle': '✅', shield: '🛡️', 'file-text': '📄', lightbulb: '💡', gear: '⚙️', 'help-circle': '❓' };

export const AnalyticsNav: React.FC<AnalyticsNavProps> = React.memo(({ section, onSectionChange }) => (
  <nav style={{ display: 'flex', flexDirection: 'column', gap: 2, padding: 'var(--space-component-gap)' }} aria-label="Analytics sections">
    {sections.map((s) => (
      <button key={s} onClick={() => onSectionChange(s)} style={section === s ? active : base}
        aria-current={section === s ? 'page' : undefined}
        onMouseEnter={(e) => { if (section !== s) e.currentTarget.style.background = 'var(--color-bg-surface-raised)'; }}
        onMouseLeave={(e) => { if (section !== s) e.currentTarget.style.background = 'none'; }}>
        <span style={{ width: 20, fontSize: 14, textAlign: 'center', opacity: 0.7 }} aria-hidden="true">{Icons[ANALYTICS_SECTION_ICONS[s]] ?? '•'}</span>
        {ANALYTICS_SECTION_LABELS[s]}
      </button>
    ))}
  </nav>
));
