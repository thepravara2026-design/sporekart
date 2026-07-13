import { useState, useEffect } from 'react'

interface Violation {
  id: string
  severity: string
  module: string
  description: string
  remediated: boolean
}

type SeverityFilter = 'ALL' | 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW' | 'INFO'

function ViolationViewer() {
  const [violations, setViolations] = useState<Violation[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [filter, setFilter] = useState<SeverityFilter>('ALL')

  useEffect(() => {
    const fetchViolations = async () => {
      try {
        setLoading(true)
        const res = await fetch('/api/v1/compliance/violations')
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        const data: Violation[] = await res.json()
        setViolations(data)
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Failed to load violations'
        setError(msg)
      } finally {
        setLoading(false)
      }
    }
    fetchViolations()
  }, [])

  const filtered = filter === 'ALL'
    ? violations
    : violations.filter((v) => v.severity === filter)

  if (loading) return <div className="spinner">Loading violations...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Violations</h2>
      </div>
      <div className="filter-bar">
        <label htmlFor="sev-filter">Severity:</label>
        <select
          id="sev-filter"
          value={filter}
          onChange={(e) => setFilter(e.target.value as SeverityFilter)}
        >
          <option value="ALL">All</option>
          <option value="CRITICAL">Critical</option>
          <option value="HIGH">High</option>
          <option value="MEDIUM">Medium</option>
          <option value="LOW">Low</option>
          <option value="INFO">Info</option>
        </select>
      </div>
      <div className="list">
        {filtered.map((v) => (
          <div key={v.id} className="list-item">
            <div className="list-item-header">
              <h4>{v.module}</h4>
              <div>
                <span className={`badge ${v.severity.toLowerCase()}`}>{v.severity}</span>
                {v.remediated && <span className="badge passed" style={{ marginLeft: 8 }}>Remediated</span>}
              </div>
            </div>
            <div className="list-item-body">
              <p>{v.description}</p>
            </div>
          </div>
        ))}
        {filtered.length === 0 && <div className="spinner">No violations match the filter.</div>}
      </div>
    </div>
  )
}

export default ViolationViewer
