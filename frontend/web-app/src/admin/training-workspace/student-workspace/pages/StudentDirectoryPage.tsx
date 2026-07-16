import { memo, useMemo, useState, useCallback } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';
import { Skeleton } from '../../../../design-system/components/display/Skeleton';
import { StudentCard, StudentCompactCard } from '../components/StudentCard';
import { StudentSearchBar } from '../components/StudentSearchFilter';
import { StudentPagination } from '../components/StudentPagination';
import StudentEmptyState from '../components/StudentEmptyStates';
import { useStudentWorkspace } from '../state/WorkspaceContext';
import type { Student } from '../types';

type DirectoryGroupBy = 'none' | 'status' | 'category' | 'language' | 'course' | 'state';

const GROUP_OPTIONS: { value: DirectoryGroupBy; label: string; icon: string }[] = [
  { value: 'none', label: 'Flat', icon: 'list' },
  { value: 'status', label: 'By Status', icon: 'tag' },
  { value: 'category', label: 'By Category', icon: 'folder' },
  { value: 'language', label: 'By Language', icon: 'globe' },
  { value: 'course', label: 'By Course', icon: 'book-open' },
  { value: 'state', label: 'By State', icon: 'map-pin' },
];

const StudentDirectoryPage = memo(function StudentDirectoryPage() {
  const {
    students,
    filteredStudents,
    paginatedStudents,
    searchQuery,
    setSearchQuery,
    clearFilters,
    page,
    totalPages,
    totalFiltered,
    pageSize,
    handlePageChange,
    handlePageSizeChange,
    pageSizeOptions,
    selectedIds,
    toggleSelection,
    loading,
  } = useStudentWorkspace();

  const [groupBy, setGroupBy] = useState<DirectoryGroupBy>('none');
  const [viewMode, setViewMode] = useState<'card' | 'compact'>('card');

  const groupedStudents = useMemo(() => {
    if (groupBy === 'none' || !filteredStudents.length) return null;

    const groups: Record<string, Student[]> = {};
    for (const student of filteredStudents) {
      let key = '';
      switch (groupBy) {
        case 'status':
          key = student.status;
          break;
        case 'category':
          key = student.category;
          break;
        case 'language':
          key = student.language;
          break;
        case 'course':
          key = student.course || 'Not Assigned';
          break;
        case 'state':
          key = student.state;
          break;
      }
      if (!groups[key]) groups[key] = [];
      groups[key].push(student);
    }

    return Object.entries(groups)
      .sort(([a], [b]) => a.localeCompare(b))
      .map(([key, items]) => ({ key, items }));
  }, [filteredStudents, groupBy]);

  const handleAddStudent = useCallback(() => {}, []);

  if (loading) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
        <div>
          <Skeleton variant="text" width="30%" height={28} />
          <Skeleton variant="text" width="50%" height={16} />
        </div>
        <div style={{ display: 'flex', gap: 8 }}>
          <Skeleton variant="rounded" width={240} height={36} />
          <Skeleton variant="rounded" width={100} height={36} />
        </div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {Array.from({ length: 6 }).map((_, i) => (
            <Card key={i} padding="md">
              <div style={{ display: 'flex', gap: 12 }}>
                <Skeleton variant="circular" width={40} height={40} />
                <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 6 }}>
                  <Skeleton variant="text" width="60%" height={14} />
                  <Skeleton variant="text" width="80%" height={12} />
                  <Skeleton variant="text" width="40%" height={12} />
                </div>
              </div>
            </Card>
          ))}
        </div>
      </div>
    );
  }

  if (students.length === 0) {
    return <StudentEmptyState type="noStudents" onAddStudent={handleAddStudent} />;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>
          Student Directory
        </h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          Browse and explore students across multiple dimensions
        </p>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
        <StudentSearchBar
          searchQuery={searchQuery}
          onSearchChange={setSearchQuery}
          loading={loading}
        />
        <div style={{ flex: 1 }} />
        <div style={{ display: 'flex', gap: 4 }} role="radiogroup" aria-label="Group by">
          {GROUP_OPTIONS.map((opt) => (
            <button
              key={opt.value}
              type="button"
              onClick={() => setGroupBy(opt.value)}
              aria-label={opt.label}
              aria-pressed={groupBy === opt.value}
              style={{
                display: 'inline-flex', alignItems: 'center', gap: 4,
                padding: '4px 8px', height: 28,
                borderRadius: 'var(--radius-sm)',
                background: groupBy === opt.value ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
                border: '1px solid var(--color-border-default)',
                cursor: 'pointer',
                color: groupBy === opt.value ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
              }}
            >
              <Icon name={opt.icon} size={12} color="currentColor" />
              <span>{opt.label}</span>
            </button>
          ))}
        </div>
        <div style={{ width: 1, height: 20, background: 'var(--color-border-default)', margin: '0 4px' }} />
        <div style={{ display: 'flex', gap: 4 }} role="radiogroup" aria-label="View mode">
          {(['card', 'compact'] as const).map((mode) => (
            <button
              key={mode}
              type="button"
              onClick={() => setViewMode(mode)}
              aria-label={mode === 'card' ? 'Card view' : 'Compact view'}
              aria-pressed={viewMode === mode}
              style={{
                display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
                width: 32, height: 32, borderRadius: 'var(--radius-sm)',
                background: viewMode === mode ? 'var(--color-bg-primary-subtle)' : 'var(--color-bg-surface-default)',
                border: '1px solid var(--color-border-default)',
                cursor: 'pointer',
                color: viewMode === mode ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              }}
            >
              <Icon name={mode === 'card' ? 'grid' : 'list'} size={14} color="currentColor" />
            </button>
          ))}
        </div>
      </div>

      {totalFiltered === 0 ? (
        <StudentEmptyState type="noSearchResults" onClearFilters={clearFilters} />
      ) : (
        <>
          {groupedStudents ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
              {groupedStudents.map(({ key, items }) => (
                <section key={key} aria-label={`Students in ${key}`}>
                  <div style={{
                    display: 'flex', alignItems: 'center', gap: 8,
                    padding: '8px 0', borderBottom: '2px solid var(--color-border-default)',
                    marginBottom: 8,
                  }}>
                    <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)', textTransform: 'capitalize' }}>
                      {key.replace(/-/g, ' ')}
                    </span>
                    <span style={{
                      fontSize: 'var(--text-caption)', padding: '1px 6px',
                      borderRadius: 'var(--radius-full)',
                      background: 'var(--color-bg-primary-subtle)',
                      color: 'var(--color-primary)',
                    }}>
                      {items.length}
                    </span>
                  </div>
                  {viewMode === 'card' ? (
                    <div style={{
                      display: 'grid',
                      gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
                      gap: 'var(--space-component-gap)',
                    }}>
                      {items.map((student) => (
                        <StudentCard
                          key={student.id}
                          student={student}
                          selected={selectedIds.has(student.id)}
                          onSelect={toggleSelection}
                        />
                      ))}
                    </div>
                  ) : (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                      {items.map((student) => (
                        <StudentCompactCard
                          key={student.id}
                          student={student}
                          selected={selectedIds.has(student.id)}
                          onSelect={toggleSelection}
                        />
                      ))}
                    </div>
                  )}
                </section>
              ))}
            </div>
          ) : (
            <>
              {viewMode === 'card' ? (
                <div style={{
                  display: 'grid',
                  gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
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
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                  {paginatedStudents.map((student) => (
                    <StudentCompactCard
                      key={student.id}
                      student={student}
                      selected={selectedIds.has(student.id)}
                      onSelect={toggleSelection}
                    />
                  ))}
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
        </>
      )}
    </div>
  );
});

export default StudentDirectoryPage;
