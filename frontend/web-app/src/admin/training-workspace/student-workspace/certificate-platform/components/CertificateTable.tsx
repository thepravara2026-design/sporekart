import { memo } from 'react';
import type { Certificate } from '../types';
import { CertificateStatusBadge } from './CertificateStatusBadge';
import { CERTIFICATE_TYPE_LABELS } from '../types';

interface CertificateTableProps {
  certificates: Certificate[];
  onSelect?: (id: string) => void;
  selectedId?: string | null;
}

export const CertificateTable = memo(function CertificateTable({ certificates, onSelect, selectedId }: CertificateTableProps) {
  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }}>
        <thead>
          <tr style={{ borderBottom: '2px solid var(--color-border-default)', textAlign: 'left' }}>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Certificate</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Student</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Course</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Type</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Issue Date</th>
            <th style={{ padding: '8px 12px', fontWeight: 'var(--weight-semibold)' }}>Status</th>
          </tr>
        </thead>
        <tbody>
          {certificates.map((c) => (
            <tr
              key={c.id}
              onClick={() => onSelect?.(c.id)}
              onKeyDown={(e) => { if ((e.key === 'Enter' || e.key === ' ') && onSelect) { e.preventDefault(); onSelect(c.id); } }}
              tabIndex={onSelect ? 0 : undefined}
              role={onSelect ? 'button' : undefined}
              style={{ borderBottom: '1px solid var(--color-border-subtle)', cursor: onSelect ? 'pointer' : 'default', background: selectedId === c.id ? 'var(--color-bg-primary-subtle)' : 'transparent' }}
            >
              <td style={{ padding: '10px 12px' }}>
                <div style={{ fontWeight: 'var(--weight-medium)' }}>{c.title}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'monospace' }}>{c.certificateNumber}</div>
              </td>
              <td style={{ padding: '10px 12px' }}>{c.studentName}</td>
              <td style={{ padding: '10px 12px', maxWidth: 140, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{c.courseName}</td>
              <td style={{ padding: '10px 12px', fontSize: 'var(--text-caption)' }}>{CERTIFICATE_TYPE_LABELS[c.certificateType]}</td>
              <td style={{ padding: '10px 12px', fontSize: 'var(--text-caption)' }}>{c.issueDate || '—'}</td>
              <td style={{ padding: '10px 12px' }}><CertificateStatusBadge status={c.status} /></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
