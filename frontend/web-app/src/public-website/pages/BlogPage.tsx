import { useNavigate } from 'react-router-dom';
import { PublicLayout } from '../PublicLayout';
import { PublicContentContainer } from '../PublicContentContainer';
import { BlogStyles } from '../blog/BlogStyles';
import { Icon } from '../../design-system/icons/Icon';
import { Reveal } from '../home/Reveal';
import { NewsletterCta } from '../home/sections/NewsletterCta';
import { SectionHeading, CtaBanner, sectionPad } from './PageShell';
import { ArticleCard } from '../blog/components/ArticleCard';
import { FeaturedArticle } from '../blog/components/FeaturedArticle';
import { CategoryGrid } from '../blog/components/CategoryCard';
import { TagCloud } from '../blog/components/TagChip';
import { SearchBar } from '../blog/components/SearchBar';
import {
  getFeaturedArticles,
  getLatestArticles,
  getTrendingArticles,
  POPULAR_SEARCHES,
} from '../blog/data';

const ORG_SCHEMA = {
  '@context': 'https://schema.org',
  '@type': 'Organization',
  name: 'SporeKart',
  url: 'https://sporekart.example.com',
  description: 'India’s trusted mushroom knowledge platform — research-driven guides, spawn, and training.',
};
const BREADCRUMB_SCHEMA = {
  '@context': 'https://schema.org',
  '@type': 'BreadcrumbList',
  itemListElement: [
    { '@type': 'ListItem', position: 1, name: 'Home', item: 'https://sporekart.example.com/' },
    { '@type': 'ListItem', position: 2, name: 'Blog', item: 'https://sporekart.example.com/blog' },
  ],
};

export default function BlogPage() {
  const navigate = useNavigate();
  const featured = getFeaturedArticles();
  const latest = getLatestArticles(6);
  const trending = getTrendingArticles(3);

  return (
    <PublicLayout
      breadcrumbs={[{ label: 'Home', href: '/' }, { label: 'Blog' }]}
      seo={{
        title: 'Blog & Knowledge Hub — SporeKart',
        description:
          'Practical mushroom cultivation guides, spawn science, business tips, and research from the SporeKart team. India’s trusted mushroom knowledge platform.',
        canonical: 'https://sporekart.example.com/blog',
        type: 'website',
        structuredData: [ORG_SCHEMA, BREADCRUMB_SCHEMA],
      }}
    >
      <BlogStyles />

      {/* Hero */}
      <section style={{ background: 'linear-gradient(135deg, var(--color-bg-accent-subtle, #e8f1ec), var(--color-bg-surface-default, #ffffff))', borderBottom: '1px solid var(--color-border-subtle, #eef2f7)' }}>
        <PublicContentContainer maxWidth="lg">
          <div style={{ padding: 'var(--space-9, 64px) 0 var(--space-7, 48px)', textAlign: 'center' }}>
            <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-surface-default, #ffffff)', color: 'var(--color-text-accent, #2F6F4F)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', border: '1px solid var(--color-border-subtle, #eef2f7)' }}>
              <Icon name="book-open" size={14} aria-label="Knowledge Hub" /> Knowledge Hub
            </span>
            <h1 style={{ margin: 'var(--space-4, 16px) 0 0', fontSize: 'var(--text-title-xl, 40px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', letterSpacing: '-0.02em' }}>
              Grow with confidence
            </h1>
            <p style={{ margin: 'var(--space-3, 12px) auto 0', maxWidth: 640, fontSize: 'var(--text-body-lg, 18px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
              Research-driven guides, spawn science, and business playbooks from the SporeKart team — written for Indian growers.
            </p>
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'flex', justifyContent: 'center' }}>
              <SearchBar onSubmit={(q) => navigate(q ? `/search?q=${encodeURIComponent(q)}` : '/search')} />
            </div>
          </div>
        </PublicContentContainer>
      </section>

      {/* Featured */}
      {featured.length > 0 && (
        <Reveal>
          <div style={sectionPad}>
            <PublicContentContainer maxWidth="xl">
              <SectionHeading eyebrow="Editor’s pick" title="Featured article" description="Our most useful guide this season." />
              <div style={{ marginTop: 'var(--space-5, 24px)' }}>
                <FeaturedArticle article={featured[0]} />
              </div>
            </PublicContentContainer>
          </div>
        </Reveal>
      )}

      {/* Categories */}
      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="xl">
            <SectionHeading eyebrow="Browse" title="Explore by topic" description="Find guidance in the area you’re growing or building." />
            <div style={{ marginTop: 'var(--space-5, 24px)' }}>
              <CategoryGrid />
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      {/* Latest */}
      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="xl">
            <SectionHeading eyebrow="Fresh" title="Latest articles" description="New guides and research from the SporeKart library." />
            <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 'var(--space-4, 16px)' }}>
              {latest.map((a) => (
                <ArticleCard key={a.slug} article={a} />
              ))}
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      {/* Trending + Popular tags */}
      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="xl">
            <div style={{ display: 'grid', gridTemplateColumns: 'minmax(0, 2fr) minmax(0, 1fr)', gap: 'var(--space-7, 48px)', alignItems: 'start' }}>
              <div>
                <SectionHeading eyebrow="Popular now" title="Trending articles" />
                <div style={{ marginTop: 'var(--space-5, 24px)', display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 'var(--space-4, 16px)' }}>
                  {trending.map((a) => (
                    <ArticleCard key={a.slug} article={a} />
                  ))}
                </div>
              </div>
              <aside aria-label="Popular tags">
                <SectionHeading eyebrow="Discover" title="Popular tags" />
                <div style={{ marginTop: 'var(--space-4, 16px)' }}>
                  <TagCloud />
                </div>
                <div style={{ marginTop: 'var(--space-6, 40px)', padding: 'var(--space-5, 24px)', borderRadius: 'var(--radius-lg, 12px)', backgroundColor: 'var(--color-bg-surface-muted, #f1f5f9)', border: '1px solid var(--color-border-subtle, #eef2f7)' }}>
                  <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', fontWeight: 700, color: 'var(--color-text-muted, #9ca3af)', textTransform: 'uppercase', letterSpacing: '0.06em' }}>Popular searches</p>
                  <div style={{ marginTop: 'var(--space-3, 12px)', display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)' }}>
                    {POPULAR_SEARCHES.map((s) => (
                      <button key={s} type="button" onClick={() => navigate(`/search?q=${encodeURIComponent(s)}`)} className="sk-blog-tag" style={{ cursor: 'pointer', border: 'none' }}>
                        {s}
                      </button>
                    ))}
                  </div>
                </div>
              </aside>
            </div>
          </PublicContentContainer>
        </div>
      </Reveal>

      {/* Training promotion */}
      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <CtaBanner
              title="Turn knowledge into yield"
              description="Join a hands-on SporeKart training cohort and grow with expert mentorship."
              primaryLabel="Explore training"
              primaryTo="/training"
            />
          </PublicContentContainer>
        </div>
      </Reveal>

      {/* Product promotion */}
      <Reveal>
        <div style={sectionPad}>
          <PublicContentContainer maxWidth="lg">
            <CtaBanner
              title="Need spawn or fresh mushrooms?"
              description="Browse certified spawn, substrates, and fresh produce from trusted growers."
              primaryLabel="Shop products"
              primaryTo="/products"
            />
          </PublicContentContainer>
        </div>
      </Reveal>

      <NewsletterCta />
    </PublicLayout>
  );
}
