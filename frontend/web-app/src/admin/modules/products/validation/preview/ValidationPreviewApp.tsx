import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { ValidationPage } from '../ValidationPage';
import { CertificationDashboard } from '../components/CertificationDashboard';
import { ProductHealthDashboard } from '../components/ProductHealthDashboard';
import { PublishingReadiness } from '../components/PublishingReadiness';
import { ValidationReports } from '../components/ValidationReports';
import { MOCK_VALIDATION_SCORES, MOCK_PRODUCT_HEALTH } from '../mock/mockValidation';
import { MOCK_REPORTS } from '../mock/mockReports';

const MOCK_PUBLISHING = MOCK_VALIDATION_SCORES.map((v) => ({
  productId: v.productId, productName: v.productName,
  status: v.publishing >= 85 ? 'ready' as const : v.publishing >= 70 ? 'needs_review' as const : v.publishing >= 50 ? 'blocked' as const : 'incomplete' as const,
  readinessScore: v.publishing,
  blockers: v.publishing < 70 ? ['Low validation score', 'Marketplace gaps'] : [],
  warnings: v.publishing < 85 ? ['Review before publishing'] : [],
}));

const FullPreview: React.FC = () => <ValidationPage />;

const HealthPreview: React.FC = () => (
  <ProductHealthDashboard health={{
    overallHealth: MOCK_PRODUCT_HEALTH.overall,
    validationScore: MOCK_PRODUCT_HEALTH.overall,
    seoScore: MOCK_PRODUCT_HEALTH.seo,
    complianceScore: MOCK_PRODUCT_HEALTH.compliance,
    marketplaceScore: MOCK_PRODUCT_HEALTH.marketplace,
    accessibilityScore: MOCK_PRODUCT_HEALTH.accessibility,
    aiReadinessScore: MOCK_PRODUCT_HEALTH.aiReadiness,
    publishingScore: MOCK_PRODUCT_HEALTH.publishing,
    riskLevel: MOCK_PRODUCT_HEALTH.riskLevel,
    certificationStatus: MOCK_PRODUCT_HEALTH.certificationStatus,
    totalProducts: MOCK_VALIDATION_SCORES.length,
    validatedProducts: MOCK_VALIDATION_SCORES.filter((s) => s.overall >= 70).length,
    failedProducts: MOCK_VALIDATION_SCORES.filter((s) => s.overall < 50).length,
  }} />
);

const CertificationPreview: React.FC = () => <CertificationDashboard />;

const PublishingPreview: React.FC = () => (
  <div style={{ padding: 'var(--space-component-gap)' }}>
    <PublishingReadiness entries={MOCK_PUBLISHING} selectedId={null} onSelect={() => {}} />
  </div>
);

const ReportsPreview: React.FC = () => (
  <div style={{ padding: 'var(--space-component-gap)' }}>
    <ValidationReports reports={MOCK_REPORTS} />
  </div>
);

export const ValidationPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="workspace" replace />} />
      <Route path="workspace" element={<FullPreview />} />
      <Route path="health" element={<HealthPreview />} />
      <Route path="certification" element={<CertificationPreview />} />
      <Route path="publishing" element={<PublishingPreview />} />
      <Route path="reports" element={<ReportsPreview />} />
      <Route path="info" element={<ValidationInfo />} />
    </Routes>
  );
};

function ValidationInfo() {
  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 720 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        Product Validation & QA — Architecture
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        <ArchCard title="Mock Mode">
          This module operates entirely in mock mode. 12 validation scores, 7 compliance results, 8 certifications, 6 reports, and 15 activity events are statically defined.
        </ArchCard>
        <ArchCard title="Validation Architecture">
          16-section workspace covering completeness, quality, compliance, media, SEO, pricing, variants, packaging, marketplace, accessibility, AI readiness, publishing, certification, reporting, settings, and help.
        </ArchCard>
        <ArchCard title="Key Components">
          Product Health Dashboard (8 categories), Completeness Engine (7 sections, 24 fields), Quality Assurance (12 checks), Compliance Checker (14 checks across 14 categories), Certification System (5 levels), Publishing Readiness (6 status states).
        </ArchCard>
        <ArchCard title="Integration Points">
          Future: Government Compliance APIs (FSSAI, BIS, Agri Marketing), AI-assisted validation, Marketplace APIs, automated certification, PDF report export.
        </ArchCard>
        <ArchCard title="Preview Routes">
          <code>/preview/products/validation/workspace</code> — Full workspace<br />
          <code>/preview/products/validation/health</code> — Health dashboard<br />
          <code>/preview/products/validation/certification</code> — Certification dashboard<br />
          <code>/preview/products/validation/publishing</code> — Publishing readiness<br />
          <code>/preview/products/validation/reports</code> — Validation reports<br />
          <code>/preview/products/validation/info</code> — Architecture notes
        </ArchCard>
      </div>
    </div>
  );
}

function ArchCard({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h3 style={{ margin: '0 0 8px', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)' }}>{title}</h3>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 'var(--leading-relaxed)' }}>{children}</div>
    </div>
  );
}

export default ValidationPreviewApp;
