import { useEffect, useState } from 'react';
import { api, type Capability, type CapabilityInput } from '../api/client';
import StatusBadge from '../components/StatusBadge';

export default function CapabilityExplorer() {
  const [items, setItems] = useState<Capability[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState<CapabilityInput>({ name: '', category: '' });

  async function load() {
    setError(null);
    try {
      const list = await api.get<Capability[]>('/capability-discovery');
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
      await api.post<Capability>('/capability-discovery', form);
      setForm({ name: '', category: '' });
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h1 className="page-title">Capability Explorer</h1>
      <p className="page-subtitle">Discoverable platform capabilities and availability.</p>

      <div className="card">
        <h3>Register Capability</h3>
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
            Category
            <input
              value={form.category}
              onChange={(e) => setForm({ ...form, category: e.target.value })}
              required
            />
          </label>
          <button type="submit">Register</button>
        </form>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="card">
        <h3>Capabilities</h3>
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Category</th>
              <th>Available</th>
              <th>Discovered Via</th>
            </tr>
          </thead>
          <tbody>
            {items.map((c) => (
              <tr key={c.id}>
                <td>{c.name}</td>
                <td>{c.category}</td>
                <td>{c.available ? <StatusBadge status="available" /> : <StatusBadge status="unavailable" />}</td>
                <td>{c.discoveredVia}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
