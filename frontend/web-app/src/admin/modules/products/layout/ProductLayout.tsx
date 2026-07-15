import React from 'react';
import { Breadcrumb, type Crumb } from '../../../../design-system/components/navigation/Breadcrumb';
import { StatusBadge } from '../../../components/status';

interface ProductLayoutProps {
  title: string;
  breadcrumbs: Crumb[];
  actions?: React.ReactNode;
  children: React.ReactNode;
}

export const ProductLayout = React.memo(function ProductLayout({ title, breadcrumbs, actions, children }: ProductLayoutProps) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', minHeight: '100%' }}>
      <header style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-component-gap)', flexWrap: 'wrap' }}>
          <h1 style={{ fontSize: 'var(--text-h1)', color: 'var(--color-text-primary)', margin: 0, fontWeight: 'var(--weight-bold)' }}>{title}</h1>
          {actions && <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>{actions}</div>}
        </div>
        <Breadcrumb crumbs={breadcrumbs} />
      </header>

      <main style={{ flex: 1, minWidth: 0 }}>{children}</main>

      <footer
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: 'var(--space-inline-xs)',
          paddingTop: 'var(--space-component-gap)',
          borderTop: '1px solid var(--color-border)',
        }}
      >
        <StatusBadge status="Mock Mode" variant="info" size="sm" />
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>SporeKart Product Management · Foundation Layer</span>
      </footer>
    </div>
  );
});

export default ProductLayout;
