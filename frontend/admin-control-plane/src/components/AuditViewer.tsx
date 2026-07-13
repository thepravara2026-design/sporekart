import { useEffect, useState } from 'react'

interface AuditEntry {
  id: string
  action: string
  entityType: string
  entityId: string
  performedBy: string
  details: Record<string, unknown>
  timestamp: string
}

function AuditViewer() {
  const [entries, setEntries] = useState<AuditEntry[]>([])
  const [entityFilter, setEntityFilter] = useState('')
  const [loading, setLoading] = useState(true)

  const fetchAudit = (entityId?: string) => {
    setLoading(true)
    const url = entityId
      ? `/api/v1/admin/audit?entityId=${encodeURIComponent(entityId)}`
      : '/api/v1/admin/audit'
    fetch(url)
      .then((r) => r.json())
      .then((data) => setEntries(Array.isArray(data) ? data : []))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    fetchAudit()
  }, [])

  const handleFilter = () => {
    fetchAudit(entityFilter || undefined)
  }

  if (loading) return <div>Loading audit logs...</div>

  return (
    <div>
      <h2 style={{ marginBottom: 20, color: '#c0c0e0' }}>Audit Log</h2>
      <div className="card">
        <div className="form-row" style={{ alignItems: 'flex-end' }}>
          <div className="form-group">
            <label>Filter by Entity ID</label>
            <input
              value={entityFilter}
              onChange={(e) => setEntityFilter(e.target.value)}
              placeholder="Enter entity ID..."
              onKeyDown={(e) => e.key === 'Enter' && handleFilter()}
            />
          </div>
          <button className="btn" onClick={handleFilter} style={{ marginBottom: 16, height: 38 }}>Filter</button>
        </div>
      </div>
      <div className="card">
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Action</th>
                <th>Entity Type</th>
                <th>Entity ID</th>
                <th>Performed By</th>
                <th>Details</th>
                <th>Timestamp</th>
              </tr>
            </thead>
            <tbody>
              {entries.map((entry) => (
                <tr key={entry.id}>
                  <td><span className={`status-badge ${entry.action.includes('ERROR') ? 'inactive' : 'pending'}`}>{entry.action}</span></td>
                  <td>{entry.entityType}</td>
                  <td style={{ fontFamily: 'monospace', fontSize: '0.8rem' }}>{entry.entityId}</td>
                  <td>{entry.performedBy}</td>
                  <td style={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                    {JSON.stringify(entry.details)}
                  </td>
                  <td style={{ color: '#8080a0', fontSize: '0.8rem' }}>{entry.timestamp}</td>
                </tr>
              ))}
              {entries.length === 0 && (
                <tr>
                  <td colSpan={6} style={{ textAlign: 'center', color: '#8080a0' }}>No audit entries found</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default AuditViewer
