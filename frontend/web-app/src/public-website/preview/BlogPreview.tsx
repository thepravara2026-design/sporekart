import { useLocation } from 'react-router-dom';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { ResponsivePreview } from '../../design-system/playground/components/ResponsivePreview';
import BlogPage from '../pages/BlogPage';
import ArticlePage from '../pages/ArticlePage';
import SearchPage from '../pages/SearchPage';
import { getFeaturedArticles } from '../blog/data';

type View = 'landing' | 'article' | 'search';

function useView(): View {
  const { pathname } = useLocation();
  if (pathname.includes('/article')) return 'article';
  if (pathname.includes('/search')) return 'search';
  return 'landing';
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
  const featuredSlug = getFeaturedArticles()[0]?.slug ?? 'beginner-mushroom-farming-4-week-plan';
  if (view === 'article') {
    return (
      <MemoryRouter initialEntries={[`/blog/${featuredSlug}`]}>
        <Routes>
          <Route path="/blog/:slug" element={<ArticlePage />} />
        </Routes>
      </MemoryRouter>
    );
  }
  if (view === 'search') {
    return (
      <MemoryRouter initialEntries={['/search?q=oyster']}>
        <Routes>
          <Route path="/search" element={<SearchPage />} />
        </Routes>
      </MemoryRouter>
    );
  }
  return <BlogPage />;
}

const CHECKLISTS: Record<View, { a11y: string[]; resp: string[]; sections: string[] }> = {
  landing: {
    a11y: ['Header/main/footer landmarks; single H1, logical H2/H3.', 'Search bar has role="search" and labelled input.', 'Cards are links with descriptive aria-labels.', 'Contrast via design tokens (WCAG 2.2 AA).'],
    resp: ['Fluid grids reflow; hero stacks below laptop.', 'Trending + tags two-column collapses under 920px.', 'No horizontal overflow at 375px.', 'Sticky behavior limited to article pages.'],
    sections: ['Hero + search', 'Featured article', 'Category grid', 'Latest articles', 'Trending + tags', 'Training & product promos', 'Newsletter'],
  },
  article: {
    a11y: ['Breadcrumb navigation present.', 'Sticky TOC links have aria-current.', 'Reading progress bar has role="progressbar".', 'Share buttons have aria-labels; placeholder banner is a note.'],
    resp: ['Two-column body + TOC collapses under 920px.', 'TOC becomes static on mobile.', 'Author/meta row wraps gracefully.', 'Comfortable reading width via container.'],
    sections: ['Hero banner', 'Reading progress', 'Sticky TOC', 'Article body (callouts/quotes/media)', 'Share + tags', 'Prev/Next', 'Related articles', 'Product/Training promos'],
  },
  search: {
    a11y: ['Search input labelled; form role="search".', 'Result count announced in a paragraph.', 'Empty state offers suggested links.', 'noindex applied to search pages.'],
    resp: ['Centered hero; results grid auto-fills.', 'Popular searches + tag cloud wrap.', 'Empty state fits mobile width.', 'No layout shift between states.'],
    sections: ['Search hero', 'Results grid', 'Empty state', 'Popular searches', 'Tag cloud', 'Recent/suggestions placeholders'],
  },
};

export function BlogPreview() {
  const view = useView();
  const meta = CHECKLISTS[view];

  return (
    <div style={{ padding: 'var(--space-5, 24px)' }}>
      <div style={{ marginBottom: 'var(--space-5, 24px)' }}>
        <h1 style={{ margin: 0, fontSize: 'var(--text-title-lg, 26px)', fontWeight: 800 }}>
          Blog & Knowledge Hub — {view === 'article' ? 'Article' : view === 'search' ? 'Search' : 'Landing'} Preview
        </h1>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', color: 'var(--color-text-secondary, #4b5563)' }}>
          Review the SporeKart blog experience across viewports. Use the switcher in each frame.
        </p>
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

export default BlogPreview;
