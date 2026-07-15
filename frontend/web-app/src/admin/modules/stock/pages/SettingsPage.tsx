import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';
import { STOCK_SETTINGS_SECTIONS } from '../constants';

export const SettingsPage = memo(function SettingsPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Settings" description="Configure stock management preferences — state behaviour, health thresholds, reservation rules and availability calculation." />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {STOCK_SETTINGS_SECTIONS.map((s) => (
          <div key={s.id} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 12, opacity: s.placeholder ? 0.5 : 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-primary-alpha)', color: 'var(--color-primary)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Icon name={s.icon} size={18} />
              </div>
              <h4 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{s.label}</h4>
            </div>
            <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{s.description}</p>
            {s.placeholder && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', background: 'var(--color-surface-hover)', padding: '4px 10px', borderRadius: 'var(--radius-badge)', alignSelf: 'flex-start' }}>Placeholder</span>}
          </div>
        ))}
      </div>
    </div>
  );
});
