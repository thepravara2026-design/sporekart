import { useState } from 'react';
import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';
import { NavButton } from '../NavButton';

export const FAQ_ITEMS = [
  { q: 'What is included in a growing kit?', a: 'Each kit ships with prepared substrate, verified spawn, and step-by-step instructions so you can start cultivating in days.' },
  { q: 'Do you ship across India?', a: 'Yes. We deliver pan-India using temperature-controlled cold-chain packaging to protect viability.' },
  { q: 'Is training suitable for beginners?', a: 'Absolutely. Our programs start from fundamentals and include hands-on labs for first-time growers.' },
  { q: 'How do I get cultivation support?', a: 'Our grower support team is available to help with contamination, fruiting, and yield questions.' },
];

export function FaqPreview() {
  const [open, setOpen] = useState<number | null>(0);
  const headerStyle: React.CSSProperties = { marginBottom: 'var(--space-6, 32px)', maxWidth: 640 };

  return (
    <section className="sk-home-faq" aria-labelledby="sk-home-faq-title" style={{ backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
      <PublicContentContainer maxWidth="md">
        <div style={headerStyle}>
          <span style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', textTransform: 'uppercase', letterSpacing: '0.08em' }}>FAQ</span>
          <h2 id="sk-home-faq-title" style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
            Frequently asked questions
          </h2>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)' }}>
          {FAQ_ITEMS.map((item, i) => {
            const isOpen = open === i;
            return (
              <div key={item.q} style={{ borderRadius: 'var(--radius-lg, 12px)', border: '1px solid var(--color-border-default, #e5e7eb)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', overflow: 'hidden' }}>
                <h3 style={{ margin: 0 }}>
                  <button
                    type="button"
                    aria-expanded={isOpen}
                    aria-controls={`sk-faq-panel-${i}`}
                    id={`sk-faq-trigger-${i}`}
                    onClick={() => setOpen(isOpen ? null : i)}
                    style={{ width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-3, 12px)', padding: 'var(--space-4, 16px)', background: 'transparent', border: 'none', cursor: 'pointer', textAlign: 'left', fontSize: 'var(--text-body-md, 16px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}
                  >
                    {item.q}
                    <Icon name={isOpen ? 'chevron-up' : 'chevron-down'} size={20} aria-label={isOpen ? 'Collapse' : 'Expand'} color="var(--color-text-muted, #9ca3af)" />
                  </button>
                </h3>
                {isOpen && (
                  <div id={`sk-faq-panel-${i}`} role="region" aria-labelledby={`sk-faq-trigger-${i}`} style={{ padding: '0 var(--space-4, 16px) var(--space-4, 16px)', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
                    {item.a}
                  </div>
                )}
              </div>
            );
          })}
        </div>

        <div style={{ marginTop: 'var(--space-5, 20px)' }}>
          <NavButton to="/faq" variant="outline" rightIcon={<Icon name="arrow-right" size={18} aria-label="All" />}>View all FAQs</NavButton>
        </div>
      </PublicContentContainer>
    </section>
  );
}
