export function paginate<T>(items: T[], page: number, pageSize: number): { items: T[]; totalPages: number } {
  const totalPages = Math.max(1, Math.ceil(items.length / pageSize));
  const safePage = Math.min(page, totalPages);
  return { items: items.slice((safePage - 1) * pageSize, safePage * pageSize), totalPages };
}
