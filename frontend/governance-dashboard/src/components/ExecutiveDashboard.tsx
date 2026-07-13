import { useEffect, useState } from 'react'

interface Stats {
  totalMetrics: number
  totalReports: number
  totalKPIs: number
  totalExports: number
}

interface Summary {
  metrics: Record<string, unknown>
  kpis: Record<string, unknown>
  trends: Record<string, unknown>
  generatedAt: string
}

function ExecutiveDashboard() {
  const [stats, setStats] = useState<Stats | null>(null)
  const [summary, setSummary] = useState<Summary | null>(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      fetch('/api/v1/governance/statistics').then(r => r.json()),
      fetch('/api/v1/governance/dashboard').then(r => r.json()),
    ])
      .then(([statsData, dashboardData]) => {
        setStats(statsData)
        setSummary({
          metrics: dashboardData.configuration || {},
          kpis: {},
          trends: {},
          generatedAt: dashboardData.createdAt || '',
        })
      })
      .catch(() => setError('Failed to load dashboard data'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="spinner">Loading executive dashboard...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Executive Dashboard</h2>
      </div>

      <div className="metrics-grid">
        <div className="metric-card">
          <h3>Total Metrics</h3>
          <div className="value primary">{stats?.totalMetrics ?? 0}</div>
        </div>
        <div className="metric-card">
          <h3>Reports</h3>
          <div className="value success">{stats?.totalReports ?? 0}</div>
        </div>
        <div className="metric-card">
          <h3>KPIs</h3>
          <div className="value warning">{stats?.totalKPIs ?? 0}</div>
        </div>
        <div className="metric-card">
          <h3>Exports</h3>
          <div className="value danger">{stats?.totalExports ?? 0}</div>
        </div>
      </div>

      <div className="card">
        <h3 className="section-title">Dashboard Summary</h3>
        <p>{summary?.generatedAt ? `Last updated: ${new Date(summary.generatedAt).toLocaleString()}` : 'No summary available'}</p>
      </div>
    </div>
  )
}

export default ExecutiveDashboard
