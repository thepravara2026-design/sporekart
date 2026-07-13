import { useEffect, useState } from 'react'

interface Environment {
  id: string
  name: string
  type: string
  description: string
  active: boolean
}

interface ExportData {
  environment: string
  configuration: Record<string, unknown>
  exportedAt: string
}

const ENV_TYPES = ['DEVELOPMENT', 'STAGING', 'PRODUCTION', 'DISASTER_RECOVERY', 'SANDBOX']

function EnvironmentManager() {
  const [environments, setEnvironments] = useState<Environment[]>([])
  const [exportData, setExportData] = useState<ExportData | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      fetch('/api/v1/admin/configuration/export?environment=all', { method: 'POST' }).then((r) => r.json()),
    ])
      .then(([exportResult]: [ExportData]) => {
        setExportData(exportResult)
        const envs: Environment[] = ENV_TYPES.map((type, i) => ({
          id: `env-${i}`,
          name: type.charAt(0) + type.slice(1).toLowerCase().replace(/_/g, ' '),
          type,
          description: `${type.toLowerCase().replace(/_/g, ' ')} environment`,
          active: type === 'DEVELOPMENT',
        }))
        setEnvironments(envs)
      })
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div>Loading environments...</div>

  return (
    <div>
      <h2 style={{ marginBottom: 20, color: '#c0c0e0' }}>Environment Manager</h2>

      <div className="card-grid">
        {environments.map((env) => (
          <div className="stat-card" key={env.id}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <span style={{ fontWeight: 600, color: '#c0c0e0' }}>{env.name}</span>
              <span className={`status-badge ${env.active ? 'active' : 'inactive'}`}>
                {env.active ? 'Active' : 'Inactive'}
              </span>
            </div>
            <p style={{ marginTop: 8, fontSize: '0.85rem', color: '#8080a0' }}>{env.description}</p>
            <p style={{ marginTop: 4, fontSize: '0.8rem', color: '#606080' }}>{env.type}</p>
          </div>
        ))}
      </div>

      {exportData && (
        <div className="card">
          <h3>Exported Configuration</h3>
          <p style={{ color: '#8080a0', marginBottom: 8 }}>
            Environment: {exportData.environment} | Exported: {exportData.exportedAt}
          </p>
          <pre style={{ background: '#15152a', padding: 12, borderRadius: 6, fontSize: '0.8rem', overflow: 'auto', maxHeight: 300 }}>
            {JSON.stringify(exportData.configuration, null, 2)}
          </pre>
        </div>
      )}
    </div>
  )
}

export default EnvironmentManager
