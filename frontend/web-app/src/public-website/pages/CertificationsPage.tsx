import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { MediaPlaceholder } from '../home/MediaPlaceholder';
import { PageHeader, SectionHeading, Prose, CtaBanner, sectionPad } from './PageShell';

const CERTS = [
  { icon: 'shield', title: 'Lab-Verified Spawn', description: 'Every spawn lot is tested for purity and viability before dispatch.' },
  { icon: 'check-circle', title: 'Quality Management', description: 'Process controls across sourcing, production, and fulfillment.' },
  { icon: 'truck', title: 'Safe Handling & Delivery', description: 'Cold-chain and careful packing for fresh produce.' },
  { icon: 'file', title: 'Compliance Ready', description: 'Documentation and traceability for commercial buyers.' },
];

export default function CertificationsPage() {
  const crumbs = [{ label: 'Home', href: '/' }, { label: 'Certifications' }];
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: 'Certifications — SporeKart', description: 'Quality, compliance, and certification commitments behind SporeKart products and training.', canonical: 'https://sporekart.example.com/certifications' }}
    >
      <PageHeader
        eyebrow="Trust"
        title="Quality and compliance you can verify"
        intro="We hold ourselves to measurable standards so growers and buyers can trust every SporeKart input."
      />

      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <SectionHeading eyebrow="Our commitments" title="Certifications & standards" description="Specific certificates are placeholders pending official documentation." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {CERTS.map((c) => (
                <div key={c.title} style={{ padding: 'var(--space-5, 24px)', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)' }}>
                  <span style={{ display: 'inline-flex', width: 48, height: 48, alignItems: 'center', justifyContent: 'center', borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', color: 'var(--color-text-accent, #1d4ed8)', marginBottom: 'var(--space-3, 12px)' }}>
                    <Icon name={c.icon} size={26} aria-label={c.title} />
                  </span>
                  <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{c.title}</h3>
                  <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{c.description}</p>
                </div>
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={{ ...sectionPad, backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)' }}>
          <PublicContentContainer maxWidth="lg">
            <MediaPlaceholder label="Certificates" icon="file" aspectRatio="21 / 9" caption="Certificate images placeholder" />
          </PublicContentContainer>
        </div>
      </Reveal>

      <Prose>
        <p>
          We are continuously improving our quality system. Official certificates, audit reports, and compliance
          documentation will be published here as they are finalized.
        </p>
      </Prose>

      <CtaBanner title="Growing commercially?" description="Get the documentation and traceability your buyers need." primaryLabel="Contact us" primaryTo="/contact" />
    </PublicLayout>
  );
}
