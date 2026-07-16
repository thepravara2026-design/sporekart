import { memo, useMemo } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Card } from '../../../design-system/components/composite/Card';
import { Badge } from '../../../design-system/components/display/Badge';
import { Icon } from '../../../design-system/icons/Icon';
import { Seo } from '../../Seo';
import { BreadcrumbFoundation } from '../../BreadcrumbFoundation';
import {
  categoryColor,
  categoryLabel,
  deliveryLabel,
  levelLabel,
  buildDiscoveryCourse,
  type DiscoveryCourse,
} from '../data/discoveryMockData';
import { MOCK_COURSES } from '../../../admin/training-workspace/courses/data/courseMockData';
import { CourseCard } from '../components/CourseCard';

function formatPrice(price: number, currency: string): string {
  if (price === 0) return 'Free';
  const symbol = currency === 'INR' ? '₹' : '$';
  return `${symbol}${price.toLocaleString('en-IN')}`;
}

function SectionHeading({ title, eyebrow }: { title: string; eyebrow?: string }) {
  return (
    <div style={{ marginBottom: 'var(--space-4, 16px)' }}>
      {eyebrow && <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-bg-accent-default, #2F6F4F)' }}>{eyebrow}</span>}
      <h2 style={{ margin: '4px 0 0', fontSize: 'var(--text-h3, 22px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{title}</h2>
    </div>
  );
}

export const CourseDetailsPage = memo(function CourseDetailsPage() {
  const { slug } = useParams<{ slug: string }>();
  const course = useMemo(() => MOCK_COURSES.find((c) => c.slug === slug), [slug]);

  const dc: DiscoveryCourse | null = useMemo(
    () => (course ? buildDiscoveryCourse(course, MOCK_COURSES) : null),
    [course],
  );

  const related = useMemo<DiscoveryCourse[]>(() => {
    if (!dc) return [];
    return dc.relatedIds
      .map((id) => MOCK_COURSES.find((c) => c.id === id))
      .filter(Boolean)
      .map((c) => buildDiscoveryCourse(c!, MOCK_COURSES));
  }, [dc]);

  if (!course || !dc) {
    return (
      <div style={{ textAlign: 'center', padding: 'var(--space-section-gap, 48px) 0' }}>
        <Seo title="Course not found" noindex />
        <h1 style={{ color: 'var(--color-text-primary)' }}>Course not found</h1>
        <p style={{ color: 'var(--color-text-secondary)' }}>The course you are looking for is not available.</p>
        <Link to="/training/courses" style={{ color: 'var(--color-bg-accent-default, #2F6F4F)', fontWeight: 700 }}>Back to catalog</Link>
      </div>
    );
  }

  const accent = categoryColor(course.category);
  const seatRatio = dc.availableSeats / dc.totalSeats;
  const canonical = `https://sporekart.example.com/training/courses/${course.slug}`;

  const structuredData = {
    '@context': 'https://schema.org',
    '@type': 'Course',
    name: course.name,
    description: course.shortDescription,
    provider: {
      '@type': 'Organization',
      name: 'SporeKart',
      sameAs: 'https://sporekart.example.com',
    },
    courseCode: course.code,
    educationalLevel: levelLabel(course.level),
    inLanguage: course.language,
    teaches: course.learningObjectives,
    offers: {
      '@type': 'Offer',
      price: dc.price,
      priceCurrency: dc.currency,
      availability: seatRatio <= 0 ? 'https://schema.org/SoldOut' : 'https://schema.org/InStock',
      url: canonical,
    },
  };

  return (
    <>
      <Seo
        title={course.seoTitle || course.name}
        description={course.seoDescription || course.shortDescription}
        canonical={canonical}
        type="website"
        structuredData={structuredData}
      />
      <BreadcrumbFoundation
        items={[
          { label: 'Training', href: '/training' },
          { label: 'Courses', href: '/training/courses' },
          { label: categoryLabel(course.category), href: `/training/courses/category/${course.category}` },
          { label: course.name },
        ]}
      />

      {/* Hero Banner */}
      <section
        style={{
          position: 'relative',
          borderRadius: 'var(--radius-xl, 16px)',
          overflow: 'hidden',
          background: `linear-gradient(135deg, ${accent}22, ${accent}0a)`,
          border: '1px solid var(--color-border-default)',
          padding: 'var(--space-6, 32px)',
          display: 'grid',
          gridTemplateColumns: 'minmax(0, 1fr)',
          gap: 'var(--space-4, 16px)',
        }}
      >
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
          {dc.badges.map((b) => (
            <Badge key={b.kind} size="md" variant={b.kind === 'featured' ? 'primary' : b.kind === 'new' ? 'success' : b.kind === 'trending' ? 'warning' : 'info'}>{b.label}</Badge>
          ))}
          <Badge size="md" variant="neutral">{categoryLabel(course.category)}</Badge>
        </div>
        <h1 style={{ margin: 0, fontSize: 'var(--text-h1, 32px)', fontWeight: 800, color: 'var(--color-text-primary)', lineHeight: 1.2 }}>{course.name}</h1>
        <p style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary)', maxWidth: 720 }}>{course.shortDescription}</p>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-4, 16px)', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary)' }}>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="bar-chart" size={16} color={accent} aria-label="Level" /> {levelLabel(course.level)}</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="clock" size={16} color={accent} aria-label="Duration" /> {course.duration}</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="monitor" size={16} color={accent} aria-label="Delivery" /> {deliveryLabel(course.deliveryMode)}</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="globe" size={16} color={accent} aria-label="Language" /> {course.language}</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="book-open" size={16} color={accent} aria-label="Modules" /> {course.moduleCount} modules</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="star" size={16} color="var(--color-warning)" aria-label="Rating" /> {course.rating.toFixed(1)} ({dc.enrollmentCountPlaceholder} enrolled)</span>
        </div>
      </section>

      {/* Main two-column layout */}
      <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1fr) 320px', gap: 'var(--space-5, 24px)', alignItems: 'start', marginTop: 'var(--space-5, 24px)' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-6, 32px)' }}>
          {/* Overview */}
          <div>
            <SectionHeading title="Overview" eyebrow="About this course" />
            <Card variant="default" padding="lg">
              <p style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', lineHeight: 1.7, color: 'var(--color-text-secondary)' }}>{course.longDescription}</p>
            </Card>
          </div>

          {/* Course Highlights */}
          <div>
            <SectionHeading title="Course highlights" eyebrow="Why join" />
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: 'var(--space-3, 12px)' }}>
              {[
                { icon: 'users', label: 'Expert trainers', value: `${course.instructorCount} instructors` },
                { icon: 'layers', label: 'Modules', value: `${course.moduleCount} lessons` },
                { icon: 'calendar', label: 'Batches', value: `${course.batchCount} cohorts` },
                { icon: 'star', label: 'Certificate', value: 'On completion' },
              ].map((h) => (
                <Card key={h.label} variant="outlined" padding="md">
                  <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                    <Icon name={h.icon} size={20} color={accent} aria-label={h.label} />
                    <div>
                      <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 700, color: 'var(--color-text-primary)' }}>{h.value}</div>
                      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{h.label}</div>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          </div>

          {/* Learning Objectives */}
          <div>
            <SectionHeading title="Learning objectives" eyebrow="What you'll learn" />
            <Card variant="default" padding="lg">
              <ul style={{ margin: 0, paddingLeft: '1.2em', display: 'grid', gap: 'var(--space-2, 8px)' }}>
                {course.learningObjectives.map((o) => (
                  <li key={o} style={{ fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary)', lineHeight: 1.6 }}>{o}</li>
                ))}
              </ul>
            </Card>
          </div>

          {/* Who Should Join */}
          <div>
            <SectionHeading title="Who should join" eyebrow="Ideal for" />
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)' }}>
              {course.targetAudience.map((a) => (
                <Badge key={a} size="md" variant="default">{a}</Badge>
              ))}
            </div>
          </div>

          {/* Prerequisites */}
          <div>
            <SectionHeading title="Prerequisites" eyebrow="Before you start" />
            <Card variant="default" padding="lg">
              {course.prerequisites.length ? (
                <ul style={{ margin: 0, paddingLeft: '1.2em' }}>
                  {course.prerequisites.map((p) => (
                    <li key={p} style={{ fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary)', lineHeight: 1.6 }}>{p}</li>
                  ))}
                </ul>
              ) : (
                <p style={{ margin: 0, color: 'var(--color-text-secondary)' }}>No prerequisites. This course welcomes absolute beginners.</p>
              )}
            </Card>
          </div>

          {/* Curriculum Overview */}
          <div>
            <SectionHeading title="Curriculum overview" eyebrow="Syllabus highlights" />
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
              {dc.curriculumHighlights.map((m, i) => (
                <Card key={m} variant="outlined" padding="md" style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)' }}>
                  <span style={{ width: 32, height: 32, borderRadius: 'var(--radius-full)', background: `${accent}1f`, color: accent, display: 'inline-flex', alignItems: 'center', justifyContent: 'center', fontWeight: 800, fontSize: 'var(--text-body-sm)' }}>{i + 1}</span>
                  <span style={{ fontWeight: 600, color: 'var(--color-text-primary)' }}>{m}</span>
                </Card>
              ))}
            </div>
          </div>

          {/* Resource Highlights */}
          <div>
            <SectionHeading title="Resource highlights" eyebrow="Included materials" />
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 'var(--space-3, 12px)' }}>
              {['Downloadable guides', 'Video lectures', 'Hands-on worksheets', 'Community access'].map((r) => (
                <Card key={r} variant="outlined" padding="md" style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                  <Icon name="file" size={18} color={accent} aria-label="Resource" />
                  <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{r}</span>
                </Card>
              ))}
            </div>
          </div>

          {/* Trainer Placeholder */}
          <div>
            <SectionHeading title="Your trainers" eyebrow="Meet the experts" />
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-3, 12px)' }}>
              {dc.trainerNames.map((t) => (
                <Card key={t} variant="outlined" padding="md" style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)' }}>
                  <span style={{ width: 44, height: 44, borderRadius: 'var(--radius-full)', background: `${accent}1f`, color: accent, display: 'inline-flex', alignItems: 'center', justifyContent: 'center', fontWeight: 800 }}>
                    {t.split(' ').map((w) => w[0]).slice(0, 2).join('')}
                  </span>
                  <div>
                    <div style={{ fontWeight: 700, color: 'var(--color-text-primary)' }}>{t}</div>
                    <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>SporeKart Faculty</div>
                  </div>
                </Card>
              ))}
            </div>
          </div>

          {/* Certification Preview */}
          <div>
            <SectionHeading title="Certification" eyebrow="Earn recognition" />
            <Card variant="default" padding="lg" style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-4, 16px)', background: `linear-gradient(135deg, ${accent}14, transparent)` }}>
              <span style={{ width: 56, height: 56, borderRadius: 'var(--radius-md)', background: accent, color: '#fff', display: 'inline-flex', alignItems: 'center', justifyContent: 'center' }}>
                <Icon name="star" size={28} color="#fff" />
              </span>
              <div>
                <div style={{ fontWeight: 800, color: 'var(--color-text-primary)' }}>SporeKart Certificate of Completion</div>
                <p style={{ margin: '4px 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)' }}>A verifiable certificate is issued upon finishing all modules and assessments. Shareable on LinkedIn and employer portals.</p>
              </div>
            </Card>
          </div>

          {/* FAQs */}
          <div>
            <SectionHeading title="FAQs" eyebrow="Common questions" />
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
              {dc.faqs.map((f) => (
                <Card key={f.question} variant="outlined" padding="md">
                  <div style={{ display: 'flex', alignItems: 'center', gap: 8, fontWeight: 700, color: 'var(--color-text-primary)' }}>
                    <Icon name="help-circle" size={16} color={accent} aria-label="Question" /> {f.question}
                  </div>
                  <p style={{ margin: 'var(--space-2, 8px) 0 0', color: 'var(--color-text-secondary)', fontSize: 'var(--text-body-sm)', lineHeight: 1.6 }}>{f.answer}</p>
                </Card>
              ))}
            </div>
          </div>

          {/* Related Courses */}
          {related.length > 0 && (
            <div>
              <SectionHeading title="Related courses" eyebrow="You may also like" />
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 'var(--space-4, 16px)' }}>
                {related.map((r) => (
                  <CourseCard key={r.course.id} dc={r} view="grid" />
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Sticky Enrollment Panel */}
        <aside style={{ position: 'sticky', top: 'var(--space-5, 24px)', display: 'flex', flexDirection: 'column', gap: 'var(--space-3, 12px)' }}>
          <Card variant="elevated" padding="lg">
            <div style={{ display: 'flex', alignItems: 'baseline', gap: 8 }}>
              <span style={{ fontSize: 'var(--text-h2, 26px)', fontWeight: 800, color: 'var(--color-text-primary)' }}>{formatPrice(dc.price, dc.currency)}</span>
              {dc.originalPrice && <span style={{ textDecoration: 'line-through', color: 'var(--color-text-tertiary)', fontSize: 'var(--text-body-sm)' }}>{formatPrice(dc.originalPrice, dc.currency)}</span>}
            </div>
            <div style={{ marginTop: 'var(--space-3, 12px)', display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)' }}>
                <span style={{ color: 'var(--color-text-secondary)' }}>Seats available</span>
                <strong style={{ color: seatRatio <= 0 ? 'var(--color-danger)' : seatRatio <= 0.2 ? 'var(--color-warning)' : 'var(--color-success)' }}>{seatRatio <= 0 ? 'Waitlist only' : `${dc.availableSeats} / ${dc.totalSeats}`}</strong>
              </div>
              <Link to="/training/enroll" style={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'center', gap: 6, padding: '10px 16px', borderRadius: 'var(--radius-pill)', background: 'var(--color-bg-accent-default, #2F6F4F)', color: '#fff', fontWeight: 800, textDecoration: 'none', fontSize: 'var(--text-body-md)' }} aria-label={`Enroll in ${course.name}`}>
                Enroll Now <Icon name="arrow-right" size={16} color="#fff" />
              </Link>
              <button type="button" style={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'center', gap: 6, padding: '10px 16px', borderRadius: 'var(--radius-pill)', border: '1px solid var(--color-border-default)', background: 'transparent', color: 'var(--color-text-secondary)', fontWeight: 700, cursor: 'pointer', fontSize: 'var(--text-body-md)' }}>
                <Icon name="book" size={16} color="currentColor" /> Save for later
              </button>
            </div>
            <div style={{ marginTop: 'var(--space-3, 12px)', paddingTop: 'var(--space-3, 12px)', borderTop: '1px solid var(--color-border-default)', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
              Next batch starts {dc.nextBatchDate} · {deliveryLabel(course.deliveryMode)} · {course.language}
            </div>
          </Card>

          <Card variant="outlined" padding="md">
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
              {[
                { icon: 'check-circle', label: 'Lifetime access to materials' },
                { icon: 'check-circle', label: 'Expert-led sessions' },
                { icon: 'check-circle', label: 'Certificate of completion' },
                { icon: 'check-circle', label: 'Community support' },
              ].map((b) => (
                <div key={b.label} style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                  <Icon name={b.icon} size={16} color="var(--color-success)" aria-label="Included" /> {b.label}
                </div>
              ))}
            </div>
          </Card>
        </aside>
      </div>
    </>
  );
});

export default CourseDetailsPage;
