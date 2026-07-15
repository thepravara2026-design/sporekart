import React from 'react';
import { ANALYTICS_DATA } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Grid } from '../../../design-system/components/layout/Grid';

export const AnalyticsDashboard: React.FC = () => {
  const { monthlyYields, sterilitySuccessRate } = ANALYTICS_DATA;

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Grower Workspace Analytics
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Interactive analytics tracking monthly spawn harvests, autoclave sterility margins, and AI yield forecasts.
        </p>
      </div>

      {/* Grid overview */}
      <Grid columns="1.5fr 1fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left: Harvest progress charts */}
        <Card variant="outlined" padding="lg">
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '0 0 16px' }}>
            Monthly Harvest Yields vs. Targets
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {monthlyYields.map((row, idx) => {
              const percentage = Math.min(Math.round((row.yieldKg / row.targetKg) * 100), 100);
              return (
                <div key={idx}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginBottom: '4px' }}>
                    <span>{row.month}</span>
                    <strong>{row.yieldKg} kg / {row.targetKg} kg ({percentage}%)</strong>
                  </div>

                  <div style={{ width: '100%', height: '12px', background: '#cbd5e1', borderRadius: '6px', overflow: 'hidden' }}>
                    <div 
                      style={{ 
                        width: `${percentage}%`, 
                        height: '100%', 
                        background: 'linear-gradient(to right, var(--color-primary), #0284c7)' 
                      }} 
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </Card>

        {/* Right: Sterility indicator and forecasting */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Sterility gauge */}
          <Card variant="outlined" padding="lg" style={{ textAlign: 'center' }}>
            <h4 style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '0 0 8px' }}>
              STERILITY SUCCESS MARGIN
            </h4>
            <strong style={{ fontSize: '36px', color: 'var(--color-text-success, #166534)', display: 'block', margin: '4px 0' }}>
              {sterilitySuccessRate}%
            </strong>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              Calculated from 12 autoclave run logs and petri plate inoculation checks.
            </p>
          </Card>

          {/* AI Yield Forecast */}
          <Card 
            variant="outlined" 
            padding="lg" 
            style={{ 
              background: 'linear-gradient(135deg, var(--color-bg-primary-weak) 0%, #fff 100%)' 
            }}
          >
            <span style={{ fontSize: '9px', color: 'var(--color-primary)', fontWeight: 'bold' }}>AI YIELD PREDICTIONS</span>
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '6px 0 4px' }}>
              August Harvest Forecast: 280 kg
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              Based on the 65kg grain spawn purchased and average 98% laboratory sterility ratios.
            </p>
          </Card>
        </div>

      </Grid>
      
    </div>
  );
};
export default AnalyticsDashboard;
