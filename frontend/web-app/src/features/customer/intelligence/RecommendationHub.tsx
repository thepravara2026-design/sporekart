import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SUGGESTIONS } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const RecommendationHub: React.FC = () => {
  const navigate = useNavigate();
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Personalized Recommendation Hub
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Suggested cultivars, laboratory supplies, and learning modules selected for your cultivation setups.
        </p>
      </div>

      {/* Products recommendations shelf */}
      <div>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', marginBottom: '16px' }}>
          Trending Products & Spawns
        </h3>
        
        <Grid columns="repeat(auto-fit, minmax(280px, 1fr))" gap="20px">
          {SUGGESTIONS.map((item) => (
            <Card 
              key={item.id}
              variant="outlined"
              padding="md"
              style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', height: '100%' }}
            >
              <div>
                <div style={{ fontSize: '36px', marginBottom: '8px' }}>{item.image}</div>
                <span style={{ fontSize: '9px', color: 'var(--color-primary)', fontWeight: 'bold', textTransform: 'uppercase' }}>
                  {item.category}
                </span>
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '6px 0 2px' }}>
                  {item.name}
                </h4>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
                  {item.description}
                </p>
              </div>

              <div style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: '10px', marginTop: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{item.price}</strong>
                <button
                  type="button"
                  className="cw-btn cw-btn--primary cw-btn--sm"
                  onClick={() => {
                    setToastMessage(`Added "${item.name}" to cart!`);
                    setTimeout(() => setToastMessage(null), 3000);
                  }}
                >
                  Buy Now
                </button>
              </div>
            </Card>
          ))}
        </Grid>
      </div>

      {/* Courses recommendations shelf */}
      <div>
        <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', marginBottom: '16px' }}>
          Related Study Modules
        </h3>
        
        <Card variant="outlined" padding="md" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
          <div>
            <span style={{ fontSize: '10px', color: 'var(--color-primary)', fontWeight: 'bold' }}>RECOMMENDED COURSE</span>
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '4px 0 2px' }}>
              Advanced Liquid Culture Isolation
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0 }}>
              Master cloning wild mushrooms and storing liquid cultures in refrigerators up to 6 months.
            </p>
          </div>

          <button
            type="button"
            className="cw-btn cw-btn--primary"
            onClick={() => navigate('/dashboard/training/courses')}
          >
            Enroll Course
          </button>
        </Card>
      </div>

    </div>
  );
};
export default RecommendationHub;
