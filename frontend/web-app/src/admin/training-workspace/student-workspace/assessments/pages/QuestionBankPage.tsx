import { useAssessments } from '../state/AssessmentContext';
import { QuestionCategoryCard } from '../components/QuestionCategoryCard';
import { EmptyState } from '../components/EmptyStates';

export function QuestionBankPage() {
  const { questionCategories } = useAssessments();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Question Bank</h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 'var(--space-component-gap)' }}>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Categories</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{questionCategories.length}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Questions</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{questionCategories.reduce((sum, c) => sum + c.questionCount, 0)}</div>
        </div>
        <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border-default)' }}>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>Total Marks</div>
          <div style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)' }}>{questionCategories.reduce((sum, c) => sum + c.totalMarks, 0)}</div>
        </div>
      </div>

      {questionCategories.length === 0 ? (
        <EmptyState type="noQuestionCategories" />
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {questionCategories.map((cat) => (
            <QuestionCategoryCard key={cat.id} category={cat} />
          ))}
        </div>
      )}
    </div>
  );
}
