import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { MediaPlaceholder } from '../home/MediaPlaceholder';
import { PageHeader, SectionHeading, Prose, CtaBanner, sectionPad } from './PageShell';

const VALUES = [
  { icon: 'check-circle', title: 'Verified quality', description: 'Every spawn lot is lab-tested before it reaches a farm.' },
  { icon: 'users', title: 'Farmer-first', description: 'We measure success by our growers’ harvests, not just sales.' },
  { icon: 'book-open', title: 'Open knowledge', description: 'Guides, training, and research shared freely with the community.' },
  { icon: 'trending-up', title: 'Sustainable by design', description: 'Low-input, high-yield cultivation that respects the land.' },
];

const TEAM = [
  { name: 'Founder & CEO', role: 'Placeholder', note: 'Bio pending approval.' },
  { name: 'Head of Research', role: 'Placeholder', note: 'Bio pending approval.' },
  { name: 'Training Lead', role: 'Placeholder', note: 'Bio pending approval.' },
  { name: 'Field Support', role: 'Placeholder', note: 'Bio pending approval.' },
];

export default function AboutPage() {
  const crumbs = [{ label: 'Home', href: '/' }, { label: 'About' }];
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: 'About SporeKart', description: 'SporeKart is India’s trusted mushroom cultivation ecosystem — verified inputs, training, and farmer-first support.', canonical: 'https://sporekart.example.com/about' }}
    >
      <PageHeader
        eyebrow="Our story"
        title="We make mushroom cultivation predictable and profitable"
        intro="SporeKart connects lab-verified spawn, fresh and dried mushrooms, hands-on training, and ongoing support into one ecosystem built for Indian farmers."
      />

      <Reveal>
        <div style={{ ...sectionPad, backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)' }}>
          <PublicContentContainer maxWidth="lg">
            <MediaPlaceholder label="Team or farm photo" icon="image" aspectRatio="21 / 9" caption="Brand photography placeholder" />
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="What we believe" title="Our values" description="The principles behind every product, course, and conversation." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {VALUES.map((v) => (
                <div key={v.title} style={{ padding: 'var(--space-4, 16px)', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
                  <span style={{ display: 'inline-flex', width: 44, height: 44, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)', marginBottom: 'var(--space-3, 12px)' }}>
                    <Icon name={v.icon} size={24} aria-label={v.title} />
                  </span>
                  <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{v.title}</h3>
                  <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{v.description}</p>
                </div>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={{ ...sectionPad, backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)' }}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="The people" title="Team" description="Real bios and photos are placeholders pending approval." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {TEAM.map((m) => (
                <div key={m.name} style={{ textAlign: 'center' }}>
                  <MediaPlaceholder label={m.name} icon="users" aspectRatio="1 / 1" />
                  <h3 style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-md, 16px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{m.name}</h3>
                  <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{m.role}</p>
                </div>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Prose>
        <p>
          SporeKart began with a simple belief: that any farmer, anywhere in India, should be able to grow mushrooms
          successfully. We combine verified science, practical training, and a support network so cultivation is no
          longer a gamble — it’s a dependable livelihood.
        </p>
      </Prose>

      <CtaBanner title="Grow with us" description="Explore verified spawn, training, and resources for every stage." primaryLabel="Browse products" primaryTo="/products" />
    </PublicLayout>
  );
}
