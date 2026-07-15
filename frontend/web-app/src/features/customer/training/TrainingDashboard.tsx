import React from 'react';
import { useNavigate } from 'react-router-dom';
import { INITIAL_COURSES, LIVE_SESSIONS, MOCK_CERTIFICATES } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';

export const TrainingDashboard: React.FC = () => {
  const navigate = useNavigate();

  const enrolledCourses = INITIAL_COURSES.filter(c => c.enrolled);
  const completedCourses = enrolledCourses.filter(c => c.progress === 100);
  const inProgressCourse = enrolledCourses.find(c => c.progress < 100);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Welcome Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Grower Training Center
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Scale up your mushroom cultivation yields and master sterile laboratory techniques.
        </p>
      </div>

      {/* Metrics Row */}
      <Grid columns="repeat(auto-fit, minmax(180px, 1fr))" gap="16px">
        {[
          { label: 'Enrolled Courses', value: enrolledCourses.length, icon: 'book-open', color: 'var(--color-primary)' },
          { label: 'Completed Courses', value: completedCourses.length, icon: 'check-circle', color: 'var(--color-text-success, #166534)' },
          { label: 'Earned Certificates', value: MOCK_CERTIFICATES.length, icon: 'award', color: 'var(--color-text-warning, #d97706)' },
          { label: 'Training Hours Logged', value: '10.5h', icon: 'clock', color: '#0ea5e9' },
        ].map((stat, i) => (
          <Card key={i} variant="outlined" padding="md" style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
            <div style={{ 
              width: '40px', 
              height: '40px', 
              borderRadius: '50%', 
              background: 'var(--color-bg-primary-weak)', 
              color: stat.color, 
              display: 'flex', 
              alignItems: 'center', 
              justifyContent: 'center' 
            }}>
              <Icon name={stat.icon} size={20} color="currentColor" />
            </div>
            <div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block' }}>
                {stat.label}
              </span>
              <strong style={{ fontSize: 'var(--text-body-lg)', color: 'var(--color-text-primary)' }}>
                {stat.value}
              </strong>
            </div>
          </Card>
        ))}
      </Grid>

      {/* Main Grid: Continue learning + Sidebars */}
      <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left Area */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Continue learning card */}
          {inProgressCourse && (
            <Card variant="outlined" padding="lg">
              <span style={{ fontSize: '10px', fontWeight: 'bold', color: 'var(--color-primary)', display: 'block', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                CONTINUE LEARNING
              </span>
              
              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '6px 0 12px' }}>
                {inProgressCourse.title}
              </h3>

              <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '0 0 16px', lineHeight: '1.4' }}>
                Active Module: <strong>Module 2: Agar Formulations & Pouring</strong> · Current Lesson: <em>Sterile Pouring Techniques</em>
              </p>

              {/* Progress Slider */}
              <div style={{ marginBottom: '20px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginBottom: '6px' }}>
                  <span>Syllabus Completion</span>
                  <span>{inProgressCourse.progress}% Completed</span>
                </div>
                <div style={{ width: '100%', height: '8px', background: '#e2e8f0', borderRadius: '4px', overflow: 'hidden' }}>
                  <div style={{ width: `${inProgressCourse.progress}%`, height: '100%', background: 'var(--color-primary)' }} />
                </div>
              </div>

              <div style={{ display: 'flex', gap: '12px' }}>
                <button
                  type="button"
                  className="cw-btn cw-btn--primary"
                  onClick={() => navigate(`/dashboard/training/classroom/${inProgressCourse.id}`)}
                >
                  Resume Lecture
                </button>
                <button
                  type="button"
                  className="cw-btn cw-btn--outlined"
                  onClick={() => navigate(`/dashboard/training/course/${inProgressCourse.id}`)}
                >
                  View Course Details
                </button>
              </div>
            </Card>
          )}

          {/* Quick Actions Row */}
          <div>
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
              Training Catalog Options
            </h3>
            <Grid columns="1fr 1fr" gap="16px">
              {[
                { label: 'Browse Courses', desc: 'Find sterile, spawn, and culture modules.', route: '/dashboard/training/courses', icon: 'book' },
                { label: 'My Enrolled Courses', desc: 'Check current syllabus progress.', route: '/dashboard/training/my-learning', icon: 'award' },
                { label: 'Earned Certificates', desc: 'Download PDF credentials.', route: '/dashboard/training/certificates', icon: 'award' },
                { label: 'Live Webinar Schedule', desc: 'Book interactive Q&A seats.', route: '/dashboard/training/schedule', icon: 'calendar' },
              ].map((action, i) => (
                <Card 
                  key={i} 
                  variant="outlined" 
                  padding="md"
                  onClick={() => navigate(action.route)}
                  style={{ cursor: 'pointer', transition: 'transform 0.15s ease' }}
                >
                  <div style={{ display: 'flex', gap: '12px', alignItems: 'flex-start' }}>
                    <div style={{ color: 'var(--color-primary)', marginTop: '2px' }}>
                      <Icon name={action.icon} size={18} color="currentColor" />
                    </div>
                    <div>
                      <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                        {action.label}
                      </h4>
                      <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '4px 0 0', lineHeight: '1.3' }}>
                        {action.desc}
                      </p>
                    </div>
                  </div>
                </Card>
              ))}
            </Grid>
          </div>
        </div>

        {/* Right Area: Webinars and AI Advisor */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Upcoming Live sessions */}
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
              Live Training Schedule
            </h4>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {LIVE_SESSIONS.map((session) => (
                <div 
                  key={session.id} 
                  style={{ 
                    borderBottom: '1px solid var(--color-border-default)', 
                    paddingBottom: '12px',
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '4px'
                  }}
                >
                  <span style={{ fontSize: '10px', color: 'var(--color-primary)', fontWeight: 'bold' }}>
                    {session.date} · {session.time}
                  </span>
                  <strong style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-primary)', lineHeight: '1.3' }}>
                    {session.title}
                  </strong>
                  <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                    Trainer: {session.trainer}
                  </span>
                  {session.registered ? (
                    <span style={{ fontSize: '10px', color: 'var(--color-text-success, #166534)', fontWeight: 'bold', marginTop: '4px' }}>
                      ✓ REGISTERED
                    </span>
                  ) : (
                    <button
                      type="button"
                      className="cw-btn cw-btn--outlined cw-btn--xs"
                      onClick={() => navigate('/dashboard/training/schedule')}
                      style={{ alignSelf: 'flex-start', marginTop: '6px', fontSize: '9px', padding: '2px 6px' }}
                    >
                      Book Seat
                    </button>
                  )}
                </div>
              ))}
            </div>
          </Card>

          {/* AI Mentor Recommendation Banner */}
          <Card 
            variant="outlined" 
            padding="md"
            style={{ 
              background: 'linear-gradient(135deg, var(--color-bg-primary-weak) 0%, #f9fbf4 100%)', 
              border: '1px solid var(--color-border-default)' 
            }}
          >
            <div style={{ display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
              <span style={{ fontSize: '20px' }}>🤖</span>
              <div>
                <h5 style={{ fontSize: 'var(--text-caption)', fontWeight: 'bold', color: 'var(--color-text-primary)', margin: 0 }}>
                  AI Learning Assistant
                </h5>
                <p style={{ fontSize: '11px', color: 'var(--color-text-secondary)', margin: '4px 0 0', lineHeight: '1.4' }}>
                  Based on your purchase history of <strong>Pink Oyster Grain Spawn</strong>, we recommend enrolling in <strong>Commercial Monotub Cultivation</strong> to optimize substrate mixes!
                </p>
                <button
                  type="button"
                  className="cw-btn cw-btn--primary cw-btn--xs"
                  onClick={() => navigate('/dashboard/training/courses')}
                  style={{ marginTop: '8px', fontSize: '9px' }}
                >
                  Explore Course
                </button>
              </div>
            </div>
          </Card>
        </div>
      </Grid>
    </div>
  );
};
export default TrainingDashboard;
