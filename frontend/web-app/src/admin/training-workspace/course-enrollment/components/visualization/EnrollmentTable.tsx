import { Badge } from '../../../../../design-system/components/display/Badge';
import { Button } from '../../../../../design-system/components/core/Button';
import { StatusBadge } from './StatusBadge';
import {
  PRICING_MODEL_LABELS,
  formatCurrency,
  type EnrollmentRequest,
  type RegistrationStatus,
} from '../../data/enrollmentMockData';

interface EnrollmentTableProps {
  requests: EnrollmentRequest[];
  onStatusChange?: (id: string, status: RegistrationStatus) => void;
}

const ELIGIBILITY_VARIANT: Record<string, 'success' | 'danger' | 'warning'> = {
  pass: 'success',
  fail: 'danger',
  warning: 'warning',
};

const th: React.CSSProperties = { padding: '8px 12px', textAlign: 'left', whiteSpace: 'nowrap' };
const td: React.CSSProperties = { padding: '8px 12px', verticalAlign: 'middle' };

export function EnrollmentTable({ requests, onStatusChange }: EnrollmentTableProps) {
  if (requests.length === 0) {
    return <div style={{ padding: 'var(--space-6)', textAlign: 'center', color: 'var(--color-text-tertiary)' }}>No enrollment requests match the filters.</div>;
  }

  return (
    <div style={{ overflowX: 'auto' }}>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--font-size-sm)' }}>
        <caption style={{ position: 'absolute', width: 1, height: 1, overflow: 'hidden', clip: 'rect(0 0 0 0)' }}>
          Enrollment requests with applicant, pricing, eligibility and status
        </caption>
        <thead>
          <tr style={{ color: 'var(--color-text-tertiary)', borderBottom: '1px solid var(--color-border-default)' }}>
            <th scope="col" style={th}>Applicant</th>
            <th scope="col" style={th}>Course</th>
            <th scope="col" style={th}>Type</th>
            <th scope="col" style={th}>Pricing</th>
            <th scope="col" style={th}>Amount</th>
            <th scope="col" style={th}>Eligibility</th>
            <th scope="col" style={th}>Status</th>
            <th scope="col" style={th}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((r) => (
            <tr key={r.id} style={{ borderBottom: '1px solid var(--color-border-default)' }}>
              <td style={{ ...td, fontWeight: 600 }}>{r.applicantName}</td>
              <td style={td}>{r.courseName}</td>
              <td style={td}><Badge variant="neutral" size="sm">{r.applicantType}</Badge></td>
              <td style={td}>{PRICING_MODEL_LABELS[r.pricingModel]}</td>
              <td style={td}>{formatCurrency(r.amountPlaceholder)}</td>
              <td style={td}><Badge variant={ELIGIBILITY_VARIANT[r.eligibilityResult]} size="sm">{r.eligibilityResult}</Badge></td>
              <td style={td}><StatusBadge status={r.status} /></td>
              <td style={td}>
                <div style={{ display: 'flex', gap: 4 }}>
                  <Button size="sm" variant="success" disabled={r.status === 'approved'} onClick={() => onStatusChange?.(r.id, 'approved')} aria-label={`Approve ${r.applicantName}`}>Approve</Button>
                  <Button size="sm" variant="destructive" disabled={r.status === 'rejected'} onClick={() => onStatusChange?.(r.id, 'rejected')} aria-label={`Reject ${r.applicantName}`}>Reject</Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
