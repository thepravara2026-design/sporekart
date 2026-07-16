import { memo, useMemo } from 'react';
import { Seo } from '../../Seo';
import { BreadcrumbFoundation } from '../../BreadcrumbFoundation';
import { CourseComparisonTable } from '../components/CourseComparisonTable';
import { useCourseComparison } from '../state/useCourseSelection';
import { buildDiscoveryCatalog } from '../data/discoveryMockData';
import { MOCK_COURSES } from '../../../admin/training-workspace/courses/data/courseMockData';

const ALL = buildDiscoveryCatalog(MOCK_COURSES);

export const CourseComparisonPage = memo(function CourseComparisonPage() {
  const comparison = useCourseComparison(ALL);

  const suggestions = useMemo(() => ALL.slice(0, 6), []);

  return (
    <>
      <Seo title="Compare Courses — SporeKart Training" description="Compare SporeKart training courses side by side: duration, price, difficulty, delivery, certification, and seat availability." canonical="https://sporekart.example.com/training/courses/compare" />
      <BreadcrumbFoundation items={[{ label: 'Training', href: '/training' }, { label: 'Courses', href: '/training/courses' }, { label: 'Compare' }]} />

      <header style={{ marginBottom: 'var(--space-5, 24px)' }}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h1, 32px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>Compare Courses</h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary)' }}>
          Select up to {comparison.limit} courses to compare them side by side.
        </p>
      </header>

      {comparison.compareCourses.length > 0 ? (
        <CourseComparisonTable courses={comparison.compareCourses} onRemove={comparison.toggleCompare} onClear={comparison.clearCompare} />
      ) : (
        <>
          <p style={{ color: 'var(--color-text-secondary)' }}>No courses selected yet. Add courses from the catalog using the <strong>Compare</strong> button on any card.</p>
          <h2 style={{ fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)', marginTop: 'var(--space-5, 24px)' }}>Suggested to compare</h2>
          <ul style={{ paddingLeft: '1.2em', color: 'var(--color-text-secondary)' }}>
            {suggestions.map((c) => (
              <li key={c.course.id} style={{ marginBottom: 'var(--space-1, 4px)' }}>
                <a href={`/training/courses/${c.course.slug}`} style={{ color: 'var(--color-bg-accent-default, #2F6F4F)', fontWeight: 600 }}>{c.course.name}</a>
              </li>
            ))}
          </ul>
        </>
      )}
    </>
  );
});

export default CourseComparisonPage;
