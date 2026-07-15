import { memo } from 'react';
import { SectionHeader } from '../../inventory/components';
import { Icon } from '../../../../design-system/icons/Icon';
import { INVENTORY_ITEM_UNITS } from '../constants';

export const UnitsPage = memo(function UnitsPage() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SectionHeader title="Units" description="Configure measurement units and conversion factors for inventory items." />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {INVENTORY_ITEM_UNITS.map((u) => (
          <div key={u.id} style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: 20, background: 'var(--color-surface)', display: 'flex', flexDirection: 'column', gap: 12 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <div style={{ width: 36, height: 36, borderRadius: 'var(--radius-md)', background: 'var(--color-info-alpha)', color: 'var(--color-info)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Icon name={u.category === 'weight' ? 'weight' : u.category === 'volume' ? 'droplet' : 'box'} size={18} />
              </div>
              <div>
                <h4 style={{ margin: 0, fontSize: 'var(--text-body)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{u.name}</h4>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{u.symbol}</span>
              </div>
            </div>
            <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
              Category: {u.category}
            </div>
            {u.baseUnit && (
              <div style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
                Conversion: 1 {u.symbol} = {u.conversionFactor} {u.baseUnit}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
});
