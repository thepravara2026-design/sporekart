import { memo, useCallback } from 'react';
import type { Student } from '../types';
import { Card } from '../../../../design-system/components/composite/Card';
import { Avatar } from '../../../../design-system/components/display/Avatar';
import { Chip } from '../../../../design-system/components/display/Chip';
import { Icon } from '../../../../design-system/icons/Icon';
import StudentStatusBadge from './StudentStatusBadge';

interface StudentCardProps {
  student: Student;
  selected?: boolean;
  onSelect?: (id: string) => void;
  onArchive?: (id: string) => void;
  onRestore?: (id: string) => void;
}

export const StudentCard = memo(function StudentCard({
  student,
  selected = false,
  onSelect,
}: StudentCardProps) {
  const handleClick = useCallback(() => {
    if (onSelect) onSelect(student.id);
  }, [onSelect, student.id]);

  return (
    <Card
      variant={selected ? 'elevated' : 'default'}
      hoverable
      padding="md"
      onClick={handleClick}
      aria-label={`Student: ${student.fullName}, ID: ${student.studentId}, Status: ${student.status}`}
    >
      <div style={{ display: 'flex', gap: 12 }}>
        <Avatar
          name={student.fullName}
          initials={student.fullName.split(' ').map((n) => n[0]).join('').slice(0, 2).toUpperCase()}
          size="md"
          aria-label={student.fullName}
        />
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
            <span style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>
              {student.fullName}
            </span>
            <StudentStatusBadge status={student.status} size="sm" />
          </div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginTop: 2 }}>
            {student.studentId}
          </div>
          {student.course && (
            <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginTop: 1 }}>
              {student.course}
            </div>
          )}
          <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginTop: 6, fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
              <Icon name="map-pin" size={10} color="currentColor" />
              {student.district}, {student.state}
            </span>
            <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
              <Icon name="globe" size={10} color="currentColor" />
              {student.language}
            </span>
          </div>
          <div style={{ display: 'flex', gap: 4, marginTop: 6, flexWrap: 'wrap' }}>
            {student.tags.map((tag) => (
              <Chip key={tag} variant="default" size="sm" type="tag">{tag}</Chip>
            ))}
          </div>
        </div>
      </div>
    </Card>
  );
});

export const StudentCompactCard = memo(function StudentCompactCard({
  student,
  selected = false,
  onSelect,
}: StudentCardProps) {
  const handleClick = useCallback(() => {
    if (onSelect) onSelect(student.id);
  }, [onSelect, student.id]);

  const handleKeyDown = useCallback((e: React.KeyboardEvent) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      if (onSelect) onSelect(student.id);
    }
  }, [onSelect, student.id]);

  return (
    <div
      onClick={handleClick}
      onKeyDown={handleKeyDown}
      role="option"
      aria-selected={selected}
      tabIndex={0}
      style={{
        display: 'flex', alignItems: 'center', gap: 10,
        padding: '6px 8px',
        borderRadius: 'var(--radius-sm)',
        background: selected ? 'var(--color-bg-primary-subtle)' : 'transparent',
        cursor: 'pointer',
        border: selected ? '1px solid var(--color-primary)' : '1px solid transparent',
        transition: 'background var(--duration-fast) var(--easing-standard)',
      }}
    >
      <Avatar
        name={student.fullName}
        initials={student.fullName.split(' ').map((n) => n[0]).join('').slice(0, 2).toUpperCase()}
        size="sm"
      />
      <div style={{ flex: 1, minWidth: 0 }}>
        <div style={{ fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)' }}>
          {student.fullName}
        </div>
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          {student.studentId} &middot; {student.email}
        </div>
      </div>
      <StudentStatusBadge status={student.status} size="sm" />
    </div>
  );
});
