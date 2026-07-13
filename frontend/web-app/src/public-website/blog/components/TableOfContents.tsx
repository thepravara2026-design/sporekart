import { useEffect, useState } from 'react';

export interface TocItem {
  id: string;
  text: string;
}

export function TableOfContents({ headings }: { headings: TocItem[] }) {
  const [active, setActive] = useState<string>(headings[0]?.id ?? '');

  useEffect(() => {
    if (headings.length === 0) return;
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            setActive(entry.target.id);
          }
        });
      },
      { rootMargin: '-80px 0px -70% 0px', threshold: 0 },
    );
    headings.forEach((h) => {
      const el = document.getElementById(h.id);
      if (el) observer.observe(el);
    });
    return () => observer.disconnect();
  }, [headings]);

  if (headings.length === 0) return null;

  return (
    <nav className="sk-blog-toc" aria-label="Table of contents">
      <p style={{ margin: '0 0 var(--space-2, 8px)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-text-muted, #9ca3af)' }}>
        On this page
      </p>
      {headings.map((h) => (
        <a
          key={h.id}
          href={`#${h.id}`}
          className={active === h.id ? 'is-active' : ''}
          aria-current={active === h.id ? 'true' : undefined}
        >
          {h.text}
        </a>
      ))}
    </nav>
  );
}
