import { useEffect, useState } from 'react';
import { PublicContentContainer } from '../../PublicContentContainer';
import { Icon } from '../../../design-system/icons/Icon';

export interface LegalSection {
  id: string;
  heading: string;
  body: string;
}

export function LegalTemplate({
  title,
  intro,
  lastUpdated,
  sections,
}: {
  title: string;
  intro: string;
  lastUpdated: string;
  sections: LegalSection[];
}) {
  const [active, setActive] = useState<string>(sections[0]?.id ?? '');

  useEffect(() => {
    if (sections.length === 0) return;
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((e) => {
          if (e.isIntersecting) setActive(e.target.id);
        });
      },
      { rootMargin: '-80px 0px -70% 0px', threshold: 0 },
    );
    sections.forEach((s) => {
      const el = document.getElementById(s.id);
      if (el) observer.observe(el);
    });
    return () => observer.disconnect();
  }, [sections]);

  return (
    <PublicContentContainer maxWidth="xl">
      <div style={{ padding: 'var(--space-7, 48px) 0' }}>
        <div style={{ textAlign: 'center', marginBottom: 'var(--space-7, 48px)' }}>
          <span style={{ fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, letterSpacing: '0.08em', textTransform: 'uppercase', color: 'var(--color-text-accent, #2F6F4F)' }}>Legal</span>
          <h1 style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-title-xl, 36px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>{title}</h1>
          <p style={{ margin: 'var(--space-3, 12px) auto 0', maxWidth: 720, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>{intro}</p>
          <p style={{ margin: 'var(--space-4, 16px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-muted, #9ca3af)' }}>
            Last updated: {lastUpdated}
          </p>
        </div>

        <div className="sk-legal-layout">
          <article style={{ minWidth: 0 }}>
            {sections.map((s) => (
              <section key={s.id} id={s.id} style={{ marginBottom: 'var(--space-7, 44px)', scrollMarginTop: 'calc(var(--header-height, 72px) + var(--space-4, 16px))' }}>
                <h2 style={{ fontSize: 'var(--text-title-md, 24px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', margin: '0 0 var(--space-3, 12px)' }}>{s.heading}</h2>
                <p style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.8 }}>{s.body}</p>
              </section>
            ))}
            <p role="note" style={{ display: 'inline-flex', alignItems: 'center', gap: 6, marginTop: 'var(--space-4, 16px)', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-warning-subtle, #fef3c7)', color: 'var(--color-text-warning, #92400e)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700 }}>
              <Icon name="alert-triangle" size={14} aria-label="Placeholder" /> Placeholder content — pending legal review
            </p>
          </article>

          <aside aria-label="On this page">
            <nav className="sk-legal-toc" aria-label="Table of contents">
              <p style={{ margin: '0 0 var(--space-2, 8px)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-text-muted, #9ca3af)' }}>On this page</p>
              {sections.map((s) => (
                <a key={s.id} href={`#${s.id}`} className={active === s.id ? 'is-active' : ''} aria-current={active === s.id ? 'true' : undefined}>
                  {s.heading}
                </a>
              ))}
            </nav>
          </aside>
        </div>
      </div>
    </PublicContentContainer>
  );
}
