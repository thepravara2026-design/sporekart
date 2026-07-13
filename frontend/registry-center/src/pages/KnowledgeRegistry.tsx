import { useEffect, useState } from 'react';
import { api, type KnowledgeSource, type KnowledgeInput } from '../api/client';
import StatusBadge from '../components/StatusBadge';

export default function KnowledgeRegistry() {
  const [items, setItems] = useState<KnowledgeSource[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState<KnowledgeInput>({ name: '', type: '', source: '' });

  async function load() {
    setError(null);
    try {
      const list = await api.get<KnowledgeSource[]>('/knowledge-registry');
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
      await api.post<KnowledgeSource>('/knowledge-registry', form);
      setForm({ name: '', type: '', source: '' });
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  async function sync(id: string) {
    try {
      await api.post<KnowledgeSource>(`/knowledge-registry/${id}/sync`, {});
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h1 className="page-title">Knowledge Source Registry</h1>
      <p className="page-subtitle">External knowledge sources and sync status.</p>

      <div className="card">
        <h3>Register Source</h3>
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
            <select
              value={form.type}
              onChange={(e) => setForm({ ...form, type: e.target.value })}
            >
              <option value="DOCUMENT">DOCUMENT</option>
              <option value="DATABASE">DATABASE</option>
              <option value="API">API</option>
              <option value="VECTOR">VECTOR</option>
            </select>
          </label>
          <label>
            Source
            <input
              value={form.source}
              onChange={(e) => setForm({ ...form, source: e.target.value })}
              required
            />
          </label>
          <button type="submit">Register</button>
        </form>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="card">
        <h3>Sources</h3>
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Type</th>
              <th>Source</th>
              <th>Sync</th>
              <th>Last Synced</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {items.map((s) => (
              <tr key={s.id}>
                <td>{s.name}</td>
                <td>{s.type}</td>
                <td>{s.source}</td>
                <td>
                  <StatusBadge status={s.syncStatus} />
                </td>
                <td>{s.lastSyncedAt}</td>
                <td>
                  <button className="secondary" onClick={() => sync(s.id)}>
                    Sync
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
