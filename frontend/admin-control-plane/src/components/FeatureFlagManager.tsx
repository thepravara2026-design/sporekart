import { useEffect, useState } from 'react'

interface FeatureFlag {
  id: string
  key: string
  name: string
  description: string
  enabled: boolean
  environment: string
  module: string
  updatedAt: string
}

function FeatureFlagManager() {
  const [flags, setFlags] = useState<FeatureFlag[]>([])
  const [loading, setLoading] = useState(true)

  const fetchFlags = () => {
    setLoading(true)
    fetch('/api/v1/admin/feature-flags')
      .then((r) => r.json())
      .then((data) => setFlags(Array.isArray(data) ? data : []))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    fetchFlags()
  }, [])

  const handleToggle = async (flag: FeatureFlag) => {
    const res = await fetch('/api/v1/admin/feature-flags', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        key: flag.key,
        enabled: !flag.enabled,
        environment: flag.environment,
        module: flag.module,
      }),
    })
    if (res.ok) {
      fetchFlags()
    }
  }

  if (loading) return <div>Loading feature flags...</div>

  return (
    <div>
      <h2 style={{ marginBottom: 20, color: '#c0c0e0' }}>Feature Flag Manager</h2>
      <div className="card">
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Key</th>
                <th>Name</th>
                <th>Description</th>
                <th>Module</th>
                <th>Enabled</th>
                <th>Updated</th>
              </tr>
            </thead>
            <tbody>
              {flags.map((flag) => (
                <tr key={flag.id}>
                  <td>{flag.key}</td>
                  <td>{flag.name}</td>
                  <td>{flag.description}</td>
                  <td>{flag.module}</td>
                  <td>
                    <label className="toggle-switch">
                      <input
                        type="checkbox"
                        checked={flag.enabled}
                        onChange={() => handleToggle(flag)}
                      />
                      <span className="toggle-slider" />
                    </label>
                  </td>
                  <td style={{ color: '#8080a0', fontSize: '0.8rem' }}>{flag.updatedAt}</td>
                </tr>
              ))}
              {flags.length === 0 && (
                <tr>
                  <td colSpan={6} style={{ textAlign: 'center', color: '#8080a0' }}>No feature flags found</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default FeatureFlagManager
