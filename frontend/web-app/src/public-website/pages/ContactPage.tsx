import { useState } from 'react';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { PageHeader, SectionHeading, sectionPad } from './PageShell';

const CONTACT_INFO = [
  { icon: 'mail', label: 'Email', value: 'hello@sporekart.example.com' },
  { icon: 'phone', label: 'Phone', value: '+91 00000 00000' },
  { icon: 'map-pin', label: 'Address', value: 'SporeKart Research Farm, Maharashtra, India' },
  { icon: 'clock', label: 'Hours', value: 'Mon–Sat, 9am–6pm IST' },
];

const inputStyle: React.CSSProperties = {
  width: '100%',
  padding: 'var(--space-3, 12px) var(--space-4, 16px)',
  borderRadius: 'var(--radius-md, 8px)',
  border: '1px solid var(--color-border-default, #e5e7eb)',
  backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
  fontSize: 'var(--text-body-md, 16px)',
  color: 'var(--color-text-primary, #1f2933)',
  fontFamily: 'inherit',
};

export default function ContactPage() {
  const crumbs = [{ label: 'Home', href: '/' }, { label: 'Contact' }];
  const [submitted, setSubmitted] = useState(false);

  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: 'Contact — SporeKart', description: 'Get in touch with SporeKart for products, training, and support.', canonical: 'https://sporekart.example.com/contact' }}
    >
      <PageHeader
        eyebrow="Talk to us"
        title="We’re here to help you grow"
        intro="Questions about products, training, or your farm? Send a note and our team will respond."
      />

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <div style={{ display: 'grid', gridTemplateColumns: '1.2fr 1fr', gap: 'var(--space-7, 48px)', alignItems: 'start' }}>
              <form
                onSubmit={(e) => {
                  e.preventDefault();
                  setSubmitted(true);
                }}
                style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4, 16px)' }}
              >
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-4, 16px)' }}>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>
                    Name
                    <input type="text" name="name" required style={inputStyle} />
                  </label>
                  <label style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>
                    Email
                    <input type="email" name="email" required style={inputStyle} />
                  </label>
                </div>
                <label style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>
                  Subject
                  <input type="text" name="subject" style={inputStyle} />
                </label>
                <label style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2, 8px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>
                  Message
                  <textarea name="message" rows={5} required style={{ ...inputStyle, resize: 'vertical' }} />
                </label>
                <div>
                  <button
                    type="submit"
                    style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-3, 12px) var(--space-5, 20px)', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent, #1d4ed8)', color: '#ffffff', fontWeight: 700, border: 'none', cursor: 'pointer' }}
                  >
                    Send message <Icon name="arrow-right" size={18} aria-label="Send" />
                  </button>
                </div>
                <p role="status" style={{ margin: 0, minHeight: 20, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-success, #047857)' }}>
                  {submitted ? 'Thanks! This is a demo form — no message was sent.' : ''}
                </p>
              </form>

              <aside style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4, 16px)' }}>
                <SectionHeading title="Contact details" />
                {CONTACT_INFO.map((c) => (
                  <div key={c.label} style={{ display: 'flex', gap: 'var(--space-3, 12px)', alignItems: 'flex-start' }}>
                    <span style={{ flexShrink: 0, display: 'inline-flex', width: 40, height: 40, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)' }}>
                      <Icon name={c.icon} size={22} aria-label={c.label} />
                    </span>
                    <div>
                      <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-primary, #1f2933)' }}>{c.label}</p>
                      <p style={{ margin: 'var(--space-1, 4px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>{c.value}</p>
                    </div>
                  </div>
                ))}
              </aside>
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>
    </PublicLayout>
  );
}
