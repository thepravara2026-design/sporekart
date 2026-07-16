import { memo, useMemo, useCallback } from 'react';
import { useStudentWorkspace } from '../state/WorkspaceContext';
import { RegistrySkeleton } from '../components/StudentSkeletons';
import { StudentSearchBar, StudentFilterPanel } from '../components/StudentSearchFilter';
import { StudentTable } from '../components/StudentTable';
import { StudentPagination } from '../components/StudentPagination';
import { StudentCard } from '../components/StudentCard';
import StudentStatusBadge from '../components/StudentStatusBadge';
import StudentEmptyState from '../components/StudentEmptyStates';
import StudentQuickActions from '../components/StudentQuickActions';
import { Icon } from '../../../../design-system/icons/Icon';
import type { StudentViewMode } from '../types';

const VIEW_OPTIONS: { value: StudentViewMode; icon: string; label: string }[] = [
  { value: 'table', icon: 'list', label: 'Table' },
  { value: 'grid', icon: 'grid', label: 'Grid' },
  { value: 'card', icon: 'grid', label: 'Cards' },
  { value: 'compact', icon: 'list', label: 'Compact' },
];

const StudentRegistryPage = memo(function StudentRegistryPage() {
  const {
    students,
    paginatedStudents,
    searchQuery,
    setSearchQuery,
    filters,
    updateFilter,
    clearFilters,
    sort,
    handleSort,
    viewMode,
    setViewMode,
    page,
    totalPages,
    totalFiltered,
    pageSize,
    handlePageChange,
    handlePageSizeChange,
    pageSizeOptions,
    selectedIds,
    toggleSelection,
    toggleSelectAll,
    loading,
  } = useStudentWorkspace();

  const availableCourses = useMemo(
    () => [...new Set(students.map((s) => s.course).filter(Boolean) as string[])],
    [students]
  );
  const availableLanguages = useMemo(
    () => [...new Set(students.map((s) => s.language))],
    [students]
  );
  const availableCategories = useMemo(
    () => [...new Set(students.map((s) => s.category))],
    [students]
  );

  const handleAddStudent = useCallback(() => {
    // Placeholder for add student dialog
  }, []);

  if (loading) {
    return <RegistrySkeleton />;
  }

  if (students.length === 0) {
    return <StudentEmptyState type="noStudents" onAddStudent={handleAddStudent} />;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>
          Student Registry
        </h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Master student registry &mdash; {totalFiltered} student{totalFiltered !== 1 ? 's' : ''}
        </p>
      </div>

      <StudentQuickActions onAddStudent={handleAddStudent} />

      <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
        <StudentSearchBar
          searchQuery={searchQuery}
          onSearchChange={setSearchQuery}
          loading={loading}
        />
        <div style={{ flex: 1 }} />
        <div style={{ display: 'flex', gap: 4 }} role="radiogroup" aria-label="View mode">
          {VIEW_OPTIONS.map((opt) => (
            <button
              key={opt.value}
              type="button"
              onClick={() => setViewMode(opt.value)}
              aria-label={opt.label}
              aria-pressed={viewMode === opt.value}
              style={{
                display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                width: 32, height: 32, borderRadius: 'var(--radius-sm)',
                background: viewMode === opt.value ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
                border: '1px solid var(--color-border-default)',
                cursor: 'pointer',
                color: viewMode === opt.value ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              }}
            >
              <Icon name={opt.icon} size={14} color="currentColor" />
            </button>
          ))}
        </div>
      </div>

      <StudentFilterPanel
        searchQuery={searchQuery}
        onSearchChange={setSearchQuery}
        filters={filters}
        onFilterChange={updateFilter}
        onClearFilters={clearFilters}
        availableCourses={availableCourses}
        availableLanguages={availableLanguages}
        availableCategories={availableCategories}
        loading={loading}
      />

      {totalFiltered === 0 ? (
        <StudentEmptyState
          type="noSearchResults"
          onClearFilters={clearFilters}
        />
      ) : (
        <>
          {viewMode === 'table' && (
            <StudentTable
              students={paginatedStudents}
              selectedIds={selectedIds}
              onToggleSelect={toggleSelection}
              onToggleSelectAll={toggleSelectAll}
              sortField={sort.field}
              sortDirection={sort.direction}
              onSort={handleSort}
            />
          )}

          {viewMode === 'grid' && (
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))',
              gap: 'var(--space-component-gap)',
            }}>
              {paginatedStudents.map((student) => (
                <div
                  key={student.id}
                  style={{
                    display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8,
                    padding: 'var(--space-4)',
                    background: selectedIds.has(student.id) ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
                    border: selectedIds.has(student.id) ? '2px solid var(--color-primary)' : '1px solid var(--color-border-default)',
                    borderRadius: 'var(--radius-card)',
                    cursor: 'pointer',
                    transition: 'all var(--duration-fast) var(--easing-standard)',
                  }}
                  onClick={() => toggleSelection(student.id)}
                  role="option"
                  aria-selected={selectedIds.has(student.id)}
                  tabIndex={0}
                  onKeyDown={(e) => {
                    if (e.key === 'Enter' || e.key === ' ') {
                      e.preventDefault();
                      toggleSelection(student.id);
                    }
                  }}
                >
                  <span style={{
                    width: 48, height: 48, borderRadius: 'var(--radius-full)',
                    background: 'var(--color-bg-primary-subtle)',
                    color: 'var(--color-primary)',
                    display: 'flex', alignItems: 'center', justifyContent: 'center',
                    fontSize: 18, fontWeight: 'var(--weight-bold)',
                  }}>
                    {student.fullName.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase()}
                  </span>
                  <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)', textAlign: 'center' }}>
                    {student.fullName}
                  </span>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', fontFamily: 'var(--font-family-mono)' }}>
                    {student.studentId}
                  </span>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', textAlign: 'center' }}>
                    {student.course || '-'}
                  </span>
                  <span><StudentStatusBadge status={student.status} size="sm" /></span>
                </div>
              ))}
            </div>
          )}

          {viewMode === 'card' && (
            <div style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))',
              gap: 'var(--space-component-gap)',
            }}>
              {paginatedStudents.map((student) => (
                <StudentCard
                  key={student.id}
                  student={student}
                  selected={selectedIds.has(student.id)}
                  onSelect={toggleSelection}
                />
              ))}
            </div>
          )}

          {viewMode === 'compact' && (
            <div
              style={{
                display: 'flex', flexDirection: 'column', gap: 2,
                background: 'var(--color-bg-surface-default)',
                border: '1px solid var(--color-border-default)',
                borderRadius: 'var(--radius-md)',
                overflow: 'hidden',
              }}
              role="listbox"
              aria-label="Compact student list"
            >
              {paginatedStudents.map((student) => {
                const isSelected = selectedIds.has(student.id);
                return (
                  <div
                    key={student.id}
                    role="option"
                    aria-selected={isSelected}
                    tabIndex={0}
                    onClick={() => toggleSelection(student.id)}
                    onKeyDown={(e) => {
                      if (e.key === 'Enter' || e.key === ' ') {
                        e.preventDefault();
                        toggleSelection(student.id);
                      }
                    }}
                    style={{
                      display: 'flex', alignItems: 'center', gap: 8,
                      padding: '6px 12px',
                      background: isSelected ? 'var(--color-bg-primary-subtle)' : 'transparent',
                      cursor: 'pointer',
                      borderBottom: '1px solid var(--color-border-subtle)',
                      fontSize: 'var(--text-body-sm)',
                    }}
                  >
                    <input
                      type="checkbox"
                      checked={isSelected}
                      onChange={() => toggleSelection(student.id)}
                      aria-label={`Select ${student.fullName}`}
                      style={{ cursor: 'pointer' }}
                    />
                    <span style={{ fontFamily: 'var(--font-family-mono)', fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', minWidth: 120 }}>
                      {student.studentId}
                    </span>
                    <span style={{ fontWeight: 'var(--weight-medium)', minWidth: 160 }}>{student.fullName}</span>
                    <span style={{ color: 'var(--color-text-secondary)', flex: 1 }}>{student.email}</span>
                    <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)', minWidth: 80 }}>{student.language}</span>
                    <span style={{ minWidth: 100 }}>
                      <span style={{
                        display: 'inline-block', padding: '1px 6px', borderRadius: 'var(--radius-xs)',
                        fontSize: 'var(--text-caption)',
                        background: 'var(--color-bg-skeleton-base)',
                        color: 'var(--color-text-secondary)',
                      }}>
                        {student.status}
                      </span>
                    </span>
                  </div>
                );
              })}
            </div>
          )}

          <StudentPagination
            page={page}
            totalPages={totalPages}
            totalFiltered={totalFiltered}
            pageSize={pageSize}
            onPageChange={handlePageChange}
            onPageSizeChange={handlePageSizeChange}
            pageSizeOptions={pageSizeOptions}
          />
        </>
      )}
    </div>
  );
});

export default StudentRegistryPage;
