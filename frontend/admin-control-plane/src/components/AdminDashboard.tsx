import { useEffect, useState } from 'react'

interface HealthData {
  status: string
  service: string
  timestamp: number
  details: Record<string, unknown>
}

interface AuditEntry {
  id: string
  action: string
  entityType: string
  entityId: string
  performedBy: string
  details: Record<string, unknown>
  timestamp: string
}

interface Stats {
  totalOperations: number
  configChanges: number
  rollbackCount: number
  featureFlagChanges: number
  operationDistribution: Record<string, number>
}

function AdminDashboard() {
  const [health, setHealth] = useState<HealthData | null>(null)
  const [stats, setStats] = useState<Stats | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      fetch('/api/v1/admin/health').then((r) => r.json()),
      fetch('/api/v1/admin/audit').then((r) => r.json()),
    ])
      .then(([healthData, auditData]: [HealthData, AuditEntry[]]) => {
        setHealth(healthData)
        const distribution: Record<string, number> = {}
        let configChanges = 0
        let rollbackCount = 0
        let featureFlagChanges = 0
        auditData.forEach((entry) => {
          distribution[entry.action] = (distribution[entry.action] || 0) + 1
          if (entry.action.startsWith('CONFIG')) configChanges++
          if (entry.action === 'CONFIG_ROLLBACK') rollbackCount++
          if (entry.action === 'FEATURE_FLAG_CHANGE') featureFlagChanges++
        })
        setStats({
          totalOperations: auditData.length,
          configChanges,
          rollbackCount,
          featureFlagChanges,
          operationDistribution: distribution,
        })
      })
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div>Loading dashboard...</div>

  const maxDistValue = stats
    ? Math.max(...Object.values(stats.operationDistribution), 1)
    : 1

  return (
    <div>
      <h2 style={{ marginBottom: 20, color: '#c0c0e0' }}>Admin Dashboard</h2>
      <div className="card-grid">
        <div className="stat-card">
          <div className="stat-value">{stats?.totalOperations ?? 0}</div>
          <div className="stat-label">Total Operations</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{stats?.configChanges ?? 0}</div>
          <div className="stat-label">Config Changes</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{stats?.rollbackCount ?? 0}</div>
          <div className="stat-label">Rollbacks</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{stats?.featureFlagChanges ?? 0}</div>
          <div className="stat-label">Feature Flag Changes</div>
        </div>
      </div>

      {health && (
        <div className="card">
          <h3>Service Health</h3>
          <p>
            Status:{' '}
            <span className={`status-badge ${health.status === 'UP' ? 'active' : 'inactive'}`}>
              {health.status}
            </span>
          </p>
          <p style={{ marginTop: 8, color: '#8080a0' }}>{health.service}</p>
        </div>
      )}

      {stats && Object.keys(stats.operationDistribution).length > 0 && (
        <div className="chart-container">
          <h3>Operation Distribution</h3>
          {Object.entries(stats.operationDistribution).map(([key, value]) => (
            <div className="chart-bar" key={key}>
              <span className="bar-label">{key.replace(/_/g, ' ')}</span>
              <div
                className="bar-fill"
                style={{ width: `${(value / maxDistValue) * 100}%` }}
              />
              <span className="bar-value">{value}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default AdminDashboard
