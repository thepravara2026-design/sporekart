import { useState } from 'react'

interface ConfidenceData {
  assessmentId: string
  overallConfidence: number
  factorScores: Record<string, number>
  explanation: string
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

function ConfidenceDashboard() {
  const [assessmentId, setAssessmentId] = useState('')
  const [confidence, setConfidence] = useState<ConfidenceData | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchConfidence = async () => {
    if (!assessmentId.trim()) return
    try {
      setLoading(true)
      setError(null)
      const res = await fetch(`/api/v1/risk/confidence?assessmentId=${encodeURIComponent(assessmentId.trim())}`)
      if (res.status === 404) {
        setError('Confidence assessment not found')
        setConfidence(null)
        return
      }
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data: ConfidenceData = await res.json()
      setConfidence(data)
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to load confidence data'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <div className="page-header">
        <h2>Confidence Assessment</h2>
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
        <button className="btn btn-primary" onClick={fetchConfidence} disabled={loading || !assessmentId.trim()}>
          {loading ? 'Loading...' : 'Fetch Confidence'}
        </button>
      </div>
      {error && <div className="error-message">{error}</div>}
      {confidence && (
        <div>
          <div className="metrics-grid">
            <div className="metric-card">
              <h3>Overall Confidence</h3>
              <div className={`value ${getScoreClass(confidence.overallConfidence)}`}>
                {confidence.overallConfidence.toFixed(1)}
              </div>
            </div>
            <div className="metric-card">
              <h3>Assessment ID</h3>
              <div className="value" style={{ fontSize: '1rem', wordBreak: 'break-all' }}>
                {confidence.assessmentId}
              </div>
            </div>
            <div className="metric-card">
              <h3>Calculated At</h3>
              <div className="value" style={{ fontSize: '0.9rem' }}>
                {confidence.calculatedAt}
              </div>
            </div>
          </div>
          <div className="card">
            <h3 className="section-title">Factor Breakdown</h3>
            {Object.entries(confidence.factorScores).map(([factor, score]) => (
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
          {confidence.explanation && (
            <div className="card" style={{ marginTop: 16 }}>
              <h3 className="section-title">Explanation</h3>
              <p style={{ color: '#666', fontSize: '0.9rem', lineHeight: 1.6 }}>{confidence.explanation}</p>
            </div>
          )}
        </div>
      )}
    </div>
  )
}

export default ConfidenceDashboard
