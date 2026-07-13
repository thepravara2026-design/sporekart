import { useState, useEffect } from 'react'

interface ReportSummary {
  total: number
  passed: number
  failed: number
  waived: number
}

interface Report {
  id: string
  title: string
  overallStatus: string
  generated: string
  summary: ReportSummary
}

function ReportsList() {
  const [reports, setReports] = useState<Report[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchReports = async () => {
      try {
        setLoading(true)
        const res = await fetch('/api/v1/compliance/reports')
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        const data: Report[] = await res.json()
        setReports(data)
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Failed to load reports'
        setError(msg)
      } finally {
        setLoading(false)
      }
    }
    fetchReports()
  }, [])

  const formatDate = (iso: string) => {
    const d = new Date(iso)
    return d.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  if (loading) return <div className="spinner">Loading reports...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Reports</h2>
      </div>
      <div className="list">
        {reports.map((r) => (
          <div key={r.id} className="list-item">
            <div className="list-item-header">
              <h4>{r.title}</h4>
              <span className={`badge ${r.overallStatus.toLowerCase()}`}>{r.overallStatus}</span>
            </div>
            <div className="list-item-body">
              <p>Generated: {formatDate(r.generated)}</p>
            </div>
            <div className="list-item-detail">
              <p>Total: {r.summary.total} &middot; Passed: {r.summary.passed} &middot; Failed: {r.summary.failed} &middot; Waived: {r.summary.waived}</p>
            </div>
          </div>
        ))}
        {reports.length === 0 && <div className="spinner">No reports found.</div>}
      </div>
    </div>
  )
}

export default ReportsList
