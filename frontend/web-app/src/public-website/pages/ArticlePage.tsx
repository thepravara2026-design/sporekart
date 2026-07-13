import { Link, useParams } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { BlogStyles } from '../blog/BlogStyles';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { ScrollProgress } from '../home/ScrollProgress';
import { NewsletterCta } from '../home/sections/NewsletterCta';
import { SectionHeading, CtaBanner } from './PageShell';
import { ArticleCard } from '../blog/components/ArticleCard';
import { ArticleBody } from '../blog/components/ArticleBody';
import { TableOfContents, type TocItem } from '../blog/components/TableOfContents';
import { ShareButtons } from '../blog/components/ShareButtons';
import {
  getArticleBySlug,
  getCategoryBySlug,
  getLatestArticles,
  getRelatedArticles,
  articleUrl,
  formatDate,
  type Article,
} from '../blog/data';
import NotFound from '../../pages/NotFound';

const ACCENT = 'var(--color-text-accent, #2F6F4F)';

function buildArticleSchema(article: Article) {
  return {
    '@context': 'https://schema.org',
    '@type': 'Article',
    headline: article.title,
    description: article.excerpt,
    datePublished: article.publishedDate,
    dateModified: article.updatedDate ?? article.publishedDate,
    author: { '@type': 'Person', name: article.author.name, jobTitle: article.author.role },
    publisher: { '@type': 'Organization', name: 'SporeKart', url: 'https://sporekart.example.com' },
    mainEntityOfPage: { '@type': 'WebPage', '@id': articleUrl(article.slug) },
    articleSection: getCategoryBySlug(article.categorySlug)?.name,
    keywords: article.tagSlugs.join(', '),
  };
}

