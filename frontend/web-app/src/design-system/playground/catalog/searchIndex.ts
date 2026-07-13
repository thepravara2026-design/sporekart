import { componentManifest } from './componentManifest';
import { tokenManifest } from './tokenManifest';

export interface SearchResult {
  id: string;
  type: 'component' | 'token' | 'doc';
  title: string;
  description: string;
  category: string;
  url: string;
  keywords: string[];
}

interface DocEntry {
  id: string;
  title: string;
  description: string;
  category: string;
  url: string;
  keywords: string[];
}

const docEntries: DocEntry[] = [
  { id: 'playground', title: 'Playground', description: 'Design Playground architecture, purpose, review pipeline, and directory structure', category: 'architecture', url: '/design-system/docs/playground', keywords: ['playground', 'preview', 'review', 'pipeline', 'architecture'] },
  { id: 'component-catalog', title: 'Component Catalog', description: 'How the component catalog works, manifest structure, auto-discovery, and metadata fields', category: 'catalog', url: '/design-system/docs/component-catalog', keywords: ['catalog', 'manifest', 'discovery', 'metadata', 'components'] },
  { id: 'component-review-process', title: 'Component Review Process', description: '8-stage review pipeline for component approval', category: 'process', url: '/design-system/docs/component-review-process', keywords: ['review', 'pipeline', 'approval', 'gate', 'stages', 'freeze'] },
  { id: 'developer-guide', title: 'Developer Guide', description: 'How to use the design system with import paths, provider setup, theme switching, and best practices', category: 'guide', url: '/design-system/docs/developer-guide', keywords: ['developer', 'guide', 'import', 'provider', 'theme', 'responsive', 'patterns'] },
  { id: 'contribution-guide', title: 'Contribution Guide', description: 'How to contribute new components with naming conventions, file structure, and PR checklist', category: 'guide', url: '/design-system/docs/contribution-guide', keywords: ['contribute', 'contributing', 'new component', 'naming', 'pr checklist', 'testing'] },
  { id: 'versioning', title: 'Versioning Strategy', description: 'Semantic versioning for tokens, components, and the full design system', category: 'process', url: '/design-system/docs/versioning', keywords: ['versioning', 'semver', 'semantic version', 'changelog', 'deprecation', 'release'] },
  { id: 'search', title: 'Search Implementation', description: 'Client-side search index structure, query syntax, and extension guide', category: 'architecture', url: '/design-system/docs/search', keywords: ['search', 'index', 'query', 'fuzzy', 'scoring', 'find'] },
  { id: 'quality-dashboard', title: 'Quality Dashboard', description: 'Quality metrics for component coverage, documentation, accessibility, and responsive validation', category: 'metrics', url: '/design-system/docs/quality-dashboard', keywords: ['quality', 'metrics', 'dashboard', 'coverage', 'accessibility', 'approval'] },
  { id: 'release-process', title: 'Release Process', description: 'End-to-end release process from staging to announcement', category: 'process', url: '/design-system/docs/release-process', keywords: ['release', 'publish', 'npm', 'staging', 'changelog', 'announcement'] },
  { id: 'architecture', title: 'Design System Architecture', description: 'Layered architecture, token system, provider hierarchy, and dependency flow', category: 'architecture', url: '/design-system/docs/architecture', keywords: ['architecture', 'layers', 'primitives', 'providers', 'dependencies'] },
  { id: 'design-tokens', title: 'Design Tokens', description: 'Token architecture, primitive vs semantic tokens, and usage guidelines', category: 'tokens', url: '/design-system/docs/design-tokens', keywords: ['tokens', 'design tokens', 'css variables', 'primitives', 'semantic'] },
  { id: 'theme-provider', title: 'Theme Provider', description: 'Theme system with light, dark, and high-contrast themes', category: 'providers', url: '/design-system/docs/theme-provider', keywords: ['theme', 'provider', 'dark mode', 'light mode', 'high contrast', 'theming'] },
  { id: 'accessibility', title: 'Accessibility Center', description: 'WCAG 2.2 AA compliance, keyboard navigation, ARIA patterns, and testing', category: 'accessibility', url: '/design-system/docs/accessibility', keywords: ['accessibility', 'a11y', 'wcag', 'keyboard', 'aria', 'screen reader'] },
];

function damerauLevenshteinDistance(a: string, b: string): number {
  const da: Record<string, number> = {};
  const d: number[][] = [];
  const alen = a.length;
  const blen = b.length;
  const maxDist = alen + blen;

  for (let i = 0; i <= alen; i++) {
    d[i] = [];
    d[i][0] = i;
  }
  for (let j = 0; j <= blen; j++) {
    d[0][j] = j;
  }

  for (let i = 1; i <= alen; i++) {
    let db = 0;
    for (let j = 1; j <= blen; j++) {
      const k = da[b[j - 1]] || 0;
      const l = db;
      let cost: number;
      if (a[i - 1] === b[j - 1]) {
        cost = 0;
        db = j;
      } else {
        cost = 1;
      }
      d[i][j] = Math.min(
        d[i - 1][j - 1] + cost,
        d[i][j - 1] + 1,
        d[i - 1][j] + 1,
        d[k - 1]?.[l - 1] !== undefined ? d[k - 1][l - 1] + (i - k - 1) + 1 + (j - l - 1) : maxDist,
      );
    }
    da[a[i - 1]] = i;
  }
  return d[alen][blen];
}

