import { useState, useEffect } from 'react'

interface HistoryEntry {
  id: string
  assessmentId: string
  eventType: string
  description: string
  timestamp: string
}

function RiskTimeline() {
  const [entries, setEntries] = useState<HistoryEntry[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        setLoading(true)
        const res = await fetch('/api/v1/risk/history')
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        const data: { entries: HistoryEntry[]; total: number } = await res.json()
        setEntries(data.entries)
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Failed to load history'
        setError(msg)
      } finally {
        setLoading(false)
      }
    }
    fetchHistory()
  }, [])

  if (loading) return <div className="spinner">Loading timeline...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Risk Timeline</h2>
      </div>
      {entries.length === 0 ? (
        <div className="card">
          <p style={{ color: '#888' }}>No risk events recorded</p>
        </div>
      ) : (
        <div className="card">
          <div className="timeline">
            {entries.map((entry) => (
              <div key={entry.id} className="timeline-item">
                <div className="timeline-title">
                  {entry.eventType}
                  <span className={`badge ${entry.eventType.toLowerCase()}`} style={{ marginLeft: 8 }}>
                    {entry.eventType}
                  </span>
                </div>
                <div className="timeline-desc">{entry.description}</div>
                <div className="timeline-time">{entry.timestamp}</div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}

export default RiskTimeline
