import { memo } from 'react';
import type { AttendanceRecord } from '../types';
import { AttendanceStatusBadge } from './AttendanceStatusBadge';

interface AttendanceCardProps {
  record: AttendanceRecord;
  onSelect?: (id: string) => void;
  selected?: boolean;
}

export const AttendanceCard = memo(function AttendanceCard({ record, onSelect, selected }: AttendanceCardProps) {
  return (
    <div
      onClick={() => onSelect?.(record.id)}
      onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(record.id); } }}
      tabIndex={onSelect ? 0 : undefined}
      role={onSelect ? 'button' : undefined}
      aria-label={`Attendance ${record.attendanceId}`}
      style={{
        padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
        border: `1px solid ${selected ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
        background: selected ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
        cursor: onSelect ? 'pointer' : 'default',
        display: 'flex', flexDirection: 'column', gap: 8,
      }}
    >
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{record.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{record.attendanceId}</div>
        </div>
        <AttendanceStatusBadge status={record.attendanceStatus} />
      </div>
      <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{record.courseName}</div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{record.batchName}</span>
        <span>&middot;</span>
        <span>{record.trainingDate}</span>
        <span>&middot;</span>
        <span>{record.checkInTime || 'No check-in'}</span>
      </div>
      {record.remarks && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>
          {record.remarks}
        </div>
      )}
    </div>
  );
});
