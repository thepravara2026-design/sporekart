import { PublicContentContainer } from '../../PublicContentContainer';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { NavButton } from '../NavButton';

const CATEGORIES = [
  { icon: 'package', title: 'Spawn Seeds', description: 'Lab-verified spawn for reliable, high-yield colonization.', to: '/products' },
  { icon: 'sun', title: 'Fresh Mushrooms', description: 'Hand-harvested, cold-chain delivered fresh mushrooms.', to: '/products' },
  { icon: 'box', title: 'Dry Mushrooms', description: 'Long-shelf-life dried mushrooms for retail and kitchen.', to: '/products' },
  { icon: 'layers', title: 'Growing Kits', description: 'Beginner-friendly all-in-one kits to start in days.', to: '/products' },
  { icon: 'tag', title: 'Accessories', description: 'Substrates, trays, misters, and cultivation tools.', to: '/products' },
  { icon: 'book-open', title: 'Knowledge', description: 'Guides, datasets, and cultivation playbooks.', to: '/blog' },
];

export function FeaturedProducts() {
  const headerStyle: React.CSSProperties = {
    marginBottom: 'var(--space-6, 32px)',
    maxWidth: 640,
  };

  const gridStyle: React.CSSProperties = {
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))',
    gap: 'var(--space-4, 16px)',
  };

  const cardBodyStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-2, 8px)',
    height: '100%',
  };

  const ctaStyle: React.CSSProperties = {
    marginTop: 'auto',
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-1, 4px)',
    color: 'var(--color-text-accent, #1d4ed8)',
    fontWeight: 600,
    fontSize: 'var(--text-body-sm, 14px)',
  };

  return (
    <section className="sk-home-featured" aria-labelledby="sk-home-featured-title" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
      <PublicContentContainer>
        <div style={headerStyle}>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Catalog</span>
          <h2 id="sk-home-featured-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Featured product categories
          </h2>
          <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>
            From your first spawn to a full harvest — everything a cultivator needs, in one trusted place.
          </p>
        </div>

        <div style={gridStyle}>
          {CATEGORIES.map((category) => (
            <Card key={category.title} variant="outlined" padding="lg" hoverable as="article" aria-label={category.title}>
              <div style={cardBodyStyle}>
                <span
                  aria-hidden="true"
                  style={{ display: 'inline-flex', width: 48, height: 48, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)', marginBottom: 'var(--space-3, 12px)' }}
                >
                  <Icon name={category.icon} size={24} aria-label={category.title} />
                </span>
                <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{category.title}</h3>
                <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.55 }}>{category.description}</p>
                <NavButton to={category.to} variant="link" size="sm" rightIcon={<Icon name="arrow-right" size={16} aria-label="View" />} style={{ ...ctaStyle, border: 'none', background: 'transparent', padding: 0, cursor: 'pointer' }}>
                  View category
                </NavButton>
              </div>
            </Card>
          ))}
        </div>
      </PublicContentContainer>
    </section>
  );
}
