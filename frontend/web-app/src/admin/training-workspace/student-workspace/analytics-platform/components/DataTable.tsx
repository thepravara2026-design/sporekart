import { memo } from 'react';

interface Column {
  key: string;
  label: string;
  sortable?: boolean;
  render?: (value: unknown, row: Record<string, unknown>) => React.ReactNode;
}

interface DataTableProps {
  columns: Column[];
  data: Record<string, unknown>[];
  sortKey?: string;
  sortDirection?: 'asc' | 'desc';
  onSort?: (key: string) => void;
}

export const DataTable = memo(function DataTable({ columns, data, sortKey, sortDirection, onSort }: DataTableProps) {
  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid var(--color-border-default)' }}>
            {columns.map((col) => (
              <th key={col.key} onClick={col.sortable ? () => onSort?.(col.key) : undefined} style={{ textAlign: 'left', padding: '8px 12px', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-secondary)', cursor: col.sortable ? 'pointer' : 'default', whiteSpace: 'nowrap' }}>
                {col.label}
                {col.sortable && sortKey === col.key && <span style={{ marginLeft: 4 }}>{sortDirection === 'asc' ? '↑' : '↓'}</span>}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, i) => (
            <tr key={i} style={{ borderBottom: '1px solid var(--color-border-subtle)' }}>
              {columns.map((col) => (
                <td key={col.key} style={{ padding: '8px 12px', color: 'var(--color-text-primary)' }}>
                  {col.render ? col.render(row[col.key], row) : String(row[col.key] ?? '')}
                </td>
              ))}
            </tr>
          ))}
          {data.length === 0 && (
            <tr>
              <td colSpan={columns.length} style={{ padding: 24, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No data available</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
});
