import { memo, useMemo } from 'react';
import { Link } from 'react-router-dom';
import { Seo } from '../../Seo';
import { BreadcrumbFoundation } from '../../BreadcrumbFoundation';
import { Card } from '../../../design-system/components/composite/Card';
import { Badge } from '../../../design-system/components/display/Badge';
import { Icon } from '../../../design-system/icons/Icon';
import { buildDiscoveryCatalog } from '../data/discoveryMockData';
import { MOCK_COURSES } from '../../../admin/training-workspace/courses/data/courseMockData';

const ALL = buildDiscoveryCatalog(MOCK_COURSES);

interface LearningPath {
  slug: string;
  title: string;
  description: string;
  icon: string;
  courseSlugs: string[];
}

const LEARNING_PATHS: LearningPath[] = [
  {
    slug: 'beginner-grower',
    title: 'Beginner Grower Path',
    description: 'From zero to your first harvest. Foundations of cultivation and oyster farming.',
    icon: 'leaf',
    courseSlugs: ['mushroom-cultivation-fundamentals', 'commercial-oyster-farming'],
  },
  {
    slug: 'spawn-specialist',
    title: 'Spawn Production Specialist',
    description: 'Master the science of spawn and substrate for commercial scale.',
    icon: 'database',
    courseSlugs: ['advanced-spawn-production'],
  },
  {
    slug: 'commercial-farmer',
    title: 'Commercial Farm Owner',
    description: 'Build and run a profitable, climate-smart mushroom business.',
    icon: 'trending-up',
    courseSlugs: ['commercial-oyster-farming', 'value-added-mushroom-products'],
  },
  {
    slug: 'entrepreneur',
    title: 'Agri-Entrepreneur',
    description: 'Business, branding, and go-to-market for mushroom ventures.',
    icon: 'shopping-bag',
    courseSlugs: ['mushroom-business-mastery', 'value-added-mushroom-products'],
  },
  {
    slug: 'corporate',
    title: 'Corporate Training Track',
    description: 'Cohort programs for enterprise and institutional teams.',
    icon: 'package',
    courseSlugs: ['corporate-training-essentials', 'government-training-program'],
  },
  {
    slug: 'institutional',
    title: 'Institutional Programs',
    description: 'Academic and government-aligned certification pathways.',
    icon: 'user-check',
    courseSlugs: ['institutional-training-program'],
  },
];

export const LearningPathsPage = memo(function LearningPathsPage() {
  const bySlug = useMemo(() => {
    const map = new Map<string, (typeof ALL)[number]>();
    ALL.forEach((c) => map.set(c.course.slug, c));
    return map;
  }, []);

  return (
    <>
      <Seo
        title="Learning Paths — SporeKart Training"
        description="Structured learning paths to take you from beginner to commercial mushroom farmer and entrepreneur."
        canonical="https://sporekart.example.com/training/learning-paths"
      />
      <BreadcrumbFoundation items={[{ label: 'Training', href: '/training' }, { label: 'Learning Paths' }]} />

      <header style={{ marginBottom: 'var(--space-5, 24px)' }}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h1, 32px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>Learning Paths</h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary)' }}>
          Follow a guided journey from fundamentals to mastery. Each path bundles related courses into a clear progression.
        </p>
      </header>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-4, 16px)' }}>
        {LEARNING_PATHS.map((path) => {
          const courses = path.courseSlugs.map((s) => bySlug.get(s)).filter(Boolean) as (typeof ALL)[number][];
          return (
            <Card key={path.slug} variant="default" padding="lg">
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)', marginBottom: 'var(--space-3, 12px)' }}>
                <span style={{ width: 44, height: 44, borderRadius: 'var(--radius-md)', background: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-bg-accent-default, #2F6F4F)', display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Icon name={path.icon} size={22} color="var(--color-bg-accent-default, #2F6F4F)" />
                </span>
                <h2 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{path.title}</h2>
              </div>
              <p style={{ margin: '0 0 var(--space-3, 12px)', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', lineHeight: 1.6 }}>{path.description}</p>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
                {courses.map((c, i) => (
                  <Link key={c.course.id} to={`/training/courses/${c.course.slug}`} style={{ display: 'flex', alignItems: 'center', gap: 8, padding: 'var(--space-2, 8px)', borderRadius: 'var(--radius-sm)', border: '1px solid var(--color-border-default)', textDecoration: 'none', color: 'var(--color-text-primary)' }}>
                    <span style={{ width: 22, height: 22, borderRadius: 'var(--radius-full)', background: 'var(--color-bg-accent-default, #2F6F4F)', color: '#fff', fontSize: 'var(--text-caption)', display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}>{i + 1}</span>
                    <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600 }}>{c.course.name}</span>
                  </Link>
                ))}
              </div>
              <div style={{ marginTop: 'var(--space-3, 12px)' }}>
                <Badge size="sm" variant="neutral">{courses.length} courses</Badge>
              </div>
            </Card>
          );
        })}
      </div>
    </>
  );
});

export default LearningPathsPage;
