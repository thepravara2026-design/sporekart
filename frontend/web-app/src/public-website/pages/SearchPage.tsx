import { useSearchParams, Link } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { BlogStyles } from '../blog/BlogStyles';
import { Reveal } from '../home/Reveal';
import { ArticleCard } from '../blog/components/ArticleCard';
import { SearchBar } from '../blog/components/SearchBar';
import { EmptyState } from '../blog/components/EmptyState';
import { TagCloud } from '../blog/components/TagChip';
import { searchArticles, POPULAR_SEARCHES } from '../blog/data';

export default function SearchPage() {
  const [params, setParams] = useSearchParams();
  const query = params.get('q') ?? '';
  const results = query ? searchArticles(query) : [];

  const onSubmit = (q: string) => {
    if (q) {
      setParams({ q });
    } else {
      setParams({});
    }
  };

  const breadcrumbs = [
    { label: 'Home', href: '/' },
    { label: 'Blog', href: '/blog' },
    { label: 'Search' },
  ];

  return (
    <PublicLayout
      breadcrumbs={breadcrumbs}
      seo={{
        title: query ? `Search: “${query}” — SporeKart Blog` : 'Search the SporeKart Knowledge Hub',
        description: 'Search mushroom cultivation guides, research, and business playbooks on the SporeKart knowledge hub.',
        canonical: `https://sporekart.example.com/search${query ? `?q=${encodeURIComponent(query)}` : ''}`,
        type: 'website',
        noindex: true,
      }}
    >
      <BlogStyles />

      <header style={{ background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #e8f1ec), var(--color-bg-surface-default, #ffffff))', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <div style={{ padding: 'var(--space-9, 64px) 0 var(--space-7, 48px)', textAlign: 'center' }}>
            <h1 style={{ margin: 0, fontSize: 'var(--text-title-xl, 36px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>Search the Knowledge Hub</h1>
            <p style={{ margin: 'var(--space-3, 12px) auto 0', maxWidth: 560, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              Find guides, research, and business playbooks. Full-text search is placeholder-ready for the CMS.
            </p>
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'flex', justifyContent: 'center' }}>
              <SearchBar initialValue={query} onSubmit={onSubmit} autoFocus={!query} />
            </div>
          </div>
        </PublicContentContainer>
      </header>

      <Reveal>
        <div style={{ padding: 'var(--space-8, 48px) 0' }}>
          <PublicContentContainer maxWidth="xl">
            {!query && (
              <>
                <h2 style={{ margin: 0, fontSize: 'var(--text-title-md, 24px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>Popular searches</h2>
                <div style={{ marginTop: 'var(--space-4, 16px)', display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)' }}>
                  {POPULAR_SEARCHES.map((s) => (
                    <Link key={s} to={`/search?q=${encodeURIComponent(s)}`} className="sk-blog-tag">{s}</Link>
                  ))}
                </div>
                <div style={{ marginTop: 'var(--space-7, 48px)' }}>
                  <h2 style={{ margin: 0, fontSize: 'var(--text-title-md, 24px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>Browse by tag</h2>
                  <div style={{ marginTop: 'var(--space-4, 16px)' }}><TagCloud limit={12} /></div>
                </div>
                <p style={{ marginTop: 'var(--space-7, 48px)', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-muted, #9ca3af)' }}>
                  Recent searches and live suggestions are placeholder-ready for the CMS integration.
                </p>
              </>
            )}

            {query && results.length > 0 && (
              <>
                <p style={{ margin: '0 0 var(--space-5, 24px)', color: 'var(--color-text-secondary, #4b5563)' }}>
                  <span style={{ fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{results.length}</span> result{results.length === 1 ? '' : 's'} for “<strong>{query}</strong>”
                </p>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
                  {results.map((a) => (
                    <ArticleCard key={a.slug} article={a} />
                  ))}
                </div>
              </>
            )}

            {query && results.length === 0 && (
              <EmptyState
                title={`No results for “${query}”`}
                message="Try a broader term, or explore popular topics below."
                suggestions={POPULAR_SEARCHES.map((s) => ({ label: s, to: `/search?q=${encodeURIComponent(s)}` }))}
              />
            )}
          </PublicContentContainer>
        </div>
      </Reveal>
    </PublicLayout>
  );
}
