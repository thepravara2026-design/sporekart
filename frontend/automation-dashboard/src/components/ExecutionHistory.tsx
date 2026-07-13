import { useEffect, useState } from 'react'

interface HistoryEntry {
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

function ExecutionHistory() {
  const [history, setHistory] = useState<HistoryEntry[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetch('/api/v1/governance/workflows')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch execution history')
        return res.json()
      })
      .then(data => {
        setHistory(data)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message)
        setLoading(false)
      })
  }, [])

  if (loading) return <div><h1>Execution History</h1><div className="empty-state">Loading execution history...</div></div>
  if (error) return <div><h1>Execution History</h1><div className="empty-state">{error}</div></div>

  const completed = history.filter(h => h.status === 'COMPLETED' || h.status === 'FAILED' || h.status === 'CANCELLED')

  return (
    <div>
      <h1>Execution History</h1>
      {completed.length === 0 ? (
        <div className="empty-state">No completed executions found.</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Workflow</th>
              <th>Status</th>
              <th>Started</th>
              <th>Completed</th>
              <th>Duration</th>
            </tr>
          </thead>
          <tbody>
            {completed.map(entry => {
              const started = entry.startedAt ? new Date(entry.startedAt).getTime() : null
              const completedTime = entry.completedAt ? new Date(entry.completedAt).getTime() : null
              const duration = started && completedTime ? Math.round((completedTime - started) / 1000) + 's' : '-'
              return (
                <tr key={entry.id}>
                  <td>{entry.id.substring(0, 8)}...</td>
                  <td>{entry.workflowName}</td>
                  <td><span className={`status-badge ${statusClass(entry.status)}`}>{entry.status}</span></td>
                  <td>{entry.startedAt ? new Date(entry.startedAt).toLocaleString() : '-'}</td>
                  <td>{entry.completedAt ? new Date(entry.completedAt).toLocaleString() : '-'}</td>
                  <td>{duration}</td>
                </tr>
              )
            })}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default ExecutionHistory
