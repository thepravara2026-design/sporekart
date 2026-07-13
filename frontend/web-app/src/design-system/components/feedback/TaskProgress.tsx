import React from 'react';
import { LinearProgress } from './LinearProgress';

export interface Task {
  id: string;
  label: string;
  status: 'pending' | 'processing' | 'completed' | 'error' | 'skipped';
}

export interface TaskProgressProps {
  tasks: Task[];
  overallProgress?: number;
  className?: string;
  style?: React.CSSProperties;
}

const statusColor: Record<string, string> = {
  pending: 'var(--color-neutral-300)',
  processing: 'var(--color-info-500)',
  completed: 'var(--color-success-500)',
  error: 'var(--color-danger-500)',
  skipped: 'var(--color-neutral-200)',
};

const statusBgColor: Record<string, string> = {
  pending: 'transparent',
  processing: 'var(--color-info-500)',
  completed: 'var(--color-success-500)',
  error: 'var(--color-danger-500)',
  skipped: 'transparent',
};

function TaskStatusIcon({ status }: { status: Task['status'] }) {
  if (status === 'completed') {
    return (
      <svg width="14" height="14" viewBox="0 0 16 16" fill="none" stroke="#FFFFFF" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
        <polyline points="3 8 7 12 13 4" />
      </svg>
    );
  }
  if (status === 'error') {
    return (
      <svg width="14" height="14" viewBox="0 0 16 16" fill="none" stroke="#FFFFFF" strokeWidth="2.5" strokeLinecap="round">
        <line x1="5" y1="5" x2="11" y2="11" />
        <line x1="11" y1="5" x2="5" y2="11" />
      </svg>
    );
  }
  if (status === 'processing') {
    return (
      <svg width="14" height="14" viewBox="0 0 16 16" fill="none" stroke="#FFFFFF" strokeWidth="2" strokeLinecap="round" style={{ animation: 'sk-spin 0.8s linear infinite' }}>
        <path d="M8 2v3M8 11v3M3.5 3.5l2 2M10.5 10.5l2 2M2 8h3M11 8h3M3.5 12.5l2-2M10.5 5.5l2-2" />
      </svg>
    );
  }
  return null;
}

const spinKeyframes = `
  @keyframes sk-spin {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
`;

export const TaskProgress: React.FC<TaskProgressProps> = ({
  tasks,
  overallProgress,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-md)',
    ...style,
  };

  const taskListStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-xs)',
    listStyle: 'none',
    margin: 0,
    padding: 0,
  };

  const taskItemStyle = (status: Task['status']): React.CSSProperties => ({
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
    padding: 'var(--space-inline-sm) var(--space-inline-md)',
    borderRadius: 'var(--radius-sm)',
    backgroundColor: status === 'error' ? 'var(--color-danger-50)' : 'transparent',
    fontSize: 'var(--text-body-sm)',
    color: status === 'skipped' ? 'var(--color-text-disabled)' : 'var(--color-text-primary)',
    transition: `background-color var(--duration-fast) var(--easing-standard)`,
  });

  const dotStyle = (status: Task['status']): React.CSSProperties => ({
    width: 20,
    height: 20,
    borderRadius: 'var(--radius-full)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexShrink: 0,
    border: status === 'pending' || status === 'skipped' ? `2px solid ${statusColor[status]}` : 'none',
    backgroundColor: statusBgColor[status],
  });

  return (
    <div className={`sk-task-progress ${className}`.trim()} style={containerStyle}>
      <style>{spinKeyframes}</style>
      {overallProgress !== undefined && (
        <LinearProgress value={overallProgress} size="sm" label="Overall progress" showValue />
      )}
      <ul style={taskListStyle} role="list" aria-label="Task progress list">
        {tasks.map((task) => (
          <li key={task.id} style={taskItemStyle(task.status)}>
            <span style={dotStyle(task.status)}>
              <TaskStatusIcon status={task.status} />
            </span>
            <span style={{ flex: 1 }}>{task.label}</span>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', textTransform: 'capitalize' }}>
              {task.status}
            </span>
          </li>
        ))}
      </ul>
    </div>
  );
};

TaskProgress.displayName = 'TaskProgress';
export default TaskProgress;
