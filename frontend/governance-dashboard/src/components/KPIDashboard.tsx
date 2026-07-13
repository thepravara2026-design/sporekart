import { useEffect, useState } from 'react'

interface Kpi {
  id: string
  name: string
  description: string
  module: string
  currentValue: number
  targetValue: number
  status: string
  calculatedAt: string
}

function KPIDashboard() {
  const [kpis, setKpis] = useState<Kpi[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch('/api/v1/governance/kpis')
      .then(r => r.json())
      .then(setKpis)
      .catch(() => setError('Failed to load KPIs'))
      .finally(() => setLoading(false))
  }, [])

  const statusBadge = (status: string) => {
    switch (status) {
      case 'ON_TRACK': return 'badge on-track'
      case 'AT_RISK': return 'badge at-risk'
      case 'CRITICAL': return 'badge critical'
      default: return 'badge not-available'
    }
  }

  if (loading) return <div className="spinner">Loading KPIs...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>KPI Dashboard</h2>
      </div>

      <div className="kpi-grid">
        {kpis.map(k => (
          <div key={k.id} className={`kpi-card ${k.status.toLowerCase().replace('_', '-')}`}>
            <h4>{k.name}</h4>
            <div className="kpi-module">{k.module}</div>
            <div className="kpi-value">
              <span className="kpi-current">{k.currentValue.toLocaleString()}</span>
              <span className="kpi-target">/ {k.targetValue.toLocaleString()}</span>
            </div>
            <span className={statusBadge(k.status)}>{k.status.replace('_', ' ')}</span>
            {k.description && (
              <div className="kpi-description">{k.description}</div>
            )}
          </div>
        ))}
        {kpis.length === 0 && <p>No KPIs found.</p>}
      </div>
    </div>
  )
}

export default KPIDashboard
