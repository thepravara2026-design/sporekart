import { useState, useEffect } from 'react'

interface HistoryEntry {
  id: string
  decision: string
  comment: string
  reviewerId: string
  timestamp: string
}

interface Props {
  id: string
}

function ReviewerTimeline({ id }: Props) {
  const [entries, setEntries] = useState<HistoryEntry[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchTimeline = async () => {
      try {
        const res = await fetch(`/api/v1/approvals/history?requestId=${id}`)
        const data = await res.json()
        setEntries(data.entries || [])
      } catch {
        setEntries([])
      } finally {
        setLoading(false)
      }
    }
    fetchTimeline()
  }, [id])

  if (loading) {
    return <div className="detail-view"><h2>Timeline</h2><div className="empty-state"><h3>Loading...</h3></div></div>
  }

  if (entries.length === 0) {
    return (
      <div className="detail-view">
        <h2>Timeline</h2>
        <div className="empty-state"><p>No activity recorded yet</p></div>
      </div>
    )
  }

  return (
    <div className="detail-view">
      <h2>Reviewer Timeline</h2>
      <div className="timeline">
        {entries.map((entry) => (
          <div key={entry.id} className="timeline-item">
            <div className="decision">{entry.decision}</div>
            {entry.comment && <div className="comment">{entry.comment}</div>}
            <div className="meta">
              {entry.reviewerId} &middot; {entry.timestamp ? new Date(entry.timestamp).toLocaleString() : ''}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default ReviewerTimeline
