import { useSearch } from '../../../hooks';
import type { TransactionRecord } from '../types';

export function useMovementSearch(transactions: TransactionRecord[]) {
  const { query, setQuery, results } = useSearch(transactions, {
    fields: ['id', 'referenceNumber', 'product', 'sku', 'batchCode', 'warehouse'],
  });
  return { query, setQuery, filtered: results };
}
