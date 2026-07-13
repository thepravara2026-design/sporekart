import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { PageHeader, SectionHeading, CtaBanner, sectionPad } from './PageShell';

const CATEGORIES = [
  { icon: 'box', title: 'Mushroom Spawn', description: 'Lab-verified mother spawn and spawn bags for oyster, button, milky, and more.', to: '/products', tag: 'Placeholder catalog' },
  { icon: 'package', title: 'Fresh Mushrooms', description: 'Farm-fresh harvest delivered to your door across major cities.', to: '/products', tag: 'Placeholder catalog' },
  { icon: 'tag', title: 'Dried Mushrooms', description: 'Long-shelf-life dried varieties for retail and processing.', to: '/products', tag: 'Placeholder catalog' },
  { icon: 'layers', title: 'Grow Kits', description: 'Ready-to-fruit kits for beginners and homes.', to: '/products', tag: 'Placeholder catalog' },
  { icon: 'file', title: 'Substrates & Inputs', description: 'Sterilized substrates, bags, and cultivation accessories.', to: '/products', tag: 'Placeholder catalog' },
  { icon: 'book-open', title: 'Training & Guides', description: 'Courses and manuals to grow with confidence.', to: '/training', tag: 'Learn more' },
];

export default function ProductsPage() {
  const crumbs = [{ label: 'Home', href: '/' }, { label: 'Products' }];
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: 'Products — SporeKart', description: 'Explore SporeKart’s mushroom spawn, fresh and dried mushrooms, grow kits, and cultivation inputs.', canonical: 'https://sporekart.example.com/products' }}
    >
      <PageHeader
        eyebrow="Catalog"
        title="Everything a cultivator needs"
        intro="From your first spawn to a full harvest — verified inputs and kits for every stage of mushroom cultivation."
      />

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="Categories" title="Browse by need" description="Product listings are placeholders pending catalog data." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {CATEGORIES.map((c) => (
                <a key={c.title} href={c.to} style={{ textDecoration: 'none', display: 'block' }}>
                  <div style={{ height: '100%', padding: 'var(--space-5, 24px)', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', transition: 'box-shadow .15s ease, transform .15s ease' }} className="sk-pw-card">
                    <span style={{ display: 'inline-flex', width: 48, height: 48, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)', marginBottom: 'var(--space-3, 12px)' }}>
                      <Icon name={c.icon} size={26} aria-label={c.title} />
                    </span>
                    <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{c.title}</h3>
                    <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{c.description}</p>
                    <span style={{ display: 'inline-block', marginTop: 'var(--space-3, 12px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)' }}>{c.tag} →</span>
                  </div>
                </a>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <CtaBanner title="Not sure where to start?" description="Our training programs walk you from first spore to first harvest." primaryLabel="Explore training" primaryTo="/training" />
    </PublicLayout>
  );
}
