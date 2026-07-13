import React from 'react';
import { LinearProgress } from './LinearProgress';

export interface Stage {
  label: string;
  progress: number;
}

export interface ProgressiveLoaderProps {
  progress: number;
  stages?: Stage[];
  message?: string;
  className?: string;
  style?: React.CSSProperties;
}

export const ProgressiveLoader: React.FC<ProgressiveLoaderProps> = ({
  progress,
  stages,
  message,
  className = '',
  style,
}) => {
  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-md)',
    ...style,
  };

  const stageListStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    gap: 'var(--space-stack-sm)',
    listStyle: 'none',
    margin: 0,
    padding: 0,
  };

  const stageItemStyle: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
    gap: 'var(--space-inline-sm)',
  };

  const stageLabelStyle = (stage: Stage): React.CSSProperties => ({
    flex: 1,
    fontSize: 'var(--text-body-sm)',
    color: stage.progress >= 100 ? 'var(--color-text-success)' : 'var(--color-text-primary)',
    fontWeight: stage.progress > 0 && stage.progress < 100 ? 'var(--weight-medium)' : 'var(--weight-normal)',
  });

  const stagePercentStyle: React.CSSProperties = {
    fontSize: 'var(--text-caption)',
    color: 'var(--color-text-secondary)',
    minWidth: 36,
    textAlign: 'right' as const,
  };

  const activeStage = stages?.find((s) => s.progress > 0 && s.progress < 100);

  return (
    <div className={`sk-progressive-loader ${className}`.trim()} style={containerStyle} role="progressbar" aria-valuenow={progress} aria-valuemin={0} aria-valuemax={100} aria-label={message || 'Progress'}>
      <div>
        <LinearProgress value={progress} size="md" showValue />
        {message && (
          <p style={{ margin: 'var(--space-stack-xs) 0 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
            {message}
          </p>
        )}
      </div>
      {stages && stages.length > 0 && (
        <ul style={stageListStyle}>
          {stages.map((stage, index) => (
            <li key={index} style={stageItemStyle}>
              <span style={{
                width: 8,
                height: 8,
                borderRadius: 'var(--radius-full)',
                backgroundColor: stage.progress >= 100 ? 'var(--color-success-500)' : stage.progress > 0 ? 'var(--color-bg-primary-default)' : 'var(--color-neutral-200)',
                flexShrink: 0,
              }} />
              <span style={stageLabelStyle(stage)}>{stage.label}</span>
              <span style={stagePercentStyle}>{stage.progress}%</span>
            </li>
          ))}
        </ul>
      )}
      {activeStage && (
        <LinearProgress
          value={activeStage.progress}
          size="sm"
          label={`Current: ${activeStage.label}`}
          showValue
        />
      )}
    </div>
  );
};

ProgressiveLoader.displayName = 'ProgressiveLoader';
export default ProgressiveLoader;
