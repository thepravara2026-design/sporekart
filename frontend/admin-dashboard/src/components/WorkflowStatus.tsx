import { WorkflowInvocation } from '../types';

interface WorkflowStatusProps {
  workflows: WorkflowInvocation[];
}

function WorkflowStatus({ workflows }: WorkflowStatusProps) {
  if (workflows.length === 0) {
    return (
      <div className="workflow-panel">
        <h3>Workflow Invocations</h3>
        <p className="empty-text">No workflow invocations.</p>
      </div>
    );
  }

  return (
    <div className="workflow-panel">
      <h3>Workflow Invocations</h3>
      <div className="workflow-list">
        {workflows.map((w) => (
          <div key={w.id} className="workflow-item">
            <div className="workflow-header">
              <span className="workflow-name">{w.name}</span>
              <span className={`status-badge ${w.status.toLowerCase()}`}>{w.status}</span>
            </div>
            <div className="workflow-meta">
              <span>Triggered by: {w.triggeredBy}</span>
              {w.result && <span>Result: {w.result}</span>}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default WorkflowStatus;
