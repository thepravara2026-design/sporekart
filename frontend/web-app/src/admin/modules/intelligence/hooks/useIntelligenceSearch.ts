import { useSearch } from '../../../hooks';
import type { InsightData } from '../types';

export function useIntelligenceSearch(insights: InsightData[]) {
  const { query, setQuery, results } = useSearch(insights, {
    fields: ['title', 'description'],
  });
  return { query, setQuery, filtered: results };
}
