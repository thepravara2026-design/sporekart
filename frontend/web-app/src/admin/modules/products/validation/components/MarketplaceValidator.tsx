import React, { useMemo } from 'react';
import type { MarketplaceValidationResult } from '../types';
import { MOCK_VALIDATION_SCORES } from '../mock/mockValidation';

interface MarketplaceValidatorProps {
  selectedId: string | null;
}

function generateMarketplaceResult(productId: string): MarketplaceValidationResult {
  const product = MOCK_VALIDATION_SCORES.find((p) => p.productId === productId);
  const name = product?.productName ?? 'Unknown Product';
  const seed = productId.charCodeAt(productId.length - 1) || 0;
  const ok = (offset: number) => (seed + offset) % 3 !== 0;
  const missingFields: string[] = [];
  const warnings: string[] = [];
  const recommendations: string[] = [];
  if (!ok(1)) missingFields.push('Amazon: Missing product dimensions and category mapping');
  if (!ok(2)) missingFields.push('Flipkart: GTIN/EAN not provided');
  if (!ok(3)) missingFields.push('AgriBegri: Organic certification not uploaded');
  if (!ok(4)) missingFields.push('Google Shopping: Missing tax and shipping config');
  if (!ok(5)) missingFields.push('IndiaMART: Business details incomplete');
  if (!ok(6)) missingFields.push('Export: International pricing not configured');
  if (!ok(1) || !ok(2)) warnings.push('Missing key fields for major marketplaces');
  if (!ok(4)) warnings.push('Google Merchant Center feed may be rejected');
  if (ok(1) && ok(2)) recommendations.push('Consider enabling Amazon FBA for faster delivery');
  if (ok(3)) recommendations.push('Complete Google Shopping setup to maximize visibility');
  if (ok(5)) recommendations.push('Enable IndiaMART catalog sync');
  if (ok(6)) recommendations.push('Set up export pricing for international expansion');
  if (missingFields.length === 0) recommendations.push('All channels configured. Consider adding more product images.');
  return {
    productId, productName: name,
    amazonReady: ok(1), flipkartReady: ok(2), agriBegriReady: ok(3),
    googleShoppingReady: ok(4), indiaMARTReady: ok(5), exportReady: ok(6),
    overallScore: Math.round([ok(1), ok(2), ok(3), ok(4), ok(5), ok(6)].filter(Boolean).length / 6 * 100),
    missingFields, warnings, recommendations,
  };
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const panel: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };
const panelTitle: React.CSSProperties = { fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', marginBottom: 12 };

function ScoreDisplay({ score }: { score: number }) {
  const color = score >= 80 ? 'var(--color-accent-green)' : score >= 50 ? 'var(--color-accent-orange)' : 'var(--color-accent-red)';
  return (
    <div style={{ textAlign: 'center', padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
      <div style={{ width: 80, height: 80, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', border: `6px solid ${color}`, fontSize: 'var(--text-h3)', fontWeight: 700, color }}>{score}</div>
      <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>Marketplace Score</div>
    </div>
  );
}

export const MarketplaceValidator: React.FC<MarketplaceValidatorProps> = React.memo(({ selectedId }) => {
  const result = useMemo(() => selectedId ? generateMarketplaceResult(selectedId) : null, [selectedId]);

  if (!result) {
    return (
      <div style={section}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Marketplace Validation</h2>
        <div style={{ padding: 40, textAlign: 'center', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ fontSize: 32, marginBottom: 8, opacity: 0.3, color: 'var(--color-text-tertiary)' }}>🛒</div>
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No product selected for marketplace validation</div>
        </div>
      </div>
    );
  }

  const channels: Array<{ label: string; ready: boolean }> = [
    { label: 'Amazon', ready: result.amazonReady },
    { label: 'Flipkart', ready: result.flipkartReady },
    { label: 'AgriBegri', ready: result.agriBegriReady },
    { label: 'Google Shopping', ready: result.googleShoppingReady },
    { label: 'IndiaMART', ready: result.indiaMARTReady },
    { label: 'Export', ready: result.exportReady },
  ];

  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Marketplace Validation</h2>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', marginBottom: 4 }}>{result.productName}</div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 16 }}>
        <ScoreDisplay score={result.overallScore} />

        <div style={panel} aria-label="Channel readiness">
          <div style={panelTitle}>Channel Readiness</div>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 8 }}>
            {channels.map((ch) => (
              <div key={ch.label} style={{
                display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 8,
                padding: '8px 12px', borderRadius: 'var(--radius-sm)',
                border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)',
              }}>
                <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{ch.label}</span>
                <span style={{
                  padding: '2px 8px', borderRadius: 'var(--radius-xs)', fontSize: 'var(--text-body-xs)', fontWeight: 600,
                  color: ch.ready ? 'var(--color-accent-green)' : 'var(--color-accent-orange)',
                  background: ch.ready ? 'var(--color-accent-green-light)' : 'var(--color-accent-orange-light)',
                }}>
                  {ch.ready ? 'Ready' : 'Needs Work'}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 16 }}>
        <div style={panel} aria-label="Missing fields">
          <div style={panelTitle}>Missing Fields</div>
          {result.missingFields.length === 0 ? (
            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>None</div>
          ) : (
            result.missingFields.map((f, i) => (
              <div key={i} style={{ display: 'flex', gap: 6, padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-red)' }}>
                <span>✗</span> {f}
              </div>
            ))
          )}
        </div>

        <div style={panel} aria-label="Warnings">
          <div style={panelTitle}>Warnings</div>
          {result.warnings.length === 0 ? (
            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>None</div>
          ) : (
            result.warnings.map((w, i) => (
              <div key={i} style={{ display: 'flex', gap: 6, padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-orange)' }}>
                <span>⚠</span> {w}
              </div>
            ))
          )}
        </div>

        <div style={panel} aria-label="Recommendations">
          <div style={panelTitle}>Recommendations</div>
          {result.recommendations.length === 0 ? (
            <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>None</div>
          ) : (
            result.recommendations.map((r, i) => (
              <div key={i} style={{ display: 'flex', gap: 6, padding: '6px 0', borderBottom: '1px solid var(--color-border)', fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-blue)' }}>
                <span>→</span> {r}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
});
