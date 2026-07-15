import { memo } from 'react';
import { useMovementData } from '../hooks/useMovementData';
import { StatusBadge } from '../../../components/status/StatusBadge';

export const AuditPage = memo(function AuditPage() {
  const { auditRecords, loading } = useMovementData();

  if (loading) return <div>Loading...</div>;
  if (auditRecords.length === 0) return <div style={{ padding: 48, textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No audit records found.</div>;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
      <h3 style={{ margin: 0, fontSize: 'var(--text-h3, 18px)', fontWeight: 700 }}>Audit Trail ({auditRecords.length})</h3>
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body)', background: 'var(--color-surface)', borderRadius: 'var(--radius-lg)', border: '1px solid var(--color-border)' }}>
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)' }}>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Timestamp</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>User</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Action</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Field</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>Old Value</th>
              <th style={{ padding: 12, textAlign: 'left', fontWeight: 600 }}>New Value</th>
            </tr>
          </thead>
          <tbody>
            {auditRecords.map((record) => (
              <tr key={record.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: 12, whiteSpace: 'nowrap' }}>{new Date(record.timestamp).toLocaleString()}</td>
                <td style={{ padding: 12, fontWeight: 500 }}>{record.user}</td>
                <td style={{ padding: 12 }}><StatusBadge status={record.action} variant="info" /></td>
                <td style={{ padding: 12 }}>{record.field}</td>
                <td style={{ padding: 12, maxWidth: 150, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontSize: 'var(--text-caption)' }}>{record.oldValue}</td>
                <td style={{ padding: 12, maxWidth: 150, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontSize: 'var(--text-caption)' }}>{record.newValue}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
