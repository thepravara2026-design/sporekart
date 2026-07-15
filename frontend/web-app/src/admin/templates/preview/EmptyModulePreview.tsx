import { memo, type CSSProperties } from 'react';
import { DataGrid } from '../../components/data-grid/DataGrid';
import type { DataGridColumn } from '../../components/data-grid/types';

const emptyColumns: DataGridColumn<Record<string, any>>[] = [
  { key: 'id', header: 'ID', sortable: true, width: '120px' },
  { key: 'name', header: 'Name', sortable: true, filterable: true, filterType: 'dropdown' },
  { key: 'status', header: 'Status', sortable: true, filterable: true, filterType: 'status', align: 'center' },
];

export const EmptyModulePreview = memo(function EmptyModulePreview() {
  const style: CSSProperties = { display: 'flex', flexDirection: 'column', gap: 16, padding: 24 };
  return (
    <div style={style}>
      <h1 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Empty Module</h1>
      <DataGrid
        columns={emptyColumns}
        data={[]}
        sortable
        searchable
        exportable
        stickyHeader
        pageSize={10}
        emptyMessage="No records found"
        emptyDescription="This module has no data yet."
      />
    </div>
  );
});