export default function ArticlePage() {
  const { slug } = useParams<{ slug: string }>();
  const article = slug ? getArticleBySlug(slug) : undefined;

  if (!article) {
    return <NotFound />;
  }

  const category = getCategoryBySlug(article.categorySlug);
  const latest = getLatestArticles();
  const index = latest.findIndex((a) => a.slug === article.slug);
  const prev = index >= 0 && index < latest.length - 1 ? latest[index + 1] : undefined;
  const next = index > 0 ? latest[index - 1] : undefined;
  const related = getRelatedArticles(article, 3);

  const headings: TocItem[] = article.body
    .filter((b) => b.type === 'heading' && b.level === 2 && b.id)
    .map((b) => ({ id: b.id as string, text: b.text as string }));

  const breadcrumbs = [
    { label: 'Home', href: '/' },
    { label: 'Blog', href: '/blog' },
    ...(category ? [{ label: category.name, href: `/blog/category/${category.slug}` }] : []),
    { label: article.title },
  ];

  const schema = buildArticleSchema(article);
  const breadcrumbSchema = {
    '@context': 'https://schema.org',
    '@type': 'BreadcrumbList',
    itemListElement: breadcrumbs
      .filter((b) => 'href' in b)
      .map((b, i) => ({ '@type': 'ListItem', position: i + 1, name: b.label, item: `https://sporekart.example.com${b.href}` })),
  };

  return (
    <PublicLayout
      breadcrumbs={breadcrumbs}
      seo={{
        title: article.title,
        description: article.excerpt,
        canonical: articleUrl(article.slug),
        type: 'article',
        structuredData: [schema, breadcrumbSchema],
      }}
    >
      <BlogStyles />
      <div className="sk-blog-reading-progress">
        <ScrollProgress />
      </div>

      {/* Hero banner */}
      <header style={{ background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #e8f1ec), var(--color-bg-surface-default, #ffffff))', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <div style={{ padding: 'var(--space-8, 48px) 0 var(--space-7, 48px)' }}>
            {category && (
              <Link to={`/blog/category/${category.slug}`} style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: ACCENT, fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', textDecoration: 'none', border: '1px solid var(--color-border-subtle, #eef2f7)' }}>
                <Icon name={category.icon} size={14} aria-label={category.name} /> {category.name}
              </Link>
            )}
            <h1 style={{ margin: 'var(--space-4, 16px) 0 0', fontSize: 'var(--text-title-xl, 38px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', letterSpacing: '-0.02em', lineHeight: 1.2 }}>
              {article.title}
            </h1>
            <p style={{ margin: 'var(--space-3, 12px) 0 0', maxWidth: 720, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              {article.excerpt}
            </p>
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'flex', flexWrap: 'wrap', alignItems: 'center', gap: 'var(--space-4, 16px)' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)' }}>
                <span style={{ display: 'inline-flex', alignItems: 'center', justifyContent: 'center', width: 44, height: 44, borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-accent-default, #2F6F4F)', color: '#ffffff', fontWeight: 700 }}>
                  {article.author.name.split(' ').map((n) => n[0]).slice(0, 2).join('')}
                </span>
                <div>
                  <p style={{ margin: 0, fontWeight: 700, color: 'var(--color-text-primary, #1f2933)', fontSize: 'var(--text-body-md, 16px)' }}>{article.author.name}</p>
                  <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-muted, #9ca3af)' }}>{article.author.role}</p>
                </div>
              </div>
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>
                <Icon name="calendar" size={16} aria-label="Published" /> Published {formatDate(article.publishedDate)}
              </span>
              {article.updatedDate && (
                <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>
                  <Icon name="refresh-cw" size={16} aria-label="Updated" /> Updated {formatDate(article.updatedDate)}
                </span>
              )}
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>
                <Icon name="clock" size={16} aria-label="Reading time" /> {article.readingTime} min read
              </span>
            </div>
            {article.placeholder && (
              <p role="note" style={{ marginTop: 'var(--space-4, 16px)', display: 'inline-flex', alignItems: 'center', gap: 6, padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-warning-subtle, #fef3c7)', color: 'var(--color-text-warning, #92400e)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700 }}>
                <Icon name="alert-triangle" size={14} aria-label="Placeholder" /> Placeholder content — pending CMS
              </p>
            )}
          </div>
        </PublicContentContainer>
      </header>

      {/* Body + TOC */}
      <Reveal>
        <PublicContentContainer maxWidth="xl">
          <div className="sk-blog-layout" style={{ padding: 'var(--space-8, 48px) 0' }}>
            <article>
              <ArticleBody blocks={article.body} />
              <div style={{ marginTop: 'var(--space-7, 48px)', paddingTop: 'var(--space-5, 24px)', borderTop: '1px solid var(--color-border-subtle, #eef2f7)', display: 'flex', flexWrap: 'wrap', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-4, 16px)' }}>
                <ShareButtons url={articleUrl(article.slug)} title={article.title} />
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)' }}>
                  {article.tagSlugs.map((t) => (
                    <Link key={t} to={`/blog/tag/${t}`} className="sk-blog-tag">{t}</Link>
                  ))}
                </div>
              </div>
            </article>
            <aside aria-label="Article navigation">
              <TableOfContents headings={headings} />
            </aside>
          </div>
        </PublicContentContainer>
      </Reveal>

      {/* Prev / Next */}
      <div style={{ borderTop: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <nav aria-label="Previous and next articles" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-4, 16px)', padding: 'var(--space-6, 32px) 0' }}>
            {prev ? (
              <Link to={`/blog/${prev.slug}`} style={{ textDecoration: 'none', color: 'inherit', padding: 'var(--space-4, 16px)', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)' }}>
                <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-text-muted, #9ca3af)' }}><Icon name="arrow-left" size={14} aria-label="Previous" /> Previous</span>
                <p style={{ margin: 'var(--space-2, 8px) 0 0', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{prev.title}</p>
              </Link>
            ) : <span />}
            {next ? (
              <Link to={`/blog/${next.slug}`} style={{ textDecoration: 'none', color: 'inherit', padding: 'var(--space-4, 16px)', border: '1px solid var(--color-border-default, #e5e7eb)', borderRadius: 'var(--radius-lg, 12px)', textAlign: 'right' }}>
                <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: 'var(--color-text-muted, #9ca3af)' }}>Next <Icon name="arrow-right" size={14} aria-label="Next" /></span>
                <p style={{ margin: 'var(--space-2, 8px) 0 0', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{next.title}</p>
              </Link>
            ) : <span />}
          </nav>
        </PublicContentContainer>
      </div>

      {/* Related articles */}
      <Reveal>
        <div style={{ padding: 'var(--space-8, 48px) 0', backgroundColor: 'var(--color-bg-surface-muted, #f1f5f9)' }}>
          <PublicContentContainer maxWidth="xl">
            <SectionHeading eyebrow="Keep reading" title="Related articles" description="More from the SporeKart library." />
            <div className="sk-blog-related" style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {related.map((a) => (
                <ArticleCard key={a.slug} article={a} />
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      {/* Related products + training */}
      <Reveal>
        <div style={{ padding: 'var(--space-8, 48px) 0' }}>
          <PublicContentContainer maxWidth="lg">
            <CtaBanner title="Grow what you read" description="Certified spawn, substrates, and fresh mushrooms from trusted growers." primaryLabel="Shop products" primaryTo="/products" />
            <div style={{ marginTop: 'var(--space-4, 16px)' }}>
              <CtaBanner title="Learn hands-on" description="Turn this guide into real yield with a SporeKart training cohort." primaryLabel="Explore training" primaryTo="/training" />
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      <NewsletterCta />
    </PublicLayout>
  );
}
