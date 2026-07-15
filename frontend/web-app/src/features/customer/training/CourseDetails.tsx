import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { INITIAL_COURSES, Course } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';
import { Grid } from '../../../design-system/components/layout/Grid';

export const CourseDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  
  const [courses, setCourses] = useState<Course[]>(INITIAL_COURSES);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const courseIndex = courses.findIndex(c => c.id === id);
  const course = courses[courseIndex];

  // Active accordion module index state
  const [openModuleIdx, setOpenModuleIdx] = useState<number | null>(0);

  if (!course) {
    return (
      <div style={{ textAlign: 'center', padding: '48px 0' }}>
        <h2>Course not found</h2>
        <button type="button" className="cw-btn cw-btn--primary" onClick={() => navigate('/dashboard/training/courses')}>
          Back to Catalog
        </button>
      </div>
    );
  }

  const handleEnroll = () => {
    // Simulating enrollment update
    const updated = [...courses];
    updated[courseIndex] = {
      ...course,
      enrolled: true,
      progress: 0
    };
    setCourses(updated);
    
    setToastMessage("Enrollment successful! Redirecting to classroom...");
    setTimeout(() => {
      setToastMessage(null);
      navigate(`/dashboard/training/classroom/${course.id}`);
    }, 1500);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Header Navigation */}
      <div>
        <button
          type="button"
          onClick={() => navigate('/dashboard/training/courses')}
          style={{
            background: 'none',
            border: 'none',
            padding: 0,
            fontSize: 'var(--text-body-sm)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-secondary)',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
          }}
        >
          <Icon name="arrow-left" size={16} color="currentColor" />
          Back to Catalog
        </button>
      </div>

      {/* Overview Banner */}
      <div 
        style={{ 
          background: course.bannerGradient, 
          borderRadius: 'var(--radius-lg)', 
          padding: '32px',
          color: '#fff',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: '24px',
          boxShadow: 'var(--shadow-2)'
        }}
      >
        <div style={{ flex: 1, minWidth: '280px' }}>
          <span style={{ fontSize: '11px', fontWeight: 'bold', color: 'rgba(255,255,255,0.3)', textTransform: 'uppercase', letterSpacing: '1px' }}>
            {course.category} · {course.difficulty}
          </span>
          <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', margin: '8px 0 12px', color: '#fff' }}>
            {course.title}
          </h2>
          <p style={{ fontSize: 'var(--text-body-sm)', color: '#e0f2fe', margin: 0, maxWidth: '600px', lineHeight: '1.5' }}>
            Instructor: <strong>{course.instructorName}</strong> · {course.instructorRole}
          </p>

          <div style={{ display: 'flex', gap: '20px', marginTop: '24px', fontSize: 'var(--text-caption)', color: '#cbd5e1', flexWrap: 'wrap' }}>
            <span>Duration: <strong>{course.duration}</strong></span>
            <span>Language: <strong>{course.language}</strong></span>
            <span>Rating: <strong>{course.rating}★</strong></span>
          </div>
        </div>

        <div>
          {course.enrolled ? (
            <button
              type="button"
              className="cw-btn"
              style={{ background: '#fff', color: 'var(--color-primary)', fontWeight: 'bold', border: 'none', padding: '12px 24px' }}
              onClick={() => navigate(`/dashboard/training/classroom/${course.id}`)}
            >
              Enter Classroom
            </button>
          ) : (
            <button
              type="button"
              className="cw-btn"
              style={{ background: '#fff', color: 'var(--color-primary)', fontWeight: 'bold', border: 'none', padding: '12px 24px' }}
              onClick={handleEnroll}
            >
              Enroll in Course
            </button>
          )}
        </div>
      </div>

      {/* Main Grid content */}
      <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left Column: Syllabus and Outcomes */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Course description */}
          <Card variant="outlined" padding="lg">
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
              Course Overview
            </h3>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.6' }}>
              {course.description} SporeKart provides the standard, laboratory-validated sterile technique syllabus, supporting commercial mushroom growers and hobbyists in securing high-density flushes and sterile mycelium propagation.
            </p>
          </Card>

          {/* Curriculum Accordion */}
          <Card variant="outlined" padding="lg">
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 16px' }}>
              Course Syllabus
            </h3>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {course.modules.map((mod, modIdx) => {
                const isOpen = openModuleIdx === modIdx;
                return (
                  <div 
                    key={modIdx}
                    style={{ 
                      border: '1px solid var(--color-border-default)', 
                      borderRadius: 'var(--radius-md)', 
                      overflow: 'hidden' 
                    }}
                  >
                    {/* Header */}
                    <div 
                      onClick={() => setOpenModuleIdx(isOpen ? null : modIdx)}
                      style={{ 
                        background: '#f8fafc', 
                        padding: '12px 16px', 
                        display: 'flex', 
                        justifyContent: 'space-between', 
                        alignItems: 'center', 
                        cursor: 'pointer',
                        userSelect: 'none'
                      }}
                    >
                      <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>
                        {mod.title}
                      </strong>
                      <Icon name={isOpen ? 'chevron-up' : 'chevron-down'} size={16} color="var(--color-text-secondary)" />
                    </div>

                    {/* Lesson nodes */}
                    {isOpen && (
                      <div style={{ display: 'flex', flexDirection: 'column', background: 'var(--color-bg-surface-default)' }}>
                        {mod.lessons.map((lesson) => (
                          <div 
                            key={lesson.id}
                            style={{ 
                              padding: '12px 16px', 
                              borderTop: '1px solid var(--color-border-default)',
                              display: 'flex',
                              justifyContent: 'space-between',
                              alignItems: 'center',
                              fontSize: 'var(--text-caption)',
                            }}
                          >
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--color-text-primary)' }}>
                              <Icon name="play-circle" size={14} color="var(--color-text-secondary)" />
                              <span>{lesson.title}</span>
                            </div>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: 'var(--color-text-secondary)' }}>
                              <span>{lesson.duration}</span>
                              {lesson.completed && (
                                <span style={{ color: 'var(--color-text-success, #166534)', fontWeight: 'bold' }}>✓ Done</span>
                              )}
                            </div>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          </Card>

          {/* Learning Outcomes */}
          <Card variant="outlined" padding="lg">
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
              What you will learn
            </h3>
            <ul style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', paddingLeft: '16px', margin: 0, lineHeight: '1.6' }}>
              {course.outcomes.map((out, idx) => (
                <li key={idx}>{out}</li>
              ))}
            </ul>
          </Card>

        </div>

        {/* Right Column: Pre-reqs & Trainer info */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
              Course Requirements
            </h4>
            <ul style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', paddingLeft: '16px', margin: 0, lineHeight: '1.5' }}>
              {course.requirements.map((req, idx) => (
                <li key={idx} style={{ marginBottom: '8px' }}>{req}</li>
              ))}
            </ul>
          </Card>

          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              Instructor Bio
            </h4>
            <div style={{ display: 'flex', gap: '12px', alignItems: 'center', marginBottom: '12px' }}>
              <div style={{ width: '40px', height: '40px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '20px' }}>
                🎓
              </div>
              <div>
                <strong style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-primary)', display: 'block' }}>
                  {course.instructorName}
                </strong>
                <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)' }}>
                  {course.instructorRole}
                </span>
              </div>
            </div>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              Experienced mycologist with over 10 years in laboratory cultivation research and sterile technique auditing.
            </p>
          </Card>

          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              General Training FAQ
            </h4>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', fontSize: 'var(--text-caption)' }}>
              <div>
                <strong style={{ color: 'var(--color-text-primary)' }}>Do I earn a certificate?</strong>
                <span style={{ color: 'var(--color-text-secondary)', display: 'block', marginTop: '2px' }}>
                  Yes, scoring 100% on the syllabus modules generates a printable certificate.
                </span>
              </div>
              <div>
                <strong style={{ color: 'var(--color-text-primary)' }}>Can I download session assets?</strong>
                <span style={{ color: 'var(--color-text-secondary)', display: 'block', marginTop: '2px' }}>
                  All pdf templates and worksheets are downloadable inside the video learning workspace.
                </span>
              </div>
            </div>
          </Card>
        </div>

      </Grid>
    </div>
  );
};
export default CourseDetails;
