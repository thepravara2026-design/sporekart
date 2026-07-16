import { memo, useCallback } from 'react';
import type { Student, StudentSortField } from '../types';
import { Icon } from '../../../../design-system/icons/Icon';
import { Avatar } from '../../../../design-system/components/display/Avatar';
import StudentStatusBadge from './StudentStatusBadge';

interface ColumnDef {
  key: string;
  label: string;
  sortable?: boolean;
  sortField?: StudentSortField;
  width?: string;
  align?: 'left' | 'center' | 'right';
}

interface StudentTableProps {
  students: Student[];
  selectedIds: Set<string>;
  onToggleSelect: (id: string) => void;
  onToggleSelectAll: () => void;
  sortField: StudentSortField;
  sortDirection: 'asc' | 'desc';
  onSort: (field: StudentSortField) => void;
  onArchive?: (id: string) => void;
  onRestore?: (id: string) => void;
  loading?: boolean;
}

const COLUMNS: ColumnDef[] = [
  { key: 'studentId', label: 'Student ID', sortable: true, sortField: 'name', width: '110px' },
  { key: 'name', label: 'Full Name', sortable: true, sortField: 'name' },
  { key: 'email', label: 'Email', sortable: true, sortField: 'email' },
  { key: 'course', label: 'Course', sortable: true, sortField: 'course' },
  { key: 'status', label: 'Status', sortable: true, sortField: 'status', width: '130px' },
  { key: 'language', label: 'Language', sortable: true, sortField: 'language', width: '100px' },
  { key: 'category', label: 'Category', sortable: true, sortField: 'category' },
  { key: 'registrationDate', label: 'Registered', sortable: true, sortField: 'registrationDate', width: '110px' },
  { key: 'location', label: 'Location' },
];

export const StudentTable = memo(function StudentTable({
  students,
  selectedIds,
  onToggleSelect,
  onToggleSelectAll,
  sortField,
  sortDirection,
  onSort,
}: StudentTableProps) {
  const allSelected = students.length > 0 && selectedIds.size === students.length;

  const handleSort = useCallback((field?: StudentSortField) => {
    if (field) onSort(field);
  }, [onSort]);

  const headerStyle: React.CSSProperties = {
    padding: '10px 12px',
    fontSize: 'var(--text-caption)',
    fontWeight: 'var(--weight-semibold)',
    color: 'var(--color-text-tertiary)',
    textTransform: 'uppercase',
    letterSpacing: 'var(--tracking-wide)',
    borderBottom: '2px solid var(--color-border-default)',
    textAlign: 'left',
    whiteSpace: 'nowrap',
    userSelect: 'none',
    background: 'var(--color-bg-surface-default)',
  };

  const sortableHeaderStyle: React.CSSProperties = {
    ...headerStyle,
    cursor: 'pointer',
  };

  return (
    <div style={{ overflowX: 'auto' }} role="region" aria-label="Student registry table">
      <table
        style={{
          width: '100%',
          borderCollapse: 'collapse',
          fontSize: 'var(--text-body-sm)',
        }}
        role="grid"
        aria-rowcount={students.length}
        aria-colcount={COLUMNS.length}
      >
        <thead>
          <tr>
            <th scope="col" style={{ ...headerStyle, width: '40px' }}>
              <input
                type="checkbox"
                checked={allSelected}
                onChange={onToggleSelectAll}
                aria-label={allSelected ? 'Deselect all students' : 'Select all students'}
                style={{ cursor: 'pointer' }}
              />
            </th>
            {COLUMNS.map((col) => (
              <th
                key={col.key}
                scope="col"
                style={{
                  ...(col.sortable ? sortableHeaderStyle : headerStyle),
                  width: col.width,
                  textAlign: col.align || 'left',
                }}
                onClick={() => col.sortable && handleSort(col.sortField)}
                aria-sort={
                  sortField === col.sortField
                    ? sortDirection === 'asc' ? 'ascending' : 'descending'
                    : undefined
                }
                tabIndex={col.sortable ? 0 : undefined}
                onKeyDown={(e) => {
                  if (col.sortable && (e.key === 'Enter' || e.key === ' ') && col.sortField) {
                    e.preventDefault();
                    onSort(col.sortField);
                  }
                }}
              >
                <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4 }}>
                  {col.label}
                  {col.sortable && sortField === col.sortField && (
                    <Icon
                      name={sortDirection === 'asc' ? 'arrow-up' : 'arrow-down'}
                      size={10}
                      color="currentColor"
                    />
                  )}
                </span>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {students.map((student) => {
            const isSelected = selectedIds.has(student.id);
            return (
              <tr
                key={student.id}
                role="row"
                aria-selected={isSelected}
                style={{
                  background: isSelected ? 'var(--color-bg-primary-subtle)' : 'transparent',
                  borderBottom: '1px solid var(--color-border-subtle)',
                  transition: 'background var(--duration-fast) var(--easing-standard)',
                }}
                onMouseEnter={(e) => {
                  if (!isSelected) e.currentTarget.style.background = 'var(--color-bg-surface-hover)';
                }}
                onMouseLeave={(e) => {
                  if (!isSelected) e.currentTarget.style.background = 'transparent';
                }}
              >
                <td style={{ padding: '8px 12px' }}>
                  <input
                    type="checkbox"
                    checked={isSelected}
                    onChange={() => onToggleSelect(student.id)}
                    aria-label={`Select ${student.fullName}`}
                    style={{ cursor: 'pointer' }}
                  />
                </td>
                <td style={{ padding: '8px 12px', whiteSpace: 'nowrap', fontFamily: 'var(--font-family-mono)', fontSize: 'var(--text-caption)' }}>
                  {student.studentId}
                </td>
                <td style={{ padding: '8px 12px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                    <Avatar
                      name={student.fullName}
                      initials={student.fullName.split(' ').map((n) => n[0]).join('').slice(0, 2).toUpperCase()}
                      size="sm"
                    />
                    <div>
                      <div style={{ fontWeight: 'var(--weight-medium)' }}>{student.fullName}</div>
                      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
                        {student.preferredName}
                      </div>
                    </div>
                  </div>
                </td>
                <td style={{ padding: '8px 12px', color: 'var(--color-text-secondary)' }}>
                  {student.email}
                </td>
                <td style={{ padding: '8px 12px', maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                  {student.course || <span style={{ color: 'var(--color-text-tertiary)', fontStyle: 'italic' }}>Not assigned</span>}
                </td>
                <td style={{ padding: '8px 12px' }}>
                  <StudentStatusBadge status={student.status} size="sm" />
                </td>
                <td style={{ padding: '8px 12px' }}>{student.language}</td>
                <td style={{ padding: '8px 12px' }}>{student.category}</td>
                <td style={{ padding: '8px 12px', whiteSpace: 'nowrap', fontSize: 'var(--text-caption)' }}>
                  {student.registrationDate}
                </td>
                <td style={{ padding: '8px 12px', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  {student.district}, {student.state}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
});
