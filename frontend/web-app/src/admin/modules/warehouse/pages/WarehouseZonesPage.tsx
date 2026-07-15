import { memo, useMemo } from 'react';
import { ZONE_TYPES } from '../constants';
import { useWarehouseZones } from '../hooks/useWarehouseData';
import { SectionHeader } from '../components';
import { StatusBadge } from '../../../components/status/StatusBadge';
import { EmptyState } from '../components';
import { SkeletonTable } from '../components';

export const WarehouseZonesPage = memo(function WarehouseZonesPage() {
  const zonesState = useWarehouseZones();
  const zones = zonesState.data ?? [];

  const byType = useMemo(() => {
    const c: Record<string, number> = {};
    zones.forEach((z) => { c[z.type] = (c[z.type] ?? 0) + 1; });
    return c;
  }, [zones]);

  if (zonesState.loading) return <SkeletonTable rows={6} />;
  if (zones.length === 0) return <EmptyState stateKey="no_zones" />;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
      <SectionHeader title="Zones" description="Storage zones across warehouses" icon="grid" sublabel="Warehouses / Zones" />

      <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap' }}>
        {ZONE_TYPES.map((t) => (
          <span key={t.value} style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
            {t.label}: <strong style={{ color: 'var(--color-text-primary)' }}>{byType[t.value] ?? 0}</strong>
          </span>
        ))}
      </div>

      <div style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)' }}>
          <thead>
            <tr style={{ background: 'var(--color-surface-hover)', color: 'var(--color-text-secondary)' }}>
              <th style={th}>Code</th><th style={th}>Name</th><th style={th}>Type</th><th style={th}>Status</th><th style={th}>Capacity</th>
            </tr>
          </thead>
          <tbody>
            {zones.map((z) => (
              <tr key={z.id} style={{ borderTop: '1px solid var(--color-border)' }}>
                <td style={td}>{z.code}</td>
                <td style={td}>{z.name}</td>
                <td style={td}>{ZONE_TYPES.find((t) => t.value === z.type)?.label}</td>
                <td style={td}><StatusBadge status={z.status} variant={z.status === 'active' ? 'success' : z.status === 'maintenance' ? 'warning' : 'neutral'} /></td>
                <td style={td}>{z.capacity}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

const th: React.CSSProperties = { textAlign: 'left', padding: '10px 12px', fontWeight: 600, fontSize: 'var(--text-caption)' };
const td: React.CSSProperties = { padding: '10px 12px', color: 'var(--color-text-primary)' };


