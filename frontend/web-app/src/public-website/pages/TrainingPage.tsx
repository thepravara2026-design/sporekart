import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { MediaPlaceholder } from '../home/MediaPlaceholder';
import { PageHeader, SectionHeading, CtaBanner, sectionPad } from './PageShell';

const PROGRAMS = [
  { icon: 'box', title: 'Foundations of Mushroom Cultivation', level: 'Beginner', duration: '2 weeks', description: 'Spawn handling, substrate prep, and the cultivation cycle.' },
  { icon: 'package', title: 'Oyster Mushroom Masterclass', level: 'Intermediate', duration: '4 weeks', description: 'Hands-on growing, contamination control, and yield optimization.' },
  { icon: 'bar-chart', title: 'Commercial Farm Setup', level: 'Advanced', duration: '6 weeks', description: 'Scaling to a profitable, climate-smart mushroom business.' },
  { icon: 'book-open', title: 'Free Growing Guides', level: 'All levels', duration: 'Self-paced', description: 'Open articles and manuals from our research team.' },
];

const BENEFITS = [
  { icon: 'users', title: 'Learn from farmers', description: 'Instructors who grow commercially, not just in labs.' },
  { icon: 'headphone', title: 'Ongoing support', description: 'Post-course help when your crop needs it.' },
  { icon: 'check-circle', title: 'Practical first', description: 'Every module ends in a task you do on your farm.' },
];

export default function TrainingPage() {
  const crumbs = [{ label: 'Home', href: '/' }, { label: 'Training' }];
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: 'Training — SporeKart', description: 'Hands-on mushroom cultivation training, from beginner foundations to commercial farm setup.', canonical: 'https://sporekart.example.com/training' }}
    >
      <PageHeader
        eyebrow="Learn to grow"
        title="Training that turns beginners into confident cultivators"
        intro="Practical, farmer-led programs for every stage — plus free, open growing guides from our research team."
      />

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="Programs" title="Courses & pathways" description="Course listings are placeholders pending the training catalog." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {PROGRAMS.map((p) => (
                <div key={p.title} style={{ padding: 'var(--space-5, 24px)', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
                  <span style={{ display: 'inline-flex', width: 48, height: 48, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)', marginBottom: 'var(--space-3, 12px)' }}>
                    <Icon name={p.icon} size={26} aria-label={p.title} />
                  </span>
                  <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-text-secondary, #4b5563)' }}>{p.level} · {p.duration}</span>
                  <h3 style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{p.title}</h3>
                  <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{p.description}</p>
                </div>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={{ ...sectionPad, backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)' }}>
          <PublicContentContainer maxWidth="lg">
            <MediaPlaceholder label="Training session" icon="users" aspectRatio="21 / 9" caption="Photography placeholder" />
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="Why learn with us" title="Built for real farms" description="Training is only useful if it works in your field." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {BENEFITS.map((b) => (
                <div key={b.title} style={{ display: 'flex', gap: 'var(--space-3, 12px)', alignItems: 'flex-start' }}>
                  <span style={{ flexShrink: 0, display: 'inline-flex', width: 40, height: 40, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)' }}>
                    <Icon name={b.icon} size={22} aria-label={b.title} />
                  </span>
                  <div>
                    <h3 style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{b.title}</h3>
                    <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{b.description}</p>
                  </div>
                </div>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <CtaBanner title="Start your first course" description="Beginner-friendly, practical, and free to explore." primaryLabel="Browse courses" primaryTo="/training/courses" />
    </PublicLayout>
  );
}
