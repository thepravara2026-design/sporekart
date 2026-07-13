import { useEffect, useState } from 'react';
import { api, type EventType, type EventInput } from '../api/client';

export default function EventCatalog() {
  const [items, setItems] = useState<EventType[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState<EventInput>({ name: '', topic: '' });

  async function load() {
    setError(null);
    try {
      const list = await api.get<EventType[]>('/event-catalog');
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
      await api.post<EventType>('/event-catalog', form);
      setForm({ name: '', topic: '' });
      await load();
    } catch (e) {
      setError((e as Error).message);
    }
  }

  return (
    <div>
      <h1 className="page-title">Event Catalog</h1>
      <p className="page-subtitle">Domain events, producers, consumers, and subscriptions.</p>

      <div className="card">
        <h3>Register Event</h3>
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
            Topic
            <input
              value={form.topic}
              onChange={(e) => setForm({ ...form, topic: e.target.value })}
              required
            />
          </label>
          <button type="submit">Register</button>
        </form>
      </div>

      {error && <div className="error">{error}</div>}

      <div className="card">
        <h3>Events</h3>
        <table>
          <thead>
            <tr>
              <th>Name</th>
              <th>Topic</th>
              <th>Producers</th>
              <th>Consumers</th>
              <th>Subscriptions</th>
            </tr>
          </thead>
          <tbody>
            {items.map((ev) => (
              <tr key={ev.id}>
                <td>{ev.name}</td>
                <td>{ev.topic}</td>
                <td>{ev.producers.join(', ') || '-'}</td>
                <td>{ev.consumers.join(', ') || '-'}</td>
                <td>{ev.subscriptions}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
