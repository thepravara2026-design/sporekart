export { paginate } from '../../utils';
import type { ChartData } from './types';

export function filterByDateRange<T extends { timestamp: string }>(items: T[], start: string, end: string): T[] {
  return items.filter((i) => {
    if (start && new Date(i.timestamp) < new Date(start)) return false;
    if (end && new Date(i.timestamp) > new Date(end)) return false;
    return true;
  });
}

export function searchInsights<T extends { title: string; description: string }>(items: T[], query: string): T[] {
  if (!query) return items;
  const q = query.toLowerCase();
  return items.filter((i) => i.title.toLowerCase().includes(q) || i.description.toLowerCase().includes(q));
}

export function getScoreVariant(score: number): 'success' | 'warning' | 'danger' {
  if (score >= 80) return 'success';
  if (score >= 60) return 'warning';
  return 'danger';
}

export function formatNumber(n: number): string {
  return n.toLocaleString();
}

export function formatPercentage(n: number): string {
  return `${Math.round(n)}%`;
}

export function generateDistributionData(metrics: { label: string; score: number; variant: string }[]): { label: string; value: number; color: string }[] {
  return metrics.map((m) => ({ label: m.label, value: m.score, color: m.variant === 'success' ? 'var(--color-success)' : m.variant === 'warning' ? 'var(--color-warning)' : 'var(--color-danger)' }));
}

export function buildBarChartData(labels: string[], values: number[], color?: string): ChartData {
  return {
    labels,
    series: [values.map((v) => ({ name: 'Series 1', value: v, color }))],
  };
}

export function buildPieChartData(items: { label: string; value: number; color?: string }[]): ChartData {
  return {
    labels: items.map((i) => i.label),
    series: [items.map((i) => ({ name: i.label, value: i.value, color: i.color }))],
  };
}
