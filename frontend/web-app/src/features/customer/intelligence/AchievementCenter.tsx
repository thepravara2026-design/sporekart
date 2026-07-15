import React, { useState } from 'react';
import { ACHIEVEMENTS, PROFILE } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const AchievementCenter: React.FC = () => {
  const [points, setPoints] = useState(PROFILE.points);
  const [badges, setBadges] = useState(ACHIEVEMENTS);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const handleRedeemVoucher = (pointsCost: number, voucherName: string) => {
    if (points < pointsCost) {
      setToastMessage("Insufficient loyalty points balance.");
      setTimeout(() => setToastMessage(null), 3000);
      return;
    }

    setPoints(prev => prev - pointsCost);
    setToastMessage(`Redeemed "${voucherName}" voucher code successfully! Check your email.`);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handleClaimBadgePoints = (badgeId: string, pts: number) => {
    setBadges(prev =>
      prev.map(b => (b.id === badgeId ? { ...b, status: 'earned' as const } : b))
    );
    setPoints(prev => prev + pts);
    setToastMessage(`Claimed ${pts} points for completing milestone!`);
    setTimeout(() => setToastMessage(null), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Header */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Loyalty & Achievement Center
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Redeem grower points for purchase vouchers or complete academy milestones to unlock badges.
        </p>
      </div>

      {/* Points Card */}
      <Card variant="outlined" padding="lg" style={{ background: 'linear-gradient(to right, #0f172a, #1e293b)', color: '#fff' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
          <div>
            <span style={{ fontSize: '10px', color: '#94a3b8', fontWeight: 'bold', display: 'block', textTransform: 'uppercase' }}>
              CURRENT REWARDS BALANCE
            </span>
            <strong style={{ fontSize: '32px', fontWeight: 'bold', color: '#f8fafc', display: 'block', margin: '4px 0' }}>
              {points} Points
            </strong>
            <span style={{ fontSize: 'var(--text-caption)', color: '#94a3b8' }}>
              Status: <strong>Master Grower Tier</strong> (2.5x point multiplier active)
            </span>
          </div>

          <div style={{ display: 'flex', gap: '12px' }}>
            <button
              type="button"
              className="cw-btn cw-btn--primary cw-btn--sm"
              onClick={() => handleRedeemVoucher(1000, '₹500 Store Voucher')}
              style={{ background: 'var(--color-primary)', border: 'none' }}
            >
              Redeem ₹500 (1000 pts)
            </button>
            <button
              type="button"
              className="cw-btn cw-btn--outlined cw-btn--sm"
              onClick={() => handleRedeemVoucher(2000, '₹1,200 Spawn Voucher')}
              style={{ color: '#fff', borderColor: '#475569' }}
            >
              Redeem ₹1,200 (2000 pts)
            </button>
          </div>
        </div>
      </Card>

      {/* Milestones Grid */}
      <div>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', marginBottom: '16px' }}>
          Milestone Badges
        </h3>

        <Grid columns="repeat(auto-fit, minmax(260px, 1fr))" gap="16px">
          {badges.map((badge) => (
            <Card 
              key={badge.id}
              variant="outlined"
              padding="md"
              style={{
                background: badge.status === 'locked' ? '#f8fafc' : '#fff',
                opacity: badge.status === 'locked' ? 0.75 : 1,
                border: badge.status === 'locked' ? '1px dashed var(--color-border-default)' : '1px solid var(--color-border-default)',
                display: 'flex',
                gap: '16px',
                alignItems: 'flex-start'
              }}
            >
              <div style={{ fontSize: '36px' }}>{badge.icon}</div>
              <div>
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                  {badge.title}
                </h4>
                <p style={{ fontSize: '11px', color: 'var(--color-text-secondary)', margin: '4px 0 8px', lineHeight: '1.3' }}>
                  {badge.description}
                </p>

                {badge.status === 'locked' ? (
                  <button
                    type="button"
                    className="cw-btn cw-btn--outlined cw-btn--xs"
                    onClick={() => handleClaimBadgePoints(badge.id, badge.pointsReward)}
                    style={{ fontSize: '10px' }}
                  >
                    Simulate Completion (+{badge.pointsReward} pts)
                  </button>
                ) : (
                  <span style={{ fontSize: '10px', color: 'var(--color-text-success, #166534)', fontWeight: 'bold' }}>
                    ✓ UNLOCKED (+{badge.pointsReward} pts)
                  </span>
                )}
              </div>
            </Card>
          ))}
        </Grid>
      </div>

    </div>
  );
};
export default AchievementCenter;
