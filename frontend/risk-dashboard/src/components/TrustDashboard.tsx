import { useState } from 'react'

interface TrustData {
  assessmentId: string
  overallTrustScore: number
  factorScores: Record<string, number>
  factorReasons: Record<string, string>
  calculatedAt: string
}

function getScoreClass(score: number): string {
  if (score <= 33) return 'score-low'
  if (score <= 66) return 'score-medium'
  return 'score-high'
}

function getScoreColor(score: number): string {
  if (score <= 33) return '#ef4444'
  if (score <= 66) return '#eab308'
  return '#22c55e'
}

function TrustDashboard() {
  const [assessmentId, setAssessmentId] = useState('')
  const [trust, setTrust] = useState<TrustData | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchTrust = async () => {
    if (!assessmentId.trim()) return
    try {
      setLoading(true)
      setError(null)
      const res = await fetch(`/api/v1/risk/trust?assessmentId=${encodeURIComponent(assessmentId.trim())}`)
      if (res.status === 404) {
        setError('Trust assessment not found')
        setTrust(null)
        return
      }
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data: TrustData = await res.json()
      setTrust(data)
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to load trust data'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <div className="page-header">
        <h2>Trust Assessment</h2>
      </div>
      <div className="input-group">
        <div className="form-group">
          <label>Assessment ID</label>
          <input
            type="text"
            value={assessmentId}
            onChange={(e) => setAssessmentId(e.target.value)}
            placeholder="Enter assessment UUID"
          />
        </div>
        <button className="btn btn-primary" onClick={fetchTrust} disabled={loading || !assessmentId.trim()}>
          {loading ? 'Loading...' : 'Fetch Trust'}
        </button>
      </div>
      {error && <div className="error-message">{error}</div>}
      {trust && (
        <div>
          <div className="metrics-grid">
            <div className="metric-card">
              <h3>Overall Trust Score</h3>
              <div className={`value ${getScoreClass(trust.overallTrustScore)}`}>
                {trust.overallTrustScore.toFixed(1)}
              </div>
            </div>
            <div className="metric-card">
              <h3>Assessment ID</h3>
              <div className="value" style={{ fontSize: '1rem', wordBreak: 'break-all' }}>
                {trust.assessmentId}
              </div>
            </div>
            <div className="metric-card">
              <h3>Calculated At</h3>
              <div className="value" style={{ fontSize: '0.9rem' }}>
                {trust.calculatedAt}
              </div>
            </div>
          </div>
          <div className="card">
            <h3 className="section-title">Factor Breakdown</h3>
            {Object.entries(trust.factorScores).map(([factor, score]) => (
              <div key={factor} className="factor-bar">
                <span className="factor-label">{factor}</span>
                <div className="factor-track">
                  <div
                    className="factor-fill"
                    style={{ width: `${score}%`, background: getScoreColor(score) }}
                  />
                </div>
                <span style={{ width: 40, textAlign: 'right', fontWeight: 600, fontSize: '0.85rem' }}>
                  {score.toFixed(0)}
                </span>
              </div>
            ))}
          </div>
          {Object.keys(trust.factorReasons).length > 0 && (
            <div className="card" style={{ marginTop: 16 }}>
              <h3 className="section-title">Factor Reasons</h3>
              {Object.entries(trust.factorReasons).map(([factor, reason]) => (
                <div key={factor} style={{ marginBottom: 8, fontSize: '0.85rem' }}>
                  <strong>{factor}:</strong> {reason}
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  )
}

export default TrustDashboard
