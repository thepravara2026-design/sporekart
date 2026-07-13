import { useState, useEffect } from 'react'

interface Exception {
  id: string
  ruleId: string
  reason: string
  justification: string
  status: string
  createdAt?: string
}

function ExceptionManagement() {
  const [exceptions, setExceptions] = useState<Exception[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [ruleId, setRuleId] = useState('')
  const [reason, setReason] = useState('')
  const [justification, setJustification] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [submitMsg, setSubmitMsg] = useState<string | null>(null)

  const fetchExceptions = async () => {
    try {
      setLoading(true)
      const res = await fetch('/api/v1/compliance/exceptions')
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const data: Exception[] = await res.json()
      setExceptions(data)
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to load exceptions'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchExceptions()
  }, [])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setSubmitMsg(null)
    if (!ruleId.trim() || !reason.trim() || !justification.trim()) {
      setSubmitMsg('All fields are required.')
      return
    }
    try {
      setSubmitting(true)
      const res = await fetch('/api/v1/compliance/exceptions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ruleId: ruleId.trim(), reason: reason.trim(), justification: justification.trim() }),
      })
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      setRuleId('')
      setReason('')
      setJustification('')
      setSubmitMsg('Exception created successfully.')
      await fetchExceptions()
    } catch (err: unknown) {
      const msg = err instanceof Error ? err.message : 'Failed to create exception'
      setSubmitMsg(msg)
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) return <div className="spinner">Loading exceptions...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Exception Management</h2>
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <div className="content-card">
          <h3>Create Exception</h3>
          {submitMsg && (
            <div className={submitMsg.includes('successfully') ? 'success-message' : 'error-message'}>
              {submitMsg}
            </div>
          )}
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="ruleId">Rule ID</label>
              <input
                id="ruleId"
                type="text"
                value={ruleId}
                onChange={(e) => setRuleId(e.target.value)}
                placeholder="e.g. COMP-RULE-042"
              />
            </div>
            <div className="form-group">
              <label htmlFor="reason">Reason</label>
              <input
                id="reason"
                type="text"
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                placeholder="Reason for exception"
              />
            </div>
            <div className="form-group">
              <label htmlFor="justification">Justification</label>
              <textarea
                id="justification"
                value={justification}
                onChange={(e) => setJustification(e.target.value)}
                placeholder="Detailed justification"
              />
            </div>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Submitting...' : 'Create Exception'}
            </button>
          </form>
        </div>
        <div className="content-card">
          <h3>Existing Exceptions ({exceptions.length})</h3>
          <div className="list">
            {exceptions.map((ex) => (
              <div key={ex.id} className="list-item" style={{ cursor: 'default' }}>
                <div className="list-item-header">
                  <h4>{ex.ruleId}</h4>
                  <span className={`badge ${ex.status.toLowerCase()}`}>{ex.status}</span>
                </div>
                <div className="list-item-body">
                  <p>{ex.reason}</p>
                </div>
                <div className="list-item-detail">
                  <p>{ex.justification}</p>
                </div>
              </div>
            ))}
            {exceptions.length === 0 && <div className="spinner">No exceptions found.</div>}
          </div>
        </div>
      </div>
    </div>
  )
}

export default ExceptionManagement
