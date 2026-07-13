import { useState } from 'react';
import { ResponsivePreview } from '../../design-system/playground/components/ResponsivePreview';
import HomePage from '../home/HomePage';

export interface HomepagePreviewProps {
  defaultViewport?: 'desktop' | 'laptop' | 'tablet' | 'mobile';
}

const SECTION_CHECKLIST = [
  'Hero section',
  'Brand story band',
  'Trust & credibility strip',
  'Recognition / certifications',
  'Featured product categories',
  'Training highlight',
  'Why choose SporeKart',
  'Farmer success stories',
  'Cultivation journey',
  'Latest resources preview',
  'FAQ preview',
  'Newsletter / community CTA',
];

const CONTENT_CHECKLIST = [
  'Hero headline + supporting copy',
  'Story band (who we are / why mushrooms / why SporeKart)',
  'Trust metrics (marked placeholders)',
  'Partner & certification logos (placeholders)',
  'Category descriptions',
  'Training benefits + CTA',
  'Why-choose pillars',
  'Testimonial quotes',
  'Journey step copy',
  'Resource card copy',
  'FAQ questions & answers',
  'Newsletter benefits',
];

function notesBlock(title: string, items: string[], variant: 'default' | 'a11y' | 'motion' = 'default') {
  const accent =
    variant === 'a11y'
      ? 'var(--color-bg-success-subtle, #ecfdf5)'
      : variant === 'motion'
      ? 'var(--color-bg-accent-subtle, #eef2ff)'
      : 'var(--color-bg-surface-muted, #f1f5f9)';
  return (
    <div style={{ marginBottom: 'var(--space-4, 16px)', backgroundColor: accent, borderRadius: 'var(--radius-md, 8px)', padding: 'var(--space-3, 12px) var(--space-4, 16px)' }}>
      <h2 style={{ fontSize: 'var(--text-body-md, 16px)', fontWeight: 700, margin: '0 0 var(--space-2, 8px)' }}>{title}</h2>
      <ul style={{ margin: 0, paddingLeft: 'var(--space-5, 24px)', color: 'var(--color-text-secondary, #4b5563)', fontSize: 'var(--text-body-sm, 14px)', lineHeight: 1.6 }}>
        {items.map((item) => (
          <li key={item}>{item}</li>
        ))}
      </ul>
    </div>
  );
}

export function HomepagePreview({ defaultViewport = 'desktop' }: HomepagePreviewProps) {
  const [contentMode, setContentMode] = useState(false);
  const [animationMode, setAnimationMode] = useState(false);
  const [compareMode, setCompareMode] = useState(false);
  const [replayKey, setReplayKey] = useState(0);

  const toggleStyle: React.CSSProperties = {
    display: 'inline-flex',
    alignItems: 'center',
    gap: 'var(--space-2, 8px)',
    padding: 'var(--space-2, 8px) var(--space-3, 12px)',
    borderRadius: 'var(--radius-md, 8px)',
    border: '1px solid var(--color-border-default, #e5e7eb)',
    backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
    fontSize: 'var(--text-body-sm, 14px)',
    cursor: 'pointer',
  };

  return (
    <div style={{ padding: 'var(--space-5, 24px)' }}>
      <div style={{ marginBottom: 'var(--space-5, 24px)' }}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800 }}>Homepage Preview</h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', color: 'var(--color-text-secondary, #4b5563)' }}>
          Review the SporeKart homepage. Use the viewport switcher inside each frame; toggle review modes below.
        </p>

        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-3, 12px)', marginTop: 'var(--space-4, 16px)', alignItems: 'center' }}>
          <label style={toggleStyle}>
            <input type="checkbox" checked={contentMode} onChange={(e) => setContentMode(e.target.checked)} /> Content review
          </label>
          <label style={toggleStyle}>
            <input type="checkbox" checked={animationMode} onChange={(e) => setAnimationMode(e.target.checked)} /> Animation preview
          </label>
          <label style={toggleStyle}>
            <input type="checkbox" checked={compareMode} onChange={(e) => setCompareMode(e.target.checked)} /> Side-by-side compare
          </label>
          <button type="button" style={toggleStyle} onClick={() => setReplayKey((k) => k + 1)}>
            Replay animations
          </button>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-2, 8px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-warning-subtle, #fef3c7)', color: 'var(--color-text-warning, #92400e)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600 }}>
            Approval status: Pending review
          </span>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)', marginTop: 'var(--space-4, 16px)' }}>
          {notesBlock('Accessibility notes', [
            'Landmarks: header, main, footer; one H1, logical H2/H3.',
            'Keyboard-navigable CTAs, FAQ accordion (aria-expanded/controls), carousel (aria-live).',
            'Labeled newsletter input with role="status" confirmation.',
            'Animations disabled under prefers-reduced-motion.',
            'Contrast meets WCAG 2.2 AA via design tokens.',
          ], 'a11y')}
          {notesBlock('Responsive notes', [
            'Fluid typography via clamp(); auto-fit grids reflow.',
            'Two-column sections stack below laptop width.',
            'No horizontal overflow at 375px.',
            'Header collapses to mobile drawer (Part 1).',
          ])}
          {animationMode &&
            notesBlock('Animation notes', [
              'Section reveal on scroll (IntersectionObserver).',
              'Animated counters trigger when in view.',
              'Top scroll-progress bar.',
              'All motion respects prefers-reduced-motion.',
              'Use “Replay animations” to re-trigger entrance effects.',
            ], 'motion')}
          {contentMode &&
            notesBlock('Content review', [
              'Production-ready copy across all 12 sections (see list).',
              'Metrics, partner & certification logos are clearly marked placeholders.',
              'No fake statistics — real data pending approval.',
              'Tone: professional, trustworthy, farmer-friendly, tech-enabled.',
            ])}
        </div>

        {(contentMode || animationMode) && (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: 'var(--space-4, 16px)', marginTop: 'var(--space-2, 8px)' }}>
            {contentMode && notesBlock('Content sections', CONTENT_CHECKLIST)}
            {animationMode && notesBlock('Motion inventory', ['Reveal (section entrance)', 'AnimatedCounter (trust metrics)', 'ScrollProgress (top bar)', 'Hover micro-interactions on cards'])}
          </div>
        )}

        {notesBlock('Section checklist', SECTION_CHECKLIST)}
      </div>

      {compareMode ? (
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-4, 16px)' }}>
          <div>
            <p style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-secondary, #4b5563)', margin: '0 0 var(--space-2, 8px)' }}>Desktop (1280)</p>
            <ResponsivePreview defaultViewport="desktop">
              <HomePage key={`d-${replayKey}`} />
            </ResponsivePreview>
          </div>
          <div>
            <p style={{ fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-secondary, #4b5563)', margin: '0 0 var(--space-2, 8px)' }}>Mobile (375)</p>
            <ResponsivePreview defaultViewport="mobile">
              <HomePage key={`m-${replayKey}`} />
            </ResponsivePreview>
          </div>
        </div>
      ) : (
        <ResponsivePreview defaultViewport={defaultViewport}>
          <HomePage key={replayKey} />
        </ResponsivePreview>
      )}
    </div>
  );
}

export default HomepagePreview;
