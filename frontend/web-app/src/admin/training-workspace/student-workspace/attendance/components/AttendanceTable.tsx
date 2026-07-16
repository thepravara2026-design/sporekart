import { memo } from 'react';
import type { AttendanceRecord } from '../types';
import { AttendanceStatusBadge } from './AttendanceStatusBadge';

interface AttendanceTableProps {
  records: AttendanceRecord[];
  onSelect?: (id: string) => void;
  selectedId?: string | null;
}

export const AttendanceTable = memo(function AttendanceTable({ records, onSelect, selectedId }: AttendanceTableProps) {
  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid var(--color-border-default)', textAlign: 'left' }}>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Student</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Date</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Course</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Batch</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Status</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Check-in</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Check-out</th>
          </tr>
        </thead>
        <tbody>
          {records.map((rec) => (
            <tr
              key={rec.id}
              onClick={() => onSelect?.(rec.id)}
              onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(rec.id); } }}
              tabIndex={onSelect ? 0 : undefined}
              role={onSelect ? 'button' : undefined}
              aria-label={`Attendance ${rec.attendanceId}`}
              style={{
                borderBottom: '1px solid var(--color-border-subtle)',
                cursor: onSelect ? 'pointer' : 'default',
                background: selectedId === rec.id ? 'var(--color-bg-primary-subtle)' : 'transparent',
              }}
            >
              <td style={{ padding: '10px 12px' }}>
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{rec.studentName}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{rec.studentId}</div>
              </td>
              <td style={{ padding: '10px 12px', whiteSpace: 'nowrap' }}>{rec.trainingDate}</td>
              <td style={{ padding: '10px 12px', maxWidth: 160, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{rec.courseName}</td>
              <td style={{ padding: '10px 12px' }}>{rec.batchName}</td>
              <td style={{ padding: '10px 12px' }}><AttendanceStatusBadge status={rec.attendanceStatus} /></td>
              <td style={{ padding: '10px 12px', color: 'var(--color-text-tertiary)' }}>{rec.checkInTime || '-'}</td>
              <td style={{ padding: '10px 12px', color: 'var(--color-text-tertiary)' }}>{rec.checkOutTime || '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
