import { useMemo } from 'react';
import { MOCK_SEO_ENTRIES } from '../mock/mockSeo';
import { MOCK_PUBLISHING_EVENTS } from '../mock/mockPublishing';

export function useSeoAnalytics() {
  return useMemo(() => {
    const total = MOCK_SEO_ENTRIES.length;
    const published = MOCK_SEO_ENTRIES.filter((e) => e.publishingStatus === 'published').length;
    const draft = MOCK_SEO_ENTRIES.filter((e) => e.publishingStatus === 'draft').length;
    const readySeo = MOCK_SEO_ENTRIES.filter((e) => e.publishingStatus === 'ready_seo').length;
    const readyPublish = MOCK_SEO_ENTRIES.filter((e) => e.publishingStatus === 'ready_publish').length;
    const archived = MOCK_SEO_ENTRIES.filter((e) => e.publishingStatus === 'archived').length;
    const reviewRequired = MOCK_SEO_ENTRIES.filter((e) => e.publishingStatus === 'review_required').length;

    const avgSeoScore = Math.round(MOCK_SEO_ENTRIES.reduce((s, e) => s + e.seoScore, 0) / total);
    const avgAiScore = Math.round(MOCK_SEO_ENTRIES.reduce((s, e) => s + e.aiReadinessScore, 0) / total);
    const avgMpScore = Math.round(MOCK_SEO_ENTRIES.reduce((s, e) => s + e.marketplaceScore, 0) / total);

    const withErrors = MOCK_SEO_ENTRIES.filter((e) => e.validationErrors.length > 0).length;
    const withWarnings = MOCK_SEO_ENTRIES.filter((e) => e.validationWarnings.length > 0).length;
    const marketplaceReady = MOCK_SEO_ENTRIES.filter((e) => e.marketplaceScore >= 70).length;

    const statusDist = [
      { status: 'published', count: published, color: 'var(--color-accent-green)' },
      { status: 'ready_publish', count: readyPublish, color: 'var(--color-accent-purple)' },
      { status: 'ready_seo', count: readySeo, color: 'var(--color-accent-blue)' },
      { status: 'draft', count: draft, color: 'var(--color-accent-yellow)' },
      { status: 'review', count: reviewRequired, color: 'var(--color-accent-orange)' },
      { status: 'archived', count: archived, color: 'var(--color-accent-red)' },
    ];

    const totalEvents = MOCK_PUBLISHING_EVENTS.length;
    const totalPublished = MOCK_PUBLISHING_EVENTS.filter((e) => e.toStatus === 'published').length;

    return {
      total, published, draft, readySeo, readyPublish, archived, reviewRequired,
      avgSeoScore, avgAiScore, avgMpScore,
      withErrors, withWarnings, marketplaceReady,
      statusDist, totalEvents, totalPublished,
    };
  }, []);
}
