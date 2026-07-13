import { useEffect, useState } from 'react';
import { api, type Prompt, type PromptVersion, type PromptInput } from '../api/client';
import StatusBadge from '../components/StatusBadge';

export default function PromptRegistry() {
  const [prompts, setPrompts] = useState<Prompt[]>([]);
  const [versions, setVersions] = useState<PromptVersion[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState<PromptInput>({ key: '', description: '', content: '' });

  async function load() {
    setError(null);
    try {
      const [ps, vs] = await Promise.all([
        api.get<Prompt[]>('/prompt-registry'),
        api.get<PromptVersion[]>('/prompt-registry/versions')
      ]);
      setPrompts(ps);
      setVersions(vs);
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
      await api.post<Prompt>('/prompt-registry', form);
      setForm({ key: '', description: '', content: '' });
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  async function rollback(versionId: string) {
    try {
      await api.post<PromptVersion>(`/prompt-registry/versions/${versionId}/rollback`, {});
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h1 className="page-title">Prompt Version Registry</h1>
      <p className="page-subtitle">Managed prompts, versions, and rollback.</p>

      <div className="card">
        <h3>New Prompt</h3>
        <form onSubmit={submit}>
          <label>
            Key
            <input
              value={form.key}
              onChange={(e) => setForm({ ...form, key: e.target.value })}
              required
            />
          </label>
          <label>
            Description
            <input
              value={form.description}
              onChange={(e) => setForm({ ...form, description: e.target.value })}
              required
            />
          </label>
          <label>
            Content
            <textarea
              value={form.content}
              onChange={(e) => setForm({ ...form, content: e.target.value })}
              rows={2}
            />
          </label>
          <button type="submit">Create</button>
        </form>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="card">
        <h3>Prompts</h3>
        <table>
          <thead>
            <tr>
              <th>Key</th>
              <th>Description</th>
              <th>Version</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {prompts.map((p) => (
              <tr key={p.id}>
                <td>{p.key}</td>
                <td>{p.description}</td>
                <td>v{p.currentVersion}</td>
                <td>
                  <StatusBadge status={p.status} />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="card">
        <h3>Versions</h3>
        <table>
          <thead>
            <tr>
              <th>Prompt</th>
              <th>Version</th>
              <th>Status</th>
              <th>Created</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {versions.map((v) => (
              <tr key={v.id}>
                <td>{v.promptKey}</td>
                <td>v{v.version}</td>
                <td>
                  <StatusBadge status={v.status} />
                </td>
                <td>{v.createdAt}</td>
                <td>
                  <button className="secondary" onClick={() => rollback(v.id)}>
                    Rollback
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
