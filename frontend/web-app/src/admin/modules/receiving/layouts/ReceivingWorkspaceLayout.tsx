import { memo, useState } from 'react';
import { WORKSPACE_SECTIONS } from '../constants';

interface ReceivingWorkspaceLayoutProps {
  activeSection: string;
  onSectionChange: (section: string) => void;
  searchQuery: string;
  onSearchChange: (query: string) => void;
  children: React.ReactNode;
}

export const ReceivingWorkspaceLayout = memo(function ReceivingWorkspaceLayout({ activeSection, onSectionChange, searchQuery, onSearchChange, children }: ReceivingWorkspaceLayoutProps) {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100%', width: '100%', background: 'var(--color-bg)' }}>
      <header style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 20px', background: 'var(--color-surface)', borderBottom: '1px solid var(--color-border)', justifyContent: 'space-between', flexWrap: 'wrap' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
          <button onClick={() => setSidebarOpen((o) => !o)} aria-label="Toggle sidebar"
            style={{ padding: 6, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer' }}>
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none" aria-hidden={true}><path d="M3 5h12M3 9h12M3 13h12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/></svg>
          </button>
          <h2 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Purchase Receiving</h2>
        </div>
        <div style={{ position: 'relative', flex: 1, maxWidth: 360 }}>
          <input value={searchQuery} onChange={(e) => onSearchChange(e.target.value)}
            placeholder="Search by receipt, supplier, product, warehouse..."
            aria-label="Search receipts"
            style={{ width: '100%', padding: '8px 12px 8px 36px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-bg)', fontSize: 'var(--text-body)', outline: 'none', boxSizing: 'border-box' }} />
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none" aria-hidden={true} style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', color: 'var(--color-text-tertiary)' }}>
            <circle cx="7" cy="7" r="5" stroke="currentColor" strokeWidth="1.5"/><path d="M11 11l3.5 3.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
          </svg>
        </div>
        <div style={{ display: 'flex', gap: 6, alignItems: 'center' }}>
          <button style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Filters</button>
          <button style={{ padding: '6px 14px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)', background: 'var(--color-surface)', cursor: 'pointer', fontSize: 'var(--text-caption)' }}>Export</button>
          <button style={{ padding: '6px 14px', border: 'none', borderRadius: 'var(--radius-sm)', background: 'var(--color-primary)', color: '#fff', cursor: 'pointer', fontSize: 'var(--text-caption)', fontWeight: 600 }}>+ New Receipt</button>
        </div>
      </header>
      <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
        <aside style={{ width: 220, flexShrink: 0, background: 'var(--color-surface)', borderRight: '1px solid var(--color-border)', overflowY: 'auto', padding: '12px 0' }}>
          <nav aria-label="Receiving workspace sections" style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            {WORKSPACE_SECTIONS.map((section: { id: string; label: string; icon: string }) => (
              <button key={section.id} onClick={() => onSectionChange(section.id)}
                style={{ display: 'flex', alignItems: 'center', gap: 10, padding: '10px 20px', border: 'none', background: activeSection === section.id ? 'var(--color-primary-alpha)' : 'transparent', color: activeSection === section.id ? 'var(--color-primary)' : 'var(--color-text-secondary)', cursor: 'pointer', fontSize: 'var(--text-body)', fontWeight: activeSection === section.id ? 600 : 400, textAlign: 'left', width: '100%' }}>
                <span style={{ fontSize: 16, width: 20, textAlign: 'center' }}>{section.icon}</span>
                <span>{section.label}</span>
              </button>
            ))}
          </nav>
        </aside>
        <main style={{ flex: 1, overflow: 'auto', padding: 24, minHeight: 0 }}>
          {children}
        </main>
      </div>
      {sidebarOpen && (
        <div onClick={() => setSidebarOpen(false)} style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.4)', zIndex: 100 }} />
      )}
    </div>
  );
});
