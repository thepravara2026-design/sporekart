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

function MetricsView() {
  const [metrics, setMetrics] = useState<Metric[]>([])
  const [module, setModule] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    setLoading(true)
    const url = module
      ? `/api/v1/governance/metrics?module=${encodeURIComponent(module)}`
      : '/api/v1/governance/metrics'
    fetch(url)
      .then(r => r.json())
      .then(setMetrics)
      .catch(() => setError('Failed to load metrics'))
      .finally(() => setLoading(false))
  }, [module])

  return (
    <div>
      <div className="page-header">
        <h2>Governance Metrics</h2>
      </div>

      <div className="filter-bar">
        <div className="form-group">
          <label>Module Filter</label>
          <input
            type="text"
            placeholder="Filter by module..."
            value={module}
            onChange={e => setModule(e.target.value)}
          />
        </div>
      </div>

      {error && <div className="error-message">{error}</div>}
      {loading && <div className="spinner">Loading metrics...</div>}

      <div className="list">
        {metrics.map(m => (
          <div key={m.id} className="list-item">
            <div className="list-item-header">
              <h4>{m.name}</h4>
              <span className={`badge ${m.type.toLowerCase()}`}>{m.type}</span>
            </div>
            <div className="list-item-body">
              <p>Module: {m.module}</p>
              <p>Value: {m.value}</p>
              <p>Recorded: {new Date(m.recordedAt).toLocaleString()}</p>
            </div>
          </div>
        ))}
        {!loading && metrics.length === 0 && <p>No metrics found.</p>}
      </div>
    </div>
  )
}

export default MetricsView
