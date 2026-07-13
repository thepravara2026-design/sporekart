import { useEffect, useState } from 'react'

interface Report {
  id: string
  type: string
  title: string
  description: string
  summary: Record<string, unknown>
  generatedAt: string
}

function ReportsView() {
  const [reports, setReports] = useState<Report[]>([])
  const [title, setTitle] = useState('')
  const [type, setType] = useState('EXECUTIVE_SUMMARY')
  const [description, setDescription] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [creating, setCreating] = useState(false)

  useEffect(() => {
    fetch('/api/v1/governance/reports')
      .then(r => r.json())
      .then(setReports)
      .catch(() => setError('Failed to load reports'))
      .finally(() => setLoading(false))
  }, [])

  const createReport = (e: React.FormEvent) => {
    e.preventDefault()
    setCreating(true)
    fetch('/api/v1/governance/reports', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ type, title, description, params: {} }),
    })
      .then(r => r.json())
      .then(report => {
        setReports(prev => [report, ...prev])
        setTitle('')
        setDescription('')
      })
      .catch(() => setError('Failed to create report'))
      .finally(() => setCreating(false))
  }

  return (
    <div>
      <div className="page-header">
        <h2>Reports</h2>
      </div>

      <div className="report-form">
        <h3 className="section-title">Generate New Report</h3>
        <form onSubmit={createReport}>
          <div className="form-group">
            <label>Title</label>
            <input
              type="text"
              value={title}
              onChange={e => setTitle(e.target.value)}
              placeholder="Report title..."
              required
            />
          </div>
          <div className="form-group">
            <label>Type</label>
            <select value={type} onChange={e => setType(e.target.value)}>
              <option value="EXECUTIVE_SUMMARY">Executive Summary</option>
              <option value="GOVERNANCE_HEALTH">Governance Health</option>
              <option value="COMPLIANCE_REPORT">Compliance Report</option>
              <option value="RISK_REPORT">Risk Report</option>
              <option value="OPERATIONAL_REPORT">Operational Report</option>
              <option value="CUSTOM">Custom</option>
            </select>
          </div>
          <div className="form-group">
            <label>Description</label>
            <textarea
              value={description}
              onChange={e => setDescription(e.target.value)}
              placeholder="Report description..."
              rows={3}
            />
          </div>
          <button type="submit" className="btn btn-primary" disabled={creating}>
            {creating ? 'Generating...' : 'Generate Report'}
          </button>
        </form>
      </div>

      {error && <div className="error-message">{error}</div>}
      {loading && <div className="spinner">Loading reports...</div>}

      <div className="list">
        {reports.map(r => (
          <div key={r.id} className="list-item">
            <div className="list-item-header">
              <h4>{r.title}</h4>
              <span className="badge on-track">{r.type.replace('_', ' ')}</span>
            </div>
            <div className="list-item-body">
              <p>{r.description || 'No description'}</p>
              <p>Generated: {new Date(r.generatedAt).toLocaleString()}</p>
            </div>
          </div>
        ))}
        {!loading && reports.length === 0 && <p>No reports found.</p>}
      </div>
    </div>
  )
}

export default ReportsView
