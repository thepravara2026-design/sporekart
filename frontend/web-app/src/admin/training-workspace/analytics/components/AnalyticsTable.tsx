import { memo } from 'react';
import type { ReactNode } from 'react';

export interface AnalyticsTableColumn<T> {
  key: string;
  header: string;
  align?: 'left' | 'right' | 'center';
  render: (row: T) => ReactNode;
}

export interface AnalyticsTableProps<T> {
  caption: string;
  columns: AnalyticsTableColumn<T>[];
  rows: T[];
  rowKey: (row: T, index: number) => string;
  emptyMessage?: string;
}

function AnalyticsTableInner<T>({
  caption,
  columns,
  rows,
  rowKey,
  emptyMessage = 'No data available.',
}: AnalyticsTableProps<T>) {
  return (
    <div style={{ overflowX: 'auto', width: '100%' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <caption style={{ textAlign: 'left', padding: 'var(--space-1) 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-caption)' }}>
          {caption}
        </caption>
        <thead>
          <tr>
            {columns.map((col) => (
              <th
                key={col.key}
                scope="col"
                style={{
                  textAlign: col.align ?? 'left',
                  padding: 'var(--space-2) var(--space-3)',
                  borderBottom: '1px solid var(--color-border-default)',
                  color: 'var(--color-text-secondary)',
                  fontWeight: 600,
                  whiteSpace: 'nowrap',
                }}
              >
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.length === 0 ? (
            <tr>
              <td colSpan={columns.length} style={{ padding: 'var(--space-4)', textAlign: 'center', color: 'var(--color-text-secondary)' }}>
                {emptyMessage}
              </td>
            </tr>
          ) : (
            rows.map((row, i) => (
              <tr key={rowKey(row, i)}>
                {columns.map((col) => (
                  <td
                    key={col.key}
                    style={{
                      textAlign: col.align ?? 'left',
                      padding: 'var(--space-2) var(--space-3)',
                      borderBottom: '1px solid var(--color-border-default)',
                      color: 'var(--color-text-primary)',
                    }}
                  >
                    {col.render(row)}
                  </td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

const AnalyticsTable = memo(AnalyticsTableInner) as typeof AnalyticsTableInner;

export default AnalyticsTable;
