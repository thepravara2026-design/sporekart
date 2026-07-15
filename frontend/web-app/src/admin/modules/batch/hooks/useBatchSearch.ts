import { useSearch } from '../../../hooks';
import type { BatchRecord } from '../types';

export function useBatchSearch(batches: BatchRecord[]) {
  const { query, setQuery, results } = useSearch(batches, {
    fields: ['id', 'batchCode', 'product', 'sku', 'variant', 'warehouse'],
  });
  return { query, setQuery, filtered: results };
}
