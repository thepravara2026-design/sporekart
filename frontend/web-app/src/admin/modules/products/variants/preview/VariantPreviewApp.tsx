import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { VariantPage } from '../VariantPage';
import { VariantDashboard } from '../components/VariantDashboard';
import { VariantManager } from '../components/VariantManager';
import { AttributeManager } from '../components/AttributeManager';
import { SKUManager } from '../components/SKUManager';
import { PackagingManager } from '../components/PackagingManager';
import { BulkVariantOperations } from '../components/BulkVariantOperations';
import { useVariantState } from '../state/useVariantState';
import { useVariantAnalytics } from '../state/useVariantAnalytics';

const AnalyticsPreview: React.FC = () => <VariantDashboard />;

const VariantsPreview: React.FC = () => {
  const state = useVariantState();
  return <VariantManager variants={state.filteredVariants} selectedId={state.selectedId} onSelect={state.toggleSelect} />;
};

const AttrsPreview: React.FC = () => <AttributeManager definitions={[]} />;

const SKUPreview: React.FC = () => <SKUManager entries={[]} />;

const PkgPreview: React.FC = () => <PackagingManager packaging={[]} />;

const BulkPreview: React.FC = () => <BulkVariantOperations />;

const VariantInfo: React.FC = () => {
  const a = useVariantAnalytics();
  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 720 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Variant Module — Architecture
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        <AC title="Mock Mode">
          Everything operates in mock mode. 15 variants across 4 product groups, 16 attribute definitions, 17 SKU entries, 17 packaging configurations, and 4 specification sets.
        </AC>
        <AC title="Variant Architecture">
          Support for single and multi-variant products with parent-child relationships. 4 variant groups (Fresh Mushroom, Dried Shiitake, Oyster Spawn, Hydroponic Kit) with weight and skill-level based differentiation.
        </AC>
        <AC title="SKU Strategy">
          SKU entries with auto-generated, reserved, and duplicate detection. SKU pattern: `Category-Product-Variant`.
        </AC>
        <AC title="Analytics Summary">
          {a.totalVariants} variants · {a.variantGroups} groups · {a.totalAttributes} attributes · {a.totalPackaging} packaging configs · {a.primaryPackaging} primary · {a.masterCartons} master cartons
        </AC>
        <AC title="Integration Points">
          Future: Barcode (EAN-13/UPC/Code128) and QR generation APIs, inventory synchronization, warehouse integration, marketplace variant mapping, AI product classification.
        </AC>
        <AC title="Preview Routes">
          <code>/preview/products/variants</code> — Full workspace<br />
          <code>/preview/products/variants/analytics</code> — Dashboard<br />
          <code>/preview/products/variants/attributes</code> — Attributes<br />
          <code>/preview/products/variants/sku</code> — SKU<br />
          <code>/preview/products/variants/packaging</code> — Packaging<br />
          <code>/preview/products/variants/bulk</code> — Bulk operations
        </AC>
      </div>
    </div>
  );
};

const AC: React.FC<{ title: string; children: React.ReactNode }> = ({ title, children }) => (
  <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
    <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>{title}</h3>
    <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>{children}</div>
  </div>
);

export const VariantPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="workspace" replace />} />
      <Route path="workspace" element={<VariantPage />} />
      <Route path="analytics" element={<AnalyticsPreview />} />
      <Route path="variants" element={<VariantsPreview />} />
      <Route path="attributes" element={<AttrsPreview />} />
      <Route path="sku" element={<SKUPreview />} />
      <Route path="packaging" element={<PkgPreview />} />
      <Route path="bulk" element={<BulkPreview />} />
      <Route path="info" element={<VariantInfo />} />
    </Routes>
  );
};

export default VariantPreviewApp;