function fuzzyMatch(query: string, target: string): boolean {
  const q = query.toLowerCase();
  const t = target.toLowerCase();
  const dist = damerauLevenshteinDistance(q, t);
  if (dist === 0) return true;
  if (dist === 1 && t.length >= 4) return true;
  if (dist === 2 && t.length >= 6) return true;
  return false;
}

function scoreMatch(query: string, target: string): number {
  const q = query.toLowerCase();
  const t = target.toLowerCase();

  if (t === q) return 100;

  if (t.startsWith(q)) return 80;

  const wordBoundaryRegex = new RegExp(`(\\b|(?<=[a-z])(?=[A-Z]))${q.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}`, 'i');
  if (wordBoundaryRegex.test(t)) return 60;

  if (fuzzyMatch(q, t)) return 40;

  return 0;
}

function tokenize(str: string): string[] {
  return str
    .toLowerCase()
    .replace(/[^a-z0-9\s-]/g, '')
    .split(/\s+/)
    .filter(Boolean);
}

export class SearchEngine {
  private items: SearchResult[] = [];

  constructor() {
    this.buildIndex();
  }

  private buildIndex(): void {
    const results: SearchResult[] = [];

    for (const comp of componentManifest) {
      const keywords = [
        comp.name.toLowerCase(),
        ...comp.variants.map((v) => v.toLowerCase()),
        ...comp.states.map((s) => s.toLowerCase()),
        comp.category,
        ...tokenize(comp.description),
      ];

      results.push({
        id: comp.id,
        type: 'component',
        title: comp.name,
        description: comp.description,
        category: comp.category,
        url: `/design-system/component/${comp.id}`,
        keywords: [...new Set(keywords)],
      });
    }

    for (const token of tokenManifest) {
      const nameParts = token.name.replace(/^--/, '').split('-');
      const keywords = [
        token.category,
        token.type,
        ...nameParts,
        ...tokenize(token.value),
        ...token.examples.map((e) => e.toLowerCase()),
      ];

      results.push({
        id: token.id,
        type: 'token',
        title: token.name,
        description: `${token.category} token — ${token.type} — ${token.value}`,
        category: token.category,
        url: `/design-system/tokens/${token.category}`,
        keywords: [...new Set(keywords)],
      });
    }

    for (const doc of docEntries) {
      results.push({
        id: doc.id,
        type: 'doc',
        title: doc.title,
        description: doc.description,
        category: doc.category,
        url: doc.url,
        keywords: [...new Set([...doc.keywords, ...tokenize(doc.title), ...tokenize(doc.description)])],
      });
    }

    this.items = results;
  }

  search(query: string): SearchResult[] {
    const trimmed = query.trim();
    if (!trimmed) return [];

    const terms = tokenize(trimmed);
    if (terms.length === 0) return [];

    const scored = this.items.map((item) => {
      let totalScore = 0;
      const searchableTexts = [
        item.title,
        item.description,
        item.category,
        ...item.keywords,
      ];

      for (const term of terms) {
        let termBest = 0;

        for (const text of searchableTexts) {
          const score = scoreMatch(term, text);
          if (score > termBest) {
            termBest = score;
          }
        }

        if (termBest === 0) {
          for (const text of searchableTexts) {
            if (text.toLowerCase().includes(term)) {
              termBest = 20;
              break;
            }
          }
        }

        totalScore += termBest;
      }

      if (item.type === 'component') totalScore += 10;
      if (item.category === terms[0]) totalScore += 10;

      return { item, score: totalScore };
    });

    const filtered = scored.filter((s) => s.score > 0);
    filtered.sort((a, b) => b.score - a.score);

    return filtered.slice(0, 20).map((s) => s.item);
  }

  getSuggestions(query: string): string[] {
    const trimmed = query.trim().toLowerCase();
    if (!trimmed || trimmed.length < 2) return [];

    const suggestions = new Set<string>();

    for (const item of this.items) {
      const title = item.title.toLowerCase();
      if (title.startsWith(trimmed) && title !== trimmed) {
        suggestions.add(item.title);
      }
      if (suggestions.size >= 5) break;
    }

    if (suggestions.size < 5) {
      for (const item of this.items) {
        const title = item.title.toLowerCase();
        if (title.includes(trimmed) && title !== trimmed) {
          suggestions.add(item.title);
        }
        if (suggestions.size >= 5) break;
      }
    }

    return Array.from(suggestions).slice(0, 5);
  }
}

export const searchEngine = new SearchEngine();
