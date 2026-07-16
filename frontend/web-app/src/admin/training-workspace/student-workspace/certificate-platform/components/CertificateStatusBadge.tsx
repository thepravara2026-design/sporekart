import { memo } from 'react';
import type { CertificateStatus } from '../types';
import { CERTIFICATE_STATUS_LABELS, CERTIFICATE_STATUS_VARIANTS } from '../types';

const variantColors: Record<string, string> = {
  default: '#6b7280', success: '#16a34a', warning: '#ca8a04',
  danger: '#dc2626', info: '#2563eb', neutral: '#9ca3af',
};

export const CertificateStatusBadge = memo(function CertificateStatusBadge({ status }: { status: CertificateStatus }) {
  const variant = CERTIFICATE_STATUS_VARIANTS[status];
  const color = variantColors[variant];
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', padding: '2px 8px', borderRadius: 'var(--radius-full)',
      fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
      backgroundColor: color + '18', color,
    }}>
      {CERTIFICATE_STATUS_LABELS[status]}
    </span>
  );
});
