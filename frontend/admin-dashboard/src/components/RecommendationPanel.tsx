import { Recommendation } from '../types';

interface RecommendationPanelProps {
  recommendations: Recommendation[];
  onAction: (id: string) => void;
  onDismiss: (id: string) => void;
}

function RecommendationPanel({ recommendations, onAction, onDismiss }: RecommendationPanelProps) {
  if (recommendations.length === 0) {
    return (
      <div className="recommendation-panel">
        <h3>Recommendations</h3>
        <p className="empty-text">No recommendations yet.</p>
      </div>
    );
  }

  return (
    <div className="recommendation-panel">
      <h3>Recommendations</h3>
      <div className="recommendation-list">
        {recommendations.map((r) => (
          <div key={r.id} className="recommendation-card">
            <div className="rec-header">
              <span className="rec-title">{r.title}</span>
              <span className="rec-copilot">{r.copilotName}</span>
            </div>
            <p className="rec-description">{r.description}</p>
            <div className="rec-actions">
              {r.actionable && (
                <button className="btn btn-primary" onClick={() => onAction(r.id)}>
                  Apply
                </button>
              )}
              <button className="btn btn-secondary" onClick={() => onDismiss(r.id)}>
                Dismiss
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default RecommendationPanel;
