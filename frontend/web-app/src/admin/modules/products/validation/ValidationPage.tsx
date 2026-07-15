import React from 'react';
import { PermissionGate } from '../../../permissions/PermissionGate';
import { FeatureFlagProvider } from '../../../feature-flags/FeatureFlagProvider';
import { PermissionProvider } from '../../../permissions/PermissionProvider';
import { useValidationState } from './state/useValidationState';
import { ValidationNav } from './state/ValidationNav';
import { ValidationToolbar } from './components/ValidationToolbar';
import { ValidationDashboard } from './components/ValidationDashboard';
import { ProductCompleteness } from './components/ProductCompleteness';
import { QualityAssurance } from './components/QualityAssurance';
import { AiReadinessValidator } from './components/AiReadinessValidator';
import { ComplianceChecker } from './components/ComplianceChecker';
import { MediaValidator } from './components/MediaValidator';
import { SeoValidator } from './components/SeoValidator';
import { PricingValidator } from './components/PricingValidator';
import { VariantValidator } from './components/VariantValidator';
import { PackagingValidator } from './components/PackagingValidator';
import { MarketplaceValidator } from './components/MarketplaceValidator';
import { AccessibilityValidator } from './components/AccessibilityValidator';
import { PublishingReadiness } from './components/PublishingReadiness';
import { CertificationDashboard } from './components/CertificationDashboard';
import { ProductHealthDashboard } from './components/ProductHealthDashboard';
import { ValidationReports } from './components/ValidationReports';
import { CURRENT_VALIDATION_ROLE } from './types';
import './Validation.css';

