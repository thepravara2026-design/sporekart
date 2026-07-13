import { useEffect, useState } from 'react'

interface Metric {
  id: string
  name: string
  module: string
  type: string
  value: number
  labels: Record<string, unknown>
  recordedAt: string
}

function ComplianceStatusView() {
  const [metrics, setMetrics] = useState<Metric[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch('/api/v1/governance/metrics?module=compliance')
      .then(r => r.json())
      .then(setMetrics)
      .catch(() => setError('Failed to load compliance metrics'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="spinner">Loading compliance status...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Compliance Status</h2>
      </div>

      <div className="metrics-grid">
        {metrics.map(m => (
          <div key={m.id} className="metric-card">
            <h3>{m.name}</h3>
            <div className="value primary">{m.value}</div>
            <div className="list-item-body">
              <p>Type: {m.type}</p>
              <p>Recorded: {new Date(m.recordedAt).toLocaleString()}</p>
            </div>
          </div>
        ))}
        {metrics.length === 0 && <p>No compliance metrics found.</p>}
      </div>
    </div>
  )
}

export default ComplianceStatusView
