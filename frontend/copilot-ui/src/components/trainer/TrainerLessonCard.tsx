import type { Lesson } from './types/trainer';

const typeColors: Record<string, string> = {
  LECTURE: '#1565c0', DEMONSTRATION: '#f57f17',
  LAB_EXERCISE: '#2e7d32', WORKSHOP: '#6a1b9a', REVIEW: '#00838f',
};
const diffColors: Record<string, string> = {
  BEGINNER: '#2e7d32', INTERMEDIATE: '#f57f17', ADVANCED: '#c62828',
};

export function TrainerLessonCard({ lesson }: { lesson: Lesson }) {
  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, padding: 16, background: 'var(--cp-color-surface)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
        <h3 style={{ margin: 0, fontSize: 16, color: 'var(--cp-color-text)' }}>{lesson.title}</h3>
        <div style={{ display: 'flex', gap: 6 }}>
          <span style={{ background: typeColors[lesson.lessonType] || '#888', color: '#fff', padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{lesson.lessonType}</span>
          <span style={{ background: diffColors[lesson.difficulty] || '#888', color: '#fff', padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{lesson.difficulty}</span>
        </div>
      </div>
      <div style={{ fontSize: 13, color: 'var(--cp-color-text-secondary)', marginBottom: 12 }}>{lesson.content}</div>
      <div style={{ fontSize: 13, color: 'var(--cp-color-text-muted)', marginBottom: 8 }}>Duration: {lesson.durationMinutes} min</div>
      {lesson.learningObjectives.length > 0 && (
        <div style={{ marginBottom: 8 }}>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-text)', marginBottom: 4 }}>Objectives</div>
          <ul style={{ margin: 0, paddingLeft: 16, fontSize: 12, color: 'var(--cp-color-text-secondary)' }}>
            {lesson.learningObjectives.map((o, i) => <li key={i}>{o}</li>)}
          </ul>
        </div>
      )}
      {lesson.materials.length > 0 && (
        <div style={{ marginBottom: 8 }}>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-text)', marginBottom: 4 }}>Materials</div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
            {lesson.materials.map((m, i) => (
              <span key={i} style={{ background: 'var(--cp-color-primary-weak)', color: 'var(--cp-color-primary)', padding: '2px 6px', borderRadius: 4, fontSize: 11 }}>{m}</span>
            ))}
          </div>
        </div>
      )}
      {lesson.activities.length > 0 && (
        <div>
          <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-text)', marginBottom: 4 }}>Activities</div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
            {lesson.activities.map((a, i) => (
              <span key={i} style={{ background: 'var(--cp-color-primary-weak)', color: 'var(--cp-color-primary)', padding: '2px 6px', borderRadius: 4, fontSize: 11 }}>{a}</span>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
