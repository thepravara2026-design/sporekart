import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';

const INNER_PAGES = [
  { path: '/about', label: 'About' },
  { path: '/products', label: 'Products' },
  { path: '/training', label: 'Training' },
  { path: '/blog', label: 'Blog' },
  { path: '/contact', label: 'Contact' },
  { path: '/faq', label: 'FAQ' },
  { path: '/certifications', label: 'Certifications' },
  { path: '/privacy-policy', label: 'Privacy Policy' },
  { path: '/terms-and-conditions', label: 'Terms & Conditions' },
  { path: '/refund-policy', label: 'Refund Policy' },
  { path: '/shipping-policy', label: 'Shipping Policy' },
];

export function InnerPagesPreview() {
  return (
    <PublicLayout
      breadcrumbs={[{ label: 'Home', href: '/' }, { label: 'Inner Pages' }]}
      seo={{ title: 'Inner Pages Preview', description: 'Index of SporeKart public inner pages for review.', canonical: 'https://sporekart.example.com/preview/inner-pages' }}
    >
      <PublicContentContainer maxWidth="lg">
        <div style={{ padding: 'var(--space-8, 48px) 0' }}>
          <h1 style={{ margin: 0, fontSize: 'var(--text-title-xl, 32px)', fontWeight: 800 }}>Inner Pages Preview</h1>
          <p style={{ margin: 'var(--space-3, 12px) 0 var(--space-6, 40px)', color: 'var(--color-text-secondary, #4b5563)' }}>
            Review each public inner page. All pages are content-ready; data-bound lists are placeholders.
          </p>
          <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 'var(--space-3, 12px)' }}>
            {INNER_PAGES.map((p) => (
              <li key={p.path}>
                <a
                  href={p.path}
                  style={{ display: 'block', padding: 'var(--space-4, 16px)', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-md, 8px)', textDecoration: 'none', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}
                >
                  {p.label} <span style={{ fontWeight: 400, color: 'var(--color-text-secondary, #4b5563)' }}>{p.path}</span>
                </a>
              </li>
            ))}
          </ul>
        </div>
      </PublicContentContainer>
    </PublicLayout>
  );
}

export default InnerPagesPreview;
