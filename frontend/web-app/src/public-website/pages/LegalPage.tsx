import { useLocation } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { Prose } from './PageShell';

interface LegalDoc {
  title: string;
  intro: string;
  sections: { heading: string; body: string }[];
}

const LEGAL_DOCS: Record<string, LegalDoc> = {
  '/privacy-policy': {
    title: 'Privacy Policy',
    intro: 'How SporeKart handles your data. This is placeholder content pending legal review.',
    sections: [
      { heading: 'Information we collect', body: 'We collect information you provide (such as name and email) and basic usage data to improve the site. Details pending finalization.' },
      { heading: 'How we use information', body: 'To respond to inquiries, provide products and training, and communicate updates you opt into.' },
      { heading: 'Your choices', body: 'You can request access, correction, or deletion of your data by contacting us.' },
      { heading: 'Contact', body: 'Questions about privacy can be sent to hello@sporekart.example.com.' },
    ],
  },
  '/terms-and-conditions': {
    title: 'Terms & Conditions',
    intro: 'The terms governing use of SporeKart. This is placeholder content pending legal review.',
    sections: [
      { heading: 'Acceptance of terms', body: 'By using this site you agree to these terms. Final wording pending legal review.' },
      { heading: 'Use of the site', body: 'You agree to use the site lawfully and not to misuse our content or services.' },
      { heading: 'Products & training', body: 'Orders, pricing, and training enrollment are subject to separate agreements shared at purchase.' },
      { heading: 'Limitation of liability', body: 'Our liability is limited to the maximum extent permitted by applicable law.' },
    ],
  },
  '/refund-policy': {
    title: 'Refund Policy',
    intro: 'Our approach to refunds and cancellations. This is placeholder content pending finalization.',
    sections: [
      { heading: 'Eligibility', body: 'Refund eligibility depends on product type and condition. Final policy pending.' },
      { heading: 'How to request', body: 'Contact our team with your order details to start a request.' },
      { heading: 'Processing time', body: 'Approved refunds are processed within a stated period after verification.' },
    ],
  },
  '/shipping-policy': {
    title: 'Shipping Policy',
    intro: 'Delivery areas, timelines, and handling. This is placeholder content pending finalization.',
    sections: [
      { heading: 'Coverage', body: 'We plan pan-India delivery, with cold-chain handling for fresh produce where available.' },
      { heading: 'Timelines', body: 'Estimated timelines will be shared at checkout once the catalog launches.' },
      { heading: 'Tracking', body: 'Order tracking will be provided where supported.' },
    ],
  },
};

export default function LegalPage() {
  const { pathname } = useLocation();
  const doc = LEGAL_DOCS[pathname] ?? LEGAL_DOCS['/privacy-policy'];
  const crumbs = [{ label: 'Home', href: '/' }, { label: doc.title }];
  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{ title: `${doc.title} — SporeKart`, description: doc.intro, canonical: `https://sporekart.example.com${pathname}` }}
    >
      <Prose>
        <p style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, letterSpacing: '0.08em', textTransform: 'uppercase', color: 'var(--color-text-accent, #1d4ed8)', margin: 0 }}>Legal</p>
        <h1 style={{ margin: 'var(--space-2, 8px) 0 var(--space-3, 12px)', fontSize: 'var(--text-title-xl, 32px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>{doc.title}</h1>
        <p style={{ marginTop: 0, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)' }}>{doc.intro}</p>
        {doc.sections.map((s) => (
          <section key={s.heading} style={{ marginTop: 'var(--space-6, 40px)' }}>
            <h2 style={{ fontSize: 'var(--text-title-md, 22px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)', margin: 0 }}>{s.heading}</h2>
            <p style={{ marginTop: 'var(--space-2, 8px)', marginBottom: 0 }}>{s.body}</p>
          </section>
        ))}
      </Prose>
    </PublicLayout>
  );
}
