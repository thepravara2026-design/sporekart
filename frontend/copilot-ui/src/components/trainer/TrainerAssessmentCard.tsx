import type { Assessment } from './types/trainer';

const typeColors: Record<string, string> = {
  MCQ: '#1565c0', SHORT_ANSWER: '#f57f17', LONG_ANSWER: '#6a1b9a',
  SCENARIO: '#00838f', PRACTICAL: '#2e7d32', LAB: '#e65100', CERTIFICATION: '#c62828',
};
const diffColors: Record<string, string> = {
  BEGINNER: '#2e7d32', INTERMEDIATE: '#f57f17', ADVANCED: '#c62828', EXPERT: '#6a1b9a',
};

function passFailColor(passingMarks: number, totalMarks: number): string {
  const ratio = totalMarks > 0 ? passingMarks / totalMarks : 0;
  if (ratio <= 0.4) return 'var(--cp-color-success)';
  if (ratio <= 0.6) return 'var(--cp-color-warning)';
  return 'var(--cp-color-danger)';
}

export function TrainerAssessmentCard({ assessment }: { assessment: Assessment }) {
  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, padding: 16, background: 'var(--cp-color-surface)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
        <h3 style={{ margin: 0, fontSize: 16, color: 'var(--cp-color-text)' }}>{assessment.title}</h3>
        <div style={{ display: 'flex', gap: 6 }}>
          <span style={{ background: typeColors[assessment.type] || '#888', color: '#fff', padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{assessment.type}</span>
          <span style={{ background: diffColors[assessment.difficulty] || '#888', color: '#fff', padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{assessment.difficulty}</span>
        </div>
      </div>
      <div style={{ display: 'flex', gap: 16, marginBottom: 12, fontSize: 13 }}>
        <span style={{ color: 'var(--cp-color-text)' }}>Total: <strong>{assessment.totalMarks}</strong></span>
        <span style={{ color: passFailColor(assessment.passingMarks, assessment.totalMarks) }}>
          Pass: <strong>{assessment.passingMarks}</strong>
        </span>
        <span style={{ color: 'var(--cp-color-text-muted)' }}>Questions: {assessment.questions.length}</span>
      </div>
      {assessment.questions.length > 0 && (
        <div>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-text)', marginBottom: 4 }}>Questions Preview</div>
          {assessment.questions.slice(0, 3).map((q, i) => (
            <div key={q.questionId} style={{ fontSize: 12, color: 'var(--cp-color-text-secondary)', padding: '4px 0', borderBottom: '1px solid var(--cp-color-border)' }}>
              <span style={{ color: 'var(--cp-color-text-muted)', marginRight: 4 }}>{i + 1}.</span>
              {q.questionText.slice(0, 80)}{q.questionText.length > 80 ? '...' : ''}
              <span style={{ float: 'right', color: 'var(--cp-color-text-muted)' }}>{q.marks} pts</span>
            </div>
          ))}
          {assessment.questions.length > 3 && (
            <div style={{ fontSize: 11, color: 'var(--cp-color-text-muted)', marginTop: 4 }}>+{assessment.questions.length - 3} more</div>
          )}
        </div>
      )}
    </div>
  );
}
