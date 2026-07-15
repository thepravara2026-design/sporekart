import { memo, useCallback } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

interface Tab {
  id: string;
  label: string;
  href: string;
}

interface WorkspaceTabsProps {
  tabs: Tab[];
}

export const WorkspaceTabs = memo(function WorkspaceTabs({ tabs }: WorkspaceTabsProps) {
  const navigate = useNavigate();
  const location = useLocation();

  const handleKeyDown = useCallback((e: React.KeyboardEvent, index: number) => {
    let nextIndex = index;
    if (e.key === 'ArrowRight') nextIndex = Math.min(index + 1, tabs.length - 1);
    else if (e.key === 'ArrowLeft') nextIndex = Math.max(index - 1, 0);
    else return;
    e.preventDefault();
    const buttons = (e.currentTarget.parentNode as HTMLElement)?.querySelectorAll('[role="tab"]');
    (buttons?.[nextIndex] as HTMLElement)?.focus();
  }, [tabs.length]);

  return (
    <div role="tablist" aria-label="Workspace tabs" style={{ display: 'flex', gap: 0, borderBottom: '1px solid var(--color-border)', marginBottom: 16 }}>
      {tabs.map((tab, index) => {
        const active = location.pathname.startsWith(tab.href);
        const panelId = `tabpanel-${tab.id}`;
        return (
          <button
            key={tab.id}
            role="tab"
            id={`tab-${tab.id}`}
            aria-selected={active}
            aria-controls={panelId}
            onClick={() => navigate(tab.href)}
            onKeyDown={(e) => handleKeyDown(e, index)}
            style={{
              padding: '10px 20px',
              border: 'none',
              borderBottom: active ? '2px solid var(--color-primary)' : '2px solid transparent',
              background: 'transparent',
              color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontWeight: active ? 600 : 400,
              fontSize: 'var(--text-body)',
              cursor: 'pointer',
              marginBottom: -1,
            }}
          >
            {tab.label}
          </button>
        );
      })}
    </div>
  );
});
