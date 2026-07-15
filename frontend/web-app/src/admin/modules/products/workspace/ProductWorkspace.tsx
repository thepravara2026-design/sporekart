import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { type Crumb } from '../../../../design-system/components/navigation/Breadcrumb';
import { ProductLayout } from '../layout/ProductLayout';
import { ProductWorkspaceNav, PRODUCT_WORKSPACE_SECTIONS } from '../components/ProductWorkspaceNav';

interface ProductWorkspaceProps {
  children: React.ReactNode;
  activeSection: string;
}

export const ProductWorkspace = React.memo(function ProductWorkspace({ children, activeSection }: ProductWorkspaceProps) {
  const [section, setSection] = React.useState<string>(activeSection);
  const current = PRODUCT_WORKSPACE_SECTIONS.find((s) => s.id === section) ?? PRODUCT_WORKSPACE_SECTIONS[0];

  const breadcrumbs: Crumb[] = [
    { label: 'Admin', href: '/' },
    { label: 'Products', href: '/products' },
    { label: current.label, href: `/products/${section}` },
  ];

  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'minmax(200px, 240px) 1fr', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
      <aside style={{ position: 'sticky', top: 'var(--space-component-gap)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <ProductWorkspaceNav activeSection={section} onSelect={setSection} />
      </aside>
      <Card variant="ghost" padding="none" style={{ background: 'transparent', border: 'none' }}>
        <ProductLayout title={current.label} breadcrumbs={breadcrumbs} actions={undefined}>
          {children}
        </ProductLayout>
      </Card>
    </div>
  );
});

export default ProductWorkspace;
