import { memo } from 'react';
import type { EnrollmentRequest } from '../types';
import { EnrollmentStatusBadge } from './EnrollmentStatusBadge';
import { ADMISSION_TYPE_LABELS } from '../types';

interface EnrollmentTableProps {
  requests: EnrollmentRequest[];
  onSelect: (id: string) => void;
  selectedId: string | null;
}

export const EnrollmentTable = memo(function EnrollmentTable({ requests, onSelect, selectedId }: EnrollmentTableProps) {
  return (
    <div className="enrollment-table-wrapper" style={{ overflowX: 'auto' }}>
      <table className="enrollment-table" style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid var(--color-border-default)', textAlign: 'left' }}>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>App No.</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Student</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Course</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Type</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Status</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Priority</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Date</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((req) => (
            <tr
              key={req.id}
              onClick={() => onSelect(req.id)}
              style={{
                borderBottom: '1px solid var(--color-border-subtle)',
                cursor: 'pointer',
                background: selectedId === req.id ? 'var(--color-bg-primary-subtle)' : 'transparent',
              }}
              onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); onSelect(req.id); } }}
              tabIndex={0}
              role="row"
              aria-label={`Enrollment ${req.enrollmentId}`}
            >
              <td style={{ padding: '10px 12px', fontWeight: 'var(--weight-medium)' }}>{req.applicationNumber}</td>
              <td style={{ padding: '10px 12px' }}>
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{req.studentName}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{req.studentId}</div>
              </td>
              <td style={{ padding: '10px 12px', maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                {req.courseName}
              </td>
              <td style={{ padding: '10px 12px' }}>{ADMISSION_TYPE_LABELS[req.admissionType]}</td>
              <td style={{ padding: '10px 12px' }}><EnrollmentStatusBadge status={req.enrollmentStatus} /></td>
              <td style={{ padding: '10px 12px' }}>
                <span style={{
                  color: req.priority === 'urgent' ? '#dc2626' : req.priority === 'high' ? '#ca8a04' : 'var(--color-text-secondary)',
                  fontWeight: req.priority === 'urgent' || req.priority === 'high' ? 'var(--weight-semibold)' : 'var(--weight-normal)',
                  textTransform: 'capitalize',
                }}>
                  {req.priority}
                </span>
              </td>
              <td style={{ padding: '10px 12px', color: 'var(--color-text-tertiary)', whiteSpace: 'nowrap' }}>{req.applicationDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
