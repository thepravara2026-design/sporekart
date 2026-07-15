import { useState, useMemo, useCallback } from 'react';
import type { InventoryItem } from '../types';
import { searchInventoryItems } from '../utils';

export function useInventorySearch(items: InventoryItem[]) {
  const [query, setQuery] = useState('');
  const [fields, setFields] = useState<string[]>([]);

  const results = useMemo(
    () => searchInventoryItems(items, query, fields),
    [items, query, fields],
  );

  const toggleField = useCallback((field: string) => {
    setFields((prev) =>
      prev.includes(field) ? prev.filter((f) => f !== field) : [...prev, field],
    );
  }, []);

  const clear = useCallback(() => { setQuery(''); setFields([]); }, []);

  return { query, setQuery, fields, setFields, toggleField, results, clear };
}
