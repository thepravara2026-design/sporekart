import { useState, useRef, useEffect } from 'react';
import { useTrainerCopilot } from './useTrainerCopilot';
import { TrainerLessonCard } from './TrainerLessonCard';
import { TrainerAssessmentCard } from './TrainerAssessmentCard';
import { TrainerBatchCard } from './TrainerBatchCard';
import { TrainerStudentProgressCard } from './TrainerStudentProgressCard';
import { TrainerCertificationCard } from './TrainerCertificationCard';
import { TrainerCultivationGuide } from './TrainerCultivationGuide';
import { TrainerKnowledgeCard } from './TrainerKnowledgeCard';
import type { Lesson, Assessment, TrainingBatch, BatchAnalytics, StudentProgress } from './types/trainer';

type Tab = 'chat' | 'lessons' | 'assessments' | 'batches' | 'analytics' | 'cultivation';

const tabs: { key: Tab; label: string }[] = [
  { key: 'chat', label: 'Chat' },
  { key: 'lessons', label: 'Lessons' },
  { key: 'assessments', label: 'Assessments' },
  { key: 'batches', label: 'Batches' },
  { key: 'analytics', label: 'Analytics' },
  { key: 'cultivation', label: 'Cultivation' },
];

const quickActions = [
  { label: 'Generate Lesson', action: 'generate_lesson' },
  { label: 'Create Assessment', action: 'generate_assessment' },
  { label: 'View Batches', action: 'view_batches' },
  { label: 'Analytics', action: 'view_analytics' },
  { label: 'Cultivation Guide', action: 'cultivation_guide' },
  { label: 'Search Knowledge', action: 'search_knowledge' },
];

