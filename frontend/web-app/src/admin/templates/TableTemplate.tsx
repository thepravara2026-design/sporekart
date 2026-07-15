import { memo } from 'react';
import { DataGrid } from '../components/data-grid/DataGrid';
import type { DataGridColumn } from '../components/data-grid/types';

export const exampleColumns: DataGridColumn<Record<string, any>>[] = [
  {
    key: 'id',
    header: 'ID',
    sortable: true,
    width: '120px',
    render: (row) => <span style={{ fontFamily: 'var(--font-family-mono)' }}>{String(row['id'])}</span>,
  },
  {
    key: 'name',
    header: 'Name',
    sortable: true,
    filterable: true,
    filterType: 'dropdown',
    filterOptions: [
      { label: 'Acme Corp', value: 'acme' },
      { label: 'Globex', value: 'globex' },
      { label: 'Initech', value: 'initech' },
    ],
  },
  {
    key: 'category',
    header: 'Category',
    sortable: true,
    filterable: true,
    filterType: 'dropdown',
    filterOptions: [
      { label: 'Retail', value: 'retail' },
      { label: 'Wholesale', value: 'wholesale' },
      { label: 'Partner', value: 'partner' },
    ],
  },
  {
    key: 'status',
    header: 'Status',
    sortable: true,
    filterable: true,
    filterType: 'status',
    align: 'center',
    filterOptions: [
      { label: 'Active', value: 'active' },
      { label: 'Pending', value: 'pending' },
      { label: 'Archived', value: 'archived' },
    ],
    render: (row) => {
      const status = String(row['status']);
      const palette: Record<string, string> = {
        active: '#2f6f4f',
        pending: '#d97706',
        archived: '#7c3aed',
      };
      const color = palette[status] ?? 'var(--color-text-secondary)';
      return (
        <span
          style={{
            display: 'inline-block',
            padding: '2px 10px',
            borderRadius: 'var(--radius-pill)',
            fontSize: 'var(--text-caption)',
            fontWeight: 'var(--weight-medium)',
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
    width: '140px',
    render: (row) => (
      <span style={{ fontVariantNumeric: 'tabular-nums' }}>{`$${Number(row['amount'] || 0).toFixed(2)}`}</span>
    ),
  },
];

export const createColumns = (): DataGridColumn<Record<string, any>>[] => exampleColumns;

const exampleData: Record<string, any>[] = [
  { id: '1001', name: 'Acme Corp', category: 'retail', status: 'active', amount: 1240.5 },
  { id: '1002', name: 'Globex', category: 'wholesale', status: 'pending', amount: 8750.0 },
  { id: '1003', name: 'Initech', category: 'partner', status: 'active', amount: 320.75 },
  { id: '1004', name: 'Acme Corp', category: 'retail', status: 'archived', amount: 99.99 },
  { id: '1005', name: 'Globex', category: 'wholesale', status: 'active', amount: 5400.25 },
];

export const ExampleTable = memo(function ExampleTable() {
  return (
    <DataGrid
      columns={exampleColumns}
      data={exampleData}
      sortable
      searchable
      exportable
      stickyHeader
      pageSize={10}
      emptyMessage="No records found"
    />
  );
});
