import { memo } from 'react';
import type { EnrollmentStatus } from '../types';
import { ENROLLMENT_STATUS_LABELS } from '../types';

interface EnrollmentSearchFilterProps {
  searchTerm: string;
  onSearchChange: (term: string) => void;
  statusFilter: EnrollmentStatus | 'all';
  onStatusFilterChange: (status: EnrollmentStatus | 'all') => void;
  totalResults: number;
}

const statusOptions: (EnrollmentStatus | 'all')[] = [
  'all', 'draft', 'submitted', 'under-review', 'pending-approval',
  'approved', 'rejected', 'seat-reserved', 'batch-assigned', 'enrolled', 'cancelled', 'archived',
];

export const EnrollmentSearchFilter = memo(function EnrollmentSearchFilter({
  searchTerm, onSearchChange, statusFilter, onStatusFilterChange, totalResults,
}: EnrollmentSearchFilterProps) {
  return (
    <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap', alignItems: 'center' }}>
      <div style={{ position: 'relative', flex: '1 1 240px', maxWidth: 320 }}>
        <input
          type="search"
          placeholder="Search by ID, name, course..."
          value={searchTerm}
          onChange={(e) => onSearchChange(e.target.value)}
          aria-label="Search enrollments"
          style={{
            width: '100%', padding: '6px 12px', borderRadius: 'var(--radius-sm)',
            border: '1px solid var(--color-border-default)',
            background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body-sm)',
          }}
        />
      </div>
      <select
        value={statusFilter}
        onChange={(e) => onStatusFilterChange(e.target.value as EnrollmentStatus | 'all')}
        aria-label="Filter by status"
        style={{
          padding: '6px 12px', borderRadius: 'var(--radius-sm)',
          border: '1px solid var(--color-border-default)',
          background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)',
          fontSize: 'var(--text-body-sm)',
        }}
      >
        {statusOptions.map((s) => (
          <option key={s} value={s}>{s === 'all' ? 'All Statuses' : ENROLLMENT_STATUS_LABELS[s]}</option>
        ))}
      </select>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
        {totalResults} result{totalResults !== 1 ? 's' : ''}
      </span>
    </div>
  );
});
