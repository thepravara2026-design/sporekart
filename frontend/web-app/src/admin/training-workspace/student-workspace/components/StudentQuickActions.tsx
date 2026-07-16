import { memo, useCallback } from 'react';
import { Button } from '../../../../design-system/components/core/Button';
import { Icon } from '../../../../design-system/icons/Icon';
import { MOCK_QUICK_ACTIONS } from '../data/mockData';

interface StudentQuickActionsProps {
  onAddStudent?: () => void;
}

const StudentQuickActions = memo(function StudentQuickActions({ onAddStudent }: StudentQuickActionsProps) {
  const handleAction = useCallback((actionId: string) => {
    if (actionId === 'add-student' && onAddStudent) {
      onAddStudent();
      return;
    }
  }, [onAddStudent]);

  return (
    <div
      style={{
        display: 'flex', flexWrap: 'wrap', gap: 'var(--space-component-gap)',
        padding: 'var(--space-4)',
        background: 'var(--color-bg-surface-default)',
        borderRadius: 'var(--radius-card)',
        border: '1px solid var(--color-border-default)',
      }}
      role="toolbar"
      aria-label="Student quick actions"
    >
      {MOCK_QUICK_ACTIONS.map((action) => (
        <Button
          key={action.id}
          variant="ghost"
          size="sm"
          onClick={() => handleAction(action.id)}
          aria-label={action.label}
          title={action.description}
        >
          <Icon name={action.icon} size={14} color="currentColor" />
          <span>{action.label}</span>
        </Button>
      ))}
    </div>
  );
});

export default StudentQuickActions;
