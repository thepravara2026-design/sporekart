import { memo, useMemo } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Seo } from '../../Seo';
import { BreadcrumbFoundation } from '../../BreadcrumbFoundation';
import { CourseCard } from '../components/CourseCard';
import { Icon } from '../../../design-system/icons/Icon';
import { buildDiscoveryCatalog, categoryLabel } from '../data/discoveryMockData';
import { MOCK_COURSES, type TrainingCategory } from '../../../admin/training-workspace/courses/data/courseMockData';

const ALL = buildDiscoveryCatalog(MOCK_COURSES);

export interface MarketingTopic {
  slug: string;
  eyebrow: string;
  title: string;
  intro: string;
  category?: TrainingCategory;
  audience: string[];
  benefits: { icon: string; title: string; description: string }[];
  ctaLabel: string;
}

export const MARKETING_TOPICS: MarketingTopic[] = [
  {
    slug: 'beginner-courses',
    eyebrow: 'Start here',
    title: 'Beginner Mushroom Courses',
    intro: 'No experience required. Build confidence with foundational, hands-on cultivation programs.',
    category: 'mushroom-cultivation',
    audience: ['Home growers', 'New farmers', 'Students'],
    benefits: [
      { icon: 'leaf', title: 'Zero prerequisites', description: 'Start from absolute basics.' },
      { icon: 'users', title: 'Farmer-led', description: 'Learn from commercial growers.' },
      { icon: 'star', title: 'Certificate', description: 'Earn recognition on completion.' },
    ],
    ctaLabel: 'Browse beginner courses',
  },
  {
    slug: 'advanced-courses',
    eyebrow: 'Level up',
    title: 'Advanced Training',
    intro: 'Deep, technical programs for professionals scaling commercial operations.',
    category: 'spawn-production',
    audience: ['Commercial farmers', 'Lab technicians', 'Agribusiness'],
    benefits: [
      { icon: 'database', title: 'Specialized', description: 'Spawn, substrate, and quality control.' },
      { icon: 'trending-up', title: 'Scalable', description: 'Systems for volume production.' },
      { icon: 'shopping-bag', title: 'Business-ready', description: 'Operational playbooks included.' },
    ],
    ctaLabel: 'Browse advanced courses',
  },
  {
    slug: 'commercial-farming',
    eyebrow: 'Build a business',
    title: 'Commercial Farming Programs',
    intro: 'Turn cultivation into a profitable, climate-smart enterprise.',
    category: 'commercial-farming',
    audience: ['Entrepreneurs', 'Farm managers', 'Investors'],
    benefits: [
      { icon: 'trending-up', title: 'Profit plans', description: 'Unit economics and pricing.' },
      { icon: 'globe', title: 'Market access', description: 'Distribution strategies.' },
      { icon: 'shield', title: 'Risk control', description: 'Disease and contamination management.' },
    ],
    ctaLabel: 'Explore farming programs',
  },
  {
    slug: 'spawn-production',
    eyebrow: 'The science',
    title: 'Spawn Production',
    intro: 'Master the core input of every mushroom farm.',
    category: 'spawn-production',
    audience: ['Spawn producers', 'Lab technicians'],
    benefits: [
      { icon: 'database', title: 'Sterile technique', description: 'Contamination-free production.' },
      { icon: 'layers', title: 'Mother cultures', description: 'Maintain and scale genetics.' },
      { icon: 'check-circle', title: 'Quality control', description: 'Consistent, reliable spawn.' },
    ],
    ctaLabel: 'Browse spawn courses',
  },
  {
    slug: 'entrepreneurship',
    eyebrow: 'Founders',
    title: 'Entrepreneurship & Business',
    intro: 'Brand, market, and grow a mushroom venture.',
    category: 'business-training',
    audience: ['Founders', 'Marketers', 'Operators'],
    benefits: [
      { icon: 'shopping-bag', title: 'Go-to-market', description: 'Positioning and launch.' },
      { icon: 'users', title: 'Customer building', description: 'Acquisition and retention.' },
      { icon: 'bar-chart', title: 'Finance', description: 'Modeling and funding.' },
    ],
    ctaLabel: 'Explore entrepreneurship',
  },
  {
    slug: 'corporate-programs',
    eyebrow: 'For teams',
    title: 'Corporate Programs',
    intro: 'Private cohorts and tailored training for enterprises.',
    category: 'corporate-training',
    audience: ['HR leaders', 'L&D teams', 'Operations'],
    benefits: [
      { icon: 'package', title: 'Private batches', description: 'Dedicated cohort scheduling.' },
      { icon: 'sliders', title: 'Tailored', description: 'Curriculum aligned to goals.' },
      { icon: 'file', title: 'Reporting', description: 'Progress and completion analytics.' },
    ],
    ctaLabel: 'Talk to enterprise',
  },
  {
    slug: 'institutional-programs',
    eyebrow: 'Academia & government',
    title: 'Institutional Programs',
    intro: 'Certification pathways for institutions and government schemes.',
    category: 'institutional-programs',
    audience: ['Universities', 'Government bodies', 'NGOs'],
    benefits: [
      { icon: 'user-check', title: 'Accredited', description: 'Aligned to frameworks.' },
      { icon: 'users', title: 'Cohorts', description: 'Scaled delivery.' },
      { icon: 'star', title: 'Certificates', description: 'Verifiable credentials.' },
    ],
    ctaLabel: 'Explore institutional',
  },
  {
    slug: 'government-training',
    eyebrow: 'Public schemes',
    title: 'Government Training',
    intro: 'Skill-development programs aligned to government initiatives.',
    category: 'institutional-programs',
    audience: ['Govt. agencies', 'Rural development', 'SHGs'],
    benefits: [
      { icon: 'tag', title: 'Scheme-ready', description: 'Aligned to public programs.' },
      { icon: 'users', title: 'Inclusive', description: 'Rural and women-led focus.' },
      { icon: 'check-circle', title: 'Compliance', description: 'Documentation support.' },
    ],
    ctaLabel: 'Explore government',
  },
  {
    slug: 'workshop-series',
    eyebrow: 'Hands-on',
    title: 'Workshop Series',
    intro: 'Short, intensive, practical workshops.',
    category: 'mushroom-cultivation',
    audience: ['Hobbyists', 'Professionals', 'Teams'],
    benefits: [
      { icon: 'clock', title: 'Short', description: 'Hours, not weeks.' },
      { icon: 'settings', title: 'Practical', description: 'Do it in the session.' },
      { icon: 'users', title: 'Small groups', description: 'Personal attention.' },
    ],
    ctaLabel: 'Browse workshops',
  },
  {
    slug: 'certification-programs',
    eyebrow: 'Credentials',
    title: 'Certification Programs',
    intro: 'Earn recognized credentials across the cultivation stack.',
    category: 'business-training',
    audience: ['Professionals', 'Job seekers'],
    benefits: [
      { icon: 'star', title: 'Recognized', description: 'Industry-aligned certificates.' },
      { icon: 'check-circle', title: 'Assessed', description: 'Projects and exams.' },
      { icon: 'share', title: 'Shareable', description: 'LinkedIn-ready.' },
    ],
    ctaLabel: 'Explore certifications',
  },
];

