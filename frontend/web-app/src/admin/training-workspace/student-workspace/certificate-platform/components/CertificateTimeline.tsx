import { memo } from 'react';
import type { Certificate } from '../types';
import { CERTIFICATE_STATUS_LABELS } from '../types';

interface CertificateTimelineProps {
  certificate: Certificate;
}

const statusOrder: string[] = ['draft', 'pending-approval', 'approved', 'generated', 'issued', 'shared', 'verified'];
const statusColors: Record<string, string> = { draft: '#6b7280', 'pending-approval': '#ca8a04', approved: '#2563eb', generated: '#2563eb', issued: '#16a34a', shared: '#0891b2', verified: '#16a34a' };

export const CertificateTimeline = memo(function CertificateTimeline({ certificate }: CertificateTimelineProps) {
  const currentIndex = statusOrder.indexOf(certificate.status);
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 4 }}>
      {statusOrder.map((step, i) => {
        const isActive = i <= currentIndex;
        const isCurrent = i === currentIndex;
        return (
          <div key={step} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: '4px 0' }}>
            <div style={{
              width: 12, height: 12, borderRadius: '50%', flexShrink: 0,
              background: isActive ? statusColors[step] : 'var(--color-bg-skeleton-base)',
              border: isCurrent ? `3px solid ${statusColors[step]}` : 'none',
            }} />
            <span style={{
              fontSize: 'var(--text-caption)', fontWeight: isCurrent ? 'var(--weight-semibold)' : 'var(--weight-normal)',
              color: isActive ? 'var(--color-text-primary)' : 'var(--color-text-tertiary)',
            }}>
              {CERTIFICATE_STATUS_LABELS[step as keyof typeof CERTIFICATE_STATUS_LABELS]}
            </span>
          </div>
        );
      })}
    </div>
  );
});
