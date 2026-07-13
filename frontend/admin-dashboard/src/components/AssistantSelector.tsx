import { Assistant } from '../types';

interface AssistantSelectorProps {
  assistants: Assistant[];
  selectedId?: string;
  onSelect: (id: string) => void;
}

function AssistantSelector({ assistants, selectedId, onSelect }: AssistantSelectorProps) {
  return (
    <div className="assistant-selector">
      <h3>Copilots</h3>
      <div className="copilot-grid">
        {assistants.map((a) => (
          <div
            key={a.id}
            className={`copilot-card ${a.id === selectedId ? 'selected' : ''} ${a.status === 'Inactive' ? 'inactive' : ''}`}
            onClick={() => onSelect(a.id)}
          >
            <div className="copilot-card-header">
              <span className="copilot-name">{a.name}</span>
              <span className={`status-badge ${a.status.toLowerCase()}`}>{a.status}</span>
            </div>
            <p className="copilot-description">{a.description}</p>
            <div className="copilot-stats">
              <span>{a.usageCount} uses</span>
              <span>{a.avgLatency}ms</span>
              <span>{a.successRate}%</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default AssistantSelector;
