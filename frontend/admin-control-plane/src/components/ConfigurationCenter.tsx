import { useEffect, useState } from 'react'

interface ConfigEntry {
  id: string
  key: string
  value: string
  module: string
  environment: string
  description: string
  status: string
  version: number
  updatedAt: string
}

function ConfigurationCenter() {
  const [configs, setConfigs] = useState<ConfigEntry[]>([])
  const [editingId, setEditingId] = useState<string | null>(null)
  const [editValue, setEditValue] = useState('')
  const [loading, setLoading] = useState(true)

  const fetchConfigs = () => {
    setLoading(true)
    fetch('/api/v1/admin/configuration')
      .then((r) => r.json())
      .then((data) => setConfigs(Array.isArray(data) ? data : data ? [data] : []))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    fetchConfigs()
  }, [])

  const handleEdit = async (config: ConfigEntry) => {
    const res = await fetch('/api/v1/admin/configuration', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        key: config.key,
        value: editValue,
        module: config.module,
        environment: config.environment,
        description: config.description,
      }),
    })
    if (res.ok) {
      setEditingId(null)
      fetchConfigs()
    }
  }

  const handleExport = async () => {
    const res = await fetch('/api/v1/admin/configuration/export?environment=all', {
      method: 'POST',
    })
    if (res.ok) {
      const blob = await res.blob()
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = 'config-export.json'
      a.click()
    }
  }

  if (loading) return <div>Loading configurations...</div>

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 }}>
        <h2 style={{ color: '#c0c0e0' }}>Configuration Center</h2>
        <button className="btn" onClick={handleExport}>Export</button>
      </div>
      <div className="card">
        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Key</th>
                <th>Value</th>
                <th>Module</th>
                <th>Environment</th>
                <th>Status</th>
                <th>Version</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {configs.map((cfg) => (
                <tr key={cfg.id}>
                  <td>{cfg.key}</td>
                  <td>
                    {editingId === cfg.id ? (
                      <input
                        value={editValue}
                        onChange={(e) => setEditValue(e.target.value)}
                        style={{ width: 120 }}
                      />
                    ) : (
                      cfg.value
                    )}
                  </td>
                  <td>{cfg.module}</td>
                  <td>{cfg.environment}</td>
                  <td>
                    <span className={`status-badge ${cfg.status.toLowerCase()}`}>
                      {cfg.status}
                    </span>
                  </td>
                  <td>{cfg.version}</td>
                  <td>
                    {editingId === cfg.id ? (
                      <>
                        <button className="btn" style={{ marginRight: 8 }} onClick={() => handleEdit(cfg)}>Save</button>
                        <button className="btn-secondary" onClick={() => setEditingId(null)}>Cancel</button>
                      </>
                    ) : (
                      <button className="btn-secondary" onClick={() => { setEditingId(cfg.id); setEditValue(cfg.value) }}>Edit</button>
                    )}
                  </td>
                </tr>
              ))}
              {configs.length === 0 && (
                <tr>
                  <td colSpan={7} style={{ textAlign: 'center', color: '#8080a0' }}>No configurations found</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}

export default ConfigurationCenter
