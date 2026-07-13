import { Link } from 'react-router-dom';
import { TAGS, type Tag } from '../data';

export function TagChip({ tag }: { tag: Tag }) {
  return (
    <Link to={`/blog/tag/${tag.slug}`} className="sk-blog-tag">
      <span aria-hidden="true">#</span>
      {tag.name}
    </Link>
  );
}

export function TagCloud({ tags = TAGS, limit }: { tags?: Tag[]; limit?: number }) {
  const list = limit ? tags.slice(0, limit) : tags;
  return (
    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2, 8px)' }}>
      {list.map((t) => (
        <TagChip key={t.slug} tag={t} />
      ))}
    </div>
  );
}
