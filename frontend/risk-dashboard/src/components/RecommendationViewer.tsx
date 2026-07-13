import { useState } from 'react'

interface AssessRequest {
  module: string
  action: string
  context: Record<string, unknown>
  userId: string
  roles: string[]
}

interface AssessResponse {
  assessmentId: string
  proceed: boolean
  riskLevel: string
  riskScore: number
  trustScore: number
  confidenceScore: number
  recommendation: string
  message: string
  timestamp: number
}

function RecommendationViewer() {
  const [result, setResult] = useState<AssessResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const runAssessment = async () => {
    const request: AssessRequest = {
      module: 'content-generation',
      action: 'generate',
      context: { prompt: 'Sample content generation request', priority: 'normal' },
      userId: 'dashboard-user',
      roles: ['admin', 'editor'],
    }
    try {
      setLoading(true)
      setError(null)
      const res = await fetch('/api/v1/risk/assess', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request),
      })
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data: AssessResponse = await res.json()
      setResult(data)
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Assessment failed'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  const getRiskBadgeClass = (level: string): string => {
    switch (level.toUpperCase()) {
      case 'LOW': return 'badge low'
      case 'MEDIUM': return 'badge medium'
      case 'HIGH': return 'badge high'
      case 'CRITICAL': return 'badge critical'
      default: return 'badge'
    }
  }

  return (
    <div>
      <div className="page-header">
        <h2>Recommendations</h2>
      </div>
      <button className="btn btn-primary" onClick={runAssessment} disabled={loading} style={{ marginBottom: 20 }}>
        {loading ? 'Running Assessment...' : 'Run New Assessment'}
      </button>
      {error && <div className="error-message">{error}</div>}
      {result && (
        <div>
          <div className="metrics-grid">
            <div className="metric-card">
              <h3>Risk Level</h3>
              <div>
                <span className={getRiskBadgeClass(result.riskLevel)}>{result.riskLevel}</span>
              </div>
            </div>
            <div className="metric-card">
              <h3>Risk Score</h3>
              <div className="value">{result.riskScore.toFixed(2)}</div>
            </div>
            <div className="metric-card">
              <h3>Trust Score</h3>
              <div className="value">{result.trustScore.toFixed(1)}</div>
            </div>
            <div className="metric-card">
              <h3>Confidence Score</h3>
              <div className="value">{result.confidenceScore.toFixed(1)}</div>
            </div>
          </div>
          <div className="card" style={{ marginBottom: 16 }}>
            <h3 className="section-title">Decision</h3>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 12 }}>
              <strong>Proceed:</strong>
              <span className={`badge ${result.proceed ? 'low' : 'critical'}`}>
                {result.proceed ? 'ALLOWED' : 'BLOCKED'}
              </span>
            </div>
            <p><strong>Recommendation:</strong> {result.recommendation}</p>
            <p style={{ marginTop: 8, color: '#666' }}>{result.message}</p>
            <p style={{ marginTop: 8, color: '#999', fontSize: '0.8rem' }}>
              Assessment ID: {result.assessmentId} | {new Date(result.timestamp).toLocaleString()}
            </p>
          </div>
        </div>
      )}
    </div>
  )
}

export default RecommendationViewer
