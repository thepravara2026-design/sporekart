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

function ApprovalInbox({ onSelect }: Props) {
  const [approvals, setApprovals] = useState<Approval[]>([])
  const [loading, setLoading] = useState(true)

  const fetchApprovals = useCallback(async () => {
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
    fetchApprovals()
  }, [fetchApprovals])

  const handleAction = async (id: string, action: 'approve' | 'reject') => {
    try {
      await fetch(`/api/v1/approvals/${id}/${action}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ reviewerId: 'current', comment: '' }),
      })
      fetchApprovals()
    } catch {}
  }

  if (loading) {
    return <div className="empty-state"><h3>Loading...</h3></div>
  }

  if (approvals.length === 0) {
    return <div className="empty-state"><h3>No pending approvals</h3><p>All caught up!</p></div>
  }

  return (
    <div>
      {approvals.map((a) => (
        <div key={a.id} className="approval-card" onClick={() => onSelect(a.id)}>
          <div className="approval-card-info">
            <h3>{a.module} / {a.action}</h3>
            <p>Requested by {a.userId} &middot; {new Date(a.createdAt).toLocaleDateString()}</p>
            <span className={`status-badge ${a.status.toLowerCase()}`}>{a.status}</span>
          </div>
          <div className="approval-card-actions" onClick={(e) => e.stopPropagation()}>
            <button className="btn btn-approve" onClick={() => handleAction(a.id, 'approve')}>Approve</button>
            <button className="btn btn-reject" onClick={() => handleAction(a.id, 'reject')}>Reject</button>
          </div>
        </div>
      ))}
    </div>
  )
}

export default ApprovalInbox
