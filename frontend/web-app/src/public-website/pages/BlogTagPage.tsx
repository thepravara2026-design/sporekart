import { useParams } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { BlogStyles } from '../blog/BlogStyles';
import { Reveal } from '../home/Reveal';
import { ArticleCard } from '../blog/components/ArticleCard';
import { EmptyState } from '../blog/components/EmptyState';
import { TagCloud } from '../blog/components/TagChip';
import { getArticlesByTag, getTagBySlug, TAGS } from '../blog/data';
import NotFound from '../../pages/NotFound';

export default function BlogTagPage() {
  const { slug } = useParams<{ slug: string }>();
  const tag = slug ? getTagBySlug(slug) : undefined;

  if (!tag) {
    return <NotFound />;
  }

  const articles = getArticlesByTag(tag.slug);
  const breadcrumbs = [
    { label: 'Home', href: '/' },
    { label: 'Blog', href: '/blog' },
    { label: `#${tag.name}` },
  ];

  return (
    <PublicLayout
      breadcrumbs={breadcrumbs}
      seo={{
        title: `#${tag.name} articles — SporeKart Blog`,
        description: `Articles tagged “${tag.name}” on the SporeKart knowledge hub.`,
        canonical: `https://sporekart.example.com/blog/tag/${tag.slug}`,
        type: 'website',
        structuredData: {
          '@context': 'https://schema.org',
          '@type': 'BreadcrumbList',
          itemListElement: breadcrumbs
            .filter((b) => 'href' in b)
            .map((b, i) => ({ '@type': 'ListItem', position: i + 1, name: b.label, item: `https://sporekart.example.com${b.href}` })),
        },
      }}
    >
      <BlogStyles />

      <header style={{ background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #e8f1ec), var(--color-bg-surface-default, #ffffff))', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <div style={{ padding: 'var(--space-9, 64px) 0 var(--space-7, 48px)' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-accent, #2F6F4F)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', border: '1px solid var(--color-border-subtle, #eef2f7)' }}>
              <span aria-hidden="true">#</span> {tag.name}
            </span>
            <h1 style={{ margin: 'var(--space-4, 16px) 0 0', fontSize: 'var(--text-title-xl, 36px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>
              Tagged “{tag.name}”
            </h1>
            <p style={{ margin: 'var(--space-3, 12px) 0 0', maxWidth: 640, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              {articles.length} article{articles.length === 1 ? '' : 's'} about {tag.name}.
            </p>
          </div>
        </PublicContentContainer>
      </header>

      <Reveal>
        <div style={{ padding: 'var(--space-8, 48px) 0' }}>
          <PublicContentContainer maxWidth="xl">
            {articles.length > 0 ? (
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
                {articles.map((a) => (
                  <ArticleCard key={a.slug} article={a} />
                ))}
              </div>
            ) : (
              <EmptyState title="No articles with this tag yet" message={`We’re tagging more content with “${tag.name}”. Explore other topics meanwhile.`} suggestions={TAGS.slice(0, 8).map((t) => ({ label: `#${t.name}`, to: `/blog/tag/${t.slug}` }))} />
            )}
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={{ padding: 'var(--space-8, 48px) 0', backgroundColor: 'var(--color-bg-surface-muted, #f1f5f9)' }}>
          <PublicContentContainer maxWidth="xl">
            <h2 style={{ margin: 0, fontSize: 'var(--text-title-md, 24px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>Explore more tags</h2>
            <div style={{ marginTop: 'var(--space-5, 24px)' }}>
              <TagCloud />
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>
    </PublicLayout>
  );
}
