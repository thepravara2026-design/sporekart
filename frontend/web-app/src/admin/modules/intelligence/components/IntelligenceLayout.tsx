import { memo } from 'react';
import { useIntelligenceWorkspace } from '../contexts/IntelligenceWorkspaceContext';
import { sections } from '../constants';

export const IntelligenceLayout = memo(function IntelligenceLayout({ children }: { children: React.ReactNode }) {
  const { activeSection, setActiveSection } = useIntelligenceWorkspace();
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 0, height: '100%' }}>
      <div style={{ display: 'flex', gap: 0, borderBottom: '1px solid var(--color-border)', overflowX: 'auto', background: 'var(--color-surface)' }}>
        {sections.map((s) => (
          <button key={s.id} onClick={() => setActiveSection(s.id)}
            style={{ padding: '10px 16px', border: 'none', background: activeSection === s.id ? 'var(--color-primary-alpha, rgba(99,102,241,0.1))' : 'transparent', color: activeSection === s.id ? 'var(--color-primary)' : 'var(--color-text-secondary)', fontWeight: activeSection === s.id ? 600 : 400, cursor: 'pointer', whiteSpace: 'nowrap', fontSize: 'var(--text-body)', borderBottom: activeSection === s.id ? '2px solid var(--color-primary)' : '2px solid transparent', transition: 'all 0.15s' }}>
            {s.label}
          </button>
        ))}
      </div>
      <div style={{ flex: 1, overflow: 'auto', padding: 20 }}>
        {children}
      </div>
    </div>
  );
});
