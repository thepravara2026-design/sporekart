import { useState, useEffect } from 'react'

interface RiskStats {
  totalAssessments: number
  averageRiskScore: number
  riskDistribution: Record<string, number>
  trustTrends: Record<string, number>
  confidenceTrends: Record<string, number>
  recommendationCounts: Record<string, number>
}

interface HistoryEntry {
  id: string
  assessmentId: string
  eventType: string
  description: string
  timestamp: string
}

function RiskDashboard() {
  const [stats, setStats] = useState<RiskStats | null>(null)
  const [recent, setRecent] = useState<HistoryEntry[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        const [statsRes, historyRes] = await Promise.all([
          fetch('/api/v1/risk/statistics'),
          fetch('/api/v1/risk/history'),
        ])
        if (!statsRes.ok) throw new Error(`Statistics HTTP ${statsRes.status}`)
        if (!historyRes.ok) throw new Error(`History HTTP ${historyRes.status}`)
        const statsData: RiskStats = await statsRes.json()
        const historyData: { entries: HistoryEntry[] } = await historyRes.json()
        setStats(statsData)
        setRecent(historyData.entries.slice(-5).reverse())
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Failed to load data'
        setError(msg)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  if (loading) return <div className="spinner">Loading dashboard...</div>
  if (error) return <div className="error-message">{error}</div>
  if (!stats) return null

  const highRiskCount = stats.riskDistribution['HIGH'] ?? 0
  const criticalRiskCount = stats.riskDistribution['CRITICAL'] ?? 0

  return (
    <div>
      <div className="page-header">
        <h2>Risk Dashboard</h2>
      </div>
      <div className="metrics-grid">
        <div className="metric-card">
          <h3>Total Assessments</h3>
          <div className="value">{stats.totalAssessments}</div>
        </div>
        <div className="metric-card">
          <h3>Avg Risk Score</h3>
          <div className="value">{stats.averageRiskScore.toFixed(2)}</div>
        </div>
        <div className="metric-card">
          <h3>High Risk</h3>
          <div className="value high">{highRiskCount}</div>
        </div>
        <div className="metric-card">
          <h3>Critical Risk</h3>
          <div className="value high">{criticalRiskCount}</div>
        </div>
      </div>
      <div className="card">
        <h3 className="section-title">Recent Assessments</h3>
        {recent.length === 0 ? (
          <p style={{ color: '#888' }}>No recent assessments</p>
        ) : (
          <div className="list">
            {recent.map((entry) => (
              <div key={entry.id} className="list-item">
                <div className="list-item-header">
                  <h4>{entry.eventType}</h4>
                  <span className={`badge ${entry.eventType.toLowerCase()}`}>
                    {entry.eventType}
                  </span>
                </div>
                <div className="list-item-body">
                  <p>{entry.description}</p>
                  <p style={{ color: '#999', fontSize: '0.8rem' }}>{entry.timestamp}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}

export default RiskDashboard
