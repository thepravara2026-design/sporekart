import React from 'react';
import type { PricingEntity } from '../types';
import { getPriceForTier, getDiscountPercent, getGstAmount, getFinalPrice } from '../mock/mockPrices';

interface PricePreviewProps {
  entity: PricingEntity | null;
}

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 16,
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

const previewLayout: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: '1fr 1fr 1fr',
  gap: 16,
};

const previewCard: React.CSSProperties = {
  padding: 20,
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
  display: 'flex',
  flexDirection: 'column',
  gap: 12,
};

const priceLine: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  padding: '4px 0',
  fontSize: 'var(--text-body-sm)',
};

const labelStyle: React.CSSProperties = {
  color: 'var(--color-text-tertiary)',
};

const valueStyle: React.CSSProperties = {
  fontWeight: 500,
  color: 'var(--color-text-primary)',
};

function formatPrice(amount: number): string {
  return `₹${amount.toFixed(2)}`;
}

function PreviewCard({ title, prices }: { title: string; prices: { label: string; value: string; highlight?: boolean }[] }) {
  return (
    <div style={previewCard}>
      <div style={{ fontSize: 'var(--text-h5)', fontWeight: 600, color: 'var(--color-text-primary)', textAlign: 'center' }}>
        {title}
      </div>
      {prices.map((p, i) => (
        <div key={i} style={{ ...priceLine, ...(p.highlight ? { background: 'var(--color-accent-green)10', borderRadius: 'var(--radius-xs)', padding: '6px 4px' } : {}) }}>
          <span style={labelStyle}>{p.label}</span>
          <span style={p.highlight ? { ...valueStyle, fontWeight: 700, color: 'var(--color-accent-green)' } : valueStyle}>{p.value}</span>
        </div>
      ))}
    </div>
  );
}

export const PricePreview: React.FC<PricePreviewProps> = React.memo(({ entity }) => {
  if (!entity) {
    return (
      <div style={sectionStyle}>
        <h2 style={h2Style}>Price Preview</h2>
        <div style={{ textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
          Select a product to see pricing preview
        </div>
      </div>
    );
  }

  const mrp = getPriceForTier(entity, 'mrp');
  const selling = getPriceForTier(entity, 'selling');
  const wholesale = getPriceForTier(entity, 'wholesale');
  const discount = getDiscountPercent(entity);
  const discountAmount = mrp - selling;
  const gstAmount = getGstAmount(selling, entity.gstPercentage);
  const finalPrice = getFinalPrice(entity);

  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>Price Preview</h2>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
        {entity.productName} · {entity.sku}
      </div>
      <div style={previewLayout}>
        <PreviewCard
          title="Customer Preview"
          prices={[
            { label: 'MRP', value: formatPrice(mrp) },
            { label: 'Selling Price', value: formatPrice(selling) },
            { label: 'Discount', value: discount > 0 ? `${discount}% (${formatPrice(discountAmount)})` : 'None' },
            { label: 'GST', value: `${entity.gstPercentage}% (${formatPrice(gstAmount)})` },
            { label: 'Final Price', value: formatPrice(finalPrice), highlight: true },
          ]}
        />
        <PreviewCard
          title="Admin Preview"
          prices={[
            { label: 'Cost Base', value: formatPrice(selling) },
            { label: 'Wholesale', value: formatPrice(wholesale) },
            { label: 'Margin', value: mrp > 0 ? `${Math.round(((mrp - selling) / mrp) * 100)}%` : 'N/A' },
            { label: 'GST Component', value: `${entity.gstPercentage}%` },
            { label: 'Admin Price', value: formatPrice(selling), highlight: true },
          ]}
        />
        <PreviewCard
          title="Marketplace Preview"
          prices={[
            { label: 'Listed MRP', value: formatPrice(mrp) },
            { label: 'Sale Price', value: formatPrice(selling) },
            { label: 'Commission Base', value: formatPrice(wholesale) },
            { label: 'GST on Commission', value: formatPrice(gstAmount) },
            { label: 'Marketplace Price', value: formatPrice(wholesale + gstAmount), highlight: true },
          ]}
        />
      </div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 32, padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
        <div style={{ textAlign: 'center' }}>
          <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>MRP</div>
          <div style={{ fontSize: 'var(--text-h4)', fontWeight: 700, color: discount > 0 ? 'var(--color-text-tertiary)' : 'var(--color-text-primary)', textDecoration: discount > 0 ? 'line-through' : 'none' }}>
            {formatPrice(mrp)}
          </div>
        </div>
        {discount > 0 && (
          <>
            <div style={{ fontSize: 24, color: 'var(--color-text-tertiary)' }}>→</div>
            <div style={{ textAlign: 'center' }}>
              <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-green)' }}>Selling Price</div>
              <div style={{ fontSize: 'var(--text-h3)', fontWeight: 700, color: 'var(--color-accent-green)' }}>
                {formatPrice(selling)}
              </div>
            </div>
            <div style={{ textAlign: 'center' }}>
              <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-red)' }}>You Save</div>
              <div style={{ fontSize: 'var(--text-h4)', fontWeight: 700, color: 'var(--color-accent-red)' }}>
                {formatPrice(discountAmount)}
              </div>
              <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-red)' }}>({discount}% off)</div>
            </div>
          </>
        )}
        <div style={{ textAlign: 'center' }}>
          <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>Final (incl. GST)</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 700, color: 'var(--color-accent-green)' }}>
            {formatPrice(finalPrice)}
          </div>
        </div>
      </div>
    </div>
  );
});
