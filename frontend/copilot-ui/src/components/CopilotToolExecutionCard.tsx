import { useState, useCallback } from 'react';
import type { ToolExecution } from '../types';

interface CopilotToolExecutionCardProps {
  execution: ToolExecution;
  onRetry?: (execution: ToolExecution) => void;
}

const statusIcons: Record<ToolExecution['status'], string> = {
  pending: '\u23F3',
  running: '\u27F3',
  success: '\u2713',
  error: '\u2717',
};

const statusColors: Record<ToolExecution['status'], string> = {
  pending: 'var(--cp-color-text-muted)',
  running: 'var(--cp-color-info)',
  success: 'var(--cp-color-success)',
  error: 'var(--cp-color-danger)',
};

export function CopilotToolExecutionCard({ execution, onRetry }: CopilotToolExecutionCardProps) {
  const [expanded, setExpanded] = useState(false);

  const handleRetry = useCallback(() => {
    onRetry?.(execution);
  }, [execution, onRetry]);

  const handleKeyDown = useCallback(
    (e: React.KeyboardEvent) => {
      if (e.key === 'Enter' || e.key === ' ') {
        e.preventDefault();
        setExpanded((p) => !p);
      }
    },
    [],
  );

  return (
    <div className={`cp-tool-card cp-tool-card--${execution.status}`} role="region" aria-label={`Tool: ${execution.toolName}`}>
      <div
        className="cp-tool-card__header"
        onClick={() => setExpanded((p) => !p)}
        onKeyDown={handleKeyDown}
        role="button"
        tabIndex={0}
        aria-expanded={expanded}
      >
        <span className="cp-tool-card__icon" style={{ color: statusColors[execution.status] }} aria-hidden="true">
          {statusIcons[execution.status]}
        </span>
        <span className="cp-tool-card__name">{execution.toolName}</span>
        <span className={`cp-tool-card__status cp-tool-card__status--${execution.status}`}>
          {execution.status}
        </span>
        {execution.executionTimeMs != null && (
          <span className="cp-tool-card__time">{execution.executionTimeMs}ms</span>
        )}
        <span className="cp-tool-card__chevron" aria-hidden="true">
          {expanded ? '\u25BC' : '\u25B6'}
        </span>
      </div>
      {expanded && (
        <div className="cp-tool-card__body">
          <div className="cp-tool-card__section">
            <span className="cp-tool-card__section-label">Input</span>
            <pre className="cp-tool-card__code">{JSON.stringify(execution.input, null, 2)}</pre>
          </div>
          {execution.output && (
            <div className="cp-tool-card__section">
              <span className="cp-tool-card__section-label">Output</span>
              <pre className="cp-tool-card__code">{execution.output}</pre>
            </div>
          )}
          {execution.error && (
            <div className="cp-tool-card__section cp-tool-card__section--error">
              <span className="cp-tool-card__section-label">Error</span>
              <pre className="cp-tool-card__code">{execution.error}</pre>
            </div>
          )}
          {execution.status === 'error' && onRetry && (
            <button
              type="button"
              className="cp-tool-card__retry"
              onClick={handleRetry}
              aria-label={`Retry ${execution.toolName}`}
            >
              Retry
            </button>
          )}
        </div>
      )}
    </div>
  );
}
