import { memo } from 'react';
import type { VerificationRecord } from '../types';
import { VERIFICATION_STATUS_LABELS, CERTIFICATE_TYPE_LABELS } from '../types';

interface VerificationCardProps {
  record: VerificationRecord;
}

const statusColors: Record<string, string> = {
  verified: '#16a34a', pending: '#ca8a04', failed: '#dc2626', unverified: '#6b7280', expired: '#9ca3af',
};

export const VerificationCard = memo(function VerificationCard({ record }: VerificationCardProps) {
  const color = statusColors[record.status] || '#6b7280';
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: `1px solid ${color}44`,
      background: record.status === 'verified' ? '#f0fdf4' : record.status === 'failed' ? '#fef2f2' : 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{record.studentName}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'monospace' }}>{record.certificateNumber}</div>
        </div>
        <span style={{
          display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: color + '18', color,
        }}>
          {VERIFICATION_STATUS_LABELS[record.status]}
        </span>
      </div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{record.courseName}</span>
        <span>&middot;</span>
        <span>{CERTIFICATE_TYPE_LABELS[record.certificateType]}</span>
        <span>&middot;</span>
        <span>Via: {record.verificationMethod}</span>
      </div>
      {record.verifiedBy && (
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          Verified by: {record.verifiedBy}
        </div>
      )}
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        {record.notes}
      </div>
    </div>
  );
});
