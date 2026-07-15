import { memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import { useDataGrid } from '../data-grid/DataGridProvider';

export const BulkActions = memo(function BulkActions() {
  const { selectedRows, clearSelection } = useDataGrid();
  const count = selectedRows.size;

  if (count === 0) return null;

  return (
    <div
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '8px 16px',
        background: 'var(--color-primary-alpha)',
        borderBottom: '1px solid var(--color-border)',
        gap: 12,
        flexWrap: 'wrap',
      }}
    >
      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
        <Icon name="check-square" size={16} />
        <span style={{ fontWeight: 500, fontSize: 'var(--text-body)' }}>
          {count} selected
        </span>
        <Button variant="ghost" size="sm" onClick={clearSelection}>
          Clear selection
        </Button>
      </div>
      <div style={{ display: 'flex', gap: 6 }}>
        <Button variant="outline" size="sm">
          <Icon name="download" size={14} /> Export
        </Button>
        <Button variant="outline" size="sm">
          <Icon name="edit" size={14} /> Update
        </Button>
        <Button variant="outline" size="sm" style={{ color: 'var(--color-error)', borderColor: 'var(--color-error)' }}>
          <Icon name="trash" size={14} /> Delete
        </Button>
      </div>
    </div>
  );
});
