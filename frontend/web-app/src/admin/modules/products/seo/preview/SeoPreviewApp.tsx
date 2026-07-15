import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { SeoPage } from '../SeoPage';
import { SearchPreview } from '../components/SearchPreview';
import { SocialPreview } from '../components/SocialPreview';
import { StructuredDataBuilder } from '../components/StructuredDataBuilder';
import { MOCK_SEO_ENTRIES } from '../mock/mockSeo';
import { MOCK_STRUCTURED_DATA_EXAMPLES } from '../mock/mockStructuredData';

const FullPreview: React.FC = () => {
  return <SeoPage />;
};

const SearchPreviewStandalone: React.FC = () => {
  return <SearchPreview entry={MOCK_SEO_ENTRIES[0]} />;
};

const SocialPreviewStandalone: React.FC = () => {
  return <SocialPreview entry={MOCK_SEO_ENTRIES[0]} />;
};

const StructuredDataStandalone: React.FC = () => {
  return <StructuredDataBuilder entry={MOCK_SEO_ENTRIES[0]} examples={MOCK_STRUCTURED_DATA_EXAMPLES} />;
};

export const SeoPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="workspace" replace />} />
      <Route path="workspace" element={<FullPreview />} />
      <Route path="search-preview" element={<SearchPreviewStandalone />} />
      <Route path="social-preview" element={<SocialPreviewStandalone />} />
      <Route path="structured-data" element={<StructuredDataStandalone />} />
      <Route path="info" element={<SeoInfo />} />
    </Routes>
  );
};

function SeoInfo() {
  return (
    <div style={{ padding: 'var(--space-component-gap)', maxWidth: 720 }}>
      <h2 style={{ margin: '0 0 var(--space-component-gap)', fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>
        SEO & Marketplace — Architecture
      </h2>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
        <ArchCard title="Mock Mode">
          This module operates entirely in mock mode. All 7 SEO entries, 10 publishing events, 6 marketplace configs, and 15 activity events are statically defined.
        </ArchCard>
        <ArchCard title="SEO Architecture">
          Each product entry includes full meta (title, description, keywords, OG, Twitter), canonical URL, structured data toggles (12 schema types), publishing status across 8 states, and marketplace readiness for 6 channels.
        </ArchCard>
        <ArchCard title="Key Components">
          SEO Health Dashboard scores 12 categories, AI Readiness Dashboard scores 8 categories, Content Validation per product with error/warning/info levels, Search Previews (Google/Bing/AI/Featured Snippet), Social Previews (Facebook/Twitter/LinkedIn), and Structured Data Builder with live JSON-LD examples.
        </ArchCard>
        <ArchCard title="Integration Points">
          Future: Supabase for persistence, Google Search Console API for real SEO data, marketplace API connectors for automated readiness checks, AI-powered content suggestions, automated sitemap generation.
        </ArchCard>
        <ArchCard title="Preview Routes">
          <code>/preview/products/seo/workspace</code> — Full workspace<br />
          <code>/preview/products/seo/search-preview</code> — Search preview<br />
          <code>/preview/products/seo/social-preview</code> — Social preview<br />
          <code>/preview/products/seo/structured-data</code> — Structured data builder<br />
          <code>/preview/products/seo/info</code> — Architecture notes
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

export default SeoPreviewApp;
