import { ExecutionLog } from '../types';

interface ExecutionLogsProps {
  logs: ExecutionLog[];
}

function ExecutionLogs({ logs }: ExecutionLogsProps) {
  if (logs.length === 0) {
    return (
      <div className="execution-logs-panel">
        <h3>Execution Logs</h3>
        <p className="empty-text">No execution logs available.</p>
      </div>
    );
  }

  return (
    <div className="execution-logs-panel">
      <h3>Execution Logs</h3>
      <div className="logs-table-wrapper">
        <table className="logs-table">
          <thead>
            <tr>
              <th>Action</th>
              <th>Copilot</th>
              <th>Latency</th>
              <th>Status</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((log) => (
              <tr key={log.id}>
                <td>{log.action}</td>
                <td>{log.copilot}</td>
                <td>{log.latency}ms</td>
                <td>
                  <span className={`status-badge ${log.status.toLowerCase()}`}>
                    {log.status}
                  </span>
                </td>
                <td>{new Date(log.timestamp).toLocaleTimeString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default ExecutionLogs;
