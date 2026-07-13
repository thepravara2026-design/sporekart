import React from 'react';

function StateCard({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '12px', alignItems: 'center' }}>{children}</div>
    </div>
  );
}

import { DatePicker } from '../../components/charts/filters/DatePicker';
import { DateRangePicker } from '../../components/charts/filters/DateRangePicker';
import { FilterChips } from '../../components/charts/filters/FilterChips';
import { SearchFilter } from '../../components/charts/filters/SearchFilter';
import { QuickFilter } from '../../components/charts/filters/QuickFilter';

export default function DataFiltersPreview() {
  const [date, setDate] = React.useState<Date | undefined>(new Date());
  const [startDate, setStartDate] = React.useState<Date | undefined>(undefined);
  const [endDate, setEndDate] = React.useState<Date | undefined>(undefined);
  const [searchValue, setSearchValue] = React.useState('');
  const [quickSelected, setQuickSelected] = React.useState<string[]>([]);
  const [multiSelected, setMultiSelected] = React.useState<string[]>(['option-2']);
  const [chips, setChips] = React.useState([
    { id: 'c1', label: 'Status: Active', onRemove: () => {} },
    { id: 'c2', label: 'Department: Engineering', onRemove: () => {} },
    { id: 'c3', label: 'Priority: High', onRemove: () => {} },
  ]);

  const removeChip = (id: string) => {
    setChips((prev) => prev.filter((c) => c.id !== id));
  };

  const quickOptions = [
    { label: 'Today', value: 'today' },
    { label: 'This Week', value: 'this-week' },
    { label: 'This Month', value: 'this-month' },
    { label: 'Last Quarter', value: 'last-quarter' },
    { label: 'This Year', value: 'this-year' },
  ];

  const multiOptions = [
    { label: 'Design', value: 'design' },
    { label: 'Engineering', value: 'engineering' },
    { label: 'Marketing', value: 'marketing' },
    { label: 'Sales', value: 'sales' },
    { label: 'Support', value: 'support' },
  ];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Data Filters</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Date pickers, filter chips, search, and quick filters</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>DatePicker</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="With calendar popover">
            <DatePicker value={date} onChange={(d) => setDate(d)} />
          </StateCard>
          <StateCard label="With placeholder">
            <DatePicker placeholder="Pick a date" />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>DateRangePicker</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Range selection">
            <DateRangePicker
              startDate={startDate}
              endDate={endDate}
              onChange={(s, e) => { setStartDate(s); setEndDate(e); }}
            />
          </StateCard>
          <StateCard label="With placeholder">
            <DateRangePicker placeholder="Select range" />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>FilterChips</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '16px' }}>
          <StateCard label="Active filters with removable chips">
            <FilterChips
              filters={chips.map((c) => ({
                ...c,
                onRemove: () => removeChip(c.id),
              }))}
            />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>SearchFilter</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '16px' }}>
          <StateCard label="Debounced search input">
            <SearchFilter
              value={searchValue}
              onChange={(v) => setSearchValue(v)}
              placeholder="Search by name, email..."
              debounceMs={400}
            />
          </StateCard>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>QuickFilter</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '16px' }}>
          <StateCard label="Single select mode">
            <QuickFilter
              options={quickOptions}
              selected={quickSelected}
              onChange={(v) => setQuickSelected(v)}
              multi={false}
            />
          </StateCard>
          <StateCard label="Multi select mode">
            <QuickFilter
              options={multiOptions}
              selected={multiSelected}
              onChange={(v) => setMultiSelected(v)}
              multi
            />
          </StateCard>
        </div>
      </section>
    </div>
  );
}
