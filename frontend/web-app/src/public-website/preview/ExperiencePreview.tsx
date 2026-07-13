import { useLocation } from 'react-router-dom';
import { ResponsivePreview } from '../../design-system/playground/components/ResponsivePreview';
import ContactPage from '../pages/ContactPage';
import SupportPage from '../pages/SupportPage';
import FaqPage from '../pages/FaqPage';
import LegalPage from '../pages/LegalPage';

type View = 'contact' | 'support' | 'faq' | 'legal';

function useView(): View {
  const { pathname } = useLocation();
  if (pathname.includes('/support')) return 'support';
  if (pathname.includes('/faq')) return 'faq';
  if (pathname.includes('/legal')) return 'legal';
  return 'contact';
}

function notesBlock(title: string, items: string[], accent: string) {
  return (
    <div style={{ backgroundColor: accent, borderRadius: 'var(--radius-md, 8px)', padding: 'var(--space-3, 12px) var(--space-4, 16px)' }}>
      <h3 style={{ fontSize: 'var(--text-body-md, 16px)', fontWeight: 700, margin: '0 0 var(--space-2, 8px)' }}>{title}</h3>
      <ul style={{ margin: 0, paddingLeft: 'var(--space-5, 24px)', color: 'var(--color-text-secondary, #4b5563)', fontSize: 'var(--text-body-sm, 14px)', lineHeight: 1.6 }}>
        {items.map((it) => <li key={it}>{it}</li>)}
      </ul>
    </div>
  );
}

function PreviewContent({ view }: { view: View }) {
  if (view === 'support') return <SupportPage />;
  if (view === 'faq') return <FaqPage />;
  if (view === 'legal') return <LegalPage />;
  return <ContactPage />;
}

const META: Record<View, { title: string; a11y: string[]; resp: string[]; sections: string[] }> = {
  contact: {
    title: 'Contact',
    a11y: ['Form uses labelled Input/Checkbox + aria-invalid on errors.', 'Success/Error via role="alert" alerts.', 'Map is an aria-labelled placeholder; links have aria-labels.', 'Single H1; landmarks from PublicLayout.'],
    resp: ['Two-column form/info collapses under ~1024px.', 'Quick-contact cards auto-fill.', 'No overflow at 375px; hero centers copy.'],
    sections: ['Hero', 'Contact form', 'Contact info + map', 'Social links', 'Emergency note', 'Quick contact cards', 'Training CTA', 'Newsletter'],
  },
  support: {
    title: 'Support',
    a11y: ['Category cards are links with descriptive aria-labels.', 'Timeline uses semantic ul/ol.', 'CTAs are buttons/links; no hidden traps.'],
    resp: ['Hero + category grid reflow.', 'Response times / timeline two-column → stacked.', 'No overflow on mobile.'],
    sections: ['Hero', 'Help categories', 'Response times', 'Support journey timeline', 'Escalation flow', 'Contact CTA', 'Newsletter'],
  },
  faq: {
    title: 'FAQ',
    a11y: ['Search input labelled; category chips are aria-pressed buttons.', 'Accordion buttons: aria-expanded + aria-controls + region.', 'FAQ schema (JSON-LD) for rich results.'],
    resp: ['Search + chips wrap.', 'Accordion list single column.', 'Related cards auto-fill; CTA band.'],
    sections: ['Hero + search', 'Category filter', 'Accordion (helpful vote placeholder)', 'Related links', 'Contact CTA', 'Newsletter'],
  },
  legal: {
    title: 'Legal',
    a11y: ['TOC links have aria-current; sticky nav.', 'Single H1, logical H2s; placeholder note.', 'Print stylesheet hides chrome/TOC.'],
    resp: ['Reading width constrained; two-column → single under 920px.', 'TOC becomes static on mobile.', 'Comfortable line-height for long docs.'],
    sections: ['Hero + last updated', 'Content sections', 'Sticky table of contents', 'Print-ready layout'],
  },
};

export function ExperiencePreview() {
  const view = useView();
  const meta = META[view];
  return (
    <div style={{ padding: 'var(--space-5, 24px)' }}>
      <div style={{ marginBottom: 'var(--space-5, 24px)' }}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800 }}>Contact • Support • FAQ • Legal — {meta.title} Preview</h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', color: 'var(--color-text-secondary, #4b5563)' }}>Review the experience across viewports. Use the switcher in each frame.</p>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', marginTop: 'var(--space-3, 12px)', padding: 'var(--space-2, 8px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-warning-subtle, #fef3c7)', color: 'var(--color-text-warning, #92400e)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600 }}>
          Approval status: Pending review
        </span>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)', marginTop: 'var(--space-4, 16px)' }}>
          {notesBlock('Accessibility notes', meta.a11y, 'var(--color-bg-success-subtle, #ecfdf5)')}
          {notesBlock('Responsive notes', meta.resp, 'var(--color-bg-surface-muted, #f1f5f9)')}
          {notesBlock('Section checklist', meta.sections, 'var(--color-bg-accent-subtle, #e8f1ec)')}
        </div>
      </div>
      <ResponsivePreview defaultViewport="desktop">
        <PreviewContent view={view} />
      </ResponsivePreview>
    </div>
  );
}

export default ExperiencePreview;
