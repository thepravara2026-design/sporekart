import { useEffect, useState } from 'react'

interface Lifecycle {
  id: string
  name: string
  entityType: string
  initialState: string
  transitions: Record<string, Record<string, string>>
  config: Record<string, unknown>
}

function LifecycleViewer() {
  const [lifecycles, setLifecycles] = useState<Lifecycle[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [expanded, setExpanded] = useState<string | null>(null)

  useEffect(() => {
    fetch('/api/v1/governance/lifecycle')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch lifecycle definitions')
        return res.json()
      })
      .then(data => {
        setLifecycles(data)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message)
        setLoading(false)
      })
  }, [])

  if (loading) return <div><h1>Lifecycle Viewer</h1><div className="empty-state">Loading lifecycle definitions...</div></div>
  if (error) return <div><h1>Lifecycle Viewer</h1><div className="empty-state">{error}</div></div>

  return (
    <div>
      <h1>Lifecycle Viewer</h1>
      {lifecycles.length === 0 ? (
        <div className="empty-state">No lifecycle definitions found.</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Entity Type</th>
              <th>Initial State</th>
              <th>Transitions</th>
            </tr>
          </thead>
          <tbody>
            {lifecycles.map(lc => (
              <tr key={lc.id}>
                <td>{lc.name}</td>
                <td>{lc.entityType}</td>
                <td><span className="status-badge pending">{lc.initialState}</span></td>
                <td>
                  <button
                    className="secondary"
                    onClick={() => setExpanded(expanded === lc.id ? null : lc.id)}
                  >
                    {expanded === lc.id ? 'Hide' : 'Show'} Transitions
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {expanded && lifecycles.filter(lc => lc.id === expanded).map(lc => (
        <div key={lc.id} style={{ marginTop: 16 }}>
          <h3>Transitions for {lc.name}</h3>
          <table>
            <thead>
              <tr>
                <th>From State</th>
                <th>Event</th>
                <th>To State</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(lc.transitions).flatMap(([fromState, events]) =>
                Object.entries(events).map(([event, toState]) => (
                  <tr key={`${fromState}-${event}`}>
                    <td><span className="status-badge pending">{fromState}</span></td>
                    <td>{event}</td>
                    <td><span className="status-badge completed">{toState}</span></td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      ))}
    </div>
  )
}

export default LifecycleViewer
