// Scoped styles for Contact / Support / FAQ / Legal experience (Sprint 21 Part 6)
// Token-only; no hardcoded colors/spacing.

export function ExperienceStyles() {
  return (
    <style>{`
      .sk-exp-card {
        background-color: var(--color-bg-surface-default, #ffffff);
        border: 1px solid var(--color-border-default, #e5e7eb);
        border-radius: var(--radius-lg, 12px);
        padding: var(--space-5, 24px);
        transition: transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease;
      }
      .sk-exp-card:hover,
      .sk-exp-card:focus-within {
        transform: translateY(-3px);
        box-shadow: var(--shadow-md, 0 8px 18px rgba(15, 42, 28, 0.1));
        border-color: var(--color-border-accent, #2F6F4F);
      }

      .sk-exp-icon {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 44px;
        height: 44px;
        border-radius: var(--radius-md, 8px);
        background-color: var(--color-bg-accent-subtle, #e8f1ec);
        color: var(--color-text-accent, #2F6F4F);
        flex-shrink: 0;
      }

      .sk-exp-quick {
        display: flex;
        flex-direction: column;
        height: 100%;
        text-decoration: none;
        color: inherit;
        background-color: var(--color-bg-surface-default, #ffffff);
        border: 1px solid var(--color-border-default, #e5e7eb);
        border-radius: var(--radius-lg, 12px);
        padding: var(--space-5, 24px);
        transition: transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease;
      }
      .sk-exp-quick:hover,
      .sk-exp-quick:focus-visible {
        transform: translateY(-3px);
        box-shadow: var(--shadow-md, 0 8px 18px rgba(15, 42, 28, 0.1));
        border-color: var(--color-border-accent, #2F6F4F);
        outline: none;
      }

      .sk-exp-map {
        width: 100%;
        aspect-ratio: 16 / 9;
        border-radius: var(--radius-lg, 12px);
        border: 1px dashed var(--color-border-default, #e5e7eb);
        background:
          repeating-linear-gradient(45deg, var(--color-bg-surface-muted, #f1f5f9) 0 12px, var(--color-bg-surface-default, #ffffff) 12px 24px);
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        gap: var(--space-2, 8px);
        color: var(--color-text-muted, #9ca3af);
      }

      .sk-exp-timeline {
        list-style: none;
        margin: 0;
        padding: 0 0 0 var(--space-2, 8px);
        border-left: 2px solid var(--color-border-accent, #2F6F4F);
      }
      .sk-exp-timeline li {
        position: relative;
        padding: 0 0 var(--space-5, 24px) var(--space-5, 20px);
      }
      .sk-exp-timeline li:last-child { padding-bottom: 0; }
      .sk-exp-timeline li::before {
        content: "";
        position: absolute;
        left: calc(-1 * var(--space-2, 8px) - 7px);
        top: 4px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background-color: var(--color-bg-accent-default, #2F6F4F);
        border: 2px solid var(--color-bg-surface-default, #ffffff);
      }

      .sk-exp-chip {
        display: inline-flex;
        align-items: center;
        padding: var(--space-2, 8px) var(--space-4, 16px);
        border-radius: var(--radius-pill, 999px);
        border: 1px solid var(--color-border-default, #e5e7eb);
        background-color: var(--color-bg-surface-default, #ffffff);
        color: var(--color-text-secondary, #4b5563);
        font-size: var(--text-body-sm, 14px);
        font-weight: 600;
        cursor: pointer;
        transition: background-color 0.2s ease, color 0.2s ease, border-color 0.2s ease;
      }
      .sk-exp-chip:hover,
      .sk-exp-chip:focus-visible { border-color: var(--color-border-accent, #2F6F4F); }
      .sk-exp-chip.is-active {
        background-color: var(--color-bg-accent-default, #2F6F4F);
        color: #ffffff;
        border-color: var(--color-bg-accent-default, #2F6F4F);
      }

      .sk-exp-faq-item {
        border: 1px solid var(--color-border-default, #e5e7eb);
        border-radius: var(--radius-lg, 12px);
        background-color: var(--color-bg-surface-default, #ffffff);
        overflow: hidden;
      }
      .sk-exp-faq-q {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: var(--space-3, 12px);
        padding: var(--space-4, 16px);
        background: transparent;
        border: none;
        cursor: pointer;
        text-align: left;
        font-size: var(--text-body-md, 16px);
        fontWeight: 700;
        color: var(--color-text-primary, #1f2933);
      }
      .sk-exp-faq-q:focus-visible {
        outline: 2px solid var(--color-border-accent, #2F6F4F);
        outline-offset: -2px;
      }
      .sk-exp-faq-a {
        padding: 0 var(--space-4, 16px) var(--space-4, 16px);
        font-size: var(--text-body-md, 16px);
        color: var(--color-text-secondary, #4b5563);
        line-height: 1.7;
      }
      .sk-exp-faq-helpful {
        display: flex;
        align-items: center;
        gap: var(--space-2, 8px);
        padding: 0 var(--space-4, 16px) var(--space-4, 16px);
        font-size: var(--text-body-sm, 14px);
        color: var(--color-text-muted, #9ca3af);
      }

      .sk-legal-toc {
        position: sticky;
        top: calc(var(--header-height, 72px) + var(--space-4, 16px));
        border: 1px solid var(--color-border-subtle, #eef2f7);
        border-radius: var(--radius-lg, 12px);
        background-color: var(--color-bg-surface-default, #ffffff);
        padding: var(--space-4, 16px) var(--space-5, 20px);
        max-height: calc(100vh - var(--header-height, 72px) - var(--space-8, 48px));
        overflow: auto;
      }
      .sk-legal-toc a {
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
      .sk-legal-toc a:hover,
      .sk-legal-toc a:focus-visible,
      .sk-legal-toc a.is-active {
        color: var(--color-text-accent, #2F6F4F);
        border-left-color: var(--color-border-accent, #2F6F4F);
        outline: none;
      }
      .sk-legal-layout {
        display: grid;
        grid-template-columns: minmax(0, 1fr) 240px;
        gap: var(--space-7, 48px);
        align-items: start;
      }
      @media (max-width: 920px) {
        .sk-legal-layout { grid-template-columns: minmax(0, 1fr); gap: var(--space-5, 24px); }
        .sk-legal-toc { position: static; max-height: none; }
      }
      @media (prefers-reduced-motion: reduce) {
        .sk-exp-card, .sk-exp-quick { transition: none !important; transform: none !important; }
      }
      @media print {
        .sk-exp-timeline li::before { print-color-adjust: exact; }
        .sk-legal-toc, .sk-public-header, .sk-public-footer, .sk-exp-faq-helpful { display: none !important; }
        .sk-legal-layout { display: block; }
      }
    `}</style>
  );
}
