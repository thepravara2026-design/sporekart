import { PublicContentContainer } from '../../PublicContentContainer';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { NavButton } from '../NavButton';

const RESOURCES = [
  { icon: 'book-open', type: 'Blog', title: 'Getting started with oyster mushrooms', excerpt: 'A beginner-friendly walkthrough for your first grow.' },
  { icon: 'file', type: 'Guide', title: 'Substrate preparation, step by step', excerpt: 'How to pasteurize and pack substrate safely at home.' },
  { icon: 'book-open', type: 'Training', title: 'What you’ll learn in the masterclass', excerpt: 'Syllabus and outcomes from our flagship program.' },
  { icon: 'globe', type: 'News', title: 'SporeKart expands pan-India delivery', excerpt: 'Faster cold-chain shipping to more Pin codes.' },
];

export function ResourcesPreview() {
  const headerStyle: React.CSSProperties = { marginBottom: 'var(--space-7, 28px)', maxWidth: 640 };
  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))',
    gap: 'var(--space-6, 24px)',
  };

  return (
    <section className="sk-home-resources" aria-labelledby="sk-home-resources-title" style={{ backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)', padding: 'var(--space-12, 48px) 0' }}>
      <PublicContentContainer>
        <div style={headerStyle}>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Resources</span>
          <h2 id="sk-home-resources-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Knowledge, free and open to every grower
          </h2>
        </div>
        <div style={gridStyle}>
          {RESOURCES.map((resource) => (
            <Card key={resource.title} variant="default" padding="md" hoverable as="article" aria-label={resource.title}>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)', height: '100%' }}>
                <span style={{ alignSelf: 'flex-start', display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1, 4px)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', padding: '2px var(--space-2, 8px)', borderRadius: 'var(--radius-pill, 999px)' }}>
                  <Icon name={resource.icon} size={14} aria-label={resource.type} /> {resource.type}
                </span>
                <h3 style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>{resource.title}</h3>
                <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{resource.excerpt}</p>
              </div>
            </Card>
          ))}
        </div>
        <div style={{ marginTop: 'var(--space-6, 24px)' }}>
          <NavButton to="/blog" variant="outline" rightIcon={<Icon name="arrow-right" size={18} aria-label="All" />}>All resources</NavButton>
        </div>
      </PublicContentContainer>
    </section>
  );
}
