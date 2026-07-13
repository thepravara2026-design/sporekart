import { useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { ExperienceStyles } from '../experience/ExperienceStyles';
import { Icon } from '../../design-system/icons/Icon';
import { Input } from '../../design-system/components/core/Input';
import { Reveal } from '../home/Reveal';
import { NewsletterCta } from '../home/sections/NewsletterCta';
import { CtaBanner, sectionPad } from './PageShell';
import { FAQ_CATEGORIES, ALL_FAQ_ITEMS } from '../experience/faq-data';

const crumbs = [{ label: 'Home', href: '/' }, { label: 'FAQ' }];

function buildFaqSchema() {
  return {
    '@context': 'https://schema.org',
    '@type': 'FAQPage',
    mainEntity: ALL_FAQ_ITEMS.map((it) => ({
      '@type': 'Question',
      name: it.q,
      acceptedAnswer: { '@type': 'Answer', text: it.a },
    })),
  };
}

const BREADCRUMB_SCHEMA = {
  '@context': 'https://schema.org',
  '@type': 'BreadcrumbList',
  itemListElement: [
    { '@type': 'ListItem', position: 1, name: 'Home', item: 'https://sporekart.example.com/' },
    { '@type': 'ListItem', position: 2, name: 'FAQ', item: 'https://sporekart.example.com/faq' },
  ],
};

export default function FaqPage() {
  const [query, setQuery] = useState('');
  const [category, setCategory] = useState<string>('all');
  const [open, setOpen] = useState<Set<string>>(new Set([ALL_FAQ_ITEMS[0]?.q]));

  const filtered = useMemo(() => {
    const base = category === 'all'
      ? ALL_FAQ_ITEMS
      : FAQ_CATEGORIES.find((c) => c.slug === category)?.items.map((it) => ({ ...it, category: FAQ_CATEGORIES.find((c) => c.slug === category)?.label ?? '' })) ?? [];
    const q = query.trim().toLowerCase();
    if (!q) return base;
    return base.filter((it) => `${it.q} ${it.a}`.toLowerCase().includes(q));
  }, [query, category]);

  const toggle = (q: string) => {
    setOpen((prev) => {
      const next = new Set(prev);
      if (next.has(q)) next.delete(q);
      else next.add(q);
      return next;
    });
  };

  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{
        title: 'FAQ — SporeKart Help & Answers',
        description: 'Answers to common questions about products, orders, shipping, payments, training, returns, refunds, and partnerships.',
        canonical: 'https://sporekart.example.com/faq',
        type: 'website',
        structuredData: [buildFaqSchema(), BREADCRUMB_SCHEMA],
      }}
    >
      <ExperienceStyles />

      <section style={{ background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #e8f1ec), var(--color-bg-surface-default, #ffffff))', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <div style={{ padding: 'var(--space-9, 64px) 0 var(--space-7, 48px)', textAlign: 'center' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-accent, #2F6F4F)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', border: '1px solid var(--color-border-subtle, #eef2f7)' }}>
              <Icon name="help-circle" size={14} aria-label="FAQ" /> Help
            </span>
            <h1 style={{ margin: 'var(--space-4, 16px) 0 0', fontSize: 'var(--text-title-xl, 40px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', letterSpacing: '-0.02em' }}>Frequently asked questions</h1>
            <p style={{ margin: 'var(--space-3, 12px) auto 0', maxWidth: 640, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              Quick answers to the things growers ask us most.
            </p>
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'flex', justifyContent: 'center' }}>
              <Input
                type="search"
                placeholder="Search FAQs…"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                prefix={<Icon name="search" size={18} aria-label="Search FAQs" />}
                fullWidth
                aria-label="Search frequently asked questions"
                style={{ maxWidth: 560 }}
              />
            </div>
          </div>
        </PublicContentContainer>
      </section>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)', marginBottom: 'var(--space-6, 32px)' }}>
              <button type="button" className={`sk-exp-chip${category === 'all' ? ' is-active' : ''}`} onClick={() => setCategory('all')} aria-pressed={category === 'all'}>
                All
              </button>
              {FAQ_CATEGORIES.map((c) => (
                <button
                  key={c.slug}
                  type="button"
                  className={`sk-exp-chip${category === c.slug ? ' is-active' : ''}`}
                  onClick={() => setCategory(c.slug)}
                  aria-pressed={category === c.slug}
                >
                  {c.label}
                </button>
              ))}
            </div>

            {filtered.length === 0 ? (
              <div className="sk-exp-card" style={{ textAlign: 'center', padding: 'var(--space-8, 48px)' }}>
                <p style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>
                  No FAQs match “{query}”. Try another term or <Link to="/contact" style={{ color: 'var(--color-text-accent, #2F6F4F)' }}>contact us</Link>.
                </p>
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3, 12px)' }}>
                {filtered.map((item) => {
                  const isOpen = open.has(item.q);
                  const panelId = `faq-panel-${encodeURIComponent(item.q)}`;
                  return (
                    <div key={item.q} className="sk-exp-faq-item">
                      <h3 style={{ margin: 0 }}>
                        <button
                          type="button"
                          className="sk-exp-faq-q"
                          aria-expanded={isOpen}
                          aria-controls={panelId}
                          onClick={() => toggle(item.q)}
                        >
                          {item.q}
                          <Icon name={isOpen ? 'chevron-up' : 'chevron-down'} size={20} aria-label={isOpen ? 'Collapse' : 'Expand'} color="var(--color-text-muted, #9ca3af)" />
                        </button>
                      </h3>
                      {isOpen && (
                        <div id={panelId} role="region" style={{ padding: 0 }}>
                          <div className="sk-exp-faq-a">{item.a}</div>
                          <div className="sk-exp-faq-helpful">
                            Was this helpful?
                            <button type="button" className="sk-exp-chip" style={{ padding: '2px 10px' }} aria-label="Yes, helpful">Yes</button>
                            <button type="button" className="sk-exp-chip" style={{ padding: '2px 10px' }} aria-label="No, not helpful">No</button>
                            <span style={{ fontStyle: 'italic' }}>(placeholder)</span>
                          </div>
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
            )}
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              <Link to="/blog" className="sk-exp-quick">
                <span className="sk-exp-icon" style={{ marginBottom: 'var(--space-3, 12px)' }}><Icon name="book-open" size={22} aria-label="Blog" /></span>
                <h3 style={{ margin: '0 0 var(--space-2, 8px)', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>Read the Blog</h3>
                <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>Guides and research for growers.</p>
              </Link>
              <Link to="/support" className="sk-exp-quick">
                <span className="sk-exp-icon" style={{ marginBottom: 'var(--space-3, 12px)' }}><Icon name="help-circle" size={22} aria-label="Support" /></span>
                <h3 style={{ margin: '0 0 var(--space-2, 8px)', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>Visit Support</h3>
                <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>Response times and escalation.</p>
              </Link>
              <Link to="/training" className="sk-exp-quick">
                <span className="sk-exp-icon" style={{ marginBottom: 'var(--space-3, 12px)' }}><Icon name="user-check" size={22} aria-label="Training" /></span>
                <h3 style={{ margin: '0 0 var(--space-2, 8px)', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>Training</h3>
                <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>Hands-on cohorts and labs.</p>
              </Link>
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <CtaBanner title="Didn’t find your answer?" description="Our team is happy to help with products, training, and support." primaryLabel="Contact us" primaryTo="/contact" />
          </PublicContentContainer>
        </div>
      </Reveal>

      <NewsletterCta />
    </PublicLayout>
  );
}
