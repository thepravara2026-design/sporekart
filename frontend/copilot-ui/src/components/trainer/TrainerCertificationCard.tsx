import type { Certification } from './types/trainer';

const recColors: Record<string, string> = {
  PASS: 'var(--cp-color-success)', NEEDS_IMPROVEMENT: 'var(--cp-color-warning)',
  ADDITIONAL_TRAINING: 'var(--cp-color-danger)',
};
const statusColors: Record<string, string> = {
  PENDING: 'var(--cp-color-warning)', APPROVED: 'var(--cp-color-success)', REJECTED: 'var(--cp-color-danger)',
};

function scoreColor(score: number): string {
  if (score >= 70) return 'var(--cp-color-success)';
  if (score >= 40) return 'var(--cp-color-warning)';
  return 'var(--cp-color-danger)';
}

export function TrainerCertificationCard({ certification }: { certification: Certification }) {
  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, padding: 16, background: 'var(--cp-color-surface)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
        <div>
          <h3 style={{ margin: 0, fontSize: 16, color: 'var(--cp-color-text)' }}>{certification.studentName}</h3>
          <div style={{ fontSize: 12, color: 'var(--cp-color-text-muted)', marginTop: 2 }}>{certification.courseName}</div>
        </div>
        <div style={{ display: 'flex', gap: 6 }}>
          <span style={{ background: recColors[certification.recommendation] || '#888', color: '#fff', padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>
            {certification.recommendation.replace(/_/g, ' ')}
          </span>
          <span style={{ background: statusColors[certification.status] || '#888', color: '#fff', padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>
            {certification.status}
          </span>
        </div>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 12 }}>
        <div style={{ position: 'relative', width: 56, height: 56 }}>
          <svg viewBox="0 0 36 36" style={{ width: '100%', height: '100%', transform: 'rotate(-90deg)' }}>
            <circle cx="18" cy="18" r="15.5" fill="none" stroke="var(--cp-color-border)" strokeWidth="3" />
            <circle cx="18" cy="18" r="15.5" fill="none" stroke={scoreColor(certification.overallScore)} strokeWidth="3"
              strokeDasharray={`${certification.overallScore * 0.967}, 96.7`} strokeLinecap="round" />
          </svg>
          <div style={{ position: 'absolute', inset: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 14, fontWeight: 700, color: scoreColor(certification.overallScore) }}>
            {certification.overallScore}%
          </div>
        </div>
        <div style={{ fontSize: 12, color: 'var(--cp-color-text-secondary)', lineHeight: 1.6 }}>
          <div>Certification: <strong>{certification.certificationId}</strong></div>
          <div>Student ID: {certification.studentId}</div>
        </div>
      </div>
      <div style={{ fontSize: 11, color: 'var(--cp-color-text-muted)', borderTop: '1px solid var(--cp-color-border)', paddingTop: 8, marginTop: 4 }}>
        Recommendation: <span style={{ color: recColors[certification.recommendation], fontWeight: 600 }}>{certification.recommendation.replace(/_/g, ' ')}</span>
        {certification.status === 'PENDING' && ' — Awaiting approval'}
      </div>
    </div>
  );
}
