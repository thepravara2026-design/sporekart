import { memo, useMemo, useState, useCallback } from 'react';
import { useStudentWorkspace } from '../state/WorkspaceContext';
import { RegistrySkeleton } from '../components/StudentSkeletons';
import { StudentSearchBar, StudentFilterPanel } from '../components/StudentSearchFilter';
import { StudentTable } from '../components/StudentTable';
import { StudentPagination } from '../components/StudentPagination';
import StudentEmptyState from '../components/StudentEmptyStates';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100];

const StudentArchivedPage = memo(function StudentArchivedPage() {
  const {
    students,
    searchQuery,
    setSearchQuery,
    filters,
    updateFilter,
    clearFilters,
    sort,
    handleSort,
    loading,
  } = useStudentWorkspace();

  const [archivedPage, setArchivedPage] = useState(1);
  const [archivedPageSize, setArchivedPageSize] = useState(10);

  const archivedStudents = useMemo(
    () => students.filter((s) => s.status === 'archived'),
    [students]
  );

  const filteredArchived = useMemo(
    () => archivedStudents.filter((s) => {
      if (!searchQuery) return true;
      const q = searchQuery.toLowerCase();
      return (
        s.fullName.toLowerCase().includes(q) ||
        s.studentId.toLowerCase().includes(q) ||
        s.email.toLowerCase().includes(q)
      );
    }),
    [archivedStudents, searchQuery]
  );

  const archivedTotalPages = Math.max(1, Math.ceil(filteredArchived.length / archivedPageSize));

  const paginatedArchived = useMemo(
    () => {
      const start = (archivedPage - 1) * archivedPageSize;
      return filteredArchived.slice(start, start + archivedPageSize);
    },
    [filteredArchived, archivedPage, archivedPageSize]
  );

  const handleArchivedPageChange = useCallback((p: number) => {
    setArchivedPage(Math.max(1, Math.min(p, archivedTotalPages)));
  }, [archivedTotalPages]);

  const handleArchivedPageSizeChange = useCallback((s: number) => {
    setArchivedPageSize(s);
    setArchivedPage(1);
  }, []);

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

  if (loading) {
    return <RegistrySkeleton />;
  }

  if (archivedStudents.length === 0) {
    return <StudentEmptyState type="noArchived" />;
  }

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>
          Archived Students
        </h1>
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)', margin: '4px 0 0 0' }}>
          {archivedStudents.length} archived student{archivedStudents.length !== 1 ? 's' : ''}
        </p>
      </div>

      <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
        <StudentSearchBar searchQuery={searchQuery} onSearchChange={setSearchQuery} />
        <div style={{ flex: 1 }} />
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

      {filteredArchived.length === 0 ? (
        <StudentEmptyState type="noSearchResults" onClearFilters={clearFilters} />
      ) : (
        <>
          <StudentTable
            students={paginatedArchived}
            selectedIds={new Set()}
            onToggleSelect={() => {}}
            onToggleSelectAll={() => {}}
            sortField={sort.field}
            sortDirection={sort.direction}
            onSort={handleSort}
          />
          <StudentPagination
            page={archivedPage}
            totalPages={archivedTotalPages}
            totalFiltered={filteredArchived.length}
            pageSize={archivedPageSize}
            onPageChange={handleArchivedPageChange}
            onPageSizeChange={handleArchivedPageSizeChange}
            pageSizeOptions={PAGE_SIZE_OPTIONS}
          />
        </>
      )}
    </div>
  );
});

export default StudentArchivedPage;
