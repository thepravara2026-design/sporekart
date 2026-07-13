import { useEffect, useState } from 'react'

interface Schedule {
  id: string
  name: string
  jobType: string
  frequency: string
  cronExpression: string
  active: boolean
  nextRunAt: string | null
}

const jobTypes = ['POLICY_REFRESH', 'CONFIG_SYNC', 'HEALTH_CHECK', 'AUDIT_CLEANUP', 'REPORT_GENERATION', 'COMPLIANCE_SCAN']
const frequencies = ['ONCE', 'HOURLY', 'DAILY', 'WEEKLY', 'MONTHLY', 'CRON_EXPRESSION']

function SchedulerConsole() {
  const [schedules, setSchedules] = useState<Schedule[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ name: '', jobType: 'HEALTH_CHECK', frequency: 'DAILY', cronExpression: '', params: '' })

  const fetchSchedules = () => {
    setLoading(true)
    fetch('/api/v1/automation/schedules')
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch schedules')
        return res.json()
      })
      .then(data => {
        setSchedules(data)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message)
        setLoading(false)
      })
  }

  useEffect(() => {
    fetchSchedules()
  }, [])

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    fetch('/api/v1/automation/jobs', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        type: form.jobType,
        name: form.name,
        params: form.params ? JSON.parse(form.params) : {},
      }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Failed to create schedule')
        return res.json()
      })
      .then(() => {
        setShowForm(false)
        setForm({ name: '', jobType: 'HEALTH_CHECK', frequency: 'DAILY', cronExpression: '', params: '' })
        fetchSchedules()
      })
      .catch(err => alert(err.message))
  }

  if (error) return <div><h1>Scheduler</h1><div className="empty-state">{error}</div></div>

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
        <h1>Scheduler</h1>
        <button className="primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'New Schedule'}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              value={form.name}
              onChange={e => setForm({ ...form, name: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Job Type</label>
            <select value={form.jobType} onChange={e => setForm({ ...form, jobType: e.target.value })}>
              {jobTypes.map(jt => <option key={jt} value={jt}>{jt}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Frequency</label>
            <select value={form.frequency} onChange={e => setForm({ ...form, frequency: e.target.value })}>
              {frequencies.map(f => <option key={f} value={f}>{f}</option>)}
            </select>
          </div>
          <div className="form-group">
            <label>Cron Expression</label>
            <input
              type="text"
              value={form.cronExpression}
              onChange={e => setForm({ ...form, cronExpression: e.target.value })}
              placeholder="0 0 * * *"
            />
          </div>
          <div className="form-group">
            <label>Params (JSON)</label>
            <input
              type="text"
              value={form.params}
              onChange={e => setForm({ ...form, params: e.target.value })}
              placeholder='{"key": "value"}'
            />
          </div>
          <div className="form-actions">
            <button type="submit" className="primary">Create</button>
          </div>
        </form>
      )}

      {loading ? (
        <div className="empty-state">Loading schedules...</div>
      ) : schedules.length === 0 ? (
        <div className="empty-state">No schedules found.</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Job Type</th>
              <th>Frequency</th>
              <th>Cron</th>
              <th>Active</th>
              <th>Next Run</th>
            </tr>
          </thead>
          <tbody>
            {schedules.map(s => (
              <tr key={s.id}>
                <td>{s.name}</td>
                <td>{s.jobType}</td>
                <td>{s.frequency}</td>
                <td>{s.cronExpression || '-'}</td>
                <td>
                  <span className={`status-badge ${s.active ? 'completed' : 'cancelled'}`}>
                    {s.active ? 'ACTIVE' : 'INACTIVE'}
                  </span>
                </td>
                <td>{s.nextRunAt ? new Date(s.nextRunAt).toLocaleString() : '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default SchedulerConsole
