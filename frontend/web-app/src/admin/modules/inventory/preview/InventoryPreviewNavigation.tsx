import type React from 'react';
import { memo } from 'react';
import { InventoryWorkspaceProvider } from '../contexts/InventoryWorkspaceContext';
import { InventoryWorkspaceLayout } from '../layouts/InventoryWorkspaceLayout';

function Frame({ label, width, children }: { label: string; width: number | string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{label}</span>
      <div
        style={{
          width,
          maxWidth: '100%',
          border: '1px solid var(--color-border)',
          borderRadius: 'var(--radius-lg)',
          overflow: 'hidden',
          background: 'var(--color-bg-surface-default)',
        }}
      >
        {children}
      </div>
    </div>
  );
}

export const InventoryPreviewNavigation = memo(function InventoryPreviewNavigation() {
  const child = <div style={{ padding: 24, color: 'var(--color-text-secondary)' }}>Section content preview</div>;
  return (
    <div style={{ padding: 16, display: 'flex', flexDirection: 'column', gap: 24 }}>
      <p style={{ margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
        The workspace layout renders a sidebar on desktop, and a collapsible top navigation on smaller viewports. Breadcrumbs, global search, notifications, and a profile control are shown in the header.
      </p>
      <Frame label="Desktop (≥1024px)" width="100%">
        <InventoryWorkspaceProvider>
          <InventoryWorkspaceLayout>{child}</InventoryWorkspaceLayout>
        </InventoryWorkspaceProvider>
      </Frame>
      <Frame label="Tablet (768–1023px)" width={820}>
        <InventoryWorkspaceProvider>
          <InventoryWorkspaceLayout>{child}</InventoryWorkspaceLayout>
        </InventoryWorkspaceProvider>
      </Frame>
      <Frame label="Mobile (320–767px)" width={390}>
        <InventoryWorkspaceProvider>
          <InventoryWorkspaceLayout>{child}</InventoryWorkspaceLayout>
        </InventoryWorkspaceProvider>
      </Frame>
    </div>
  );
});

export default InventoryPreviewNavigation;

