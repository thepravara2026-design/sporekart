import { Icon } from '../../../design-system/icons/Icon';
import { MediaPlaceholder } from '../../home/MediaPlaceholder';
import type { ArticleBlock } from '../data';

const CALLOUT_ICON: Record<NonNullable<ArticleBlock['variant']>, string> = {
  info: 'help-circle',
  success: 'check-circle',
  warning: 'alert-triangle',
  tip: 'book-open',
};

export function ArticleBody({ blocks }: { blocks: ArticleBlock[] }) {
  return (
    <div className="sk-blog-prose">
      {blocks.map((block, i) => {
        switch (block.type) {
          case 'heading':
            return block.level === 3 ? (
              <h3 key={i} id={block.id}>{block.text}</h3>
            ) : (
              <h2 key={i} id={block.id}>{block.text}</h2>
            );
          case 'paragraph':
            return <p key={i}>{block.text}</p>;
          case 'list':
            return block.ordered ? (
              <ol key={i}>{block.items?.map((it, j) => <li key={j}>{it}</li>)}</ol>
            ) : (
              <ul key={i}>{block.items?.map((it, j) => <li key={j}>{it}</li>)}</ul>
            );
          case 'callout':
            return (
              <div key={i} className={`sk-blog-callout sk-blog-callout--${block.variant ?? 'info'}`} role="note">
                <Icon name={CALLOUT_ICON[block.variant ?? 'info']} size={20} color="var(--color-text-accent, #2F6F4F)" aria-label={block.variant ?? 'note'} />
                <span>{block.text}</span>
              </div>
            );
          case 'quote':
            return (
              <blockquote key={i} className="sk-blog-quote">
                “{block.text}”
                {block.cite && <cite>— {block.cite}</cite>}
              </blockquote>
            );
          case 'image':
            return (
              <figure key={i} style={{ margin: 'var(--space-5, 24px) 0' }}>
                <MediaPlaceholder label={block.label ?? 'Image'} icon="image" aspectRatio="16 / 9" caption={block.caption} />
              </figure>
            );
          case 'video':
            return (
              <figure key={i} style={{ margin: 'var(--space-5, 24px) 0' }}>
                <MediaPlaceholder label={block.label ?? 'Video'} icon="play-circle" aspectRatio="16 / 9" caption={block.caption ?? 'Video placeholder — embed pending CMS.'} />
              </figure>
            );
          default:
            return null;
        }
      })}
    </div>
  );
}
