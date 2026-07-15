import React, { useState } from 'react';
import type { SeoEntry } from '../types';

interface StructuredDataBuilderProps {
  entry: SeoEntry | null;
  examples: Record<string, string>;
}

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 8 };
const chipBase: React.CSSProperties = { padding: '8px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)', cursor: 'pointer', fontSize: 'var(--text-body-xs)', fontWeight: 600, textAlign: 'center', transition: 'all 0.15s' };

export const StructuredDataBuilder: React.FC<StructuredDataBuilderProps> = React.memo(({ entry, examples }) => {
  const [viewSchema, setViewSchema] = useState<string | null>(null);

  const types = [
    { key: 'product', label: 'Product', active: entry?.structuredData.product },
    { key: 'brand', label: 'Brand', active: entry?.structuredData.brand },
    { key: 'offer', label: 'Offer', active: entry?.structuredData.offer },
    { key: 'organization', label: 'Organization', active: entry?.structuredData.organization },
    { key: 'breadcrumb', label: 'Breadcrumb', active: entry?.structuredData.breadcrumb },
    { key: 'faq', label: 'FAQ', active: entry?.structuredData.faq },
    { key: 'review', label: 'Review', active: entry?.structuredData.review },
    { key: 'image', label: 'Image', active: entry?.structuredData.image },
    { key: 'video', label: 'Video', active: entry?.structuredData.video },
    { key: 'aggregateRating', label: 'Aggregate Rating', active: entry?.structuredData.aggregateRating },
    { key: 'availability', label: 'Availability', active: entry?.structuredData.availability },
    { key: 'price', label: 'Price', active: entry?.structuredData.price },
  ];

  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Structured Data (JSON-LD)</h2>
      {!entry ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>Select a product to view structured data</div>
      ) : (
        <>
          <div style={grid}>
            {types.map((t) => (
              <div key={t.key}
                onClick={() => setViewSchema(t.key)}
                style={{ ...chipBase, background: t.active ? 'var(--color-accent-green)20' : 'var(--color-bg-surface-default)', borderColor: t.active ? 'var(--color-accent-green)' : 'var(--color-border)', color: t.active ? 'var(--color-accent-green)' : 'var(--color-text-secondary)' }}>
                <div>{t.active ? '✓' : '○'}</div>
                <div style={{ marginTop: 2 }}>{t.label}</div>
              </div>
            ))}
          </div>

          <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
            <div style={{ padding: '10px 12px', background: 'var(--color-bg-surface-raised)', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
              {viewSchema ? `JSON-LD: ${types.find((t) => t.key === viewSchema)?.label ?? viewSchema}` : 'Select a schema type to preview'}
            </div>
            <pre style={{ margin: 0, padding: 16, fontSize: 12, lineHeight: 1.6, overflow: 'auto', maxHeight: 400, color: 'var(--color-text-primary)', background: 'var(--color-bg-surface-default)' }}>
              {viewSchema && examples[viewSchema] ? examples[viewSchema] : '// Select a schema type above to see its JSON-LD representation'}
            </pre>
          </div>
        </>
      )}
    </div>
  );
});
