import { useState, useCallback, useRef, useEffect } from 'react';

export function useInventoryItemSearch(onSearch: (query: string) => void) {
  const [query, setQuery] = useState('');
  const timerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const handleChange = useCallback((value: string) => {
    setQuery(value);
    if (timerRef.current) clearTimeout(timerRef.current);
    timerRef.current = setTimeout(() => onSearch(value), 300);
  }, [onSearch]);

  useEffect(() => {
    return () => {
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, []);

  return { query, setQuery: handleChange, clear: () => { setQuery(''); onSearch(''); } };
}
