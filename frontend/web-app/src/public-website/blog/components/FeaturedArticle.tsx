import { Link } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import { MediaPlaceholder } from '../../home/MediaPlaceholder';
import { ArticleMeta } from './ArticleCard';
import { type Article } from '../data';

export function FeaturedArticle({ article }: { article: Article }) {
  return (
    <Link to={`/blog/${article.slug}`} className="sk-blog-card" style={{ flexDirection: 'row', minHeight: 360 }} aria-label={article.title}>
      <div className="sk-blog-card__media" style={{ flex: '1 1 52%', minHeight: 320 }}>
        <MediaPlaceholder label={article.imageLabel} icon="image" aspectRatio="auto" rounded={false} />
      </div>
      <div
        style={{
          flex: '1 1 48%',
          padding: 'var(--space-7, 40px)',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          gap: 'var(--space-3, 12px)',
        }}
      >
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-2, 8px)', alignSelf: 'flex-start', padding: 'var(--space-1, 4px) var(--space-3, 12px)', borderRadius: 'var(--radius-pill, 999px)', backgroundColor: 'var(--color-bg-accent-subtle, #e8f1ec)', color: 'var(--color-text-accent, #2F6F4F)', fontSize: 'var(--text-body-xs, 12px)', fontWeight: 700, textTransform: 'uppercase', letterSpacing: '0.06em' }}>
          <Icon name="book-open" size={14} aria-label="Featured" /> Featured
        </span>
        <ArticleMeta article={article} />
        <h2 style={{ margin: 0, fontSize: 'var(--text-title-lg, 28px)', fontWeight: 800, color: 'var(--color-text-primary, #1f2933)', lineHeight: 1.25 }}>
          {article.title}
        </h2>
        <p style={{ margin: 0, fontSize: 'var(--text-body-md, 16px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.7 }}>
          {article.excerpt}
        </p>
        <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-3, 12px)', marginTop: 'var(--space-2, 8px)', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-muted, #9ca3af)' }}>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="user-check" size={16} aria-label="Author" /> {article.author.name}</span>
          <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6 }}><Icon name="calendar" size={16} aria-label="Published" /> {article.publishedDate}</span>
        </div>
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, marginTop: 'var(--space-3, 12px)', fontSize: 'var(--text-body-md, 16px)', fontWeight: 700, color: 'var(--color-text-accent, #2F6F4F)' }}>
          Read the full guide <Icon name="arrow-right" size={18} aria-label="Read" />
        </span>
      </div>
    </Link>
  );
}
