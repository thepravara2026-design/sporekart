import { memo } from 'react';
import type { PlacementDrive } from '../types';
import { DRIVE_STAGE_LABELS } from '../types';

const STAGE_COLORS: Record<string, { bg: string; color: string }> = {
  announced: { bg: '#eff6ff', color: '#2563eb' },
  'registrations-open': { bg: '#f0fdf4', color: '#16a34a' },
  'registrations-closed': { bg: '#fefce8', color: '#ca8a04' },
  scheduled: { bg: '#f0fdf4', color: '#059669' },
  'in-progress': { bg: '#fefce8', color: '#ca8a04' },
  'results-pending': { bg: '#fef2f2', color: '#dc2626' },
  completed: { bg: '#f3f4f6', color: '#6b7280' },
  cancelled: { bg: '#fef2f2', color: '#dc2626' },
};

export const PlacementDriveCard = memo(function PlacementDriveCard({ drive }: { drive: PlacementDrive }) {
  const sc = STAGE_COLORS[drive.stage] || { bg: '#f3f4f6', color: '#6b7280' };
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8, padding: 12, borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 4 }}>
        <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body)' }}>{drive.name}</span>
        <span style={{ padding: '2px 8px', borderRadius: 'var(--radius-full)', fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)', background: sc.bg, color: sc.color }}>{DRIVE_STAGE_LABELS[drive.stage]}</span>
      </div>
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{drive.companyName} • {drive.mode.replace(/-/g, ' ')}</span>
      <div style={{ display: 'flex', gap: 16, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Registered: {drive.registeredStudents}</span>
        <span>Shortlisted: {drive.shortlistedStudents}</span>
        <span>Selected: {drive.selectedStudents}</span>
      </div>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        <span>Rounds: {drive.currentRound}/{drive.totalRounds}</span>
        <span>Drive: {drive.driveDate}</span>
      </div>
    </div>
  );
});
