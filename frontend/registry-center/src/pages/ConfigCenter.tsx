import { useEffect, useState } from 'react';
import { api, type ConfigEntry, type ConfigInput, type ConfigSnapshot } from '../api/client';
import StatusBadge from '../components/StatusBadge';

export default function ConfigCenter() {
  const [items, setItems] = useState<ConfigEntry[]>([]);
  const [snapshots, setSnapshots] = useState<ConfigSnapshot[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [editing, setEditing] = useState<ConfigEntry | null>(null);
  const [form, setForm] = useState<ConfigInput>({ key: '', value: '', environment: 'PROD' });

  async function load() {
    setError(null);
    try {
      const [cfg, snap] = await Promise.all([
        api.get<ConfigEntry[]>('/config-registry'),
        api.get<ConfigSnapshot[]>('/config-registry/snapshots')
      ]);
      setItems(cfg);
      setSnapshots(snap);
    } catch (e) {
      setError((e as Error).message);
    }
  }

  useEffect(() => {
    void load();
  }, []);

  function startEdit(c: ConfigEntry) {
    setEditing(c);
    setForm({ key: c.key, value: c.value, environment: c.environment });
  }

  async function submit(e: React.FormEvent) {
    e.preventDefault();
    try {
      if (editing) {
        await api.put<ConfigEntry>(`/config-registry/${editing.id}`, form);
        setEditing(null);
      } else {
        await api.post<ConfigEntry>('/config-registry', form);
      }
      setForm({ key: '', value: '', environment: 'PROD' });
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  async function rollback(snapshotId: string) {
    try {
      await api.post<ConfigEntry[]>(`/config-registry/snapshots/${snapshotId}/restore`, {});
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h1 className="page-title">Global Configuration Center</h1>
      <p className="page-subtitle">Environment configs with snapshots and rollback.</p>

      <div className="card">
        <h3>{editing ? `Edit ${editing.key}` : 'New Config'}</h3>
        <form onSubmit={submit}>
          <label>
            Key
            <input
              value={form.key}
              onChange={(e) => setForm({ ...form, key: e.target.value })}
              disabled={!!editing}
              required
            />
          </label>
          <label>
            Value
            <input
              value={form.value}
              onChange={(e) => setForm({ ...form, value: e.target.value })}
              required
            />
          </label>
          <label>
            Environment
            <select
              value={form.environment}
              onChange={(e) => setForm({ ...form, environment: e.target.value })}
            >
              <option value="PROD">PROD</option>
              <option value="STAGING">STAGING</option>
              <option value="DEV">DEV</option>
            </select>
          </label>
          <button type="submit">{editing ? 'Save' : 'Create'}</button>
          {editing && (
            <button
              type="button"
              className="secondary"
              onClick={() => {
                setEditing(null);
                setForm({ key: '', value: '', environment: 'PROD' });
              }}
            >
              Cancel
            </button>
          )}
        </form>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="card">
        <h3>Configs</h3>
        <table>
          <thead>
            <tr>
              <th>Key</th>
              <th>Value</th>
              <th>Env</th>
              <th>Version</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {items.map((c) => (
              <tr key={c.id}>
                <td>{c.key}</td>
                <td>{c.value}</td>
                <td>{c.environment}</td>
                <td>v{c.version}</td>
                <td>
                  <StatusBadge status={c.status} />
                </td>
                <td>
                  <button className="secondary" onClick={() => startEdit(c)}>
                    Edit
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="card">
        <h3>Snapshots</h3>
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Created</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {snapshots.map((s) => (
              <tr key={s.id}>
                <td>{s.name}</td>
                <td>{s.createdAt}</td>
                <td>
                  <button className="secondary" onClick={() => rollback(s.id)}>
                    Restore
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
