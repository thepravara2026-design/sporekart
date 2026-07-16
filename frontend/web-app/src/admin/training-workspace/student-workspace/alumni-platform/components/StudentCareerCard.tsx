import { memo } from 'react';
import type { StudentCareerProfile } from '../types';
import { StatusBadge } from './StatusBadge';

export const StudentCareerCard = memo(function StudentCareerCard({ profile }: { profile: StudentCareerProfile }) {
  return (
    <div style={{ display: 'flex', gap: 12, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ width: 40, height: 40, borderRadius: '50%', background: 'var(--color-bg-skeleton-base)', flexShrink: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18 }}>🎓</div>
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
          <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{profile.studentName}</span>
          <StatusBadge status={profile.placementStatus} />
        </div>
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{profile.targetRole} • {profile.preferredLocation}</span>
        <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          <span>Score: {profile.overallScore}%</span>
          <span>Interviews: {profile.interviewCount}</span>
          <span>Offers: {profile.offerCount}</span>
        </div>
        <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
          {profile.skills.slice(0, 4).map((s, i) => (
            <span key={i} style={{ padding: '1px 6px', borderRadius: 'var(--radius-full)', background: '#eff6ff', color: '#2563eb', fontSize: 'var(--text-caption)' }}>{s.name}</span>
          ))}
        </div>
      </div>
    </div>
  );
});
