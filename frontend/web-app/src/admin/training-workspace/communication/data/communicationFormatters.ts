// ---------------------------------------------------------------------------
// Enterprise Communication Platform — Pure formatting & mapping utilities
// Sprint 26 · Part 10. No side effects, no backend. Presentation helpers only.
// ---------------------------------------------------------------------------

import type { ToneIntent } from './communicationOptions';
import type { AudienceTarget } from './communicationTypes';
import { AUDIENCE_LABELS } from './communicationTypes';

// ---- Token resolution for tone intents -------------------------------------
export interface ToneTokens {
  fg: string;
  bg: string;
  border: string;
}

export function toneTokens(intent: ToneIntent): ToneTokens {
  switch (intent) {
    case 'success':
      return { fg: 'var(--color-success-700)', bg: 'var(--color-success-50)', border: 'var(--color-success-600)' };
    case 'warning':
      return { fg: 'var(--color-warning-700)', bg: 'var(--color-warning-50)', border: 'var(--color-warning-600)' };
    case 'danger':
      return { fg: 'var(--color-danger-700)', bg: 'var(--color-danger-50)', border: 'var(--color-danger-600)' };
    case 'info':
      return { fg: 'var(--color-primary)', bg: 'var(--color-bg-primary-weak)', border: 'var(--color-primary)' };
    case 'neutral':
    default:
      return { fg: 'var(--color-text-secondary)', bg: 'var(--color-bg-surface-muted)', border: 'var(--color-border-default)' };
  }
}

// ---- Date / time -----------------------------------------------------------
const DATE_FMT = new Intl.DateTimeFormat('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
const DATETIME_FMT = new Intl.DateTimeFormat('en-IN', {
  day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit',
});

export function formatDate(iso?: string): string {
  if (!iso) return '—';
  const d = new Date(iso);
  return Number.isNaN(d.getTime()) ? '—' : DATE_FMT.format(d);
}

export function formatDateTime(iso?: string): string {
  if (!iso) return '—';
  const d = new Date(iso);
  return Number.isNaN(d.getTime()) ? '—' : DATETIME_FMT.format(d);
}

// Deterministic relative time against fixed mock "now".
const MOCK_NOW = new Date('2026-07-16T09:00:00.000Z').getTime();

export function formatRelative(iso?: string): string {
  if (!iso) return '—';
  const t = new Date(iso).getTime();
  if (Number.isNaN(t)) return '—';
  const diffMs = t - MOCK_NOW;
  const abs = Math.abs(diffMs);
  const mins = Math.round(abs / 60000);
  const hours = Math.round(abs / 3600000);
  const days = Math.round(abs / 86400000);
  const suffix = diffMs >= 0 ? 'from now' : 'ago';
  if (mins < 60) return `${mins} min ${suffix}`;
  if (hours < 24) return `${hours} hr ${suffix}`;
  if (days < 30) return `${days} day${days === 1 ? '' : 's'} ${suffix}`;
  return formatDate(iso);
}

// ---- Numbers ---------------------------------------------------------------
export function formatCount(n: number): string {
  if (n >= 1_000_000) return `${(n / 1_000_000).toFixed(1)}M`;
  if (n >= 1_000) return `${(n / 1_000).toFixed(1)}K`;
  return String(n);
}

// ---- Audience --------------------------------------------------------------
export function formatAudience(targets: AudienceTarget[]): string {
  if (!targets.length) return '—';
  return targets
    .map((t) => t.refLabel ?? AUDIENCE_LABELS[t.scope])
    .join(', ');
}

// ---- Rich text (mock) ------------------------------------------------------
// Strips tags to a plain-text preview. NO HTML is rendered from mock content
// to avoid any injection surface; components render sanitized plain text.
export function stripHtml(html: string): string {
  return html.replace(/<[^>]*>/g, ' ').replace(/\s+/g, ' ').trim();
}

export function excerpt(text: string, max = 140): string {
  const clean = stripHtml(text);
  return clean.length <= max ? clean : `${clean.slice(0, max).trimEnd()}…`;
}
