import { useEffect, useState } from 'react'

interface Job {
  id: string
  type: string
  name: string
  status: string
  retryCount: number
  maxRetries: number
  createdAt: string | null
}

const statusOptions = ['PENDING', 'RUNNING', 'COMPLETED', 'FAILED', 'CANCELLED']

function statusClass(status: string): string {
  return status.toLowerCase()
}

function JobQueue() {
  const [jobs, setJobs] = useState<Job[]>([])
  const [statusFilter, setStatusFilter] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchJobs = (status: string) => {
    setLoading(true)
    const url = status ? `/api/v1/automation/jobs?status=${status}` : '/api/v1/automation/jobs'
    fetch(url)
      .then(res => {
        if (!res.ok) throw new Error('Failed to fetch jobs')
        return res.json()
      })
      .then(data => {
        setJobs(data)
        setLoading(false)
      })
      .catch(err => {
        setError(err.message)
        setLoading(false)
      })
  }

  useEffect(() => {
    fetchJobs(statusFilter)
  }, [statusFilter])

  const handleRetry = (id: string) => {
    fetch(`/api/v1/automation/jobs/${id}/retry`, { method: 'POST' })
      .then(res => {
        if (!res.ok) throw new Error('Retry failed')
        return res.json()
      })
      .then(() => fetchJobs(statusFilter))
      .catch(err => alert(err.message))
  }

  const handleCancel = (id: string) => {
    fetch(`/api/v1/automation/jobs/${id}/cancel`, { method: 'POST' })
      .then(res => {
        if (!res.ok) throw new Error('Cancel failed')
        return res.json()
      })
      .then(() => fetchJobs(statusFilter))
      .catch(err => alert(err.message))
  }

  if (error) return <div><h1>Job Queue</h1><div className="empty-state">{error}</div></div>

  return (
    <div>
      <h1>Job Queue</h1>
      <div className="filter-bar">
        <label htmlFor="status-filter">Status:</label>
        <select
          id="status-filter"
          value={statusFilter}
          onChange={e => setStatusFilter(e.target.value)}
        >
          <option value="">All</option>
          {statusOptions.map(s => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
      </div>
      {loading ? (
        <div className="empty-state">Loading jobs...</div>
      ) : jobs.length === 0 ? (
        <div className="empty-state">No jobs found.</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Type</th>
              <th>Name</th>
              <th>Status</th>
              <th>Retries</th>
              <th>Created</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {jobs.map(job => (
              <tr key={job.id}>
                <td>{job.id.substring(0, 8)}...</td>
                <td>{job.type}</td>
                <td>{job.name}</td>
                <td><span className={`status-badge ${statusClass(job.status)}`}>{job.status}</span></td>
                <td>{job.retryCount}/{job.maxRetries}</td>
                <td>{job.createdAt ? new Date(job.createdAt).toLocaleString() : '-'}</td>
                <td>
                  {job.status === 'FAILED' && (
                    <button className="secondary" onClick={() => handleRetry(job.id)} style={{ marginRight: 8 }}>
                      Retry
                    </button>
                  )}
                  {(job.status === 'PENDING' || job.status === 'RUNNING') && (
                    <button className="danger" onClick={() => handleCancel(job.id)}>
                      Cancel
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default JobQueue
