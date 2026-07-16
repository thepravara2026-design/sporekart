import { memo } from 'react';
import type { Certificate } from '../types';
import { CertificateStatusBadge } from './CertificateStatusBadge';
import { CERTIFICATE_TYPE_LABELS } from '../types';

interface CertificateCardProps {
  certificate: Certificate;
  onSelect?: (id: string) => void;
  selected?: boolean;
}

export const CertificateCard = memo(function CertificateCard({ certificate: c, onSelect, selected }: CertificateCardProps) {
  return (
    <div
      onClick={() => onSelect?.(c.id)}
      onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(c.id); } }}
      tabIndex={onSelect ? 0 : undefined}
      role={onSelect ? 'button' : undefined}
      aria-label={`Certificate ${c.certificateNumber}`}
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
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{c.title}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'monospace' }}>{c.certificateNumber}</div>
        </div>
        <CertificateStatusBadge status={c.status} />
      </div>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{c.studentName} &middot; {c.courseName}</div>
      <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
        <span>{CERTIFICATE_TYPE_LABELS[c.certificateType]}</span>
        <span>&middot;</span>
        <span>Issued: {c.issueDate || '—'}</span>
        {c.expiryDate && <><span>&middot;</span><span>Expires: {c.expiryDate}</span></>}
      </div>
    </div>
  );
});
