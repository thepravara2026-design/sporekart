import React from 'react';
import type { SeoEntry } from '../types';

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(400px, 1fr))', gap: 16 };

function GoogleResult({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: '#fff', color: '#000', fontFamily: 'Arial, sans-serif' }}>
      <div style={{ fontSize: 12, color: '#202124', marginBottom: 2 }}>Ad · {entry.canonicalUrl}</div>
      <div style={{ fontSize: 18, color: '#1a0dab', fontWeight: 400, cursor: 'pointer', marginBottom: 2, textDecoration: 'underline' }}>{entry.metaTitle}</div>
      <div style={{ fontSize: 14, color: '#545454', lineHeight: 1.58 }}>{entry.metaDescription}</div>
    </div>
  );
}

function BingResult({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: '#fff', color: '#000', fontFamily: 'Segoe UI, Arial, sans-serif' }}>
      <div style={{ fontSize: 12, color: '#767676', marginBottom: 2 }}>{entry.canonicalUrl}</div>
      <div style={{ fontSize: 16, color: '#1a0dab', fontWeight: 600, cursor: 'pointer', marginBottom: 2 }}>{entry.metaTitle}</div>
      <div style={{ fontSize: 13, color: '#545454', lineHeight: 1.5 }}>{entry.metaDescription}</div>
    </div>
  );
}

function AiSnippet({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
        <span style={{ fontSize: 20 }}>🤖</span>
        <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>AI Overview</span>
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 1.6 }}>
        <strong>{entry.productName}</strong> is available on SporeKart. {entry.productSummary} 
        {entry.metaDescription.slice(0, 100)}…
      </div>
      <div style={{ marginTop: 8, fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-blue)' }}>
        Source: SporeKart · {entry.canonicalUrl}
      </div>
    </div>
  );
}

function FeaturedSnippet({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: '#fff', color: '#000' }}>
      <div style={{ fontSize: 12, color: '#70757a', marginBottom: 4 }}>Featured snippet</div>
      <div style={{ fontSize: 14, color: '#1a0dab', fontWeight: 600, marginBottom: 4 }}>{entry.productName}</div>
      <div style={{ fontSize: 14, color: '#545454', lineHeight: 1.58 }}>
        {entry.productSummary} {entry.keywords.slice(0, 3).join(', ')}.
      </div>
    </div>
  );
}

export const SearchPreview: React.FC<{ entry: SeoEntry | null }> = React.memo(({ entry }) => {
  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Search Engine Preview</h2>
      {!entry ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>Select a product to preview search results</div>
      ) : (
        <div style={grid}>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Google Search</h3><GoogleResult entry={entry} /></div>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Bing Search</h3><BingResult entry={entry} /></div>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>AI Search Preview (GEO/AEO)</h3><AiSnippet entry={entry} /></div>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Featured Snippet</h3><FeaturedSnippet entry={entry} /></div>
        </div>
      )}
    </div>
  );
});
