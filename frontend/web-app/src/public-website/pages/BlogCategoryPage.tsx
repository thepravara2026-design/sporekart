import { useParams } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { BlogStyles } from '../blog/BlogStyles';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { ArticleCard } from '../blog/components/ArticleCard';
import { EmptyState } from '../blog/components/EmptyState';
import { CategoryGrid } from '../blog/components/CategoryCard';
import { getArticlesByCategory, getCategoryBySlug, CATEGORIES } from '../blog/data';
import NotFound from '../../pages/NotFound';

export default function BlogCategoryPage() {
  const { slug } = useParams<{ slug: string }>();
  const category = slug ? getCategoryBySlug(slug) : undefined;

  if (!category) {
    return <NotFound />;
  }

  const articles = getArticlesByCategory(category.slug);
  const breadcrumbs = [
    { label: 'Home', href: '/' },
    { label: 'Blog', href: '/blog' },
    { label: category.name },
  ];

  return (
    <PublicLayout
      breadcrumbs={breadcrumbs}
      seo={{
        title: `${category.name} articles — SporeKart Blog`,
        description: `${category.description} Browse ${articles.length} SporeKart guides in ${category.name}.`,
        canonical: `https://sporekart.example.com/blog/category/${category.slug}`,
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
            <span style={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'center', width: 52, height: 52, borderRadius: 'var(--radius-md, 8px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-accent, #2F6F4F)', border: '1px solid var(--color-border-subtle, #eef2f7)', marginBottom: 'var(--space-4, 16px)' }}>
              <Icon name={category.icon} size={26} aria-label={category.name} />
            </span>
            <h1 style={{ margin: 0, fontSize: 'var(--text-title-xl, 36px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>{category.name}</h1>
            <p style={{ margin: 'var(--space-3, 12px) 0 0', maxWidth: 640, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              {category.description}
            </p>
            <p style={{ margin: 'var(--space-3, 12px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-muted, #9ca3af)' }}>
              {articles.length} article{articles.length === 1 ? '' : 's'} · placeholder counts
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
              <EmptyState title="No articles yet" message={`We’re preparing ${category.name} content. Check back soon or explore all topics.`} suggestions={CATEGORIES.slice(0, 6).map((c) => ({ label: c.name, to: `/blog/category/${c.slug}` }))} />
            )}
          </PublicContentContainer>
        </div>
      </Reveal>

      <Reveal>
        <div style={{ padding: 'var(--space-8, 48px) 0', backgroundColor: 'var(--color-bg-surface-muted, #f1f5f9)' }}>
          <PublicContentContainer maxWidth="xl">
            <h2 style={{ margin: 0, fontSize: 'var(--text-title-md, 24px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)' }}>Browse all topics</h2>
            <div style={{ marginTop: 'var(--space-5, 24px)' }}>
              <CategoryGrid />
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>
    </PublicLayout>
  );
}
