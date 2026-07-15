import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import type { OrgBrand } from '../types';
import { DetailPanel } from './DetailPanel';

interface BrandManagerProps {
  brands: OrgBrand[];
}

const tableHeaderStyle: React.CSSProperties = {
  display: 'grid', gridTemplateColumns: '2fr 1.5fr 1fr 80px 80px', gap: 'var(--space-inline-sm)',
  padding: '10px 12px', borderBottom: '2px solid var(--color-border-default)',
  fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)',
  color: 'var(--color-text-tertiary)', textTransform: 'uppercase', letterSpacing: 'var(--tracking-wide)',
};

const tableRowStyle: React.CSSProperties = {
  display: 'grid', gridTemplateColumns: '2fr 1.5fr 1fr 80px 80px', gap: 'var(--space-inline-sm)',
  padding: '10px 12px', borderBottom: '1px solid var(--color-border-weak)',
  fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)',
  cursor: 'pointer', transition: 'background var(--duration-fast) var(--easing-standard)',
  alignItems: 'center',
};

export const BrandManager = React.memo(function BrandManager({ brands }: BrandManagerProps) {
  const [selectedId, setSelectedId] = React.useState<string | null>(null);
  const selected = selectedId ? brands.find((b) => b.id === selectedId) ?? null : null;

  const statusColor = (s: string) => s === 'active' ? 'var(--color-success)' : s === 'inactive' ? 'var(--color-warning)' : 'var(--color-text-tertiary)';

  return (
    <div style={{ display: 'grid', gridTemplateColumns: selected ? '1fr 320px' : '1fr', gap: 'var(--space-section-gap)', alignItems: 'start' }}>
      <div style={{ minWidth: 0, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
        <div style={tableHeaderStyle}>
          <span>Brand</span>
          <span>Manufacturer</span>
          <span>Country</span>
          <span>Products</span>
          <span>Status</span>
        </div>
        {brands.map((b) => (
          <div key={b.id} style={{ ...tableRowStyle, background: selectedId === b.id ? 'var(--color-primary-alpha)' : 'transparent' }} onClick={() => setSelectedId(b.id)} onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); setSelectedId(b.id); } }} role="button" tabIndex={0}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
              <Icon name="shield" size={14} style={{ color: 'var(--color-text-tertiary)' }} />
              <span style={{ fontWeight: 'var(--weight-medium)' }}>{b.name}</span>
              {b.isFeatured && <Icon name="star" size={12} style={{ color: 'var(--color-accent-orange)' }} />}
            </div>
            <span style={{ color: 'var(--color-text-secondary)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{b.manufacturer}</span>
            <span style={{ color: 'var(--color-text-secondary)' }}>{b.country}</span>
            <span>{b.productCount}</span>
            <span style={{ color: statusColor(b.status), textTransform: 'capitalize' }}>{b.status}</span>
          </div>
        ))}
      </div>
      {selected && (
        <aside style={{ position: 'sticky', top: 'var(--space-component-gap)' }}>
          <BrandDetailPanel brand={selected} />
        </aside>
      )}
    </div>
  );
});

function BrandDetailPanel({ brand }: { brand: OrgBrand }) {
  const rowStyle: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--color-border-weak)', fontSize: 'var(--text-body-sm)' };
  const labelStyle: React.CSSProperties = { color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' };

  return (
    <DetailPanel title={brand.name} subtitle={brand.country} icon="shield">
      <div style={rowStyle}><span style={labelStyle}>Status</span><span style={{ textTransform: 'capitalize' }}>{brand.status}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Manufacturer</span><span>{brand.manufacturer}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Country</span><span>{brand.country}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Products</span><span>{brand.productCount}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Featured</span><span>{brand.isFeatured ? 'Yes' : 'No'}</span></div>
      <div style={rowStyle}><span style={labelStyle}>Priority</span><span>{brand.priority}</span></div>
      {brand.website && <div style={rowStyle}><span style={labelStyle}>Website</span><span>{brand.website}</span></div>}
      <div style={rowStyle}><span style={labelStyle}>Created</span><span>{new Date(brand.createdAt).toLocaleDateString()}</span></div>
      <div style={rowStyle}><span style={labelStyle}>By</span><span>{brand.createdBy}</span></div>
      {brand.description && <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '8px 0 0', lineHeight: 'var(--leading-relaxed)' }}>{brand.description}</p>}
      {brand.notes && <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', margin: '8px 0 0', fontStyle: 'italic' }}>Notes: {brand.notes}</p>}
    </DetailPanel>
  );
}

export default BrandManager;
