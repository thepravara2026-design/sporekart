import { PublicContentContainer } from '../PublicContentContainer';

export function PageHeader({
  eyebrow,
  title,
  intro,
}: {
  eyebrow?: string;
  title: string;
  intro?: string;
}) {
  return (
    <header style={{ padding: 'var(--space-9, 64px) 0 var(--space-6, 40px)', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
      <PublicContentContainer maxWidth="lg">
        {eyebrow && (
          <p style={{ margin: 0, fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, letterSpacing: '0.08em', textTransform: 'uppercase', color: 'var(--color-text-accent, #1d4ed8)' }}>
            {eyebrow}
          </p>
        )}
        <h1 style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-xl, 36px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', letterSpacing: '-0.01em' }}>
          {title}
        </h1>
        {intro && (
          <p style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', maxWidth: 720, lineHeight: 1.6 }}>
            {intro}
          </p>
        )}
      </PublicContentContainer>
    </header>
  );
}

export function SectionHeading({
  eyebrow,
  title,
  description,
  align = 'left',
}: {
  eyebrow?: string;
  title: string;
  description?: string;
  align?: 'left' | 'center';
}) {
  return (
    <div style={{ textAlign: align, maxWidth: align === 'center' ? 720 : undefined, margin: align === 'center' ? '0 auto' : undefined }}>
      {eyebrow && (
        <p style={{ margin: 0, fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, letterSpacing: '0.08em', textTransform: 'uppercase', color: 'var(--color-text-accent, #1d4ed8)' }}>
          {eyebrow}
        </p>
      )}
      <h2 style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-lg, 28px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
        {title}
      </h2>
      {description && (
        <p style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
          {description}
        </p>
      )}
    </div>
  );
}

export function Prose({ children }: { children: React.ReactNode }) {
  return (
    <PublicContentContainer maxWidth="lg">
      <div style={{ padding: 'var(--space-6, 40px) 0', fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.75 }}>
        {children}
      </div>
    </PublicContentContainer>
  );
}

export const sectionPad: React.CSSProperties = { padding: 'var(--space-8, 48px) 0' };

export function CtaBanner({
  title,
  description,
  primaryLabel,
  primaryTo,
}: {
  title: string;
  description?: string;
  primaryLabel: string;
  primaryTo: string;
}) {
  return (
    <PublicContentContainer maxWidth="lg">
      <div
        style={{
          margin: 'var(--space-8, 48px) 0',
          padding: 'var(--space-7, 48px)',
          borderRadius: 'var(--radius-lg, 12px)',
          background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #eef2ff), var(--color-bg-surface-default, #ffffff))',
          border: '1px solid var(--color-border-subtle, #e5e7eb)',
          textAlign: 'center',
        }}
      >
        <h2 style={{ margin: 0, fontSize: 'var(--text-title-lg, 28px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>{title}</h2>
        {description && (
          <p style={{ margin: 'var(--space-3, 12px) auto 0', maxWidth: 560, fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)' }}>
            {description}
          </p>
        )}
        <div style={{ marginTop: 'var(--space-5, 24px)' }}>
          <a
            href={primaryTo}
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: 'var(--space-2, 8px)',
              padding: 'var(--space-3, 12px) var(--space-5, 20px)',
              borderRadius: 'var(--radius-md, 8px)',
              backgroundColor: 'var(--color-bg-accent, #1d4ed8)',
              color: '#ffffff',
              fontWeight: 700,
              textDecoration: 'none',
            }}
          >
            {primaryLabel}
          </a>
        </div>
      </div>
    </PublicContentContainer>
  );
}
