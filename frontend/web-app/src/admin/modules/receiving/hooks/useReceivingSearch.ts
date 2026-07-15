import { useSearch } from '../../../hooks';
import type { GoodsReceiptRecord } from '../types';

export function useReceivingSearch(receipts: GoodsReceiptRecord[]) {
  const { query, setQuery, results } = useSearch(receipts, {
    fields: ['receiptNumber', 'referenceNumber', 'supplier', 'product', 'warehouse', 'batchCode'],
  });
  return { query, setQuery, filtered: results };
}
