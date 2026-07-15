import { useState, useMemo } from 'react';

export function useSearch<T>(
  items: T[],
  options?: { fields?: (keyof T)[] },
) {
  const [query, setQuery] = useState('');

  const results = useMemo(() => {
    if (!query) return items;
    const q = query.toLowerCase();
    const searchFields = options?.fields;
    return items.filter((item) => {
      if (searchFields && searchFields.length > 0) {
        return searchFields.some((field) => {
          const val = item[field];
          return String(val ?? '').toLowerCase().includes(q);
        });
      }
      return Object.values(item as Record<string, unknown>).some((val) =>
        String(val ?? '').toLowerCase().includes(q),
      );
    });
  }, [items, query, options?.fields]);

  return { query, setQuery, results };
}
