import React from 'react';
import { usePricingState } from './state/usePricingState';
import { PricingNav } from './state/PricingNav';
import { PricingToolbar } from './components/PricingToolbar';
import { PricingDashboard } from './components/PricingDashboard';
import { ProductPricingManager } from './components/ProductPricingManager';
import { DiscountManager } from './components/DiscountManager';
import { PromotionalPricing } from './components/PromotionalPricing';
import { TaxManager } from './components/TaxManager';
import { GSTManager } from './components/GSTManager';
import { HSNManager } from './components/HSNManager';
import { CurrencySettings } from './components/CurrencySettings';
import { PricePreview } from './components/PricePreview';
import { PricingHistory } from './components/PricingHistory';
import { ScheduledPricing } from './components/ScheduledPricing';
import { BulkPricing } from './components/BulkPricing';
import type { PricingSectionId } from './types';
import './Pricing.css';

const workspaceOuter: React.CSSProperties = {
  display: 'flex',
  height: '100%',
  minHeight: 'calc(100vh - 120px)',
  background: 'var(--color-bg-canvas)',
};

const sidebarStyle: React.CSSProperties = {
  width: 220,
  flexShrink: 0,
  borderRight: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  overflowY: 'auto',
};

const contentArea: React.CSSProperties = {
  flex: 1,
  display: 'flex',
  flexDirection: 'column',
  overflow: 'auto',
  minWidth: 0,
};

const mainContent: React.CSSProperties = {
  flex: 1,
  overflowY: 'auto',
};

const footerStyle: React.CSSProperties = {
  padding: '12px var(--space-component-gap)',
  borderTop: '1px solid var(--color-border)',
  fontSize: 'var(--text-body-xs)',
  color: 'var(--color-text-tertiary)',
  textAlign: 'center',
};

function SectionContent({ section }: { section: PricingSectionId }) {
  const state = usePricingState();

  switch (section) {
    case 'overview':
      return <PricingDashboard />;
    case 'product-pricing':
      return (
        <ProductPricingManager
          entities={state.filteredPricing}
          selectedId={state.selectedId}
          onSelect={state.toggleSelect}
        />
      );
    case 'discount-rules':
      return <DiscountManager discounts={state.discountRules} />;
    case 'promotions':
      return <PromotionalPricing campaigns={state.campaigns} />;
    case 'tax-rules':
      return <TaxManager rules={state.taxRules} />;
    case 'gst':
      return <GSTManager entries={state.gstEntries} />;
    case 'hsn':
      return <HSNManager entries={state.hsnEntries} />;
    case 'scheduled-pricing':
      return <ScheduledPricing schedules={state.schedules} />;
    case 'price-history':
      return <PricingHistory history={state.priceHistory} />;
    case 'bulk-pricing':
      return <BulkPricing />;
    case 'price-preview':
      return <PricePreview entity={state.selectedPricing} />;
    case 'settings':
      return <CurrencySettings currencies={state.currencies} />;
    case 'help':
      return <PricingHelp />;
    default:
      return <PricingDashboard />;
  }
}

const PricingHelp: React.FC = () => (
  <div style={{ padding: 'var(--space-component-gap)', maxWidth: 640 }}>
    <h2 style={{ margin: '0 0 16px', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
      Pricing Module Help
    </h2>
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <HelpCard title="Product Pricing" desc="Manage MRP, selling, wholesale, distributor, dealer, retail, farmer, training and bundle prices for all products." />
      <HelpCard title="Discount Rules" desc="Create percentage, fixed, volume, wholesale and coupon-based discount rules with conditions, stackable settings, and priority ordering." />
      <HelpCard title="GST Management" desc="Configure GST rates with CGST/SGST/IGST split. Monitor effective tax rates across product categories." />
      <HelpCard title="HSN Codes" desc="Manage HSN/SAC code classification for all products. Essential for tax compliance and government reporting." />
      <HelpCard title="Scheduled Pricing" desc="Schedule future price changes, campaigns, seasonal pricing, flash sales, and promotional periods." />
      <HelpCard title="Bulk Operations" desc="Perform bulk price updates, discount applications, GST/HSN changes, and workflow actions across multiple products." />
      <HelpCard title="Price Preview" desc="Preview how prices appear to customers, administrators, and marketplace channels with full GST and discount breakdown." />
    </div>
  </div>
);

const HelpCard: React.FC<{ title: string; desc: string }> = ({ title, desc }) => (
  <div style={{ padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
    <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 4 }}>{title}</div>
    <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>{desc}</div>
  </div>
);

export const PricingPage: React.FC = React.memo(() => {
  const state = usePricingState();

  return (
    <div className="pricing-workspace" style={workspaceOuter}>
      <aside className="pricing-sidebar" style={sidebarStyle}>
        <PricingNav section={state.section} onSectionChange={state.setSection} />
      </aside>
      <div className="pricing-content" style={contentArea}>
        {state.section !== 'overview' && state.section !== 'settings' && state.section !== 'help' && (
          <div style={{ padding: 'var(--space-component-gap) 0 0', borderBottom: '1px solid var(--color-border)' }}>
            <PricingToolbar
              search={state.search}
              onSearchChange={state.setSearch}
              sort={state.sort}
              onSortChange={state.setSort}
              activeFilterCount={state.activeFilterCount}
              onClearFilters={state.clearAllFilters}
            />
          </div>
        )}
        <div className="pricing-main" style={mainContent}>
          <SectionContent section={state.section} />
        </div>
        <div style={footerStyle}>
          Pricing Module v1.0 · Mock Mode · No data persistence
        </div>
      </div>
    </div>
  );
});

export default PricingPage;