function ValidationHistory({ activity }: { activity: Array<{ id: string; type: string; message: string; productName: string; user: string; timestamp: string; icon: string }> }) {
  const ev: React.CSSProperties = { display: 'flex', gap: 12, padding: '10px 16px', borderBottom: '1px solid var(--color-border)', alignItems: 'center', fontSize: 'var(--text-body-xs)' };
  const dot: Record<string, string> = { 'check-circle': 'var(--color-accent-green)', award: 'var(--color-accent-gold)', shield: 'var(--color-accent-blue)', 'thumbs-up': 'var(--color-accent-green)', 'x-circle': 'var(--color-accent-red)', 'file-text': 'var(--color-accent-purple)', layers: 'var(--color-accent-orange)', search: 'var(--color-accent-blue)', 'shopping-cart': 'var(--color-accent-yellow)' };
  return (
    <div style={{ padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Validation History</h2>
      <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        {activity.slice(0, 20).map((a) => (
          <div key={a.id} style={ev}>
            <span style={{ width: 10, height: 10, borderRadius: '50%', background: dot[a.icon] || 'var(--color-text-tertiary)', flexShrink: 0 }} />
            <div style={{ flex: 1 }}><span style={{ fontWeight: 500 }}>{a.productName}</span><span style={{ color: 'var(--color-text-tertiary)', marginLeft: 4 }}>{a.message}</span></div>
            <span style={{ color: 'var(--color-text-tertiary)' }}>{a.user}</span>
            <span style={{ color: 'var(--color-text-tertiary)', fontSize: 11 }}>{new Date(a.timestamp).toLocaleDateString()}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

function ValidationSettings() {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h3 style={{ margin: '0 0 12px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Validation Settings</h3>
      <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        Validation settings are in Mock Mode. Future settings will include auto-validation rules, compliance thresholds, certification criteria, and integration with government APIs.
      </p>
      <div style={{ marginTop: 12, display: 'flex', flexDirection: 'column', gap: 8 }}>
        <div style={settRow}><span>Auto-Validation on Save</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Enabled</span></div>
        <div style={settRow}><span>Compliance Threshold</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>70%</span></div>
        <div style={settRow}><span>Certification Auto-Renew</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>30 days before expiry</span></div>
        <div style={settRow}><span>Marketplace Gate</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>Requires Gold+ certification</span></div>
        <div style={settRow}><span>Active Role (Mock)</span><span style={{ fontWeight: 'var(--weight-semibold)' }}>{CURRENT_VALIDATION_ROLE}</span></div>
      </div>
    </div>
  );
}

function ValidationHelp() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>About the Validation Workspace</h3>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          The Enterprise Product Validation, Quality Assurance & Compliance Framework validates every product before it is published anywhere. It is the final Quality Gate for every product entering the SporeKart ecosystem. This module operates entirely in Mock Mode.
        </p>
      </div>
      <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>Key Concepts</h3>
        <ul style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>
          <li><strong>Product Completeness</strong> — Completion % across 7 sections (24 fields)</li>
          <li><strong>Quality Assurance</strong> — 12 quality checks per product</li>
          <li><strong>Compliance</strong> — 14 compliance checks including labels, MRP, GST, HSN</li>
          <li><strong>Certification</strong> — 5 levels from Bronze to Enterprise Certified</li>
          <li><strong>Marketplace Validation</strong> — Readiness for 6 sales channels</li>
          <li><strong>Publishing Readiness</strong> — 6 status states from Ready to Blocked</li>
        </ul>
      </div>
    </div>
  );
}

export const ValidationPage: React.FC = () => {
  const state = useValidationState();

  const renderSection = () => {
    switch (state.section) {
      case 'overview':
        return <ValidationDashboard />;
      case 'validation':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
            <ProductHealthDashboard health={{
              overallHealth: state.productHealth.overall,
              validationScore: state.productHealth.overall,
              seoScore: state.productHealth.seo,
              complianceScore: state.productHealth.compliance,
              marketplaceScore: state.productHealth.marketplace,
              accessibilityScore: state.productHealth.accessibility,
              aiReadinessScore: state.productHealth.aiReadiness,
              publishingScore: state.productHealth.publishing,
              riskLevel: state.productHealth.riskLevel,
              certificationStatus: state.productHealth.certificationStatus,
              totalProducts: state.scores.length,
              validatedProducts: state.scores.filter((s) => s.overall >= 70).length,
              failedProducts: state.scores.filter((s) => s.overall < 50).length,
            }} />
            <QualityAssurance selectedQuality={state.selectedQuality} scores={state.scores} selectedId={state.selectedId} onSelect={state.toggleSelect} />
          </div>
        );
      case 'compliance':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
            <ComplianceChecker selectedCompliance={state.selectedCompliance} scores={state.scores} selectedId={state.selectedId} onSelect={state.toggleSelect} />
            <CertificationDashboard />
          </div>
        );
      case 'completeness':
        return <ProductCompleteness entries={state.completenessResults} selectedId={state.selectedId} onSelect={state.toggleSelect} />;
      case 'seo':
        return <SeoValidator selectedId={state.selectedId} />;
      case 'media':
        return <MediaValidator selectedId={state.selectedId} />;
      case 'pricing':
        return <PricingValidator selectedId={state.selectedId} />;
      case 'variants':
        return <VariantValidator selectedId={state.selectedId} />;
      case 'packaging':
        return <PackagingValidator selectedId={state.selectedId} />;
      case 'marketplace':
        return <MarketplaceValidator selectedId={state.selectedId} />;
      case 'accessibility':
        return (
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
            <AccessibilityValidator selectedId={state.selectedId} />
            <AiReadinessValidator selectedId={state.selectedId} />
          </div>
        );
      case 'publishing':
        return <PublishingReadiness entries={state.publishingReadiness} selectedId={state.selectedId} onSelect={state.toggleSelect} />;
      case 'history':
        return <ValidationHistory activity={state.activity} />;
      case 'reports':
        return <ValidationReports reports={state.reports} />;
      case 'settings':
        return <ValidationSettings />;
      case 'help':
        return <ValidationHelp />;
      default:
        return <ValidationDashboard />;
    }
  };

  return (
    <PermissionProvider initialRole="manager">
      <FeatureFlagProvider>
        <PermissionGate action="view" resource="validation">
          <div className="sk-val-page">
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <div>
                <h1 style={{ margin: 0, fontSize: 'var(--text-h1)', color: 'var(--color-text-primary)' }}>Product Validation & QA</h1>
                <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
                  Quality assurance, compliance, certification, and publishing readiness
                </p>
              </div>
            </div>

            <ValidationToolbar
              search={state.search}
              onSearchChange={state.setSearch}
              sort={state.sort}
              onSortChange={state.setSort}
              activeFilterCount={state.activeFilterCount}
              onClearFilters={state.clearAllFilters}
            />

            <div className="sk-val-grid">
              <aside className="sk-val-sidebar">
                <ValidationNav section={state.section} onSectionChange={state.setSection} />
              </aside>
              <main className="sk-val-content">
                {renderSection()}
              </main>
            </div>

            <footer style={{ marginTop: 'var(--space-section-gap)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', borderTop: '1px solid var(--color-border)', paddingTop: 'var(--space-component-gap)', display: 'flex', justifyContent: 'space-between' }}>
              <span>Mock Mode — no persistence. Role: {CURRENT_VALIDATION_ROLE}</span>
              <span>Product Validation & QA · Sprint 24 Part 10</span>
            </footer>
          </div>
        </PermissionGate>
      </FeatureFlagProvider>
    </PermissionProvider>
  );
};

const settRow: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' };

export default ValidationPage;