export function TrainerCopilotPanel() {
  const [activeTab, setActiveTab] = useState<Tab>('chat');
  const [inputValue, setInputValue] = useState('');
  const chatEndRef = useRef<HTMLDivElement>(null);
  const copilot = useTrainerCopilot();

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [copilot.messages]);

  const handleQuickAction = (action: string) => {
    switch (action) {
      case 'generate_lesson':
        copilot.generateLesson('Create a new lesson plan');
        setActiveTab('lessons');
        break;
      case 'generate_assessment':
        copilot.generateAssessment('Create a new assessment');
        setActiveTab('assessments');
        break;
      case 'view_batches':
        copilot.fetchBatches();
        setActiveTab('batches');
        break;
      case 'view_analytics':
        copilot.fetchAnalytics();
        setActiveTab('analytics');
        break;
      case 'cultivation_guide':
        copilot.fetchCultivationGuide();
        setActiveTab('cultivation');
        break;
      case 'search_knowledge':
        copilot.searchKnowledge('cultivation techniques');
        setActiveTab('chat');
        break;
    }
  };

  const handleSend = () => {
    const trimmed = inputValue.trim();
    if (!trimmed) return;
    copilot.sendMessage(trimmed);
    setInputValue('');
  };

  const analytics = copilot.analytics;

  return (
    <div style={{
      display: 'flex', flexDirection: 'column', height: '100%',
      background: 'var(--cp-color-bg)', color: 'var(--cp-color-text)',
      fontFamily: 'system-ui, -apple-system, sans-serif', fontSize: 14,
    }}>
      <header style={{
        padding: '12px 20px', borderBottom: '1px solid var(--cp-color-border)',
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
        background: 'var(--cp-color-surface)',
      }}>
        <h1 style={{ margin: 0, fontSize: 18, fontWeight: 600 }}>Trainer Copilot</h1>
        {copilot.streaming && <span style={{ fontSize: 12, color: 'var(--cp-color-primary)' }}>Streaming…</span>}
      </header>

      <div style={{ display: 'flex', gap: 4, padding: '8px 20px', borderBottom: '1px solid var(--cp-color-border)', background: 'var(--cp-color-surface)' }}>
        {tabs.map(tab => (
          <button key={tab.key} onClick={() => setActiveTab(tab.key)}
            style={{
              background: activeTab === tab.key ? 'var(--cp-color-primary)' : 'transparent',
              color: activeTab === tab.key ? '#fff' : 'var(--cp-color-text-secondary)',
              border: 'none', borderRadius: 6, padding: '6px 14px',
              fontSize: 13, fontWeight: 500, cursor: 'pointer',
            }}>
            {tab.label}
          </button>
        ))}
      </div>

      <div style={{ flex: 1, overflow: 'auto', padding: 16 }}>
        {activeTab === 'chat' && (
          <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <div style={{ flex: 1, overflow: 'auto', marginBottom: 12 }}>
              {copilot.messages.length === 0 && (
                <div style={{ textAlign: 'center', padding: '40px 20px', color: 'var(--cp-color-text-muted)' }}>
                  <div style={{ fontSize: 24, marginBottom: 8 }}>💬</div>
                  <div style={{ fontSize: 14, fontWeight: 500, marginBottom: 4 }}>Welcome to Trainer Copilot</div>
                  <div style={{ fontSize: 12 }}>Ask a question or use a quick action below.</div>
                </div>
              )}
              {copilot.messages.map(msg => (
                <div key={msg.id} style={{
                  display: 'flex', justifyContent: msg.role === 'user' ? 'flex-end' : 'flex-start',
                  marginBottom: 8,
                }}>
                  <div style={{
                    maxWidth: '75%', padding: '10px 14px', borderRadius: 12,
                    background: msg.role === 'user' ? 'var(--cp-color-user-bubble)' : 'var(--cp-color-assistant-bubble)',
                    color: 'var(--cp-color-text)', fontSize: 13, lineHeight: 1.5,
                    border: msg.role === 'user' ? 'none' : '1px solid var(--cp-color-border)',
                  }}>
                    {msg.content}
                    {msg.suggestions && msg.suggestions.length > 0 && (
                      <div style={{ marginTop: 8, display: 'flex', flexWrap: 'wrap', gap: 4 }}>
                        {msg.suggestions.map((s, i) => (
                          <button key={i} onClick={() => handleQuickAction(s.action)}
                            style={{
                              background: 'var(--cp-color-primary-weak)', color: 'var(--cp-color-primary)',
                              border: '1px solid var(--cp-color-primary)', borderRadius: 4,
                              padding: '3px 8px', fontSize: 11, cursor: 'pointer',
                            }}>
                            {s.label}
                          </button>
                        ))}
                      </div>
                    )}
                  </div>
                </div>
              ))}
              {copilot.loading && (
                <div style={{ display: 'flex', justifyContent: 'flex-start', marginBottom: 8 }}>
                  <div style={{ padding: '10px 14px', borderRadius: 12, background: 'var(--cp-color-assistant-bubble)', border: '1px solid var(--cp-color-border)', fontSize: 13, color: 'var(--cp-color-text-muted)' }}>
                    Thinking…
                  </div>
                </div>
              )}
              <div ref={chatEndRef} />
            </div>

            {copilot.error && (
              <div style={{ background: '#ffebee', color: '#c62828', padding: '8px 12px', borderRadius: 6, marginBottom: 8, fontSize: 12 }}>
                {copilot.error}
              </div>
            )}

            <div style={{ display: 'flex', gap: 8 }}>
              <input value={inputValue} onChange={e => setInputValue(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && handleSend()}
                placeholder="Ask the trainer copilot…"
                style={{
                  flex: 1, padding: '10px 14px', borderRadius: 8, border: '1px solid var(--cp-color-border)',
                  background: 'var(--cp-color-surface)', color: 'var(--cp-color-text)',
                  fontSize: 13, outline: 'none',
                }} />
              <button onClick={handleSend} disabled={!inputValue.trim() || copilot.loading || copilot.streaming}
                style={{
                  padding: '10px 20px', borderRadius: 8, border: 'none',
                  background: inputValue.trim() ? 'var(--cp-color-primary)' : 'var(--cp-color-border)',
                  color: inputValue.trim() ? '#fff' : 'var(--cp-color-text-muted)',
                  fontSize: 13, fontWeight: 600, cursor: inputValue.trim() ? 'pointer' : 'default',
                }}>
                Send
              </button>
            </div>
          </div>
        )}

        {activeTab === 'lessons' && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
              <h2 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>Generated Lessons</h2>
              <button onClick={() => copilot.generateLesson('Create a new lesson')}
                style={{ background: 'var(--cp-color-primary)', color: '#fff', border: 'none', borderRadius: 6, padding: '6px 14px', fontSize: 12, fontWeight: 600, cursor: 'pointer' }}>
                + New Lesson
              </button>
            </div>
            {copilot.lessons.length === 0 ? (
              <div style={{ textAlign: 'center', padding: 40, color: 'var(--cp-color-text-muted)', fontSize: 13 }}>
                No lessons generated yet. Click "+ New Lesson" or use the chat to generate one.
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {copilot.lessons.map(lesson => <TrainerLessonCard key={lesson.lessonId} lesson={lesson} />)}
              </div>
            )}
          </div>
        )}

        {activeTab === 'assessments' && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
              <h2 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>Assessments</h2>
              <button onClick={() => copilot.generateAssessment('Create new assessment')}
                style={{ background: 'var(--cp-color-primary)', color: '#fff', border: 'none', borderRadius: 6, padding: '6px 14px', fontSize: 12, fontWeight: 600, cursor: 'pointer' }}>
                + New Assessment
              </button>
            </div>
            {copilot.assessments.length === 0 ? (
              <div style={{ textAlign: 'center', padding: 40, color: 'var(--cp-color-text-muted)', fontSize: 13 }}>
                No assessments created yet.
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {copilot.assessments.map(a => <TrainerAssessmentCard key={a.assessmentId} assessment={a} />)}
              </div>
            )}
          </div>
        )}

        {activeTab === 'batches' && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
              <h2 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>Training Batches</h2>
              <button onClick={() => copilot.fetchBatches()}
                style={{ background: 'var(--cp-color-primary)', color: '#fff', border: 'none', borderRadius: 6, padding: '6px 14px', fontSize: 12, fontWeight: 600, cursor: 'pointer' }}>
                Refresh
              </button>
            </div>
            {copilot.batches.length === 0 ? (
              <div style={{ textAlign: 'center', padding: 40, color: 'var(--cp-color-text-muted)', fontSize: 13 }}>
                No batches loaded. Click refresh to fetch batches.
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                {copilot.batches.map(b => (
                  <div key={b.batchId}>
                    <TrainerBatchCard batch={b} />
                    {copilot.batchAnalytics[b.batchId] && (
                      <BatchAnalyticsSection analytics={copilot.batchAnalytics[b.batchId]} />
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {activeTab === 'analytics' && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
              <h2 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>Analytics Overview</h2>
              <button onClick={() => copilot.fetchAnalytics()}
                style={{ background: 'var(--cp-color-primary)', color: '#fff', border: 'none', borderRadius: 6, padding: '6px 14px', fontSize: 12, fontWeight: 600, cursor: 'pointer' }}>
                Refresh
              </button>
            </div>
            {!analytics ? (
              <div style={{ textAlign: 'center', padding: 40, color: 'var(--cp-color-text-muted)', fontSize: 13 }}>
                Click refresh to load analytics.
              </div>
            ) : (
              <div>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(140px, 1fr))', gap: 12, marginBottom: 16 }}>
                  <StatCard label="Total Students" value={analytics.totalStudents} />
                  <StatCard label="Total Batches" value={analytics.totalBatches} />
                  <StatCard label="Active Batches" value={analytics.activeBatches} />
                  <StatCard label="Completed Batches" value={analytics.completedBatches} />
                  <StatCard label="Avg Attendance" value={`${analytics.avgAttendanceAcrossBatches}%`} />
                  <StatCard label="Avg Score" value={`${analytics.avgScoreAcrossBatches}%`} />
                  <StatCard label="At Risk" value={analytics.atRiskStudents} color="var(--cp-color-danger)" />
                  <StatCard label="Certifications" value={analytics.totalCertificationsIssued} />
                  <StatCard label="Pending Certs" value={analytics.pendingCertifications} color="var(--cp-color-warning)" />
                </div>
              </div>
            )}
          </div>
        )}

        {activeTab === 'cultivation' && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
              <h2 style={{ margin: 0, fontSize: 16, fontWeight: 600 }}>Cultivation Guides</h2>
              <button onClick={() => copilot.fetchCultivationGuide()}
                style={{ background: 'var(--cp-color-primary)', color: '#fff', border: 'none', borderRadius: 6, padding: '6px 14px', fontSize: 12, fontWeight: 600, cursor: 'pointer' }}>
                + Add Guide
              </button>
            </div>
            <TrainerCultivationGuide guides={copilot.cultivationGuides} />
          </div>
        )}
      </div>

      <div style={{
        padding: '8px 20px', borderTop: '1px solid var(--cp-color-border)',
        background: 'var(--cp-color-surface)',
      }}>
        <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
          {quickActions.map(qa => (
            <button key={qa.action} onClick={() => handleQuickAction(qa.action)}
              style={{
                background: 'var(--cp-color-primary-weak)', color: 'var(--cp-color-primary)',
                border: '1px solid transparent', borderRadius: 6, padding: '5px 12px',
                fontSize: 12, cursor: 'pointer', fontWeight: 500, whiteSpace: 'nowrap',
              }}>
              {qa.label}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}

function StatCard({ label, value, color }: { label: string; value: string | number; color?: string }) {
  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, padding: '12px 16px', background: 'var(--cp-color-surface)' }}>
      <div style={{ fontSize: 11, color: 'var(--cp-color-text-muted)', marginBottom: 4, textTransform: 'uppercase', letterSpacing: '0.5px' }}>{label}</div>
      <div style={{ fontSize: 22, fontWeight: 700, color: color || 'var(--cp-color-text)' }}>{value}</div>
    </div>
  );
}

function BatchAnalyticsSection({ analytics }: { analytics: BatchAnalytics }) {
  return (
    <div style={{ marginTop: 8, padding: '8px 12px', background: 'var(--cp-color-bg)', borderRadius: 6, fontSize: 12, color: 'var(--cp-color-text-secondary)' }}>
      <div style={{ display: 'flex', gap: 16, marginBottom: 8 }}>
        <span>Avg Attendance: <strong>{analytics.avgAttendance}%</strong></span>
        <span>Avg Score: <strong>{analytics.avgScore}%</strong></span>
        <span>Modules: <strong>{analytics.completedModules}/{analytics.totalModules}</strong></span>
      </div>
      {analytics.topStudents.length > 0 && (
        <div style={{ marginBottom: 4 }}>
          <span style={{ fontWeight: 600, color: 'var(--cp-color-success)' }}>Top Students: </span>
          {analytics.topStudents.map(s => s.studentName).join(', ')}
        </div>
      )}
      {analytics.atRiskStudents.length > 0 && (
        <div>
          <span style={{ fontWeight: 600, color: 'var(--cp-color-danger)' }}>At Risk: </span>
          {analytics.atRiskStudents.map(s => s.studentName).join(', ')}
        </div>
      )}
    </div>
  );
}
