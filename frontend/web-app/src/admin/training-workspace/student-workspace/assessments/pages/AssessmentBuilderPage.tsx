import { useAssessments } from '../state/AssessmentContext';
import { AssessmentStatusBadge } from '../components/AssessmentStatusBadge';
import { ASSESSMENT_TYPE_LABELS, DIFFICULTY_LABELS } from '../types';

export function AssessmentBuilderPage() {
  const { assessments } = useAssessments();
  const drafts = assessments.filter((a) => a.status === 'draft');

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Assessment Builder</h2>

      <div style={{ padding: 'var(--space-4)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
        <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Create New Assessment</h3>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', margin: '0 0 16px 0' }}>
          Use the assessment builder to create quizzes, examinations, practical assessments, and more.
          Select an assessment type to get started with pre-configured question templates.
        </p>
        <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
          {(['quiz', 'objective-test', 'subjective-test', 'practical-assessment', 'final-examination', 'certification-assessment'] as const).map((type) => (
            <div key={type} style={{
              padding: '12px', borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border-default)', cursor: 'pointer',
              background: 'var(--color-bg-surface-default)', minWidth: 160, flex: 1,
            }}>
              <div style={{ fontWeight: 'var(--weight-medium)', fontSize: 'var(--text-body-sm)' }}>{ASSESSMENT_TYPE_LABELS[type]}</div>
              <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginTop: 4 }}>Quick create template</div>
            </div>
          ))}
        </div>
      </div>

      <div>
        <h3 style={{ fontSize: 'var(--text-body)', fontWeight: 'var(--weight-semibold)', margin: '0 0 12px 0' }}>Draft Assessments ({drafts.length})</h3>
        {drafts.length === 0 ? (
          <div style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)', padding: '12px 0' }}>No draft assessments. Create a new assessment to get started.</div>
        ) : (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-component-gap)' }}>
            {drafts.map((a) => (
              <div key={a.id} style={{ padding: 'var(--space-3)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 8 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{a.title}</div>
                  <AssessmentStatusBadge status={a.status} />
                </div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{a.assessmentCode} &middot; {ASSESSMENT_TYPE_LABELS[a.assessmentType]} &middot; {DIFFICULTY_LABELS[a.difficulty]}</div>
                <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{a.totalQuestions} questions &middot; {a.durationMinutes} min &middot; {a.maxMarks} marks</div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
