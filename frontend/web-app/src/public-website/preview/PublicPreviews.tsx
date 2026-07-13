import { ResponsivePreview } from '../../design-system/playground/components/ResponsivePreview';
import { PublicLayout } from '../PublicLayout';
import { PublicHeader } from '../PublicHeader';
import { PublicFooter } from '../PublicFooter';
import { PublicNav } from '../PublicNav';
import { PublicContentContainer } from '../PublicContentContainer';
import { Seo } from '../Seo';
import { TrustSection } from '../sections/TrustSection';
import { FutureCta } from '../sections/FutureCta';
import { FutureTestimonial } from '../sections/FutureTestimonial';
import { FutureStatistics } from '../sections/FutureStatistics';
import { FutureBlogSection } from '../sections/FutureBlogSection';

function previewShell(label: string, description: string, children: React.ReactNode) {
  return (
    <div style={{ padding: 'var(--space-5, 24px)' }}>
      <div style={{ marginBottom: 'var(--space-4, 16px)' }}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-title-lg, 26px)', fontWeight: 700 }}>{label}</h1>
        <p style={{ margin: 'var(--space-1, 4px) 0 0', color: 'var(--color-text-secondary, #4b5563)' }}>{description}</p>
      </div>
      {children}
    </div>
  );
}

export function PublicLayoutPreview() {
  return previewShell(
    'Public Layout',
    'Full public website shell: announcement region, header, content region, and footer with responsive behaviour.',
    <ResponsivePreview defaultViewport="desktop">
      <PublicLayout
        announcement={{}}
        seo={{ title: 'Layout preview' }}
        breadcrumbs={[{ label: 'Home', href: '/' }, { label: 'Preview' }]}
      >
        <TrustSection />
        <FutureStatistics />
        <FutureCta />
        <FutureTestimonial />
        <FutureBlogSection />
      </PublicLayout>
    </ResponsivePreview>,
  );
}

export function PublicHeaderPreview() {
  return previewShell(
    'Public Header',
    'Sticky public header with brand, primary navigation, search, and authentication entry. Resize to see the mobile drawer.',
    <ResponsivePreview defaultViewport="laptop">
      <PublicHeader />
      <PublicContentContainer>
        <p style={{ color: 'var(--color-text-secondary, #4b5563)' }}>Header preview area. Open at mobile width to trigger the navigation drawer.</p>
      </PublicContentContainer>
    </ResponsivePreview>,
  );
}

export function PublicFooterPreview() {
  return previewShell(
    'Public Footer',
    'Public footer with link columns, social links, and legal row.',
    <ResponsivePreview defaultViewport="desktop">
      <PublicFooter />
    </ResponsivePreview>,
  );
}

export function PublicNavigationPreview() {
  return previewShell(
    'Public Navigation',
    'Primary navigation items used across the public website header and mobile drawer.',
    <ResponsivePreview defaultViewport="mobile">
      <div style={{ padding: 'var(--space-5, 24px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
        <PublicNav orientation="vertical" />
      </div>
    </ResponsivePreview>,
  );
}

export function PublicSeoPreview() {
  return previewShell(
    'Public SEO',
    'Injects document title, meta description, canonical, Open Graph, Twitter cards, and JSON-LD structured data. Inspect the document head to verify.',
    <>
      <Seo
        title="SEO Preview"
        description="SporeKart public website SEO foundation preview."
        canonical="https://sporekart.example.com/preview/public-seo"
        type="website"
        structuredData={{
          '@context': 'https://schema.org',
          '@type': 'Organization',
          name: 'SporeKart',
          url: 'https://sporekart.example.com',
        }}
      />
      <PublicContentContainer maxWidth="md">
        <div style={{ padding: 'var(--space-6, 32px)', borderRadius: 'var(--radius-lg, 12px)', border: '1px dashed var(--color-border-default, #e5e7eb)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
          <p style={{ margin: 0, color: 'var(--color-text-secondary, #4b5563)' }}>
            This preview renders the <code>Seo</code> component. Open your browser devtools and inspect <code>&lt;head&gt;</code> to see the injected meta tags, canonical link, and JSON-LD structured data.
          </p>
        </div>
      </PublicContentContainer>
    </>,
  );
}
