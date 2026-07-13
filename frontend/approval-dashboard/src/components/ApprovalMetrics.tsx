import { useState, useEffect } from 'react'

interface Stats {
  totalRequests: number
  pending: number
  approved: number
  rejected: number
  avgReviewTimeMs: number
  detailed: Record<string, unknown>
}

function ApprovalMetrics() {
  const [stats, setStats] = useState<Stats | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const res = await fetch('/api/v1/approvals/statistics')
        const data = await res.json()
        setStats(data)
      } catch {
      } finally {
        setLoading(false)
      }
    }
    fetchStats()
  }, [])

  if (loading) {
    return <div className="empty-state"><h3>Loading statistics...</h3></div>
  }

  if (!stats) {
    return <div className="empty-state"><h3>Failed to load statistics</h3></div>
  }

  const cards = [
    { label: 'Total Requests', value: stats.totalRequests },
    { label: 'Pending', value: stats.pending },
    { label: 'Approved', value: stats.approved },
    { label: 'Rejected', value: stats.rejected },
    { label: 'Avg Review Time', value: `${(stats.avgReviewTimeMs / 1000).toFixed(1)}s` },
  ]

  return (
    <div>
      <div className="stats-grid">
        {cards.map((card) => (
          <div key={card.label} className="stat-card">
            <h3>{card.value}</h3>
            <p>{card.label}</p>
          </div>
        ))}
      </div>
      {Object.keys(stats.detailed).length > 0 && (
        <div className="detail-view">
          <h2>Detailed Metrics</h2>
          <div className="detail-grid">
            {Object.entries(stats.detailed).map(([key, value]) => (
              <div key={key} className="detail-item">
                <label>{key}</label>
                <span>{String(value)}</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

export default ApprovalMetrics