export const MarketingLandingPage = memo(function MarketingLandingPage({ topic: topicProp }: { topic?: MarketingTopic }) {
  const { slug } = useParams<{ slug: string }>();
  const topic = topicProp ?? MARKETING_TOPICS.find((t) => t.slug === slug);

  if (!topic) {
    return (
      <>
        <Seo title="Topic not found" noindex />
        <div style={{ textAlign: 'center', padding: 'var(--space-section-gap, 48px) 0' }}>
          <h1 style={{ color: 'var(--color-text-primary)' }}>Topic not found</h1>
          <Link to="/training/courses" style={{ color: 'var(--color-bg-accent-default, #2F6F4F)', fontWeight: 700 }}>Back to catalog</Link>
        </div>
      </>
    );
  }

  const courses = useMemo(
    () => (topic.category ? ALL.filter((c) => c.course.category === topic.category).slice(0, 6) : ALL.slice(0, 6)),
    [topic],
  );

  return (
    <>
      <Seo
        title={`${topic.title} — SporeKart Training`}
        description={topic.intro}
        canonical={`https://sporekart.example.com/training/learn/${topic.slug}`}
      />
      <BreadcrumbFoundation items={[{ label: 'Training', href: '/training' }, { label: topic.title }]} />

      <section style={{ borderRadius: 'var(--radius-xl, 16px)', background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #eef2ff), transparent)', border: '1px solid var(--color-border-default)', padding: 'var(--space-6, 32px)', marginBottom: 'var(--space-6, 32px)' }}>
        <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-bg-accent-default, #2F6F4F)' }}>{topic.eyebrow}</span>
        <h1 style={{ margin: '4px 0 0', fontSize: 'var(--text-h1, 32px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{topic.title}</h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary)', maxWidth: 720 }}>{topic.intro}</p>
        <div style={{ marginTop: 'var(--space-4, 16px)', display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)' }}>
          {topic.audience.map((a) => (
            <span key={a} style={{ padding: '4px 12px', borderRadius: 'var(--radius-pill)', background: 'var(--color-bg-surface-default)', border: '1px solid var(--color-border-default)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{a}</span>
          ))}
        </div>
        <Link to="/training/courses" style={{ display: 'inline-flex', alignItems: 'center', gap: 6, marginTop: 'var(--space-4, 16px)', padding: '10px 20px', borderRadius: 'var(--radius-pill)', background: 'var(--color-bg-accent-default, #2F6F4F)', color: '#fff', fontWeight: 800, textDecoration: 'none' }}>
          {topic.ctaLabel} <Icon name="arrow-right" size={16} color="#fff" />
        </Link>
      </section>

      <section style={{ marginBottom: 'var(--space-6, 32px)' }}>
        <h2 style={{ fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)', marginBottom: 'var(--space-4, 16px)' }}>Why choose this path</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-3, 12px)' }}>
          {topic.benefits.map((b) => (
            <div key={b.title} style={{ padding: 'var(--space-4, 16px)', border: '1px solid var(--color-border-default)', borderRadius: 'var(--radius-lg, 12px)', background: 'var(--color-bg-surface-default)' }}>
              <Icon name={b.icon} size={22} color="var(--color-bg-accent-default, #2F6F4F)" />
              <h3 style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-md, 16px)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{b.title}</h3>
              <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{b.description}</p>
            </div>
          ))}
        </div>
      </section>

      <section>
        <h2 style={{ fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)', marginBottom: 'var(--space-4, 16px)' }}>
          {topic.category ? `${categoryLabel(topic.category)} courses` : 'Recommended courses'}
        </h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
          {courses.map((dc) => (
            <CourseCard key={dc.course.id} dc={dc} view="grid" />
          ))}
        </div>
      </section>
    </>
  );
});

export default MarketingLandingPage;
