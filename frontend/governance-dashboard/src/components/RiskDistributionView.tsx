import { useEffect, useState } from 'react'

interface Trend {
  id: string
  name: string
  module: string
  dataPoints: number[]
  timestamps: string[]
  direction: string
  changePercentage: number
}

function RiskDistributionView() {
  const [trends, setTrends] = useState<Trend[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetch('/api/v1/governance/trends')
      .then(r => r.json())
      .then(setTrends)
      .catch(() => setError('Failed to load risk distribution data'))
      .finally(() => setLoading(false))
  }, [])

  const directionIcon = (dir: string) => {
    switch (dir) {
      case 'UP': return '\u2191'
      case 'DOWN': return '\u2193'
      case 'STABLE': return '\u2192'
      default: return '~'
    }
  }

  const directionClass = (dir: string) => {
    switch (dir) {
      case 'UP': return 'value danger'
      case 'DOWN': return 'value success'
      default: return 'value'
    }
  }

  if (loading) return <div className="spinner">Loading risk distribution...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Risk Distribution</h2>
      </div>

      <div className="list">
        {trends.map(t => (
          <div key={t.id} className="list-item">
            <div className="list-item-header">
              <h4>{t.name}</h4>
              <span className={`value ${directionClass(t.direction)}`}>
                {directionIcon(t.direction)} {t.changePercentage.toFixed(1)}%
              </span>
            </div>
            <div className="list-item-body">
              <p>Module: {t.module}</p>
              <p>Direction: {t.direction}</p>
              <p>Data points: {t.dataPoints.length}</p>
            </div>
          </div>
        ))}
        {!loading && trends.length === 0 && <p>No trend data available.</p>}
      </div>
    </div>
  )
}

export default RiskDistributionView
