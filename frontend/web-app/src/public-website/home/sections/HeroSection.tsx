import { Icon } from '../../../design-system/icons/Icon';
import { NavButton } from '../NavButton';

export function HeroSection() {
  const sectionStyle: React.CSSProperties = {
    position: 'relative',
    overflow: 'hidden',
    backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
    backgroundImage:
      'radial-gradient(1200px 400px at 80% -10%, var(--color-bg-accent-subtle, #eef2ff), transparent), radial-gradient(900px 360px at 0% 110%, var(--color-bg-success-subtle, #ecfdf5), transparent)',
  };

  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'minmax(0, 1.1fr) minmax(0, 0.9fr)',
    gap: 'var(--space-10, 56px)',
    alignItems: 'center',
    padding: 'var(--space-12, 72px) var(--space-5, 24px)',
    maxWidth: 'var(--container-xl, 1200px)',
    margin: '0 auto',
  };

  const eyebrowStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-2, 8px)',
    padding: 'var(--space-1, 4px) var(--space-3, 12px)',
    borderRadius: 'var(--radius-pill, 999px)',
    backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)',
    color: 'var(--color-text-accent, #1d4ed8)',
    fontSize: 'var(--text-body-sm, 14px)',
    fontWeight: 600,
    letterSpacing: '0.02em',
  };

  const headlineStyle: React.CSSProperties = {
    margin: 'var(--space-4, 16px) 0 0',
    fontSize: 'clamp(var(--text-title-xl, 32px), 5vw, 56px)',
    lineHeight: 1.08,
    fontWeight: 800,
    color: 'var(--color-text-primary, #1f2933)',
    letterSpacing: '-0.02em',
  };

  const subheadStyle: React.CSSProperties = {
    margin: 'var(--space-4, 16px) 0 0',
    fontSize: 'var(--text-body-lg, 18px)',
    lineHeight: 1.6,
    color: 'var(--color-text-secondary, #4b5563)',
    maxWidth: 540,
  };

  const ctaRowStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    gap: 'var(--space-3, 12px)',
    marginTop: 'var(--space-6, 32px)',
  };

  const trustRowStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    gap: 'var(--space-5, 24px)',
    marginTop: 'var(--space-7, 40px)',
    color: 'var(--color-text-tertiary, #6b7280)',
    fontSize: 'var(--text-body-sm, 14px)',
  };

  const visualStyle: React.CSSProperties = {
    position: 'relative',
    borderRadius: 'var(--radius-xl, 20px)',
    border: '1px solid var(--color-border-default, #e5e7eb)',
    background: 'linear-gradient(160deg, var(--color-bg-accent-subtle, #eef2ff), var(--color-bg-surface-muted, #f8fafc))',
    minHeight: 360,
    padding: 'var(--space-6, 32px)',
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-3, 12px)',
    boxShadow: 'var(--shadow-2, 0 10px 30px rgba(0,0,0,0.08))',
  };

  const chipStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-2, 8px)',
    padding: 'var(--space-2, 8px) var(--space-3, 12px)',
    borderRadius: 'var(--radius-md, 8px)',
    backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
    border: '1px solid var(--color-border-default, #e5e7eb)',
    fontSize: 'var(--text-body-sm, 14px)',
    color: 'var(--color-text-primary, #1f2933)',
    boxShadow: 'var(--shadow-1, 0 1px 3px rgba(0,0,0,0.06))',
  };

  return (
    <section className="sk-home-hero" aria-labelledby="sk-home-hero-title" style={sectionStyle}>
      <div style={gridStyle}>
        <div>
          <span style={eyebrowStyle}>
            <Icon name="sun" size={16} aria-label="Sustainable" /> India's mushroom cultivation ecosystem
          </span>
          <h1 id="sk-home-hero-title" style={headlineStyle}>
            Grow premium mushrooms with confidence.
          </h1>
          <p style={subheadStyle}>
            From lab-verified spawn to harvest-ready kits and expert training, SporeKart gives every
            cultivator — home grower, farm, or enterprise — the tools, knowledge, and support to
            succeed. Technology-driven agriculture, made farmer-friendly.
          </p>
          <div style={ctaRowStyle}>
            <NavButton to="/products" size="lg" rightIcon={<Icon name="arrow-right" size={18} aria-label="Shop" />}>
              Shop Spawn & Kits
            </NavButton>
            <NavButton to="/training" size="lg" variant="outline" leftIcon={<Icon name="book-open" size={18} aria-label="Training" />}>
              Explore Training
            </NavButton>
          </div>
          <div style={trustRowStyle}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1, 4px)' }}>
              <Icon name="shield" size={16} aria-label="Verified" /> Lab-verified spawn
            </span>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1, 4px)' }}>
              <Icon name="truck" size={16} aria-label="Delivery" /> Pan-India cold-chain delivery
            </span>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1, 4px)' }}>
              <Icon name="headphone" size={16} aria-label="Support" /> Grower support, 7 days
            </span>
          </div>
        </div>

        <div style={visualStyle} aria-hidden="true">
          <span style={chipStyle}><Icon name="package" size={18} aria-label="Spawn" /> Premium Spawn Seeds</span>
          <span style={chipStyle}><Icon name="sun" size={18} aria-label="Fresh" /> Fresh & Dried Mushrooms</span>
          <span style={chipStyle}><Icon name="book-open" size={18} aria-label="Training" /> Expert-led Training</span>
          <span style={chipStyle}><Icon name="bar-chart" size={18} aria-label="Yield" /> Higher Yields, Lower Risk</span>
          <div style={{ marginTop: 'auto', display: 'flex', alignItems: 'center', gap: 'var(--space-2, 8px)', color: 'var(--color-text-accent, #1d4ed8)', fontWeight: 600 }}>
            <Icon name="zap" size={18} aria-label="Innovation" /> Cultivation, simplified.
          </div>
        </div>
      </div>

      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          paddingBottom: 'var(--space-4, 16px)',
          color: 'var(--color-text-muted, #9ca3af)',
        }}
      >
        <Icon name="chevron-down" size={22} aria-label="Scroll to explore" />
      </div>
    </section>
  );
}
