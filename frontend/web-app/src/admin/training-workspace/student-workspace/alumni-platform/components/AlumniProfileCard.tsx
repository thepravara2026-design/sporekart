import { memo } from 'react';
import type { AlumniProfile } from '../types';
import { EngagementBadge } from './EngagementBadge';

export const AlumniProfileCard = memo(function AlumniProfileCard({ alumni }: { alumni: AlumniProfile }) {
  return (
    <div style={{ display: 'flex', gap: 12, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ width: 48, height: 48, borderRadius: '50%', background: 'var(--color-bg-skeleton-base)', flexShrink: 0, display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 20 }}>👤</div>
      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
          <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{alumni.fullName}</span>
          <EngagementBadge level={alumni.engagementLevel} />
        </div>
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{alumni.currentPosition} at {alumni.currentCompany}</span>
        <div style={{ display: 'flex', gap: 8, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', flexWrap: 'wrap' }}>
          <span>{alumni.batchName} • {alumni.graduationYear}</span>
          <span>{alumni.location}</span>
          {alumni.isAmbassador && <span style={{ color: '#2563eb' }}>★ Ambassador</span>}
          {alumni.isDonor && <span style={{ color: '#059669' }}>❤ Donor</span>}
        </div>
      </div>
    </div>
  );
});
