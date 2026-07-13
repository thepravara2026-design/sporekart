import { Link } from 'react-router-dom';
import { Icon } from '../../../design-system/icons/Icon';
import { CATEGORIES, type Category } from '../data';

export function CategoryCard({ category }: { category: Category }) {
  return (
    <Link to={`/blog/category/${category.slug}`} className="sk-blog-cat" aria-label={`${category.name} articles`}>
      <span
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: 44,
          height: 44,
          borderRadius: 'var(--radius-md, 8px)',
          backgroundColor: 'var(--color-bg-accent-subtle, #e8f1ec)',
          color: 'var(--color-text-accent, #2F6F4F)',
          marginBottom: 'var(--space-3, 12px)',
        }}
      >
        <Icon name={category.icon} size={22} aria-label={category.name} />
      </span>
      <h3 style={{ margin: 0, fontSize: 'var(--text-body-lg, 18px)', fontWeight: 700, color: 'var(--color-text-primary, #1f2933)' }}>{category.name}</h3>
      <p style={{ margin: 'var(--space-2, 8px) 0 0', fontSize: 'var(--text-body-sm, 14px)', color: 'var(--color-text-secondary, #4b5563)', lineHeight: 1.6 }}>
        {category.description}
      </p>
      <span style={{ display: 'inline-flex', alignItems: 'center', gap: 4, marginTop: 'var(--space-3, 12px)', fontSize: 'var(--text-body-sm, 14px)', fontWeight: 600, color: 'var(--color-text-accent, #2F6F4F)' }}>
        {category.count} articles <Icon name="arrow-right" size={16} aria-label="View" />
      </span>
    </Link>
  );
}

export function CategoryGrid({ categories = CATEGORIES }: { categories?: Category[] }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))', gap: 'var(--space-4, 16px)' }}>
      {categories.map((c) => (
        <CategoryCard key={c.slug} category={c} />
      ))}
    </div>
  );
}
