import { memo } from 'react';
import { CourseCard } from './CourseCard';
import type { Course } from '../data/courseMockData';

interface CourseGridViewProps {
  courses: Course[];
  selectedIds: Set<string>;
  onToggleSelect: (id: string) => void;
  onTogglePin: (id: string) => void;
  onToggleFavorite: (id: string) => void;
}

export const CourseGridView = memo(function CourseGridView({
  courses, selectedIds, onToggleSelect, onTogglePin, onToggleFavorite,
}: CourseGridViewProps) {
  return (
    <div
      style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
        gap: 'var(--space-component-gap)',
      }}
      role="list"
      aria-label="Courses grid view"
    >
      {courses.map((course) => (
        <div key={course.id} role="listitem">
          <CourseCard
            course={course}
            selected={selectedIds.has(course.id)}
            onToggleSelect={onToggleSelect}
            onTogglePin={onTogglePin}
            onToggleFavorite={onToggleFavorite}
          />
        </div>
      ))}
    </div>
  );
});
