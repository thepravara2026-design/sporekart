import { useEffect, useState } from 'react'

interface Stats {
  totalWorkflows: number
  totalJobs: number
  jobSuccessRate: number
  retryCount: number
  escalationCount: number
  detailed: Record<string, unknown>
}

function AutomationDashboard() {
  const [stats, setStats] = useState<Stats | null>(null)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetch('/api/v1/automation/statistics')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch statistics')
        return res.json()
      })
      .then(data => setStats(data))
      .catch(err => setError(err.message))
  }, [])

  if (error) {
    return (
      <div>
        <h1>Automation Dashboard</h1>
        <div className="empty-state">{error}</div>
      </div>
    )
  }

  if (!stats) {
    return (
      <div>
        <h1>Automation Dashboard</h1>
        <div className="empty-state">Loading statistics...</div>
      </div>
    )
  }

  const cards = [
    { label: 'Total Workflows', value: stats.totalWorkflows },
    { label: 'Total Jobs', value: stats.totalJobs },
    { label: 'Job Success Rate', value: `${(stats.jobSuccessRate * 100).toFixed(1)}%` },
    { label: 'Retries', value: stats.retryCount },
    { label: 'Escalations', value: stats.escalationCount },
  ]

  return (
    <div>
      <h1>Automation Dashboard</h1>
      <div className="cards-container">
        {cards.map(card => (
          <div key={card.label} className="card">
            <div className="card-label">{card.label}</div>
            <div className="card-value">{card.value}</div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default AutomationDashboard
