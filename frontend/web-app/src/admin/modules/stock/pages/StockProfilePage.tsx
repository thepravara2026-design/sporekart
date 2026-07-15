import { memo, useState } from 'react';
import { SectionHeader, SummaryCard } from '../../inventory/components';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { StockHealthBadge, AvailabilityBadge, StockTimeline } from '../components';
import { getStockRecords } from '../services/stockMockService';
import { getStatusVariant, totalStock } from '../utils';

export const StockProfilePage = memo(function StockProfilePage() {
  const [records] = useState(() => getStockRecords());
  const rec = records[0];

  if (!rec) return <SectionHeader title="Stock Profile" description="Stock record not found." />;

  const stockStates = Object.entries(rec.quantities).filter(([, qty]) => qty > 0);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: 12 }}>
        <div>
          <SectionHeader title={rec.inventoryItemName} description={`Stock Record ${rec.code} · ${rec.warehouseName}`} />
          <div style={{ display: 'flex', gap: 8, marginTop: 8, flexWrap: 'wrap' }}>
            <StatusBadge status={rec.status} variant={getStatusVariant(rec.status)} />
            <StockHealthBadge health={rec.health} />
            <AvailabilityBadge level={rec.availability} />
          </div>
        </div>
        <div style={{ fontSize: 'var(--text-h2)', fontWeight: 700, color: 'var(--color-text-primary)' }}>
          {totalStock(rec).toLocaleString()} <span style={{ fontSize: 'var(--text-body)', fontWeight: 400, color: 'var(--color-text-tertiary)' }}>total</span>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12, padding: 20, border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)' }}>
        <Field label="Item Code" value={rec.code} />
        <Field label="SKU" value={rec.sku} />
        <Field label="Product" value={rec.productName} />
        <Field label="Variant" value={rec.variantName} />
        <Field label="Warehouse" value={rec.warehouseName} />
        <Field label="Health Score" value={`${rec.healthScore}%`} />
        <Field label="Reorder Point" value={rec.reorderPoint.toLocaleString()} />
        <Field label="Safety Stock" value={rec.safetyStock.toLocaleString()} />
        <Field label="Min Stock" value={rec.minStock.toLocaleString()} />
        <Field label="Max Stock" value={rec.maxStock.toLocaleString()} />
        <Field label="Created" value={rec.createdAt} />
        <Field label="Updated" value={rec.updatedAt} />
      </div>

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Stock States</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 8 }}>
          {stockStates.map(([state, qty]) => (
            <SummaryCard key={state} title={state.replace(/_/g, ' ')}>
              <span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{qty.toLocaleString()}</span>
            </SummaryCard>
          ))}
        </div>
      </div>

      {rec.reservations.length > 0 && (
        <div>
          <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Reservations ({rec.reservations.length})</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {rec.reservations.map((res) => (
              <div key={res.id} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-md)', padding: '12px 16px', background: 'var(--color-surface)', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 8 }}>
                <div><strong>{res.type.replace(/_/g, ' ')}</strong> · Qty: {res.quantity}</div>
                {res.reference && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Ref: {res.reference}</span>}
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{res.createdAt}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      <div>
        <h3 style={{ margin: 0, fontSize: 'var(--text-h3)', color: 'var(--color-text-primary)', marginBottom: 12 }}>Stock Timeline</h3>
        <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', background: 'var(--color-surface)' }}>
          <StockTimeline events={rec.timeline} />
        </div>
      </div>
    </div>
  );
});

function Field({ label, value }: { label: string; value: string }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: '0.04em', fontWeight: 600 }}>{label}</span>
      <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{value}</span>
    </div>
  );
}
