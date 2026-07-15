import { useState, useMemo } from 'react';
import type { Warehouse } from '../types';
import { searchWarehouses } from '../utils';

export function useWarehouseSearch(items: Warehouse[], fields: string[]) {
  const [query, setQuery] = useState('');

  const results = useMemo(() => searchWarehouses(items, query, fields), [items, query, fields]);

  return { query, setQuery, results };
}


