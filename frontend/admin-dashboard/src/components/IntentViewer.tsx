import { Intent } from '../types';

interface IntentViewerProps {
  intent: Intent;
}

function IntentViewer({ intent }: IntentViewerProps) {
  const confidencePercent = Math.round(intent.confidence * 100);
  const priorityClass = intent.priority.toLowerCase();

  return (
    <div className="intent-card">
      <div className="intent-header">
        <span className="intent-name">{intent.name}</span>
        <span className={`status-badge priority-${priorityClass}`}>{intent.priority}</span>
      </div>
      <div className="intent-confidence">
        <label>Confidence</label>
        <div className="progress-bar">
          <div className="progress-fill" style={{ width: `${confidencePercent}%` }} />
        </div>
        <span className="confidence-value">{confidencePercent}%</span>
      </div>
      <div className="intent-entities">
        <label>Entities</label>
        {intent.entities.length === 0 ? (
          <p className="empty-text">No entities</p>
        ) : (
          <ul>
            {intent.entities.map((e, i) => (
              <li key={i}>
                <strong>{e.name}:</strong> {e.value}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default IntentViewer;
