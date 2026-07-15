import { useMemo } from 'react';
import { MOCK_VALIDATION_SCORES } from '../mock/mockValidation';
import { MOCK_VALIDATION_ACTIVITY } from '../mock/mockActivity';

export function useValidationAnalytics() {
  return useMemo(() => {
    const total = MOCK_VALIDATION_SCORES.length;
    const avgOverall = Math.round(MOCK_VALIDATION_SCORES.reduce((s, v) => s + v.overall, 0) / total);
    const lowRisk = MOCK_VALIDATION_SCORES.filter((v) => v.riskLevel === 'low').length;
    const mediumRisk = MOCK_VALIDATION_SCORES.filter((v) => v.riskLevel === 'medium').length;
    const highRisk = MOCK_VALIDATION_SCORES.filter((v) => v.riskLevel === 'high').length;
    const criticalRisk = MOCK_VALIDATION_SCORES.filter((v) => v.riskLevel === 'critical').length;
    const certified = MOCK_VALIDATION_SCORES.filter((v) => v.certificationStatus !== 'none').length;
    const needsImprovement = MOCK_VALIDATION_SCORES.filter((v) => v.overall < 70).length;
    const published = MOCK_VALIDATION_SCORES.filter((v) => v.publishing >= 80).length;

    const riskDist = [
      { label: 'Low Risk', count: lowRisk, color: 'var(--color-accent-green)' },
      { label: 'Medium Risk', count: mediumRisk, color: 'var(--color-accent-orange)' },
      { label: 'High Risk', count: highRisk, color: 'var(--color-accent-red)' },
      { label: 'Critical', count: criticalRisk, color: 'var(--color-accent-red-dark)' },
    ];

    const certDist = [
      { label: 'Enterprise', count: MOCK_VALIDATION_SCORES.filter((v) => v.certificationStatus === 'enterprise').length, color: 'var(--color-accent-purple)' },
      { label: 'Gold', count: MOCK_VALIDATION_SCORES.filter((v) => v.certificationStatus === 'gold').length, color: 'var(--color-accent-gold)' },
      { label: 'Silver', count: MOCK_VALIDATION_SCORES.filter((v) => v.certificationStatus === 'silver').length, color: 'var(--color-accent-silver)' },
      { label: 'Bronze', count: MOCK_VALIDATION_SCORES.filter((v) => v.certificationStatus === 'bronze').length, color: 'var(--color-accent-bronze)' },
      { label: 'None', count: MOCK_VALIDATION_SCORES.filter((v) => v.certificationStatus === 'none').length, color: 'var(--color-text-tertiary)' },
    ];

    return {
      total, avgOverall, lowRisk, mediumRisk, highRisk, criticalRisk,
      certified, needsImprovement, published, riskDist, certDist,
      totalEvents: MOCK_VALIDATION_ACTIVITY.length,
      totalReports: 6,
    };
  }, []);
}
