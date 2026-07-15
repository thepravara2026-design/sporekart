import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { PricingPage } from '../PricingPage';
import { PricingDashboard } from '../components/PricingDashboard';
import { ProductPricingManager } from '../components/ProductPricingManager';
import { DiscountManager } from '../components/DiscountManager';
import { PromotionalPricing } from '../components/PromotionalPricing';
import { GSTManager } from '../components/GSTManager';
import { HSNManager } from '../components/HSNManager';
import { PricingHistory } from '../components/PricingHistory';
import { BulkPricing } from '../components/BulkPricing';
import { CommercialRulesPanel } from '../components/CommercialRulesPanel';
import { usePricingState } from '../state/usePricingState';
import { usePricingAnalytics } from '../state/usePricingAnalytics';
import { MOCK_COMMERCIAL_RULES } from '../mock/mockCommercialRules';

const AnalyticsPreview: React.FC = () => <PricingDashboard />;

const ProductPricingPreview: React.FC = () => {
  const state = usePricingState();
  return <ProductPricingManager entities={state.filteredPricing} selectedId={state.selectedId} onSelect={state.toggleSelect} />;
};

const DiscountPreview: React.FC = () => <DiscountManager discounts={[]} />;

const PromoPreview: React.FC = () => <PromotionalPricing campaigns={[]} />;

const GSTPreview: React.FC = () => <GSTManager entries={[]} />;

const HSNPreview: React.FC = () => <HSNManager entries={[]} />;

const HistoryPreview: React.FC = () => <PricingHistory history={[]} />;

const BulkPreview: React.FC = () => <BulkPricing />;

const RulesPreview: React.FC = () => <CommercialRulesPanel rules={MOCK_COMMERCIAL_RULES} />;

const PricingInfo: React.FC = () => {
  const analytics = usePricingAnalytics();
  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 720 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Pricing Module — Architecture
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        <ArchCard title="Mock Mode">
          This module operates entirely in mock mode. All 15 products, 10 discount rules, 7 GST entries, 11 HSN codes,
          8 promotional campaigns, and 7 price schedules are statically defined. Bulk operations simulate confirmation.
        </ArchCard>
        <ArchCard title="Pricing Architecture">
          9 price tiers (MRP, Selling, Wholesale, Distributor, Dealer, Retail, Farmer, Training, Bundle) with
          automatic discount calculation, GST computation, and final price determination. Supports INR with
          multi-currency framework ready.
        </ArchCard>
        <ArchCard title="Commercial Rules">
          10 commercial rules covering minimum price, maximum price, suggested price, margin, cost, and approval
          requirements across categories, brands, and products.
        </ArchCard>
        <ArchCard title="Analytics Summary">
          {analytics.totalProducts} products · {analytics.avgDiscount}% avg discount · {analytics.activeDiscounts} active discounts · {analytics.activeCampaigns} active campaigns · {analytics.totalRules} commercial rules
        </ArchCard>
        <ArchCard title="Integration Points">
          Future: Dynamic pricing engine, AI-assisted price recommendations, inventory-linked pricing,
          marketplace price sync, multi-currency support, real-time GST validation, tax filing integration.
        </ArchCard>
        <ArchCard title="Preview Routes">
          <code>/preview/products/pricing</code> — Full workspace<br />
          <code>/preview/products/pricing/analytics</code> — Pricing dashboard<br />
          <code>/preview/products/pricing/rules</code> — Commercial rules<br />
          <code>/preview/products/pricing/history</code> — Price history<br />
          <code>/preview/products/pricing/bulk</code> — Bulk operations
        </ArchCard>
      </div>
    </div>
  );
};

const ArchCard: React.FC<{ title: string; children: React.ReactNode }> = ({ title, children }) => (
  <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
    <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>{title}</h3>
    <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>{children}</div>
  </div>
);

export const PricingPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="workspace" replace />} />
      <Route path="workspace" element={<PricingPage />} />
      <Route path="analytics" element={<AnalyticsPreview />} />
      <Route path="products" element={<ProductPricingPreview />} />
      <Route path="discounts" element={<DiscountPreview />} />
      <Route path="promotions" element={<PromoPreview />} />
      <Route path="gst" element={<GSTPreview />} />
      <Route path="hsn" element={<HSNPreview />} />
      <Route path="history" element={<HistoryPreview />} />
      <Route path="rules" element={<RulesPreview />} />
      <Route path="bulk" element={<BulkPreview />} />
      <Route path="info" element={<PricingInfo />} />
    </Routes>
  );
};

export default PricingPreviewApp;
