import { Link as RouterLink } from 'react-router-dom';
import { Icon } from '../../design-system/icons/Icon';
import { PublicContentContainer } from '../PublicContentContainer';

export interface FutureBlogSectionProps {
  posts?: { title: string; href: string; excerpt: string; tag: string }[];
}

export function FutureBlogSection({
  posts = [
    { title: 'Getting started with oyster mushrooms', href: '/blog', excerpt: 'A beginner-friendly walkthrough for your first grow.', tag: 'Guides' },
    { title: 'Sterilization best practices', href: '/blog', excerpt: 'How we keep every kit contamination-free.', tag: 'Quality' },
    { title: 'From spawn to harvest', href: '/blog', excerpt: 'Timelines and tips for a healthy flush.', tag: 'Cultivation' },
  ],
}: FutureBlogSectionProps) {
  const cardStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-2, 8px)',
    padding: 'var(--space-4, 16px)',
    borderRadius: 'var(--radius-lg, 12px)',
    border: '1px solid var(--color-border-default, #e5e7eb)',
    backgroundColor: 'var(--color-bg-surface-default, #ffffff)',
    textDecoration: 'none',
    color: 'inherit',
  };

  return (
    <section className="sk-public-blog" aria-label="From the blog" style={{ backgroundColor: 'var(--color-bg-surface-muted, #f8fafc)' }}>
      <PublicContentContainer>
        <div style={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', marginBottom: 'var(--space-4, 16px)' }}>
          <h2 style={{ margin: 0, fontSize: 'var(--text-title-md, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>From the blog</h2>
          <RouterLink to="/blog" style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1, 4px)', color: 'var(--color-text-accent, #1d4ed8)', textDecoration: 'none', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600 }}>
            All articles <Icon name="arrow-right" size={16} aria-label="All articles" />
          </RouterLink>
        </div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)' }}>
          {posts.map((post) => (
            <RouterLink key={post.title} to={post.href} style={cardStyle}>
              <span style={{ alignSelf: 'flex-start', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 600, color: 'var(--color-text-accent, #1d4ed8)', backgroundColor: 'var(--color-bg-accent-subtle, #eef2ff)', padding: '2px var(--space-2, 8px)', borderRadius: 'var(--radius-pill, 999px)' }}>
                {post.tag}
              </span>
              <h3 style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', fontWeight: 600 }}>{post.title}</h3>
              <p style={{ margin: 0, fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)' }}>{post.excerpt}</p>
            </RouterLink>
          ))}
        </div>
      </PublicContentContainer>
    </section>
  );
}
