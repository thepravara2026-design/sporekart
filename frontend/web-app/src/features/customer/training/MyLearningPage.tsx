import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { INITIAL_COURSES } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';

export const MyLearningPage: React.FC = () => {
  const navigate = useNavigate();
  const [courses] = useState(INITIAL_COURSES);
  const [activeTab, setActiveTab] = useState<'all' | 'progress' | 'completed'>('all');

  const enrolled = courses.filter(c => c.enrolled);
  
  const filtered = enrolled.filter(c => {
    if (activeTab === 'completed') return c.progress === 100;
    if (activeTab === 'progress') return c.progress < 100;
    return true;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Title */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
            My Learning
          </h2>
          <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
            Track your current study courses, progress bars, and certification goals.
          </p>
        </div>

        <button 
          type="button" 
          className="cw-btn cw-btn--primary cw-btn--sm"
          onClick={() => navigate('/dashboard/training/courses')}
        >
          Browse Catalog
        </button>
      </div>

      {/* Tabs list */}
      <div style={{ borderBottom: '1px solid var(--color-border-default)', display: 'flex', gap: '20px' }}>
        {[
          { id: 'all', label: `All Enrolled (${enrolled.length})` },
          { id: 'progress', label: 'In Progress' },
          { id: 'completed', label: 'Completed' },
        ].map((tab) => (
          <button
            key={tab.id}
            type="button"
            onClick={() => setActiveTab(tab.id as any)}
            style={{
              background: 'none',
              border: 'none',
              padding: '8px 0',
              fontSize: 'var(--text-body-sm)',
              fontWeight: activeTab === tab.id ? 'var(--weight-bold)' : 'var(--weight-medium)',
              color: activeTab === tab.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              borderBottom: activeTab === tab.id ? '2px solid var(--color-primary)' : '2px solid transparent',
              cursor: 'pointer'
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* List Container */}
      {filtered.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>🎓</div>
          <div>
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>No courses found</h3>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Explore our catalog to start studying.</p>
          </div>
        </div>
      ) : (
        <Grid columns="repeat(auto-fill, minmax(320px, 1fr))" gap="24px">
          {filtered.map((c) => (
            <Card 
              key={c.id} 
              variant="outlined" 
              padding="none" 
              style={{ display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}
            >
              {/* Banner */}
              <div style={{ background: c.bannerGradient, height: '80px', padding: '16px', display: 'flex', alignItems: 'flex-end' }}>
                <h4 style={{ color: '#fff', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', margin: 0 }}>
                  {c.title}
                </h4>
              </div>

              {/* Body */}
              <div style={{ padding: '16px', flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)', textTransform: 'uppercase', fontWeight: 'bold' }}>
                    Instructor: {c.instructorName}
                  </span>
                  
                  {/* Progress bar details */}
                  <div style={{ margin: '16px 0' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '11px', color: 'var(--color-text-secondary)', marginBottom: '4px' }}>
                      <span>Study progress</span>
                      <strong>{c.progress}%</strong>
                    </div>
                    <div style={{ width: '100%', height: '6px', background: '#cbd5e1', borderRadius: '3px', overflow: 'hidden' }}>
                      <div style={{ width: `${c.progress}%`, height: '100%', background: 'var(--color-primary)' }} />
                    </div>
                  </div>
                </div>

                <div style={{ display: 'flex', gap: '8px', borderTop: '1px solid var(--color-border-default)', paddingTop: '12px' }}>
                  <button
                    type="button"
                    className="cw-btn cw-btn--primary cw-btn--sm"
                    style={{ flex: 1 }}
                    onClick={() => navigate(`/dashboard/training/classroom/${c.id}`)}
                  >
                    {c.progress === 100 ? 'Review Lectures' : 'Continue Studying'}
                  </button>
                  {c.progress === 100 && (
                    <button
                      type="button"
                      className="cw-btn cw-btn--outlined cw-btn--sm"
                      onClick={() => navigate('/dashboard/training/certificates')}
                      style={{ padding: '6px' }}
                      title="View Certificate"
                    >
                      <Icon name="award" size={16} color="currentColor" />
                    </button>
                  )}
                </div>

              </div>
            </Card>
          ))}
        </Grid>
      )}

    </div>
  );
};
export default MyLearningPage;
