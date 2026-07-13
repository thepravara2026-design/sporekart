import { useEffect, useState } from 'react'

interface WorkflowExecution {
  id: string
  workflowName: string
  status: string
  result: Record<string, unknown> | null
  startedAt: string | null
  completedAt: string | null
}

function statusClass(status: string): string {
  return status.toLowerCase()
}

function WorkflowMonitor() {
  const [workflows, setWorkflows] = useState<WorkflowExecution[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetch('/api/v1/governance/workflows')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch workflows')
        return res.json()
      })
      .then(data => {
        setWorkflows(data)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message)
        setLoading(false)
      })
  }, [])

  if (loading) return <div><h1>Workflow Monitor</h1><div className="empty-state">Loading workflows...</div></div>
  if (error) return <div><h1>Workflow Monitor</h1><div className="empty-state">{error}</div></div>

  return (
    <div>
      <h1>Workflow Monitor</h1>
      {workflows.length === 0 ? (
        <div className="empty-state">No workflow executions found.</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Status</th>
              <th>Started</th>
              <th>Completed</th>
            </tr>
          </thead>
          <tbody>
            {workflows.map(wf => (
              <tr key={wf.id}>
                <td>{wf.id.substring(0, 8)}...</td>
                <td>{wf.workflowName}</td>
                <td><span className={`status-badge ${statusClass(wf.status)}`}>{wf.status}</span></td>
                <td>{wf.startedAt ? new Date(wf.startedAt).toLocaleString() : '-'}</td>
                <td>{wf.completedAt ? new Date(wf.completedAt).toLocaleString() : '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default WorkflowMonitor
