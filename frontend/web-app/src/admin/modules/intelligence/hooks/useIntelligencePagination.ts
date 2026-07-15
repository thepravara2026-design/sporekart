import { useState } from 'react';

export function useIntelligencePagination<T>(items: T[], pageSize = 10) {
  const [page, setPage] = useState(1);
  const totalPages = Math.max(1, Math.ceil(items.length / pageSize));
  const safePage = Math.min(page, totalPages);
  const paginatedItems = items.slice((safePage - 1) * pageSize, safePage * pageSize);
  return { page: safePage, setPage, totalPages, items: paginatedItems, hasNext: safePage < totalPages, hasPrev: safePage > 1, next: () => setPage((p) => Math.min(p + 1, totalPages)), prev: () => setPage((p) => Math.max(p - 1, 1)) };
}
