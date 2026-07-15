import { memo, useState } from 'react';
import { SectionHeader } from '../../inventory/components';
import { BATCH_SETTINGS_SECTIONS } from '../constants';

export const SettingsPage = memo(function SettingsPage() {
  const [activeTab, setActiveTab] = useState('general');

  const section = BATCH_SETTINGS_SECTIONS.find((s) => s.id === activeTab) ?? BATCH_SETTINGS_SECTIONS[0];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap, 16px)' }}>
      <SectionHeader title="Settings" description="Batch preferences and configuration." />
      <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap', borderBottom: '1px solid var(--color-border)', paddingBottom: 8 }}>
        {BATCH_SETTINGS_SECTIONS.map((s) => (
          <button key={s.id} onClick={() => setActiveTab(s.id)} style={{ padding: '8px 16px', borderRadius: 'var(--radius-md)', border: 'none', background: activeTab === s.id ? 'var(--color-primary)' : 'transparent', color: activeTab === s.id ? '#fff' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: activeTab === s.id ? 600 : 400 }}>{s.label}</button>
        ))}
      </div>
      <div style={{ background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)', padding: 24 }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)' }}>{section.label}</h3>
        <p style={{ margin: '0 0 16px', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>{section.description}</p>
        <div style={{ padding: 16, background: 'var(--color-surface-hover)', borderRadius: 'var(--radius-md)', fontSize: 'var(--text-body)', color: 'var(--color-text-tertiary)', textAlign: 'center' }}>
          Settings configuration UI — Mock Mode. Extensible in Sprint 25 Part 6.
        </div>
      </div>
    </div>
  );
});

