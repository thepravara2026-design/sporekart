import React from 'react';
import type { BarcodeEntry } from '../types';

interface BarcodePlaceholderProps {
  barcodes: BarcodeEntry[];
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };

export const BarcodePlaceholder: React.FC<BarcodePlaceholderProps> = React.memo(({ barcodes }) => {
  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Barcode & QR Code</h2>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }}>
        {barcodes.map((b) => (
          <div key={b.id} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
            <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
              {b.sku}
            </div>
            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginTop: 2 }}>Type: {b.type}</div>
            <div style={{ marginTop: 12, padding: 12, borderRadius: 'var(--radius-sm)', background: 'var(--color-bg-surface-raised)', textAlign: 'center', fontFamily: 'monospace', fontSize: 'var(--text-body-sm)' }}>
              {b.status === 'generated' ? (
                <>
                  <div style={{ letterSpacing: 2, fontWeight: 700, color: 'var(--color-text-primary)' }}>
                    {b.barcode ?? 'N/A'}
                  </div>
                  <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginTop: 4 }}>
                    QR: {b.qrCode ?? 'N/A'}
                  </div>
                </>
              ) : (
                <div style={{ color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>
                  {b.status === 'pending' ? '⏳ Pending generation' : '❌ Failed'}
                </div>
              )}
            </div>
            <div style={{ display: 'flex', gap: 8, marginTop: 12 }}>
              <PlaceholderBtn>Download</PlaceholderBtn>
              <PlaceholderBtn>Print</PlaceholderBtn>
            </div>
          </div>
        ))}
      </div>
      <div style={{ padding: 12, borderRadius: 'var(--radius-sm)', border: '1px dashed var(--color-border)', background: 'var(--color-bg-surface-raised)', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', textAlign: 'center' }}>
        Barcode & QR generation requires external API integration (EAN-13, UPC, Code128, QR). Mock placeholders shown.
      </div>
    </div>
  );
});

function PlaceholderBtn({ children }: { children: React.ReactNode }) {
  return (
    <button style={{
      flex: 1, padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border)',
      background: 'var(--color-bg-surface-default)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-xs)', cursor: 'pointer', opacity: 0.6,
    }} disabled>
      {children}
    </button>
  );
}
