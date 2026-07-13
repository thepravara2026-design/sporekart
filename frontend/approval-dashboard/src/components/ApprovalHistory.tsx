import { useState, useEffect, useCallback } from 'react'

interface HistoryEntry {
  id: string
  decision: string
  comment: string
  reviewerId: string
  timestamp: string
}

interface HistoryResponse {
  entries: HistoryEntry[]
  total: number
}

interface Props {
  onSelect: (id: string) => void
}

function ApprovalHistory({ onSelect }: Props) {
  const [data, setData] = useState<HistoryResponse>({ entries: [], total: 0 })
  const [searchTerm, setSearchTerm] = useState('')
  const [decisionFilter, setDecisionFilter] = useState('')
  const [loading, setLoading] = useState(true)

  const fetchHistory = useCallback(async () => {
    setLoading(true)
    try {
      const params = new URLSearchParams()
      if (searchTerm) params.append('requestId', searchTerm)
      if (decisionFilter) params.append('decision', decisionFilter)
      const res = await fetch(`/api/v1/approvals/history?${params.toString()}`)
      const json = await res.json()
      setData(json)
    } catch {
      setData({ entries: [], total: 0 })
    } finally {
      setLoading(false)
    }
  }, [searchTerm, decisionFilter])

  useEffect(() => {
    fetchHistory()
  }, [fetchHistory])

  if (loading) {
    return <div className="empty-state"><h3>Loading...</h3></div>
  }

  return (
    <div>
      <div className="filters">
        <input
          type="text"
          placeholder="Search by request ID..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <select value={decisionFilter} onChange={(e) => setDecisionFilter(e.target.value)}>
          <option value="">All Decisions</option>
          <option value="APPROVE">Approved</option>
          <option value="REJECT">Rejected</option>
          <option value="DELEGATE">Delegated</option>
          <option value="ESCALATE">Escalated</option>
          <option value="CANCEL">Cancelled</option>
        </select>
      </div>
      {data.entries.length === 0 ? (
        <div className="empty-state"><h3>No history found</h3></div>
      ) : (
        data.entries.map((entry) => (
          <div key={entry.id} className="approval-card" onClick={() => onSelect(entry.id)}>
            <div className="approval-card-info">
              <h3>Decision: {entry.decision}</h3>
              <p>By {entry.reviewerId} &middot; {entry.timestamp ? new Date(entry.timestamp).toLocaleString() : ''}</p>
              {entry.comment && <p style={{ fontSize: 13, color: '#555', marginTop: 4 }}>{entry.comment}</p>}
              <span className={`status-badge ${entry.decision.toLowerCase()}`}>{entry.decision}</span>
            </div>
          </div>
        ))
      )}
    </div>
  )
}

export default ApprovalHistory
