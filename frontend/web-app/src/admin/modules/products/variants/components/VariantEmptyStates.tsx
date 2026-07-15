import React from 'react';

const container: React.CSSProperties = {
  display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
  padding: '64px 24px', textAlign: 'center', gap: 12,
};

function EmptyState({ icon, title, desc }: { icon: string; title: string; desc: string }) {
  return (
    <div style={container}>
      <div style={{ fontSize: 48, opacity: 0.3 }} aria-hidden="true">{icon}</div>
      <div style={{ fontSize: 'var(--text-h4)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{title}</div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', maxWidth: 360 }}>{desc}</div>
    </div>
  );
}

export const NoVariants: React.FC = () => (
  <EmptyState icon="🧩" title="No Variants" desc="No product variants exist. Create variants to manage different product options like size, weight, and packaging." />
);
export const NoAttributes: React.FC = () => (
  <EmptyState icon="📋" title="No Attributes" desc="No attribute definitions created. Attributes define how your variants differ from each other." />
);
export const NoPackaging: React.FC = () => (
  <EmptyState icon="📦" title="No Packaging" desc="No packaging configurations found. Add packaging details for your products and variants." />
);
export const NoSpecifications: React.FC = () => (
  <EmptyState icon="📄" title="No Specifications" desc="No specifications have been defined. Specifications provide detailed product information." />
);
export const NoSearchResults: React.FC = () => (
  <EmptyState icon="🔍" title="No Results" desc="No variants match your search. Try adjusting your search terms or filters." />
);
export const NoSKU: React.FC = () => (
  <EmptyState icon="🏷️" title="No SKU Entries" desc="No SKU entries found. Generate SKUs for your variants to enable inventory tracking." />
);
export const PermissionDenied: React.FC = () => (
  <EmptyState icon="🔒" title="Access Denied" desc="You do not have permission to access this section. Contact your administrator." />
);
