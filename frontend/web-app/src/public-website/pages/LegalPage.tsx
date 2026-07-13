import { useLocation } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { ExperienceStyles } from '../experience/ExperienceStyles';
import { LegalTemplate, type LegalSection } from '../experience/components/LegalTemplate';

interface LegalDoc {
  title: string;
  intro: string;
  lastUpdated: string;
  sections: { heading: string; body: string }[];
}

const slug = (s: string) => s.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '');

const LEGAL_DOCS: Record<string, LegalDoc> = {
  '/privacy-policy': {
    title: 'Privacy Policy',
    intro: 'How SporeKart handles your data. This is placeholder content pending legal review.',
    lastUpdated: '2026-07-13',
    sections: [
      { heading: 'Information we collect', body: 'We collect information you provide (such as name and email) and basic usage data to improve the site. Details pending finalization.' },
      { heading: 'How we use information', body: 'To respond to inquiries, provide products and training, and communicate updates you opt into.' },
      { heading: 'Cookies & tracking', body: 'We use essential cookies and, where consented, analytics cookies. See our Cookie Policy.' },
      { heading: 'Your choices', body: 'You can request access, correction, or deletion of your data by contacting us.' },
      { heading: 'Contact', body: 'Questions about privacy can be sent to privacy@sporekart.example.com.' },
    ],
  },
  '/terms-and-conditions': {
    title: 'Terms & Conditions',
    intro: 'The terms governing use of SporeKart. This is placeholder content pending legal review.',
    lastUpdated: '2026-07-13',
    sections: [
      { heading: 'Acceptance of terms', body: 'By using this site you agree to these terms. Final wording pending legal review.' },
      { heading: 'Use of the site', body: 'You agree to use the site lawfully and not to misuse our content or services.' },
      { heading: 'Products & training', body: 'Orders, pricing, and training enrollment are subject to separate agreements shared at purchase.' },
      { heading: 'Limitation of liability', body: 'Our liability is limited to the maximum extent permitted by applicable law.' },
    ],
  },
  '/shipping-policy': {
    title: 'Shipping Policy',
    intro: 'Delivery areas, timelines, and handling. This is placeholder content pending finalization.',
    lastUpdated: '2026-07-13',
    sections: [
      { heading: 'Coverage', body: 'We plan pan-India delivery, with cold-chain handling for fresh produce where available.' },
      { heading: 'Timelines', body: 'Estimated timelines will be shared at checkout once the catalog launches.' },
      { heading: 'Tracking', body: 'Order tracking will be provided where supported.' },
    ],
  },
  '/refund-policy': {
    title: 'Refund Policy',
    intro: 'Our approach to refunds and cancellations. This is placeholder content pending finalization.',
    lastUpdated: '2026-07-13',
    sections: [
      { heading: 'Eligibility', body: 'Refund eligibility depends on product type and condition. Final policy pending.' },
      { heading: 'How to request', body: 'Contact our team with your order details to start a request.' },
      { heading: 'Processing time', body: 'Approved refunds are processed within a stated period after verification.' },
    ],
  },
  '/cookie-policy': {
    title: 'Cookie Policy',
    intro: 'How and why we use cookies. This is placeholder content pending legal review.',
    lastUpdated: '2026-07-13',
    sections: [
      { heading: 'What cookies are', body: 'Cookies are small files stored on your device to help the site function and improve experience.' },
      { heading: 'Types we use', body: 'Essential cookies (required), and, with consent, analytics cookies to understand usage.' },
      { heading: 'Managing cookies', body: 'You can control or delete cookies via your browser settings at any time.' },
      { heading: 'Changes', body: 'We will update this policy if our cookie use changes.' },
    ],
  },
  '/disclaimer': {
    title: 'Disclaimer',
    intro: 'Important disclaimers about SporeKart content and services. Placeholder content pending legal review.',
    lastUpdated: '2026-07-13',
    sections: [
      { heading: 'Informational purposes', body: 'Guides and articles are for general information and are not professional, legal, or agricultural advice.' },
      { heading: 'Results may vary', body: 'Yields and outcomes depend on conditions; we do not guarantee specific results.' },
      { heading: 'Third-party links', body: 'We are not responsible for the content or practices of external sites we link to.' },
      { heading: 'Limitation', body: 'To the extent permitted by law, SporeKart excludes liability for indirect or consequential loss.' },
    ],
  },
};

export default function LegalPage() {
  const { pathname } = useLocation();
  const doc = LEGAL_DOCS[pathname] ?? LEGAL_DOCS['/privacy-policy'];
  const sections: LegalSection[] = doc.sections.map((s) => ({ id: slug(s.heading), heading: s.heading, body: s.body }));
  const crumbs = [{ label: 'Home', href: '/' }, { label: doc.title }];

  return (
    <PublicLayout
      breadcrumbs={crumbs}
      seo={{
        title: `${doc.title} — SporeKart`,
        description: doc.intro,
        canonical: `https://sporekart.example.com${pathname}`,
        type: 'website',
        structuredData: {
          '@context': 'https://schema.org',
          '@type': 'BreadcrumbList',
          itemListElement: [
            { '@type': 'ListItem', position: 1, name: 'Home', item: 'https://sporekart.example.com/' },
            { '@type': 'ListItem', position: 2, name: doc.title, item: `https://sporekart.example.com${pathname}` },
          ],
        },
      }}
    >
      <ExperienceStyles />
      <LegalTemplate title={doc.title} intro={doc.intro} lastUpdated={doc.lastUpdated} sections={sections} />
    </PublicLayout>
  );
}
