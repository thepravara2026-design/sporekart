import { memo } from 'react';
import type { QuestionCategory } from '../types';
import { DIFFICULTY_LABELS } from '../types';

interface QuestionCategoryCardProps {
  category: QuestionCategory;
}

export const QuestionCategoryCard = memo(function QuestionCategoryCard({ category }: QuestionCategoryCardProps) {
  return (
    <div style={{
      padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
      border: '1px solid var(--color-border-default)',
      background: 'var(--color-bg-surface-default)',
      display: 'flex', flexDirection: 'column', gap: 8,
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ fontWeight: 'var(--weight-semibold)', fontSize: 'var(--text-body-sm)' }}>{category.name}</div>
          <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{category.description}</div>
        </div>
        <span style={{
          padding: '2px 8px', borderRadius: 'var(--radius-full)',
          fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-medium)',
          backgroundColor: '#2563eb18', color: '#2563eb', whiteSpace: 'nowrap',
        }}>
          {DIFFICULTY_LABELS[category.difficulty]}
        </span>
      </div>
      <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-body-sm)' }}>
        <span>Questions: <strong>{category.questionCount}</strong></span>
        <span>Marks: <strong>{category.totalMarks}</strong></span>
      </div>
      <div>
        <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4 }}>Learning Outcomes:</div>
        <ul style={{ margin: 0, paddingLeft: 16, fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
          {category.learningOutcomes.map((outcome, i) => (
            <li key={i}>{outcome}</li>
          ))}
        </ul>
      </div>
      <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
        {category.tags.map((tag) => (
          <span key={tag} style={{
            padding: '1px 6px', borderRadius: 'var(--radius-full)',
            fontSize: 'var(--text-caption)', background: 'var(--color-bg-skeleton-base)',
            color: 'var(--color-text-tertiary)',
          }}>
            {tag}
          </span>
        ))}
      </div>
    </div>
  );
});
