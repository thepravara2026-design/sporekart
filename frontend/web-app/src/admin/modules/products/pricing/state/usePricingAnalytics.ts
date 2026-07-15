import { useMemo } from 'react';
import { MOCK_PRICING_ENTITIES } from '../mock/mockPrices';
import { MOCK_DISCOUNT_RULES } from '../mock/mockDiscounts';
import { MOCK_PRICE_SCHEDULES } from '../mock/mockScheduledPricing';
import { MOCK_PROMOTIONAL_CAMPAIGNS } from '../mock/mockCampaigns';
import { MOCK_COMMERCIAL_RULES } from '../mock/mockCommercialRules';

export function usePricingAnalytics() {
  return useMemo(() => {
    const totalProducts = MOCK_PRICING_ENTITIES.length;
    const activeProducts = MOCK_PRICING_ENTITIES.filter((e) => e.status === 'active').length;
    const draftProducts = MOCK_PRICING_ENTITIES.filter((e) => e.status === 'draft').length;
    const pendingProducts = MOCK_PRICING_ENTITIES.filter((e) => e.status === 'pending').length;
    const archivedProducts = MOCK_PRICING_ENTITIES.filter((e) => e.status === 'archived').length;
    const approvedProducts = MOCK_PRICING_ENTITIES.filter((e) => e.approved).length;
    const pendingApproval = MOCK_PRICING_ENTITIES.filter((e) => !e.approved && e.status !== 'archived').length;

    const avgMrp = Math.round(
      MOCK_PRICING_ENTITIES.reduce((sum, e) => sum + (e.prices.find((p) => p.tier === 'mrp')?.amount ?? 0), 0) / totalProducts
    );
    const avgSelling = Math.round(
      MOCK_PRICING_ENTITIES.reduce((sum, e) => sum + (e.prices.find((p) => p.tier === 'selling')?.amount ?? 0), 0) / totalProducts
    );
    const avgDiscount =
      avgMrp > 0 ? Math.round(((avgMrp - avgSelling) / avgMrp) * 100) : 0;

    const totalDiscountRules = MOCK_DISCOUNT_RULES.length;
    const activeDiscounts = MOCK_DISCOUNT_RULES.filter((d) => d.status === 'active').length;
    const scheduledDiscounts = MOCK_DISCOUNT_RULES.filter((d) => d.status === 'scheduled').length;

    const totalSchedules = MOCK_PRICE_SCHEDULES.length;
    const activeSchedules = MOCK_PRICE_SCHEDULES.filter((s) => s.status === 'active').length;
    const draftSchedules = MOCK_PRICE_SCHEDULES.filter((s) => s.status === 'draft').length;

    const totalCampaigns = MOCK_PROMOTIONAL_CAMPAIGNS.length;
    const activeCampaigns = MOCK_PROMOTIONAL_CAMPAIGNS.filter((c) => c.status === 'active').length;

    const totalRules = MOCK_COMMERCIAL_RULES.length;
    const activeRules = MOCK_COMMERCIAL_RULES.filter((r) => r.status === 'active').length;

    const gstDistribution = [0, 5, 12, 18, 28].map((pct) => ({
      percentage: pct,
      count: MOCK_PRICING_ENTITIES.filter((e) => e.gstPercentage === pct).length,
    }));

    const statusDistribution = [
      { status: 'active' as const, count: activeProducts, color: 'var(--color-accent-green)' },
      { status: 'draft' as const, count: draftProducts, color: 'var(--color-accent-yellow)' },
      { status: 'pending' as const, count: pendingProducts, color: 'var(--color-accent-orange)' },
      { status: 'archived' as const, count: archivedProducts, color: 'var(--color-accent-red)' },
    ];

    return {
      totalProducts,
      activeProducts,
      draftProducts,
      pendingProducts,
      archivedProducts,
      approvedProducts,
      pendingApproval,
      avgMrp,
      avgSelling,
      avgDiscount,
      totalDiscountRules,
      activeDiscounts,
      scheduledDiscounts,
      totalSchedules,
      activeSchedules,
      draftSchedules,
      totalCampaigns,
      activeCampaigns,
      totalRules,
      activeRules,
      gstDistribution,
      statusDistribution,
    };
  }, []);
}
