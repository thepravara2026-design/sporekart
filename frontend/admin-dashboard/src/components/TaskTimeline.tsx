import { Task } from '../types';

const TASKS: Task[] = [
  { id: '1', name: 'Analyze sales data', description: 'Running quarterly sales analysis', status: 'Completed', duration: 3400, createdAt: new Date().toISOString() },
  { id: '2', name: 'Generate report', description: 'Compiling insights into PDF report', status: 'Executing', createdAt: new Date().toISOString() },
  { id: '3', name: 'Send notifications', description: 'Email digest to stakeholders', status: 'Queued', createdAt: new Date().toISOString() },
  { id: '4', name: 'Sync CRM', description: 'Two-way sync with Salesforce', status: 'Planning', createdAt: new Date().toISOString() },
  { id: '5', name: 'Backup data', description: 'Full backup to cold storage', status: 'Pending', createdAt: new Date().toISOString() },
];

function TaskTimeline() {
  return (
    <div className="task-timeline-page">
      <h2>Task Timeline</h2>
      <div className="timeline">
        {TASKS.map((task) => (
          <div key={task.id} className={`timeline-item ${task.status.toLowerCase()}`}>
            <div className="timeline-dot" />
            <div className="timeline-content">
              <div className="task-header">
                <span className="task-name">{task.name}</span>
                <span className={`status-badge ${task.status.toLowerCase()}`}>{task.status}</span>
              </div>
              <p className="task-description">{task.description}</p>
              {task.duration && <span className="task-duration">{task.duration}ms</span>}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TaskTimeline;
