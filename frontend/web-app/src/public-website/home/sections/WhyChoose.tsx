import { PublicContentContainer } from '../../PublicContentContainer';
import { FeatureCard } from '../../../design-system/components/composite/FeatureCard';
import { Icon } from '../../../design-system/icons/Icon';

const PILLARS = [
  { icon: 'shield', title: 'Premium Quality', description: 'Lab-verified spawn and strict contamination controls at every step.' },
  { icon: 'zap', title: 'Research-Led', description: 'Breeding and techniques informed by working mycologists.' },
  { icon: 'headphone', title: 'Grower Support', description: 'Guidance from our cultivation team whenever you need it.' },
  { icon: 'truck', title: 'Fast Delivery', description: 'Pan-India cold-chain shipping that protects viability.' },
  { icon: 'book-open', title: 'Education First', description: 'Training and playbooks that shorten your learning curve.' },
  { icon: 'sun', title: 'Sustainable', description: 'Low-waste kits and responsible sourcing for cleaner farms.' },
];

export function WhyChoose() {
  const headerStyle: React.CSSProperties = {
    marginBottom: 'var(--space-6, 32px)',
    maxWidth: 640,
  };

  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))',
    gap: 'var(--space-4, 16px)',
  };

  return (
    <section className="sk-home-why" aria-labelledby="sk-home-why-title" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
      <PublicContentContainer>
        <div style={headerStyle}>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Why SporeKart</span>
          <h2 id="sk-home-why-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Built for cultivators, end to end
          </h2>
          <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>
            Six reasons thousands of growers across India build their farms with SporeKart.
          </p>
        </div>
        <div style={gridStyle}>
          {PILLARS.map((pillar) => (
            <FeatureCard
              key={pillar.title}
              title={pillar.title}
              description={pillar.description}
              icon={<Icon name={pillar.icon} size={24} aria-label={pillar.title} color="var(--color-text-accent, #1d4ed8)" />}
            />
          ))}
        </div>
      </PublicContentContainer>
    </section>
  );
}
