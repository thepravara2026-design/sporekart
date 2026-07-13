import { useState, useEffect, useCallback } from 'react'

interface Approval {
  id: string
  module: string
  action: string
  status: string
  userId: string
  reason: string
  urgency: string
  createdAt: number
}

interface Props {
  onSelect: (id: string) => void
}

function PendingApprovals({ onSelect }: Props) {
  const [approvals, setApprovals] = useState<Approval[]>([])
  const [filtered, setFiltered] = useState<Approval[]>([])
  const [moduleFilter, setModuleFilter] = useState('')
  const [statusFilter, setStatusFilter] = useState('')
  const [urgencyFilter, setUrgencyFilter] = useState('')
  const [loading, setLoading] = useState(true)

  const fetchAll = useCallback(async () => {
    setLoading(true)
    try {
      const res = await fetch('/api/v1/approvals/pending?userId=current')
      const data = await res.json()
      setApprovals(data.approvals || [])
    } catch {
      setApprovals([])
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchAll()
  }, [fetchAll])

  useEffect(() => {
    let result = [...approvals]
    if (moduleFilter) result = result.filter((a) => a.module.toLowerCase().includes(moduleFilter.toLowerCase()))
    if (statusFilter) result = result.filter((a) => a.status.toLowerCase() === statusFilter.toLowerCase())
    if (urgencyFilter) result = result.filter((a) => a.urgency.toLowerCase() === urgencyFilter.toLowerCase())
    setFiltered(result)
  }, [approvals, moduleFilter, statusFilter, urgencyFilter])

  const handleAction = async (id: string, action: 'approve' | 'reject') => {
    try {
      await fetch(`/api/v1/approvals/${id}/${action}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ reviewerId: 'current', comment: '' }),
      })
      fetchAll()
    } catch {}
  }

  if (loading) {
    return <div className="empty-state"><h3>Loading...</h3></div>
  }

  return (
    <div>
      <div className="filters">
        <input
          type="text"
          placeholder="Filter by module..."
          value={moduleFilter}
          onChange={(e) => setModuleFilter(e.target.value)}
        />
        <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
          <option value="">All Statuses</option>
          <option value="PENDING">Pending</option>
          <option value="ASSIGNED">Assigned</option>
          <option value="UNDER_REVIEW">Under Review</option>
        </select>
        <select value={urgencyFilter} onChange={(e) => setUrgencyFilter(e.target.value)}>
          <option value="">All Urgencies</option>
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
          <option value="CRITICAL">Critical</option>
        </select>
      </div>
      {filtered.length === 0 ? (
        <div className="empty-state"><h3>No matching approvals</h3></div>
      ) : (
        filtered.map((a) => (
          <div key={a.id} className="approval-card" onClick={() => onSelect(a.id)}>
            <div className="approval-card-info">
              <h3>{a.module} / {a.action}</h3>
              <p>Requested by {a.userId} &middot; {new Date(a.createdAt).toLocaleDateString()}</p>
              <span className={`status-badge ${a.status.toLowerCase()}`}>{a.status}</span>
              <span style={{ marginLeft: 8, fontSize: 12, color: '#999' }}>{a.urgency}</span>
            </div>
            <div className="approval-card-actions" onClick={(e) => e.stopPropagation()}>
              <button className="btn btn-approve" onClick={() => handleAction(a.id, 'approve')}>Approve</button>
              <button className="btn btn-reject" onClick={() => handleAction(a.id, 'reject')}>Reject</button>
            </div>
          </div>
        ))
      )}
    </div>
  )
}

export default PendingApprovals
