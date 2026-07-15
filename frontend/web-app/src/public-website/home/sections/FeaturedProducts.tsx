import { useState } from 'react';
import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';

const CATEGORIES = [
  { id: 'spawn', icon: 'package', title: 'Spawn Seeds' },
  { id: 'fresh', icon: 'sun', title: 'Fresh Mushrooms' },
  { id: 'dry', icon: 'box', title: 'Dry Mushrooms' },
  { id: 'kits', icon: 'layers', title: 'Growing Kits' },
  { id: 'accessories', icon: 'tag', title: 'Accessories' },
  { id: 'knowledge', icon: 'book-open', title: 'Knowledge' },
];

interface ProductItem {
  title: string;
  price: string;
}

const CATEGORY_GRADIENTS: Record<string, string> = {
  spawn: 'linear-gradient(150deg, #2f6f4f 0%, #4a9e6e 100%)',
  fresh: 'linear-gradient(150deg, #d97706 0%, #f59e0b 100%)',
  dry: 'linear-gradient(150deg, #92400e 0%, #b45309 100%)',
  kits: 'linear-gradient(150deg, #1e40af 0%, #3b82f6 100%)',
  accessories: 'linear-gradient(150deg, #6d28d9 0%, #8b5cf6 100%)',
  knowledge: 'linear-gradient(150deg, #92400e 0%, #d97706 100%)',
};

const MOCK_PRODUCTS: Record<string, ProductItem[]> = {
  spawn: [
    { title: 'Oyster Mushroom Spawn', price: '₹249' },
    { title: 'Button Mushroom Spawn', price: '₹299' },
    { title: 'Shiitake Spawn Bag', price: '₹349' },
    { title: 'King Oyster Spawn', price: '₹279' },
    { title: 'Milky Mushroom Spawn', price: '₹219' },
  ],
  fresh: [
    { title: 'Fresh Oyster Mushrooms', price: '₹180' },
    { title: 'Fresh Button Mushrooms', price: '₹160' },
    { title: 'Fresh Shiitake', price: '₹320' },
    { title: 'Fresh King Oyster', price: '₹240' },
  ],
  dry: [
    { title: 'Dried Oyster Mushrooms', price: '₹450' },
    { title: 'Dried Shiitake', price: '₹580' },
    { title: 'Mushroom Powder Mix', price: '₹350' },
    { title: 'Dried Porcini', price: '₹690' },
  ],
  kits: [
    { title: 'Starter Grow Kit', price: '₹599' },
    { title: 'Oyster Mushroom Kit', price: '₹449' },
    { title: 'Premium Combo Kit', price: '₹999' },
    { title: 'Button Mushroom Kit', price: '₹549' },
  ],
  accessories: [
    { title: 'Substrate Bags (10pk)', price: '₹299' },
    { title: 'Misting Sprayer', price: '₹199' },
    { title: 'Growing Trays (Set of 3)', price: '₹349' },
    { title: 'pH Testing Kit', price: '₹249' },
    { title: 'Humidity Monitor', price: '₹399' },
  ],
  knowledge: [
    { title: 'Cultivation Guide Vol 1', price: 'Free' },
    { title: 'Masterclass Recording', price: '₹499' },
    { title: 'Farm Setup Playbook', price: '₹299' },
    { title: 'Disease ID Chart', price: 'Free' },
  ],
};

function Pill({ label, icon: iconName, active: isActive, onClick }: { label: string; icon: string; active: boolean; onClick: () => void }) {
  return (
    <button
      type="button"
      onClick={onClick}
      aria-pressed={isActive}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: 'var(--space-1, 4px)',
        padding: 'var(--space-2, 8px) var(--space-4, 16px)',
        borderRadius: 'var(--radius-pill, 999px)',
        border: isActive ? '1px solid var(--color-bg-accent-default, #2f6f4f)' : '1px solid var(--color-border-default, #e5e7eb)',
        background: isActive ? 'var(--color-bg-accent-default, #2f6f4f)' : 'var(--color-bg-surface-default, #ffffff)',
        color: isActive ? 'var(--color-text-inverse, #ffffff)' : 'var(--color-text-secondary, #4b5563)',
        fontSize: 'var(--text-body-sm, 14px)',
        fontWeight: isActive ? 600 : 500,
        cursor: 'pointer',
        whiteSpace: 'nowrap',
        transition: 'all 200ms cubic-bezier(0.4, 0, 0.2, 1)',
        flexShrink: 0,
        boxShadow: isActive ? '0 2px 8px rgba(47, 111, 79, 0.25)' : 'none',
      }}
    >
      <Icon name={iconName} size={14} aria-label="" />
      {label}
    </button>
  );
}

