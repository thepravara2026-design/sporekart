import { memo, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { MOCK_COURSES } from '../data/courseMockData';
import { CourseCard } from '../components/CourseCard';
import { CourseEmptyState } from '../components/CourseEmptyStates';
import { Icon } from '../../../../design-system/icons/Icon';

const CoursePublishedPage = memo(function CoursePublishedPage() {
  const navigate = useNavigate();
  const published = useMemo(() => MOCK_COURSES.filter((c) => c.lifecycle === 'published'), []);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div>
        <h2 style={{
          margin: '0 0 var(--space-stack-xs)', fontSize: 'var(--text-h2)',
          color: 'var(--color-text-primary)',
        }}>
          Published Courses
        </h2>
        <p style={{
          margin: 0, fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)',
        }}>
          {published.length} course{published.length !== 1 ? 's' : ''} currently published
        </p>
      </div>

      {published.length === 0 ? (
        <CourseEmptyState type="no-published" />
      ) : (
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
          gap: 'var(--space-component-gap)',
        }}>
          {published.map((course) => (
            <CourseCard key={course.id} course={course} />
          ))}
        </div>
      )}

      <div style={{
        display: 'flex', justifyContent: 'center', marginTop: 'var(--space-stack-sm)',
      }}>
        <button
          type="button"
          onClick={() => navigate('/admin/training/courses')}
          style={{
            display: 'flex', alignItems: 'center', gap: 4,
            padding: '6px 14px', borderRadius: 'var(--radius-input)',
            background: 'transparent', border: '1px solid var(--color-border-default)',
            cursor: 'pointer', color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-body-sm)',
          }}
        >
          <Icon name="arrow-left" size={14} color="currentColor" />
          Back to all courses
        </button>
      </div>
    </div>
  );
});

export default CoursePublishedPage;
