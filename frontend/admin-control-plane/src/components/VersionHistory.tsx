import { useState } from 'react'

interface ExportData {
  environment: string
  configuration: Record<string, unknown>
  exportedAt: string
}

interface ImportResult {
  success: boolean
  imported: number
  failed: number
  errors: string[]
  message: string
}

function VersionHistory() {
  const [exportData, setExportData] = useState<ExportData | null>(null)
  const [importResult, setImportResult] = useState<ImportResult | null>(null)
  const [importJson, setImportJson] = useState('')
  const [importEnv, setImportEnv] = useState('development')
  const [dryRun, setDryRun] = useState(true)

  const handleExport = async (env: string) => {
    const res = await fetch(`/api/v1/admin/configuration/export?environment=${env}`, {
      method: 'POST',
    })
    if (res.ok) {
      const data: ExportData = await res.json()
      setExportData(data)
    }
  }

  const handleImport = async () => {
    try {
      const config = JSON.parse(importJson)
      const res = await fetch('/api/v1/admin/configuration/import', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          configuration: config,
          environment: importEnv,
          dryRun,
        }),
      })
      if (res.ok) {
        const data: ImportResult = await res.json()
        setImportResult(data)
      }
    } catch {
      setImportResult({ success: false, imported: 0, failed: 0, errors: ['Invalid JSON'], message: 'Parse error' })
    }
  }

  return (
    <div>
      <h2 style={{ marginBottom: 20, color: '#c0c0e0' }}>Version History</h2>

      <div className="card-grid">
        <div className="card">
          <h3>Export Configuration</h3>
          <div className="form-group">
            <label>Environment</label>
            <select onChange={(e) => handleExport(e.target.value)} defaultValue="">
              <option value="" disabled>Select environment</option>
              <option value="development">Development</option>
              <option value="staging">Staging</option>
              <option value="production">Production</option>
            </select>
          </div>
          {exportData && (
            <div style={{ marginTop: 12 }}>
              <p style={{ color: '#8080a0', fontSize: '0.85rem' }}>Exported at: {exportData.exportedAt}</p>
              <pre style={{ background: '#15152a', padding: 8, borderRadius: 4, fontSize: '0.75rem', marginTop: 8, maxHeight: 200, overflow: 'auto' }}>
                {JSON.stringify(exportData.configuration, null, 2)}
              </pre>
            </div>
          )}
        </div>

        <div className="card">
          <h3>Import Configuration</h3>
          <div className="form-group">
            <label>Environment</label>
            <select value={importEnv} onChange={(e) => setImportEnv(e.target.value)}>
              <option value="development">Development</option>
              <option value="staging">Staging</option>
              <option value="production">Production</option>
            </select>
          </div>
          <div className="form-group">
            <label>Configuration JSON</label>
            <textarea
              rows={6}
              value={importJson}
              onChange={(e) => setImportJson(e.target.value)}
              placeholder='{"key": "value"}'
            />
          </div>
          <div className="form-group">
            <label className="toggle-switch" style={{ marginRight: 8 }}>
              <input type="checkbox" checked={dryRun} onChange={(e) => setDryRun(e.target.checked)} />
              <span className="toggle-slider" />
            </label>
            <span style={{ color: '#a0a0b8' }}>Dry Run</span>
          </div>
          <button className="btn" onClick={handleImport}>Import</button>
          {importResult && (
            <div style={{ marginTop: 12 }}>
              <p style={{ color: importResult.success ? '#50c878' : '#c85050' }}>
                {importResult.message}
              </p>
              <p style={{ fontSize: '0.85rem', color: '#8080a0' }}>
                Imported: {importResult.imported} | Failed: {importResult.failed}
              </p>
              {importResult.errors.length > 0 && (
                <ul style={{ fontSize: '0.8rem', color: '#c85050', marginTop: 4 }}>
                  {importResult.errors.map((e, i) => <li key={i}>{e}</li>)}
                </ul>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default VersionHistory
