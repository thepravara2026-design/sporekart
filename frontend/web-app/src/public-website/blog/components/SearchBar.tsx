import { useState } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

export function SearchBar({
  initialValue = '',
  onSubmit,
  placeholder = 'Search guides, topics, or keywords…',
  autoFocus = false,
}: {
  initialValue?: string;
  onSubmit: (query: string) => void;
  placeholder?: string;
  autoFocus?: boolean;
}) {
  const [value, setValue] = useState(initialValue);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(value.trim());
  };

  return (
    <form className="sk-blog-search" role="search" onSubmit={handleSubmit} style={{ maxWidth: 560 }}>
      <Icon name="search" size={18} color="var(--color-text-muted, #9ca3af)" aria-label="Search" />
      <input
        type="search"
        value={value}
        autoFocus={autoFocus}
        onChange={(e) => setValue(e.target.value)}
        placeholder={placeholder}
        aria-label="Search the SporeKart knowledge hub"
      />
      <button
        type="submit"
        style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: 6,
          padding: 'var(--space-2, 8px) var(--space-4, 16px)',
          borderRadius: 'var(--radius-pill, 999px)',
          border: 'none',
          backgroundColor: 'var(--color-bg-accent-default, #2F6F4F)',
          color: '#ffffff',
          fontWeight: 700,
          fontSize: 'var(--text-body-sm, 14px)',
          cursor: 'pointer',
        }}
      >
        Search
      </button>
    </form>
  );
}
