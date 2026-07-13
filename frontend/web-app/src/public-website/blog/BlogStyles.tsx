// Blog & Knowledge Hub — shared scoped styles (Sprint 21 Part 5)
// Injected once per blog page. Uses design tokens only (no hardcoded colors).

export function BlogStyles() {
  return (
    <style>{`
      .sk-blog-card {
        display: flex;
        flex-direction: column;
        height: 100%;
        text-decoration: none;
        background-color: var(--color-bg-surface-default, #ffffff);
        border: 1px solid var(--color-border-default, #e5e7eb);
        border-radius: var(--radius-lg, 12px);
        overflow: hidden;
        transition: transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease;
        color: inherit;
      }
      .sk-blog-card:hover,
      .sk-blog-card:focus-visible {
        transform: translateY(-4px);
        box-shadow: var(--shadow-lg, 0 12px 28px rgba(15, 42, 28, 0.12));
        border-color: var(--color-border-accent, #2F6F4F);
        outline: none;
      }
      .sk-blog-card__media {
        overflow: hidden;
      }
      .sk-blog-card__media > figure {
        margin: 0;
        transition: transform 0.4s ease;
      }
      .sk-blog-card:hover .sk-blog-card__media > figure,
      .sk-blog-card:focus-visible .sk-blog-card__media > figure {
        transform: scale(1.04);
      }

      .sk-blog-cat {
        display: block;
        text-decoration: none;
        color: inherit;
        background-color: var(--color-bg-surface-default, #ffffff);
        border: 1px solid var(--color-border-default, #e5e7eb);
        border-radius: var(--radius-lg, 12px);
        padding: var(--space-5, 24px);
        transition: transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease;
      }
      .sk-blog-cat:hover,
      .sk-blog-cat:focus-visible {
        transform: translateY(-3px);
        box-shadow: var(--shadow-md, 0 8px 18px rgba(15, 42, 28, 0.1));
        border-color: var(--color-border-accent, #2F6F4F);
        outline: none;
      }

      .sk-blog-tag {
        display: inline-flex;
        align-items: center;
        gap: var(--space-1, 4px);
        padding: var(--space-1, 4px) var(--space-3, 12px);
        border-radius: var(--radius-pill, 999px);
        background-color: var(--color-bg-surface-muted, #f1f5f9);
        color: var(--color-text-secondary, #4b5563);
        border: 1px solid var(--color-border-subtle, #eef2f7);
        text-decoration: none;
        font-size: var(--text-body-sm, 14px);
        font-weight: 600;
        transition: background-color 0.2s ease, color 0.2s ease, border-color 0.2s ease;
      }
      .sk-blog-tag:hover,
      .sk-blog-tag:focus-visible {
        background-color: var(--color-bg-accent-subtle, #e8f1ec);
        color: var(--color-text-accent, #2F6F4F);
        border-color: var(--color-border-accent, #2F6F4F);
        outline: none;
      }

      .sk-blog-toc {
        position: sticky;
        top: calc(var(--header-height, 72px) + var(--space-4, 16px));
        border: 1px solid var(--color-border-subtle, #eef2f7);
        border-radius: var(--radius-lg, 12px);
        background-color: var(--color-bg-surface-default, #ffffff);
        padding: var(--space-4, 16px) var(--space-5, 20px);
        max-height: calc(100vh - var(--header-height, 72px) - var(--space-8, 48px));
        overflow: auto;
      }
      .sk-blog-toc a {
        display: block;
        padding: var(--space-1, 4px) 0;
        color: var(--color-text-secondary, #4b5563);
        text-decoration: none;
        font-size: var(--text-body-sm, 14px);
        border-left: 2px solid transparent;
        padding-left: var(--space-3, 12px);
        margin-left: calc(-1 * var(--space-3, 12px));
        transition: color 0.2s ease, border-color 0.2s ease;
      }
      .sk-blog-toc a:hover,
      .sk-blog-toc a:focus-visible,
      .sk-blog-toc a.is-active {
        color: var(--color-text-accent, #2F6F4F);
        border-left-color: var(--color-border-accent, #2F6F4F);
        outline: none;
      }

      .sk-blog-prose h2 {
        font-size: var(--text-title-md, 24px);
        font-weight: 800;
        color: var(--color-text-primary, #1f2933);
        margin: var(--space-7, 44px) 0 var(--space-3, 12px);
        scroll-margin-top: calc(var(--header-height, 72px) + var(--space-4, 16px));
      }
      .sk-blog-prose h3 {
        font-size: var(--text-title-sm, 20px);
        font-weight: 700;
        color: var(--color-text-primary, #1f2933);
        margin: var(--space-5, 28px) 0 var(--space-2, 8px);
        scroll-margin-top: calc(var(--header-height, 72px) + var(--space-4, 16px));
      }
      .sk-blog-prose p {
        margin: 0 0 var(--space-4, 16px);
        line-height: 1.8;
        color: var(--color-text-secondary, #4b5563);
      }
      .sk-blog-prose ul,
      .sk-blog-prose ol {
        margin: 0 0 var(--space-4, 16px);
        padding-left: var(--space-6, 28px);
        color: var(--color-text-secondary, #4b5563);
        line-height: 1.8;
      }
      .sk-blog-prose li {
        margin: var(--space-1, 4px) 0;
      }

      .sk-blog-callout {
        display: flex;
        gap: var(--space-3, 12px);
        padding: var(--space-4, 16px) var(--space-5, 20px);
        border-radius: var(--radius-md, 8px);
        border: 1px solid var(--color-border-subtle, #eef2f7);
        margin: var(--space-5, 24px) 0;
        line-height: 1.7;
        color: var(--color-text-primary, #1f2933);
      }
      .sk-blog-callout--info { background-color: var(--color-bg-accent-subtle, #e8f1ec); border-color: var(--color-border-accent, #2F6F4F); }
      .sk-blog-callout--success { background-color: var(--color-bg-success-subtle, #ecfdf5); border-color: var(--color-border-success, #10b981); }
      .sk-blog-callout--warning { background-color: var(--color-bg-warning-subtle, #fef3c7); border-color: var(--color-border-warning, #f59e0b); }
      .sk-blog-callout--tip { background-color: var(--color-bg-info-subtle, #eff6ff); border-color: var(--color-border-info, #3b82f6); }

      .sk-blog-quote {
        margin: var(--space-5, 24px) 0;
        padding: var(--space-4, 16px) var(--space-5, 20px);
        border-left: 4px solid var(--color-border-accent, #2F6F4F);
        background-color: var(--color-bg-surface-muted, #f1f5f9);
        border-radius: 0 var(--radius-md, 8px) var(--radius-md, 8px) 0;
        font-style: italic;
        color: var(--color-text-primary, #1f2933);
      }
      .sk-blog-quote cite {
        display: block;
        margin-top: var(--space-2, 8px);
        font-style: normal;
        font-size: var(--text-body-sm, 14px);
        color: var(--color-text-muted, #9ca3af);
      }

      .sk-blog-share {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        border-radius: var(--radius-md, 8px);
        border: 1px solid var(--color-border-default, #e5e7eb);
        background-color: var(--color-bg-surface-default, #ffffff);
        color: var(--color-text-secondary, #4b5563);
        text-decoration: none;
        transition: background-color 0.2s ease, color 0.2s ease, border-color 0.2s ease;
      }
      .sk-blog-share:hover,
      .sk-blog-share:focus-visible {
        background-color: var(--color-bg-accent-subtle, #e8f1ec);
        color: var(--color-text-accent, #2F6F4F);
        border-color: var(--color-border-accent, #2F6F4F);
        outline: none;
      }

      .sk-blog-search {
        display: flex;
        align-items: center;
        gap: var(--space-2, 8px);
        padding: var(--space-3, 12px) var(--space-4, 16px);
        border: 1px solid var(--color-border-default, #e5e7eb);
        border-radius: var(--radius-pill, 999px);
        background-color: var(--color-bg-surface-default, #ffffff);
        transition: border-color 0.2s ease, box-shadow 0.2s ease;
      }
      .sk-blog-search:focus-within {
        border-color: var(--color-border-accent, #2F6F4F);
        box-shadow: 0 0 0 3px var(--color-bg-accent-subtle, #e8f1ec);
      }
      .sk-blog-search input {
        border: none;
        outline: none;
        background: transparent;
        font-size: var(--text-body-md, 16px);
        color: var(--color-text-primary, #1f2933);
        flex: 1 1 auto;
        min-width: 0;
      }

      .sk-blog-layout {
        display: grid;
        grid-template-columns: minmax(0, 1fr) 260px;
        gap: var(--space-7, 48px);
        align-items: start;
      }
      @media (max-width: 920px) {
        .sk-blog-layout {
          grid-template-columns: minmax(0, 1fr);
          gap: var(--space-5, 24px);
        }
        .sk-blog-toc {
          position: static;
          max-height: none;
        }
      }

      @media (prefers-reduced-motion: reduce) {
        .sk-blog-card,
        .sk-blog-cat,
        .sk-blog-card__media > figure { transition: none !important; transform: none !important; }
      }

      @media print {
        .sk-blog-toc, .sk-blog-share, .sk-public-header, .sk-public-footer, .sk-blog-related, .sk-blog-newsletter, .sk-blog-reading-progress { display: none !important; }
        .sk-blog-prose { font-size: 12pt; }
      }
    `}</style>
  );
}
