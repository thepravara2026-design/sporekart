import { useEffect, useState } from 'react';
import { api, type Provider, type ProviderInput, type Health } from '../api/client';
import StatusBadge from '../components/StatusBadge';

export default function ProviderRegistry() {
  const [items, setItems] = useState<Provider[]>([]);
  const [health, setHealth] = useState<Health | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState<ProviderInput>({ name: '', type: '', endpoint: '' });

  async function load() {
    setLoading(true);
    setError(null);
    try {
      const [list, h] = await Promise.all([
        api.get<Provider[]>('/provider-registry'),
        api.get<Health>('/provider-registry/health')
      ]);
      setItems(list);
      setHealth(h);
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void load();
  }, []);

  async function submit(e: React.FormEvent) {
    e.preventDefault();
    try {
      await api.post<Provider>('/provider-registry', form);
      setForm({ name: '', type: '', endpoint: '' });
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h1 className="page-title">Provider Registry</h1>
      <p className="page-subtitle">Registered AI/model providers and their health.</p>

      <div className="card">
        <strong>Health:</strong>{' '}
        {health ? <StatusBadge status={health.status} /> : <span className="notice">loading...</span>}
      </div>

      <div className="card">
        <h3>Register Provider</h3>
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
            Type
            <input
              value={form.type}
              onChange={(e) => setForm({ ...form, type: e.target.value })}
              required
            />
          </label>
          <label>
            Endpoint
            <input
              value={form.endpoint}
              onChange={(e) => setForm({ ...form, endpoint: e.target.value })}
              required
            />
          </label>
          <button type="submit">Register</button>
        </form>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="card">
        <h3>Providers</h3>
        {loading ? (
          <p className="notice">Loading...</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Endpoint</th>
                <th>Status</th>
                <th>Registered</th>
              </tr>
            </thead>
            <tbody>
              {items.map((p) => (
                <tr key={p.id}>
                  <td>{p.name}</td>
                  <td>{p.type}</td>
                  <td>{p.endpoint}</td>
                  <td>
                    <StatusBadge status={p.status} />
                  </td>
                  <td>{p.registeredAt}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
