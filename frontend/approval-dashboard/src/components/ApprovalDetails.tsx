import { useState, useEffect } from 'react'

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
  id: string
}

function ApprovalDetails({ id }: Props) {
  const [approval, setApproval] = useState<Approval | null>(null)
  const [loading, setLoading] = useState(true)
  const [comment, setComment] = useState('')

  useEffect(() => {
    const fetchApproval = async () => {
      try {
        const res = await fetch(`/api/v1/approvals/${id}`)
        if (!res.ok) return
        const data = await res.json()
        setApproval(data)
      } catch {
      } finally {
        setLoading(false)
      }
    }
    fetchApproval()
  }, [id])

  const handleAction = async (action: string) => {
    try {
      const body = action === 'cancel'
        ? { userId: 'current', reason: comment }
        : action === 'delegate'
        ? { fromReviewerId: 'current', toReviewerId: '', reason: comment }
        : { reviewerId: 'current', comment }
      await fetch(`/api/v1/approvals/${id}/${action}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      })
    } catch {}
  }

  if (loading) {
    return <div className="empty-state"><h3>Loading...</h3></div>
  }

  if (!approval) {
    return <div className="empty-state"><h3>Approval not found</h3></div>
  }

  return (
    <div className="detail-view">
      <h2>Approval Details</h2>
      <div className="detail-grid">
        <div className="detail-item">
          <label>Module</label>
          <span>{approval.module}</span>
        </div>
        <div className="detail-item">
          <label>Action</label>
          <span>{approval.action}</span>
        </div>
        <div className="detail-item">
          <label>Status</label>
          <span><span className={`status-badge ${approval.status.toLowerCase()}`}>{approval.status}</span></span>
        </div>
        <div className="detail-item">
          <label>Urgency</label>
          <span>{approval.urgency}</span>
        </div>
        <div className="detail-item">
          <label>Requested By</label>
          <span>{approval.userId}</span>
        </div>
        <div className="detail-item">
          <label>Reason</label>
          <span>{approval.reason || '-'}</span>
        </div>
        <div className="detail-item">
          <label>Created At</label>
          <span>{new Date(approval.createdAt).toLocaleString()}</span>
        </div>
      </div>
      <div className="action-bar">
        <input
          type="text"
          placeholder="Add a comment..."
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          style={{ flex: 1, padding: '8px 12px', border: '1px solid #ddd', borderRadius: 6, fontSize: 14 }}
        />
      </div>
      <div className="action-bar">
        <button className="btn btn-approve" onClick={() => handleAction('approve')}>Approve</button>
        <button className="btn btn-reject" onClick={() => handleAction('reject')}>Reject</button>
        <button className="btn btn-secondary" onClick={() => handleAction('delegate')}>Delegate</button>
        <button className="btn btn-secondary" onClick={() => handleAction('escalate')}>Escalate</button>
        <button className="btn btn-outline" onClick={() => handleAction('cancel')}>Cancel</button>
      </div>
    </div>
  )
}

export default ApprovalDetails
