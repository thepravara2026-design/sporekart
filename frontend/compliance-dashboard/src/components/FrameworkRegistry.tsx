import { useState, useEffect } from 'react'

interface Framework {
  id: string
  name: string
  version: string
  type: string
  status: string
  description?: string
}

function FrameworkRegistry() {
  const [frameworks, setFrameworks] = useState<Framework[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [expandedId, setExpandedId] = useState<string | null>(null)

  useEffect(() => {
    const fetchFrameworks = async () => {
      try {
        setLoading(true)
        const res = await fetch('/api/v1/compliance/frameworks')
        if (!res.ok) throw new Error(`HTTP ${res.status}`)
        const data: Framework[] = await res.json()
        setFrameworks(data)
      } catch (err: unknown) {
        const msg = err instanceof Error ? err.message : 'Failed to load frameworks'
        setError(msg)
      } finally {
        setLoading(false)
      }
    }
    fetchFrameworks()
  }, [])

  if (loading) return <div className="spinner">Loading frameworks...</div>
  if (error) return <div className="error-message">{error}</div>

  return (
    <div>
      <div className="page-header">
        <h2>Framework Registry</h2>
      </div>
      <div className="list">
        {frameworks.map((fw) => (
          <div
            key={fw.id}
            className="list-item"
            onClick={() => setExpandedId(expandedId === fw.id ? null : fw.id)}
          >
            <div className="list-item-header">
              <h4>{fw.name}</h4>
              <span className={`badge ${fw.status.toLowerCase()}`}>{fw.status}</span>
            </div>
            <div className="list-item-body">
              <p>Version: {fw.version} | Type: {fw.type}</p>
            </div>
            {expandedId === fw.id && fw.description && (
              <div className="list-item-detail">
                <p>{fw.description}</p>
              </div>
            )}
          </div>
        ))}
        {frameworks.length === 0 && <div className="spinner">No frameworks found.</div>}
      </div>
    </div>
  )
}

export default FrameworkRegistry
