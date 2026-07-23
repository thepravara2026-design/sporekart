import { useCallback } from 'react';
import { type QuickAction } from '../types';

interface CopilotQuickActionsProps {
  actions: QuickAction[];
  recentActions?: QuickAction[];
  onAction: (action: QuickAction) => void;
}

export function CopilotQuickActions({ actions, recentActions, onAction }: CopilotQuickActionsProps) {
  const handleKeyDown = useCallback(
    (action: QuickAction) => (e: React.KeyboardEvent) => {
      if (e.key === 'Enter' || e.key === ' ') {
        e.preventDefault();
        onAction(action);
      }
    },
    [onAction],
  );

  return (
    <div className="cp-quick-actions" role="region" aria-label="Quick actions">
      <div className="cp-quick-actions__grid" role="list">
        {actions.map((a) => (
          <button
            key={a.id}
            type="button"
            className="cp-quick-actions__btn"
            onClick={() => onAction(a)}
            onKeyDown={handleKeyDown(a)}
            role="listitem"
            aria-label={a.label}
          >
            <span className="cp-quick-actions__icon" aria-hidden="true">{a.icon}</span>
            <span className="cp-quick-actions__label">{a.label}</span>
          </button>
        ))}
      </div>
      {recentActions && recentActions.length > 0 && (
        <div className="cp-quick-actions__recent">
          <span className="cp-quick-actions__recent-label">Recent</span>
          <div className="cp-quick-actions__recent-list" role="list">
            {recentActions.map((a) => (
              <button
                key={a.id}
                type="button"
                className="cp-quick-actions__recent-btn"
                onClick={() => onAction(a)}
                role="listitem"
                aria-label={a.label}
              >
                {a.icon} {a.label}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
