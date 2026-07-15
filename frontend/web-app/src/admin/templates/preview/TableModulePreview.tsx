import { memo, type CSSProperties } from 'react';
import { DataGrid } from '../../components/data-grid/DataGrid';
import type { DataGridColumn } from '../../components/data-grid/types';

const columns: DataGridColumn<Record<string, any>>[] = [
  { key: 'id', header: 'ID', sortable: true, width: '100px' },
  { key: 'name', header: 'Name', sortable: true, filterable: true, filterType: 'dropdown' },
  {
    key: 'category',
    header: 'Category',
    sortable: true,
    filterable: true,
    filterType: 'dropdown',
    filterOptions: [
      { label: 'Retail', value: 'retail' },
      { label: 'Wholesale', value: 'wholesale' },
    ],
  },
  {
    key: 'status',
    header: 'Status',
    sortable: true,
    filterable: true,
    filterType: 'status',
    align: 'center',
    render: (row: Record<string, any>) => {
      const status = String(row['status']);
      const color = status === 'active' ? '#2f6f4f' : status === 'pending' ? '#d97706' : '#7c3aed';
      return (
        <span
          style={{
            display: 'inline-block',
            padding: '2px 10px',
            borderRadius: 'var(--radius-pill)',
            fontSize: 'var(--text-caption)',
            color: 'var(--color-bg-surface-default)',
            backgroundColor: color,
          }}
        >
          {status}
        </span>
      );
    },
  },
  {
    key: 'amount',
    header: 'Amount',
    sortable: true,
    align: 'right',
    width: '120px',
    render: (row: Record<string, any>) => `$${Number(row['amount'] || 0).toFixed(2)}`,
  },
];

const data: Record<string, any>[] = [
  { id: 'A1', name: 'Acme Corp', category: 'retail', status: 'active', amount: 1240.5 },
  { id: 'A2', name: 'Globex', category: 'wholesale', status: 'pending', amount: 8750.0 },
  { id: 'A3', name: 'Initech', category: 'retail', status: 'active', amount: 320.75 },
  { id: 'A4', name: 'Umbrella', category: 'wholesale', status: 'archived', amount: 99.99 },
];

export const TableModulePreview = memo(function TableModulePreview() {
  const style: CSSProperties = { display: 'flex', flexDirection: 'column', gap: 16, padding: 24 };
  return (
    <div style={style}>
      <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Table Module</h1>
      <DataGrid
        columns={columns}
        data={data}
        sortable
        searchable
        exportable
        filterable
        stickyHeader
        pageSize={8}
      />
    </div>
  );
});
