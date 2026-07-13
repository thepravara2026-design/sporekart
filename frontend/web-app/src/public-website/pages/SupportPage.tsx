import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { ExperienceStyles } from '../experience/ExperienceStyles';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { NewsletterCta } from '../home/sections/NewsletterCta';
import { SectionHeading, CtaBanner, sectionPad } from './PageShell';

const crumbs = [{ label: 'Home', href: '/' }, { label: 'Support' }];

const HELP_CATEGORIES = [
  { icon: 'message-circle', title: 'Customer Support', description: 'General questions, account, and billing help.', to: '/contact?subject=General%20enquiry', cta: 'Contact' },
  { icon: 'user-check', title: 'Training Support', description: 'Cohort, lab, and post-course field help.', to: '/contact?subject=Training', cta: 'Contact' },
  { icon: 'shopping-bag', title: 'Product Support', description: 'Spawn, kits, fresh & dried product help.', to: '/contact?subject=Products%20%26%20orders', cta: 'Contact' },
  { icon: 'shopping-cart', title: 'Order Support', description: 'Track, modify, or return an order.', to: '/contact?subject=Products%20%26%20orders', cta: 'Contact' },
  { icon: 'help-circle', title: 'Technical Support', description: 'Cultivation, contamination, and yield issues.', to: '/contact?subject=Technical%20support', cta: 'Contact' },
  { icon: 'bar-chart', title: 'Business Enquiries', description: 'Distributor, partnership, and PR.', to: '/contact?subject=Partnership', cta: 'Contact' },
];

const RESPONSE_TIMES = [
  { icon: 'mail', label: 'Email', value: '< 24 business hours' },
  { icon: 'phone', label: 'Phone', value: 'Mon–Sat, 9am–6pm IST' },
  { icon: 'message-circle', label: 'WhatsApp', value: 'Typically within a few hours' },
  { icon: 'book-open', label: 'Help Center', value: 'Instant — FAQ & guides' },
];

const TIMELINE = [
  { title: 'Submit a request', text: 'Use the contact form or email with your category and order ID.' },
  { title: 'Acknowledged', text: 'You receive an automated confirmation with a reference number.' },
  { title: 'Resolved', text: 'A specialist replies with steps or a resolution.' },
  { title: 'Follow-up', text: 'We check back to confirm everything is working.' },
];

const ESCALATION = [
  'Frontline support acknowledges and triages your request.',
  'Specialist (training / product / technical) takes ownership.',
  'Senior agronomist review for complex cultivation issues.',
  'Management escalation for business / partnership matters.',
];

const ORG_SCHEMA = {
  '@context': 'https://schema.org',
  '@type': 'Organization',
  name: 'SporeKart',
  url: 'https://sporekart.example.com',
  contactPoint: { '@type': 'ContactPoint', telephone: '+91 00000 00000', contactType: 'customer support', areaServed: 'IN', availableLanguage: 'en' },
};
const BREADCRUMB_SCHEMA = {
  '@context': 'https://schema.org',
  '@type': 'BreadcrumbList',
  itemListElement: [
    { '@type': 'ListItem', position: 1, name: 'Home', item: 'https://sporekart.example.com/' },
    { '@type': 'ListItem', position: 2, name: 'Support', item: 'https://sporekart.example.com/support' },
  ],
};

export default function SupportPage() {
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{
        title: 'Support — SporeKart Help Center',
        description: 'Get help with products, training, orders, and cultivation. Response times, support process, and escalation flow.',
        canonical: 'https://sporekart.example.com/support',
        type: 'website',
        structuredData: [ORG_SCHEMA, BREADCRUMB_SCHEMA],
      }}
    >
      <ExperienceStyles />

      <section style={{ background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #e8f1ec), var(--color-bg-surface-default, #ffffff))', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <div style={{ padding: 'var(--space-9, 64px) 0 var(--space-7, 48px)', textAlign: 'center' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-accent, #2F6F4F)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', border: '1px solid var(--color-border-subtle, #eef2f7)' }}>
              <Icon name="help-circle" size={14} aria-label="Support" /> Help Center
            </span>
            <h1 style={{ margin: 'var(--space-4, 16px) 0 0', fontSize: 'var(--text-title-xl, 40px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', letterSpacing: '-0.02em' }}>How can we help?</h1>
            <p style={{ margin: 'var(--space-3, 12px) auto 0', maxWidth: 640, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              Friendly, farmer-first support for every stage of your growing journey.
            </p>
          </div>
        </PublicContentContainer>
      </section>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="xl">
            <SectionHeading eyebrow="Browse" title="Help categories" description="Choose the team best placed to help." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {HELP_CATEGORIES.map((c) => (
                <a key={c.title} href={c.to} className="sk-exp-quick">
                  <span className="sk-exp-icon" style={{ marginBottom: 'var(--space-3, 12px)' }}><Icon name={c.icon} size={22} aria-label={c.title} /></span>
                  <h3 style={{ margin: '0 0 var(--space-2, 8px)', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{c.title}</h3>
                  <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6, flex: '1 1 auto' }}>{c.description}</p>
                  <span style={{ marginTop: 'var(--space-3, 12px)', display: 'inline-flex', alignItems: 'center', gap: 4, color: 'var(--color-text-accent, #2F6F4F)', fontWeight: 700, fontSize: 'var(--text-body-sm, 14px)' }}>{c.cta} <Icon name="arrow-right" size={16} aria-label={c.cta} /></span>
                </a>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="xl">
            <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 1fr) minmax(0, 1fr)', gap: 'var(--space-7, 48px)', alignItems: 'start' }}>
              <section>
                <SectionHeading eyebrow="What to expect" title="Response times" />
                <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 'var(--space-4, 16px)' }}>
                  {RESPONSE_TIMES.map((r) => (
                    <div key={r.label} className="sk-exp-card">
                      <span className="sk-exp-icon" style={{ marginBottom: 'var(--space-3, 12px)' }}><Icon name={r.icon} size={22} aria-label={r.label} /></span>
                      <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{r.label}</p>
                      <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{r.value}</p>
                    </div>
                  ))}
                </div>
              </section>

              <section>
                <SectionHeading eyebrow="Process" title="Support journey" />
                <ul className="sk-exp-timeline" style={{ marginTop: 'var(--space-5, 24px)' }}>
                  {TIMELINE.map((t) => (
                    <li key={t.title}>
                      <p style={{ margin: 0, fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{t.title}</p>
                      <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{t.text}</p>
                    </li>
                  ))}
                </ul>
              </section>
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="If it’s urgent" title="Escalation flow" description="Complex or high-priority issues move up quickly." />
            <ol className="sk-exp-timeline" style={{ marginTop: 'var(--space-5, 24px)' }}>
              {ESCALATION.map((step, i) => (
                <li key={i}><p style={{ margin: 0, color: 'var(--color-text-secondary, #4b5563)' }}>{step}</p></li>
              ))}
            </ol>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <CtaBanner title="Still need a hand?" description="Reach the right team and we’ll get back to you fast." primaryLabel="Contact support" primaryTo="/contact" />
          </PublicContentContainer>
        </div>
      </Reveal>

      <NewsletterCta />
    </PublicLayout>
  );
}
