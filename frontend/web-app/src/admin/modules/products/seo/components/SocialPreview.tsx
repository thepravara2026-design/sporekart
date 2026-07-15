import React from 'react';
import type { SeoEntry } from '../types';

interface SocialPreviewProps {
  entry: SeoEntry | null;
}

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 16 };

function FacebookCard({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ height: 120, background: 'var(--color-bg-surface-raised)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-xs)' }}>
        {entry.ogImage ? '🖼️ ' + entry.ogImage : 'No Image'}
      </div>
      <div style={{ padding: 12 }}>
        <div style={{ fontSize: 11, color: 'var(--color-text-tertiary)', textTransform: 'uppercase', marginBottom: 4 }}>sporekart.com</div>
        <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 4 }}>{entry.ogTitle}</div>
        <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', lineHeight: 1.4 }}>{entry.ogDescription}</div>
      </div>
    </div>
  );
}

function TwitterCard({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ height: 100, background: 'var(--color-bg-surface-raised)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-xs)' }}>
        {entry.twitterImage ? '🖼️ ' + entry.twitterImage : 'No Image'}
      </div>
      <div style={{ padding: 12 }}>
        <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 4 }}>{entry.twitterTitle}</div>
        <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{entry.twitterDescription}</div>
      </div>
    </div>
  );
}

function InstagramCard({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden', background: 'var(--color-bg-surface-default)', maxWidth: 360 }}>
      <div style={{ height: 160, background: 'linear-gradient(135deg, #833AB4, #FD1D1D, #FCAF45)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontSize: 'var(--text-body-xs)' }}>
        {entry.instagramImage ? '🖼️ ' + entry.instagramImage : '📸 Instagram Post'}
      </div>
      <div style={{ padding: 12 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
          <div style={{ width: 28, height: 28, borderRadius: '50%', background: 'linear-gradient(135deg, #833AB4, #FD1D1D, #FCAF45)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontSize: 12 }}>SK</div>
          <div>
            <div style={{ fontSize: 'var(--text-body-xs)', fontWeight: 600, color: 'var(--color-text-primary)' }}>sporekart</div>
            <div style={{ fontSize: 10, color: 'var(--color-text-tertiary)' }}>SporeKart</div>
          </div>
        </div>
        <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 4 }}>{entry.instagramTitle}</div>
        <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', lineHeight: 1.4 }}>{entry.instagramDescription}</div>
        <div style={{ marginTop: 8, fontSize: 10, color: 'var(--color-text-tertiary)' }}>View on Instagram →</div>
      </div>
    </div>
  );
}

function LinkedInCard({ entry }: { entry: SeoEntry }) {
  return (
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ height: 100, background: 'var(--color-bg-surface-raised)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-xs)' }}>
        {entry.ogImage ? '🖼️ ' + entry.ogImage : 'No Image'}
      </div>
      <div style={{ padding: 12 }}>
        <div style={{ fontSize: 11, color: 'var(--color-text-tertiary)', textTransform: 'uppercase', marginBottom: 4 }}>SporeKart · Product</div>
        <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 4 }}>{entry.ogTitle}</div>
        <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{entry.ogDescription}</div>
      </div>
    </div>
  );
}

export const SocialPreview: React.FC<SocialPreviewProps> = React.memo(({ entry }) => {
  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Social Preview</h2>
      {!entry ? (
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>Select a product to preview</div>
      ) : (
        <div style={grid}>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Facebook</h3><FacebookCard entry={entry} /></div>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Twitter / X</h3><TwitterCard entry={entry} /></div>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>Instagram</h3><InstagramCard entry={entry} /></div>
          <div><h3 style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 8 }}>LinkedIn</h3><LinkedInCard entry={entry} /></div>
        </div>
      )}
    </div>
  );
});