function ProductCard({ title, price }: ProductItem) {
  return (
    <div
      style={{
        borderRadius: 'var(--radius-lg, 12px)',
        background: 'var(--color-bg-surface-default, #ffffff)',
        overflow: 'hidden',
        display: 'flex',
        flexDirection: 'column',
        transition: 'transform 300ms cubic-bezier(0.4, 0, 0.2, 1), box-shadow 300ms cubic-bezier(0.4, 0, 0.2, 1)',
        cursor: 'pointer',
      }}
      onMouseEnter={(e) => { e.currentTarget.style.transform = 'translateY(-4px)'; e.currentTarget.style.boxShadow = '0 12px 32px rgba(0,0,0,0.1)'; }}
      onMouseLeave={(e) => { e.currentTarget.style.transform = 'translateY(0)'; e.currentTarget.style.boxShadow = 'none'; }}
    >
      <div
        style={{
          position: 'relative',
          height: 200,
          background: CATEGORY_GRADIENTS[title.split(' ')[0].toLowerCase()] || 'linear-gradient(150deg, var(--color-bg-accent-subtle, #c8e6c9), var(--color-bg-surface-muted, #f8fafc))',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          transition: 'transform 300ms cubic-bezier(0.4, 0, 0.2, 1)',
        }}
        className="sk-ft-img"
      >
        <div
          style={{
            width: 48, height: 48, borderRadius: 'var(--radius-pill, 999px)',
            background: 'rgba(255,255,255,0.2)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            backdropFilter: 'blur(4px)',
          }}
        >
          <Icon name="package" size={24} color="rgba(255,255,255,0.9)" />
        </div>
      </div>
      <div style={{ padding: 'var(--space-3, 12px) var(--space-4, 16px) var(--space-4, 16px)', display: 'flex', flexDirection: 'column', gap: 'var(--space-1, 4px)' }}>
        <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)', lineHeight: 1.35 }}>{title}</span>
        <span style={{ fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-accent, #2f6f4f)', letterSpacing: '-0.01em' }}>{price}</span>
      </div>
      <style>{`
        [class*="sk-ft-img"]:hover { transform: scale(1.04); }
      `}</style>
    </div>
  );
}

export function FeaturedProducts() {
  const [active, setActive] = useState(CATEGORIES[0].id);

  const products = MOCK_PRODUCTS[active] || [];

  return (
    <section className="sk-home-featured" aria-labelledby="sk-home-featured-title" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)', padding: 'var(--space-12, 48px) 0' }}>
      <PublicContentContainer>
        <div style={{ marginBottom: 'var(--space-8, 32px)', maxWidth: 640 }}>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #2f6f4f)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>Catalog</span>
          <h2 id="sk-home-featured-title" style={{ margin: 'var(--space-2, 8px) 0 var(--space-1, 4px)', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Featured products
          </h2>
          <p style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>
            From your first spawn to a full harvest — everything a cultivator needs.
          </p>
        </div>

        <div
          role="tablist"
          aria-label="Product categories"
          style={{
            display: 'flex',
            gap: 'var(--space-2, 8px)',
            flexWrap: 'wrap',
            marginBottom: 'var(--space-6, 24px)',
          }}
        >
          {CATEGORIES.map((cat) => (
            <Pill key={cat.id} label={cat.title} icon={cat.icon} active={active === cat.id} onClick={() => setActive(cat.id)} />
          ))}
        </div>

        <div
          role="tabpanel"
          aria-label={`${CATEGORIES.find((c) => c.id === active)?.title} products`}
          className="sk-ft-grid"
        >
          {products.slice(0, 4).map((product) => (
            <ProductCard key={product.title} title={product.title} price={product.price} />
          ))}
        </div>

        <style>{`
          .sk-ft-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: var(--space-5, 20px);
          }
          @media (max-width: 900px) {
            .sk-ft-grid { grid-template-columns: repeat(2, 1fr); }
          }
          @media (max-width: 520px) {
            .sk-ft-grid { grid-template-columns: 1fr; }
          }
        `}</style>
      </PublicContentContainer>
    </section>
  );
}
