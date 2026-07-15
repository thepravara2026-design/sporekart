import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { INITIAL_COURSES, Course, Lesson } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';
import { Grid } from '../../../design-system/components/layout/Grid';

export const VideoLearningPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [courses, setCourses] = useState<Course[]>(INITIAL_COURSES);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const courseIndex = courses.findIndex(c => c.id === id);
  const course = courses[courseIndex];

  // Active Lesson state
  const [activeLessonId, setActiveLessonId] = useState<string>('l1-1');
  const [activeTab, setActiveTab] = useState<'transcript' | 'notes' | 'resources'>('transcript');
  
  // Custom video playback states
  const [playing, setPlaying] = useState(false);
  const [progressVal, setProgressVal] = useState(30);

  // User notes
  const [notesText, setNotesText] = useState('IPA alcohol wipes should be applied in clean downward sweeps.');

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

  // Find active lesson object
  let activeLesson: Lesson | undefined;
  for (const mod of course.modules) {
    const found = mod.lessons.find(l => l.id === activeLessonId);
    if (found) {
      activeLesson = found;
      break;
    }
  }

  if (!activeLesson) {
    activeLesson = course.modules[0]?.lessons[0];
  }

  const handleToggleComplete = (lessonId: string) => {
    const updated = [...courses];
    let updatedProgress = course.progress;

    // Toggle completed state inside modules
    const updatedModules = course.modules.map(mod => {
      const lessons = mod.lessons.map(les => {
        if (les.id === lessonId) {
          const newState = !les.completed;
          if (newState) {
            setToastMessage(`Lesson "${les.title}" marked complete!`);
          }
          return { ...les, completed: newState };
        }
        return les;
      });
      return { ...mod, lessons };
    });

    // Re-calculate overall course progress percentage
    const allLessons = updatedModules.flatMap(m => m.lessons);
    const completedCount = allLessons.filter(l => l.completed).length;
    updatedProgress = Math.round((completedCount / allLessons.length) * 100);

    updated[courseIndex] = {
      ...course,
      modules: updatedModules,
      progress: updatedProgress
    };

    setCourses(updated);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handleSaveNotes = () => {
    setToastMessage("Lesson notes saved successfully!");
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handleNextLesson = () => {
    // Find next lesson id
    const allLessons = course.modules.flatMap(m => m.lessons);
    const activeIdx = allLessons.findIndex(l => l.id === activeLessonId);
    if (activeIdx < allLessons.length - 1) {
      setActiveLessonId(allLessons[activeIdx + 1].id);
      setPlaying(false);
      setProgressVal(0);
    } else {
      setToastMessage("You have reached the final lesson of this course!");
      setTimeout(() => setToastMessage(null), 3000);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Header back bar */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <button
          type="button"
          onClick={() => navigate(`/dashboard/training/course/${course.id}`)}
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
          Exit Classroom
        </button>

        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          Course: <strong>{course.title}</strong>
        </span>
      </div>

      <Grid columns="2fr 1.1fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left Section: Video + Tabs */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* Simulated HTML5 Video Player */}
          <div 
            style={{ 
              width: '100%', 
              background: '#090d16', 
              borderRadius: 'var(--radius-lg)', 
              aspectRatio: '16/9',
              position: 'relative',
              overflow: 'hidden',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              alignItems: 'center',
              boxShadow: 'var(--shadow-2)'
            }}
          >
            {/* Play/Pause Video Source Tag */}
            <video 
              key={activeLesson?.id}
              width="100%" 
              height="100%" 
              style={{ objectFit: 'cover', opacity: playing ? 0.95 : 0.4 }}
              src={activeLesson?.videoUrl}
              autoPlay={playing}
              loop
              muted
            />

            {/* Play Overlays */}
            {!playing && (
              <button
                type="button"
                onClick={() => setPlaying(true)}
                style={{
                  position: 'absolute',
                  border: 'none',
                  background: 'var(--color-bg-primary-default)',
                  width: '64px',
                  height: '64px',
                  borderRadius: '50%',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  cursor: 'pointer',
                  color: '#fff',
                  boxShadow: 'var(--shadow-3)',
                }}
              >
                <Icon name="play" size={28} color="currentColor" style={{ marginLeft: '4px' }} />
              </button>
            )}

            {/* Float Label */}
            <div style={{ position: 'absolute', top: '16px', left: '16px', background: 'rgba(9, 13, 22, 0.7)', color: '#fff', padding: '4px 10px', borderRadius: 'var(--radius-sm)', fontSize: 'var(--text-caption)' }}>
              Active Lecture: {activeLesson?.title}
            </div>

            {/* Custom Control Bar Overlay */}
            <div 
              style={{ 
                position: 'absolute', 
                bottom: 0, 
                left: 0, 
                right: 0, 
                background: 'linear-gradient(to top, rgba(9, 13, 22, 0.95), rgba(9, 13, 22, 0.2))',
                padding: '12px 16px',
                display: 'flex',
                flexDirection: 'column',
                gap: '8px'
              }}
            >
              {/* Progress track */}
              <div 
                style={{ 
                  width: '100%', 
                  height: '4px', 
                  background: 'rgba(255,255,255,0.2)', 
                  cursor: 'pointer', 
                  borderRadius: '2px',
                  position: 'relative'
                }}
                onClick={(e) => {
                  const rect = e.currentTarget.getBoundingClientRect();
                  const pct = Math.round(((e.clientX - rect.left) / rect.width) * 100);
                  setProgressVal(pct);
                }}
              >
                <div style={{ width: `${progressVal}%`, height: '100%', background: 'var(--color-bg-primary-default)', borderRadius: '2px' }} />
              </div>

              {/* Action Buttons */}
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', color: '#fff' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                  <button 
                    type="button" 
                    onClick={() => setPlaying(!playing)}
                    style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#fff', display: 'flex' }}
                  >
                    <Icon name={playing ? 'pause' : 'play'} size={16} color="currentColor" />
                  </button>
                  <span style={{ fontSize: '11px', color: '#94a3b8' }}>
                    04:12 / {activeLesson?.duration}
                  </span>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                  <button type="button" style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#fff' }}>
                    <Icon name="volume-2" size={16} color="currentColor" />
                  </button>
                  <button type="button" style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#fff' }}>
                    <Icon name="maximize" size={16} color="currentColor" />
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* Complete & Next Row */}
          <div style={{ display: 'flex', justifyContent: 'space-between', gap: '12px' }}>
            <button
              type="button"
              className={`cw-btn ${activeLesson?.completed ? 'cw-btn--outlined' : 'cw-btn--primary'}`}
              onClick={() => handleToggleComplete(activeLessonId)}
              style={{ display: 'flex', alignItems: 'center', gap: '8px' }}
            >
              <Icon name={activeLesson?.completed ? "check" : "check-circle"} size={16} color="currentColor" />
              {activeLesson?.completed ? 'Lesson Completed' : 'Mark Lesson Complete'}
            </button>

            <button
              type="button"
              className="cw-btn cw-btn--outlined"
              onClick={handleNextLesson}
              style={{ display: 'flex', alignItems: 'center', gap: '8px' }}
            >
              Next Lecture
              <Icon name="arrow-right" size={16} color="currentColor" />
            </button>
          </div>

          {/* Transcript / Notes Tabs */}
          <div style={{ borderBottom: '1px solid var(--color-border-default)', display: 'flex', gap: '20px' }}>
            {[
              { id: 'transcript', label: 'Video Transcript' },
              { id: 'notes', label: 'My Notes' },
              { id: 'resources', label: `Resources (${activeLesson?.resources.length || 0})` },
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

          {/* Tabs Content */}
          <div style={{ minHeight: '120px' }}>
            {activeTab === 'transcript' && (
              <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: '1.6', margin: 0 }}>
                {activeLesson?.transcript}
              </p>
            )}

            {activeTab === 'notes' && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                <textarea
                  rows={4}
                  value={notesText}
                  onChange={(e) => setNotesText(e.target.value)}
                  placeholder="Record your lesson growth observations..."
                  style={{
                    width: '100%',
                    padding: '8px 12px',
                    borderRadius: 'var(--radius-md)',
                    border: '1px solid var(--color-border-default)',
                    fontSize: 'var(--text-body-sm)',
                    background: 'var(--color-bg-surface-default)',
                    color: 'var(--color-text-primary)',
                    outline: 'none',
                    resize: 'vertical'
                  }}
                />
                <button
                  type="button"
                  onClick={handleSaveNotes}
                  className="cw-btn cw-btn--primary cw-btn--sm"
                  style={{ alignSelf: 'flex-start' }}
                >
                  Save Notes
                </button>
              </div>
            )}

            {activeTab === 'resources' && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                {activeLesson?.resources.map((file, i) => (
                  <div 
                    key={i}
                    style={{ 
                      display: 'flex', 
                      justifyContent: 'space-between', 
                      alignItems: 'center', 
                      border: '1px solid var(--color-border-default)', 
                      borderRadius: 'var(--radius-sm)', 
                      padding: '10px 12px',
                      fontSize: 'var(--text-caption)'
                    }}
                  >
                    <span style={{ color: 'var(--color-text-primary)' }}>{file}</span>
                    <button 
                      type="button" 
                      onClick={() => {
                        setToastMessage(`Downloading ${file}...`);
                        setTimeout(() => setToastMessage(null), 2000);
                      }}
                      className="cw-btn cw-btn--outlined cw-btn--xs"
                    >
                      Download
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>

        </div>

        {/* Right Section: Lesson Navigator */}
        <Card variant="outlined" padding="md">
          <h3 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
            Course Curriculum
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px', marginTop: '12px' }}>
            {course.modules.map((mod, modIdx) => (
              <div key={modIdx}>
                <span style={{ fontSize: '10px', fontWeight: 'bold', color: 'var(--color-text-secondary)', display: 'block', marginBottom: '8px', textTransform: 'uppercase' }}>
                  {mod.title}
                </span>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
                  {mod.lessons.map((les) => {
                    const isActive = les.id === activeLessonId;
                    return (
                      <div
                        key={les.id}
                        onClick={() => {
                          setActiveLessonId(les.id);
                          setPlaying(false);
                          setProgressVal(0);
                        }}
                        style={{
                          display: 'flex',
                          alignItems: 'center',
                          gap: '10px',
                          padding: '10px',
                          borderRadius: 'var(--radius-sm)',
                          cursor: 'pointer',
                          background: isActive ? 'var(--color-bg-primary-weak)' : 'transparent',
                          border: `1px solid ${isActive ? 'var(--color-bg-primary-default)' : 'transparent'}`,
                          transition: 'all 0.15s ease',
                        }}
                      >
                        <input
                          type="checkbox"
                          checked={les.completed}
                          onChange={() => handleToggleComplete(les.id)}
                          onClick={(e) => e.stopPropagation()} // Prevent setting active
                          style={{ cursor: 'pointer' }}
                        />
                        <div style={{ flex: 1, minWidth: 0 }}>
                          <span style={{ 
                            fontSize: 'var(--text-caption)', 
                            fontWeight: isActive ? 'bold' : 'normal', 
                            color: 'var(--color-text-primary)',
                            display: 'block',
                            textOverflow: 'ellipsis',
                            overflow: 'hidden',
                            whiteSpace: 'nowrap'
                          }}>
                            {les.title}
                          </span>
                          <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)' }}>
                            Duration: {les.duration}
                          </span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>
        </Card>

      </Grid>
    </div>
  );
};
export default VideoLearningPage;
