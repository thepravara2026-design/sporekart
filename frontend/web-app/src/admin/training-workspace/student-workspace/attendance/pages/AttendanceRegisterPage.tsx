import { useMemo, useState, useCallback } from 'react';
import { useAttendance } from '../state/AttendanceContext';
import { StudentSearchBar } from '../../components/StudentSearchFilter';
import { StudentPagination } from '../../components/StudentPagination';
import { AttendanceTable } from '../components/AttendanceTable';
import { AttendanceCard } from '../components/AttendanceCard';
import { EmptyState } from '../components/EmptyStates';
import { ATTENDANCE_STATUS_LABELS } from '../types';
import type { AttendanceStatus } from '../types';

const STATUS_OPTIONS: (AttendanceStatus | 'all')[] = [
  'all', 'present', 'absent', 'late', 'half-day', 'excused', 'medical-leave',
];

export function AttendanceRegisterPage() {
  const { records, searchTerm, setSearchTerm, statusFilter, setStatusFilter } = useAttendance();
  const [viewMode, setViewMode] = useState<'table' | 'card'>('table');
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const filtered = useMemo(() => {
    let result = records;
    if (statusFilter !== 'all') result = result.filter((r) => r.attendanceStatus === statusFilter);
    if (searchTerm) {
      const t = searchTerm.toLowerCase();
      result = result.filter((r) => r.studentName.toLowerCase().includes(t) || r.attendanceId.toLowerCase().includes(t) || r.courseName.toLowerCase().includes(t));
    }
    return result;
  }, [records, searchTerm, statusFilter]);

  const paginated = useMemo(() => {
    const start = (page - 1) * pageSize;
    return filtered.slice(start, start + pageSize);
  }, [filtered, page, pageSize]);

  const totalPages = Math.max(1, Math.ceil(filtered.length / pageSize));
  const clearFilters = useCallback(() => { setSearchTerm(''); setStatusFilter('all'); }, [setSearchTerm, setStatusFilter]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Attendance Register</h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Daily attendance records across all batches
        </p>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
        <StudentSearchBar searchQuery={searchTerm} onSearchChange={setSearchTerm} />
        <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value as AttendanceStatus | 'all')} aria-label="Filter by status" style={{ padding: '6px 12px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', fontSize: 'var(--text-body-sm)', height: 36 }}>
          {STATUS_OPTIONS.map((s) => (<option key={s} value={s}>{s === 'all' ? 'All Statuses' : ATTENDANCE_STATUS_LABELS[s]}</option>))}
        </select>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{filtered.length} records</span>
        <div style={{ flex: 1 }} />
        <div style={{ display: 'flex', gap: 4 }} role="radiogroup" aria-label="View mode">
          {(['table', 'card'] as const).map((mode) => (
            <button key={mode} type="button" onClick={() => setViewMode(mode)} aria-label={mode === 'table' ? 'Table view' : 'Card view'} aria-pressed={viewMode === mode} style={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'center', width: 32, height: 32, borderRadius: 'var(--radius-sm)', background: viewMode === mode ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)', cursor: 'pointer', color: viewMode === mode ? 'var(--color-primary)' : 'var(--color-text-secondary)' }}>
              {mode === 'table' ? '📋' : '📇'}
            </button>
          ))}
        </div>
      </div>

      {filtered.length === 0 ? (
        <EmptyState type="noSearchResults" onClearFilters={clearFilters} />
      ) : viewMode === 'table' ? (
        <div style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)' }}>
          <AttendanceTable records={paginated} />
          <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={pageSize} onPageChange={setPage} onPageSizeChange={(s) => { setPageSize(s); setPage(1); }} pageSizeOptions={[10, 20, 50]} />
        </div>
      ) : (
        <>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {paginated.map((rec) => (<AttendanceCard key={rec.id} record={rec} />))}
          </div>
          <StudentPagination page={page} totalPages={totalPages} totalFiltered={filtered.length} pageSize={pageSize} onPageChange={setPage} onPageSizeChange={(s) => { setPageSize(s); setPage(1); }} pageSizeOptions={[10, 20, 50]} />
        </>
      )}
    </div>
  );
}
