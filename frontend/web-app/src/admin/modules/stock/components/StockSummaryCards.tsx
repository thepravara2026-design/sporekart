import { memo } from 'react';
import { SummaryCard } from '../../inventory/components';
import type { StockRecord } from '../types';
import { totalStock } from '../utils';

interface Props { records: StockRecord[]; title?: string; }

export const StockSummaryCards = memo(function StockSummaryCards({ records }: Props) {
  const totals = records.reduce(
    (acc, r) => {
      acc.total += totalStock(r);
      acc.available += r.quantities.available ?? 0;
      acc.reserved += r.quantities.reserved ?? 0;
      acc.incoming += r.quantities.incoming ?? 0;
      acc.allocated += r.quantities.allocated ?? 0;
      acc.damaged += r.quantities.damaged ?? 0;
      acc.expired += r.quantities.expired ?? 0;
      acc.blocked += r.quantities.blocked ?? 0;
      return acc;
    },
    { total: 0, available: 0, reserved: 0, incoming: 0, allocated: 0, damaged: 0, expired: 0, blocked: 0 },
  );

  const summaryItems = [
    { label: 'Available', value: totals.available.toLocaleString(), variant: 'success' as const },
    { label: 'Reserved', value: totals.reserved.toLocaleString(), variant: 'info' as const },
    { label: 'Incoming', value: totals.incoming.toLocaleString(), variant: 'info' as const },
    { label: 'Allocated', value: totals.allocated.toLocaleString(), variant: 'info' as const },
    { label: 'Damaged', value: totals.damaged.toLocaleString(), variant: 'danger' as const },
    { label: 'Expired', value: totals.expired.toLocaleString(), variant: 'danger' as const },
    { label: 'Blocked', value: totals.blocked.toLocaleString(), variant: 'warning' as const },
  ];

  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 8 }}>
      {summaryItems.map((item) => (
        <SummaryCard key={item.label} title={item.label}>
          <span style={{ fontSize: 'var(--text-h2)', fontWeight: 700 }}>{item.value}</span>
        </SummaryCard>
      ))}
    </div>
  );
});
