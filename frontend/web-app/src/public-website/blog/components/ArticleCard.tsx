import { Link } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import { MediaPlaceholder } from '../../home/MediaPlaceholder';
import { getCategoryBySlug, type Article } from '../data';

const ACCENT = 'var(--color-text-accent, #2F6F4F)';
const MUTED = 'var(--color-text-muted, #9ca3af)';

export function ArticleMeta({ article }: { article: Article }) {
  const category = getCategoryBySlug(article.categorySlug);
  return (
    <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em', color: ACCENT }}>
      {category?.name ?? article.categorySlug}
      <span style={{ color: MUTED, fontWeight: 500 }}>·</span>
      <span style={{ color: 'var(--color-text-secondary, #4b5563)', fontWeight: 600 }}>{article.readingTime} min read</span>
    </span>
  );
}

export function ArticleCard({ article }: { article: Article }) {
  return (
    <Link to={`/blog/${article.slug}`} className="sk-blog-card" aria-label={article.title}>
      <div className="sk-blog-card__media">
        <MediaPlaceholder label={article.imageLabel} icon="image" aspectRatio="16 / 9" rounded={false} />
      </div>
      <div style={{ padding: 'var(--space-4, 16px)', display: 'flex', flexDirection: 'column', flex: '1 1 auto' }}>
        <ArticleMeta article={article} />
        <h3 style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)', lineHeight: 1.35 }}>
          {article.title}
        </h3>
        <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6, flex: '1 1 auto' }}>
          {article.excerpt}
        </p>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, marginTop: 'var(--space-3, 12px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: ACCENT }}>
          Read article <Icon name="arrow-right" size={16} aria-label="Read" />
        </span>
      </div>
    </Link>
  );
}
