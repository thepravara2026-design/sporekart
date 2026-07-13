import { useState, useEffect } from 'react'

interface Statistics {
  totalValidations: number
  passRate: number
  failures: number
  violations: number
  avgLatency: number
}

function ComplianceDashboard() {
  const [stats, setStats] = useState<Statistics | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true)
        const res = await fetch('/api/v1/compliance/statistics')
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        const data: Statistics = await res.json()
        setStats(data)
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Failed to load statistics'
        setError(msg)
      } finally {
        setLoading(false)
      }
    }
    fetchStats()
  }, [])

  if (loading) return <div className="spinner">Loading dashboard...</div>
  if (error) return <div className="error-message">{error}</div>
  if (!stats) return null

  return (
    <div>
      <div className="page-header">
        <h2>Dashboard</h2>
      </div>
      <div className="metrics-grid">
        <div className="metric-card">
          <h3>Total Validations</h3>
          <div className="value">{stats.totalValidations}</div>
        </div>
        <div className="metric-card">
          <h3>Pass Rate</h3>
          <div className="value pass">{(stats.passRate * 100).toFixed(1)}%</div>
        </div>
        <div className="metric-card">
          <h3>Failures</h3>
          <div className="value fail">{stats.failures}</div>
        </div>
        <div className="metric-card">
          <h3>Violations</h3>
          <div className="value fail">{stats.violations}</div>
        </div>
        <div className="metric-card">
          <h3>Avg Latency</h3>
          <div className="value">{stats.avgLatency.toFixed(1)}ms</div>
        </div>
      </div>
    </div>
  )
}

export default ComplianceDashboard
