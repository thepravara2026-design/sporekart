import { useState } from 'react';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { FAQ_ITEMS } from '../home/sections/FaqPreview';
import { PageHeader, SectionHeading, CtaBanner, sectionPad } from './PageShell';

const FAQ_CATEGORIES = [
  {
    heading: 'Getting started',
    items: FAQ_ITEMS,
  },
  {
    heading: 'Products & orders',
    items: [
      { q: 'What mushroom products do you sell?', a: 'Lab-verified spawn, fresh and dried mushrooms, grow kits, and cultivation inputs. The full catalog is coming soon.' },
      { q: 'Do you ship across India?', a: 'Yes — pan-India delivery is planned, with cold-chain handling for fresh produce where available.' },
      { q: 'Are bulk or wholesale orders supported?', a: 'Yes, we support commercial growers. Contact our team to discuss volumes and pricing.' },
    ],
  },
  {
    heading: 'Training & support',
    items: [
      { q: 'Do I need experience to join training?', a: 'No. Our Foundations program is built for complete beginners and grows with you.' },
      { q: 'Is support available after a course?', a: 'Yes — post-course field support helps you troubleshoot real growing problems.' },
    ],
  },
];

function Accordion({ items }: { items: { q: string; a: string }[] }) {
  const [open, setOpen] = useState<number | null>(0);
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-3, 12px)' }}>
      {items.map((item, i) => {
        const isOpen = open === i;
        const id = `faq-item-${i}`;
        return (
          <div key={item.q} style={{ border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', overflow: 'hidden' }}>
            <h3 style={{ margin: 0 }}>
              <button
                type="button"
                onClick={() => setOpen(isOpen ? null : i)}
                aria-expanded={isOpen}
                aria-controls={id}
                style={{ width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-3, 12px)', padding: 'var(--space-4, 16px)', background: 'transparent', border: 'none', cursor: 'pointer', fontSize: 'var(--text-body-md, 16px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)', textAlign: 'left' }}
              >
                {item.q}
                <Icon name={isOpen ? 'chevron-up' : 'chevron-down'} size={20} aria-label={isOpen ? 'Collapse' : 'Expand'} />
              </button>
            </h3>
            {isOpen && (
              <div id={id} style={{ padding: '0 var(--space-4, 16px) var(--space-4, 16px)', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.7 }}>
                {item.a}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
}

export default function FaqPage() {
  const crumbs = [{ label: 'Home', href: '/' }, { label: 'FAQ' }];
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: 'FAQ — SporeKart', description: 'Answers to common questions about products, training, shipping, and support.', canonical: 'https://sporekart.example.com/faq' }}
    >
      <PageHeader
        eyebrow="Help"
        title="Frequently asked questions"
        intro="Quick answers to the things growers ask us most. Still stuck? Reach out on the contact page."
      />

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-7, 32px)' }}>
              {FAQ_CATEGORIES.map((cat) => (
                <section key={cat.heading}>
                  <SectionHeading title={cat.heading} />
                  <div style={{ marginTop: 'var(--space-4, 16px)' }}>
                    <Accordion items={cat.items} />
                  </div>
                </section>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <CtaBanner title="Didn’t find your answer?" description="Our team is happy to help with products, training, and support." primaryLabel="Contact us" primaryTo="/contact" />
    </PublicLayout>
  );
}
