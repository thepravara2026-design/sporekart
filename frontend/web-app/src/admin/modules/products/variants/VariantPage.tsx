import React from 'react';
import { useVariantState } from './state/useVariantState';
import { VariantNav } from './state/VariantNav';
import { VariantToolbar } from './components/VariantToolbar';
import { VariantDashboard } from './components/VariantDashboard';
import { VariantManager } from './components/VariantManager';
import { VariantMatrix } from './components/VariantMatrix';
import { AttributeManager } from './components/AttributeManager';
import { SKUManager } from './components/SKUManager';
import { BarcodePlaceholder } from './components/BarcodePlaceholder';
import { PackagingManager } from './components/PackagingManager';
import { PackagingPreview } from './components/PackagingPreview';
import { SpecificationBuilder } from './components/SpecificationBuilder';
import { BulkVariantOperations } from './components/BulkVariantOperations';
import type { VariantSectionId } from './types';
import './Variant.css';

const outer: React.CSSProperties = { display: 'flex', height: '100%', minHeight: 'calc(100vh - 120px)', background: 'var(--color-bg-canvas)' };
const sidebar: React.CSSProperties = { width: 220, flexShrink: 0, borderRight: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', overflowY: 'auto' };
const content: React.CSSProperties = { flex: 1, display: 'flex', flexDirection: 'column', overflow: 'auto', minWidth: 0 };
const main: React.CSSProperties = { flex: 1, overflowY: 'auto' };

function SectionContent({ section }: { section: VariantSectionId }) {
  const s = useVariantState();

  switch (section) {
    case 'overview': return <VariantDashboard />;
    case 'variants': return <VariantManager variants={s.filteredVariants} selectedId={s.selectedId} onSelect={s.toggleSelect} />;
    case 'attributes': return <AttributeManager definitions={s.attributeDefinitions} />;
    case 'sku': return <SKUManager entries={s.skuEntries} />;
    case 'packaging': return <PackagingManager packaging={s.packaging} />;
    case 'specifications': return <SpecificationBuilder specifications={{}} selectedVariantId={null} />;
    case 'configurations': return <VariantMatrix variants={s.filteredVariants} selectedId={s.selectedId} onSelect={s.toggleSelect} />;
    case 'bulk-operations': return <BulkVariantOperations />;
    case 'preview': return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
        <BarcodePlaceholder barcodes={s.barcodes} />
        <PackagingPreview packaging={s.packaging} selectedId={s.selectedId} onSelect={s.toggleSelect} />
      </div>
    );
    case 'settings': return <VariantHelp />;
    case 'help': return <VariantHelp />;
    default: return <VariantDashboard />;
  }
}

const VariantHelp: React.FC = () => (
  <div style={{ padding: 'var(--space-component-gap)', maxWidth: 640 }}>
    <h2 style={{ margin: '0 0 16px', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Variant Module Help</h2>
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <HC title="Variants" desc="Manage product variants with different weights, sizes, and configurations. Each variant has its own SKU, price, and stock." />
      <HC title="Attributes" desc="Define attributes that differentiate variants. Supports text, number, select, boolean, dimension, and weight types." />
      <HC title="SKU Management" desc="Manual and auto-generated SKUs with prefix/suffix support, duplicate detection, and reserved SKU tracking." />
      <HC title="Packaging" desc="Configure primary packaging, master cartons, dimensions, weights, materials, and storage instructions." />
      <HC title="Specifications" desc="Build detailed product specifications organized by group: general, technical, agriculture, storage, usage, safety, compliance, and marketing." />
      <HC title="Barcode & QR" desc="Placeholder for future EAN-13, UPC, Code128, and QR code generation via external APIs." />
    </div>
  </div>
);

const HC: React.FC<{ title: string; desc: string }> = ({ title, desc }) => (
  <div style={{ padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
    <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 4 }}>{title}</div>
    <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>{desc}</div>
  </div>
);

export const VariantPage: React.FC = React.memo(() => {
  const state = useVariantState();

  return (
    <div className="variant-workspace" style={outer}>
      <aside className="variant-sidebar" style={sidebar}>
        <VariantNav section={state.section} onSectionChange={state.setSection} />
      </aside>
      <div className="variant-content" style={content}>
        {state.section !== 'overview' && state.section !== 'settings' && state.section !== 'help' && (
          <VariantToolbar
            search={state.search}
            onSearchChange={state.setSearch}
            sort={state.sort}
            onSortChange={state.setSort}
            activeFilterCount={state.activeFilterCount}
            onClearFilters={state.clearAllFilters}
          />
        )}
        <div className="variant-main" style={main}>
          <SectionContent section={state.section} />
        </div>
        <div style={{ padding: '12px var(--space-component-gap)', borderTop: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textAlign: 'center' }}>
          Variant Module v1.0 · Mock Mode · No data persistence
        </div>
      </div>
    </div>
  );
});

export default VariantPage;
