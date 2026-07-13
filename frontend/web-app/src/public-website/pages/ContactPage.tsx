import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { ExperienceStyles } from '../experience/ExperienceStyles';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { NewsletterCta } from '../home/sections/NewsletterCta';
import { SectionHeading, CtaBanner, sectionPad } from './PageShell';
import { ContactForm } from '../experience/components/ContactForm';
import { COMPANY, CONTACT_QUICK_LINKS } from '../experience/business-info';

const crumbs = [{ label: 'Home', href: '/' }, { label: 'Contact' }];

const CONTACT_ROWS = [
  { icon: 'mail', label: 'Support email', value: COMPANY.supportEmail, href: `mailto:${COMPANY.supportEmail}` },
  { icon: 'phone', label: 'Phone', value: COMPANY.phone, href: `tel:${COMPANY.phone.replace(/\s|\(placeholder\)/g, '')}` },
  { icon: 'map-pin', label: 'Office', value: COMPANY.addressLines.join(', ') },
  { icon: 'clock', label: 'Business hours', value: COMPANY.hours },
];

const ORG_SCHEMA = {
  '@context': 'https://schema.org',
  '@type': 'ContactPage',
  mainEntity: {
    '@type': 'Organization',
    name: COMPANY.name,
    url: 'https://sporekart.example.com',
    email: COMPANY.supportEmail,
    telephone: COMPANY.phone,
    contactPoint: {
      '@type': 'ContactPoint',
      telephone: COMPANY.phone,
      contactType: 'customer support',
      email: COMPANY.supportEmail,
      areaServed: 'IN',
      availableLanguage: 'en',
    },
  },
};
const BREADCRUMB_SCHEMA = {
  '@context': 'https://schema.org',
  '@type': 'BreadcrumbList',
  itemListElement: [
    { '@type': 'ListItem', position: 1, name: 'Home', item: 'https://sporekart.example.com/' },
    { '@type': 'ListItem', position: 2, name: 'Contact', item: 'https://sporekart.example.com/contact' },
  ],
};

export default function ContactPage() {
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{
        title: 'Contact SporeKart — Talk to our team',
        description: 'Reach SporeKart for products, training, support, distribution, and partnerships. Farmer-friendly help across India.',
        canonical: 'https://sporekart.example.com/contact',
        type: 'website',
        structuredData: [ORG_SCHEMA, BREADCRUMB_SCHEMA],
      }}
    >
      <ExperienceStyles />

      {/* Hero */}
      <section style={{ background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #e8f1ec), var(--color-bg-surface-default, #ffffff))', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <div style={{ padding: 'var(--space-9, 64px) 0 var(--space-7, 48px)', textAlign: 'center' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-accent, #2F6F4F)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', border: '1px solid var(--color-border-subtle, #eef2f7)' }}>
              <Icon name="message-circle" size={14} aria-label="Contact" /> Get in touch
            </span>
            <h1 style={{ margin: 'var(--space-4, 16px) 0 0', fontSize: 'var(--text-title-xl, 40px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', letterSpacing: '-0.02em' }}>
              We’re here to help you grow
            </h1>
            <p style={{ margin: 'var(--space-3, 12px) auto 0', maxWidth: 640, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              Questions about products, training, or your farm? Send a note and our team will respond.
            </p>
          </div>
        </PublicContentContainer>
      </section>

      {/* Form + info */}
      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="xl">
            <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1.1fr) minmax(0, 0.9fr)', gap: 'var(--space-7, 48px)', alignItems: 'start' }}>
              <div className="sk-exp-card">
                <h2 style={{ margin: '0 0 var(--space-4, 16px)', fontSize: 'var(--text-title-md, 24px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>Send us a message</h2>
                <ContactForm />
              </div>

              <aside style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4, 16px)' }}>
                <div className="sk-exp-card">
                  <h2 style={{ margin: '0 0 var(--space-4, 16px)', fontSize: 'var(--text-title-md, 22px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>Contact information</h2>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4, 16px)' }}>
                    {CONTACT_ROWS.map((r) => (
                      <div key={r.label} style={{ display: 'flex', gap: 'var(--space-3, 12px)', alignItems: 'flex-start' }}>
                        <span className="sk-exp-icon"><Icon name={r.icon} size={22} aria-label={r.label} /></span>
                        <div>
                          <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>{r.label}</p>
                          {r.href ? (
                            <a href={r.href} style={{ fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', textDecoration: 'none' }}>{r.value}</a>
                          ) : (
                            <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>{r.value}</p>
                          )}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>

                <div className="sk-exp-card">
                  <p style={{ margin: '0 0 var(--space-3, 12px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>Find us</p>
                  <div className="sk-exp-map" role="img" aria-label="Map placeholder — SporeKart office location pending">
                    <Icon name="map-pin" size={28} aria-label="Map" />
                    <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 500 }}>Google Maps placeholder</span>
                    <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontStyle: 'italic' }}>{COMPANY.businessLocation}</span>
                  </div>
                  <div style={{ marginTop: 'var(--space-3, 12px)', display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)' }}>
                    {COMPANY.social.map((s) => (
                      <a key={s.label} href={s.href} target="_blank" rel="noopener noreferrer" className="sk-exp-chip" aria-label={s.label}>
                        <Icon name={s.icon} size={16} aria-label={s.label} /> {s.label}
                      </a>
                    ))}
                  </div>
                </div>

                <div className="sk-exp-card" style={{ backgroundColor: 'var(--color-bg-warning-subtle, #fef3c7)', borderColor: 'var(--color-border-warning, #f59e0b)' }}>
                  <p style={{ margin: 0, display: 'flex', gap: 'var(--space-2, 8px)', alignItems: 'flex-start', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-warning, #92400e)' }}>
                    <Icon name="alert-triangle" size={18} aria-label="Note" /> {COMPANY.emergencyContact}
                  </p>
                </div>
              </aside>
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      {/* Quick contact cards */}
      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="xl">
            <SectionHeading eyebrow="More ways to reach us" title="Quick contact" description="Pick the fastest path for your enquiry." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {CONTACT_QUICK_LINKS.map((q) => (
                <a key={q.title} href={q.to} className="sk-exp-quick">
                  <span className="sk-exp-icon" style={{ marginBottom: 'var(--space-3, 12px)' }}><Icon name={q.icon} size={22} aria-label={q.title} /></span>
                  <h3 style={{ margin: '0 0 var(--space-2, 8px)', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{q.title}</h3>
                  <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6, flex: '1 1 auto' }}>{q.description}</p>
                  <span style={{ marginTop: 'var(--space-3, 12px)', display: 'inline-flex', alignItems: 'center', gap: 4, color: 'var(--color-text-accent, #2F6F4F)', fontWeight: 700, fontSize: 'var(--text-body-sm, 14px)' }}>
                    {q.cta} <Icon name="arrow-right" size={16} aria-label={q.cta} />
                  </span>
                </a>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <CtaBanner title="Want to grow with us?" description="Explore training cohorts or browse the product catalog." primaryLabel="Explore training" primaryTo="/training" />
          </PublicContentContainer>
        </div>
      </Reveal>

      <NewsletterCta />
    </PublicLayout>
  );
}
