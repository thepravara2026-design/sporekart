import React, { useState } from 'react';
import { ACHIEVEMENTS, REWARDS_CATALOG } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const EngagementHubPage: React.FC = () => {
  const [points, setPoints] = useState(750);
  const [redeemedCode, setRedeemedCode] = useState<string | null>(null);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const handleRedeem = (cost: number, desc: string, code: string) => {
    if (points < cost) {
      setToastMessage("Insufficient points to redeem this reward.");
      setTimeout(() => setToastMessage(null), 3000);
      return;
    }
    setPoints(prev => prev - cost);
    setRedeemedCode(code);
    setToastMessage(`Redeemed coupon: ${desc}! Code is: ${code}`);
    setTimeout(() => setToastMessage(null), 4000);
  };

  const handleCopyReferral = () => {
    setToastMessage("Referral link copied to clipboard!");
    setTimeout(() => setToastMessage(null), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone={points >= 0 ? "success" : "info"} onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Page Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          SporeGrower Loyalty Hub
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Track achievements, collect points, and redeem rewards for spawn cultivars and sterile lab equipment.
        </p>
      </div>

      <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left Column: Membership and Points Store */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Membership card */}
          <Card 
            variant="outlined" 
            padding="lg"
            style={{ 
              background: 'linear-gradient(135deg, #1e293b 0%, #0f172a 100%)', 
              color: '#fff',
              border: 'none',
              position: 'relative',
              overflow: 'hidden',
            }}
          >
            {/* Background design pattern */}
            <div style={{ position: 'absolute', right: '-20px', bottom: '-20px', opacity: 0.15, fontSize: '140px', userSelect: 'none', pointerEvents: 'none' }}>
              🍄
            </div>

            <div style={{ position: 'relative', zIndex: 1 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <span style={{ fontSize: '11px', fontWeight: 'bold', color: 'var(--color-bg-primary-default)', textTransform: 'uppercase', letterSpacing: '1px' }}>
                    MEMBERSHIP TIER
                  </span>
                  <h3 style={{ fontSize: '28px', fontWeight: 'var(--weight-bold)', margin: '4px 0 0', color: '#fff' }}>
                    Silver Cultivator
                  </h3>
                </div>
                <div style={{ fontSize: '32px' }}>🥈</div>
              </div>

              {/* Progress bar to Gold */}
              <div style={{ marginTop: '28px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: '#cbd5e1', marginBottom: '6px' }}>
                  <span>Points Balance: <strong>{points} pts</strong></span>
                  <span>Gold Cultivator at 1,000 pts</span>
                </div>
                <div style={{ width: '100%', height: '8px', background: '#334155', borderRadius: '4px', overflow: 'hidden' }}>
                  <div style={{ width: `${(points / 1000) * 100}%`, height: '100%', background: 'var(--color-bg-primary-default)', transition: 'width 0.4s ease' }} />
                </div>
              </div>

              {/* Tier Perks info */}
              <div style={{ marginTop: '24px', borderTop: '1px solid #334155', paddingTop: '16px', display: 'flex', gap: '20px', flexWrap: 'wrap' }}>
                <div>
                  <span style={{ fontSize: '10px', color: '#94a3b8', display: 'block' }}>REWARD MULTIPLIER</span>
                  <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'bold', color: '#fff' }}>1.2x on all spawn orders</span>
                </div>
                <div>
                  <span style={{ fontSize: '10px', color: '#94a3b8', display: 'block' }}>FREE SHIPPING</span>
                  <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'bold', color: '#fff' }}>Orders above ₹2,500</span>
                </div>
                <div>
                  <span style={{ fontSize: '10px', color: '#94a3b8', display: 'block' }}>LAB PRIORITY</span>
                  <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'bold', color: '#fff' }}>24h support response</span>
                </div>
              </div>
            </div>
          </Card>

          {/* Rewards store */}
          <Card variant="outlined" padding="lg">
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 16px' }}>
              Redeem Rewards
            </h3>
            
            {redeemedCode && (
              <div 
                style={{ 
                  background: 'var(--color-bg-success-weak, #f0fdf4)', 
                  border: '1px solid #bcf0da', 
                  borderRadius: 'var(--radius-md)', 
                  padding: '12px 16px',
                  marginBottom: '20px',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center'
                }}
              >
                <div>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-success, #166534)', display: 'block', fontWeight: 'bold' }}>REDEEMED COUPON CODE:</span>
                  <strong style={{ fontSize: 'var(--text-body-lg)', color: 'var(--color-text-primary)', fontFamily: 'monospace' }}>{redeemedCode}</strong>
                </div>
                <button 
                  type="button" 
                  className="cw-btn cw-btn--sm cw-btn--primary"
                  onClick={() => {
                    navigator.clipboard.writeText(redeemedCode);
                    setToastMessage('Coupon code copied to clipboard!');
                    setTimeout(() => setToastMessage(null), 3000);
                  }}
                >
                  Copy Code
                </button>
              </div>
            )}

            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {REWARDS_CATALOG.map((coupon) => {
                const canRedeem = points >= coupon.pointsCost;
                return (
                  <div 
                    key={coupon.id}
                    style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      border: '1px solid var(--color-border-default)',
                      borderRadius: 'var(--radius-md)',
                      padding: '16px',
                      background: canRedeem ? 'transparent' : 'var(--color-bg-surface-secondary, #fafafa)',
                      opacity: canRedeem ? 1 : 0.65,
                      flexWrap: 'wrap',
                      gap: '12px',
                    }}
                  >
                    <div>
                      <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', display: 'block' }}>
                        {coupon.description}
                      </strong>
                      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '4px' }}>
                        Cost: <strong>{coupon.pointsCost} points</strong>
                      </span>
                    </div>
                    <button
                      type="button"
                      disabled={!canRedeem}
                      onClick={() => handleRedeem(coupon.pointsCost, coupon.description, coupon.code)}
                      className={`cw-btn cw-btn--sm ${canRedeem ? 'cw-btn--primary' : 'cw-btn--outlined'}`}
                    >
                      {canRedeem ? 'Redeem Reward' : 'Insufficient Points'}
                    </button>
                  </div>
                );
              })}
            </div>
          </Card>
        </div>

        {/* Right Column: Achievements & Referrals */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Achievements list */}
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-md)' }}>
              Grower Achievements
            </h4>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {ACHIEVEMENTS.map((item) => (
                <div key={item.id} style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                  <div style={{ fontSize: '24px', width: '36px', height: '36px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    {item.icon}
                  </div>
                  <div>
                    <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', display: 'block' }}>{item.title}</strong>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{item.description}</span>
                  </div>
                </div>
              ))}
            </div>
          </Card>

          {/* Referral card */}
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              Refer-a-Grower Program
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              Invite other cultivators to buy their agar, cultures, or growing supplies. Once they make their first purchase:
            </p>
            <ul style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '8px 0 16px', paddingLeft: '16px', lineHeight: '1.4' }}>
              <li>They get 10% off their first order.</li>
              <li>You receive <strong>250 loyalty points</strong>.</li>
            </ul>

            <div style={{ background: '#f4f6f8', border: '1px solid var(--color-border-default)', padding: '10px 12px', borderRadius: 'var(--radius-sm)', display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
              <span style={{ fontFamily: 'monospace', fontSize: 'var(--text-body-sm)', fontWeight: 'bold', color: 'var(--color-text-primary)' }}>GROW-SPORE-750</span>
              <button 
                type="button" 
                onClick={handleCopyReferral}
                style={{ background: 'none', border: 'none', cursor: 'pointer', padding: 4, display: 'flex', alignItems: 'center', color: 'var(--color-text-secondary)' }}
              >
                <Icon name="copy" size={14} color="currentColor" />
              </button>
            </div>

            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', borderTop: '1px solid var(--color-border-default)', paddingTop: '10px' }}>
              <span>Total Referrals: <strong>3</strong></span>
              <span>Points Earned: <strong>750 pts</strong></span>
            </div>
          </Card>
        </div>
      </Grid>
    </div>
  );
};
export default EngagementHubPage;
