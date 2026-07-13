import { useEffect, useState } from 'react'

interface Module {
  id: string
  type: string
  name: string
  description: string
  enabled: boolean
  version: string
}

const MODULE_NAMES: Record<string, string> = {
  GOVERNANCE_FOUNDATION: 'Governance Foundation',
  POLICY_ENGINE: 'Policy Engine',
  DECISION_ENGINE: 'Decision Engine',
  APPROVAL_PLATFORM: 'Approval Platform',
  COMPLIANCE_FRAMEWORK: 'Compliance Framework',
  RISK_FRAMEWORK: 'Risk Framework',
  ANALYTICS_PLATFORM: 'Analytics Platform',
}

function ModuleManager() {
  const [modules, setModules] = useState<Module[]>([])
  const [loading, setLoading] = useState(true)

  const fetchModules = () => {
    setLoading(true)
    fetch('/api/v1/admin/modules')
      .then((r) => r.json())
      .then((data) => setModules(Array.isArray(data) ? data : []))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    fetchModules()
  }, [])

  const handleToggle = async (mod: Module) => {
    const res = await fetch('/api/v1/admin/modules', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        module: mod.type,
        enabled: !mod.enabled,
      }),
    })
    if (res.ok) {
      fetchModules()
    }
  }

  if (loading) return <div>Loading modules...</div>

  return (
    <div>
      <h2 style={{ marginBottom: 20, color: '#c0c0e0' }}>Module Manager</h2>
      <div className="card">
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Module</th>
                <th>Description</th>
                <th>Version</th>
                <th>Enabled</th>
              </tr>
            </thead>
            <tbody>
              {modules.map((mod) => (
                <tr key={mod.id}>
                  <td>{MODULE_NAMES[mod.type] || mod.name}</td>
                  <td>{mod.description}</td>
                  <td style={{ color: '#8080a0' }}>{mod.version}</td>
                  <td>
                    <label className="toggle-switch">
                      <input
                        type="checkbox"
                        checked={mod.enabled}
                        onChange={() => handleToggle(mod)}
                      />
                      <span className="toggle-slider" />
                    </label>
                  </td>
                </tr>
              ))}
              {modules.length === 0 && (
                <tr>
                  <td colSpan={4} style={{ textAlign: 'center', color: '#8080a0' }}>No modules found</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default ModuleManager
