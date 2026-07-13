import { useEffect, useState } from 'react';
import { api, type ApiEntry, type ApiInput } from '../api/client';
import StatusBadge from '../components/StatusBadge';

export default function ApiRegistry() {
  const [items, setItems] = useState<ApiEntry[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState<ApiInput>({ name: '', path: '', method: 'GET' });

  async function load() {
    setError(null);
    try {
      const list = await api.get<ApiEntry[]>('/api-registry');
      setItems(list);
    } catch (e) {
      setError((e as Error).message);
    }
  }

  useEffect(() => {
    void load();
  }, []);

  async function submit(e: React.FormEvent) {
    e.preventDefault();
    try {
      await api.post<ApiEntry>('/api-registry', form);
      setForm({ name: '', path: '', method: 'GET' });
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h1 className="page-title">API Registry Viewer</h1>
      <p className="page-subtitle">Exposed APIs, health, and dependencies.</p>

      <div className="card">
        <h3>Register API</h3>
        <form onSubmit={submit}>
          <label>
            Name
            <input
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              required
            />
          </label>
          <label>
            Path
            <input
              value={form.path}
              onChange={(e) => setForm({ ...form, path: e.target.value })}
              required
            />
          </label>
          <label>
            Method
            <select
              value={form.method}
              onChange={(e) => setForm({ ...form, method: e.target.value })}
            >
              <option value="GET">GET</option>
              <option value="POST">POST</option>
              <option value="PUT">PUT</option>
              <option value="DELETE">DELETE</option>
            </select>
          </label>
          <button type="submit">Register</button>
        </form>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="card">
        <h3>APIs</h3>
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Method</th>
              <th>Path</th>
              <th>Health</th>
              <th>Dependencies</th>
            </tr>
          </thead>
          <tbody>
            {items.map((a) => (
              <tr key={a.id}>
                <td>{a.name}</td>
                <td>{a.method}</td>
                <td>{a.path}</td>
                <td>
                  <StatusBadge status={a.health} />
                </td>
                <td>{a.dependencies.join(', ') || '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
