import { useEffect, useState } from 'react';
import { api, type UsageSummary } from '../api/client';

export default function UsageDashboard() {
  const [data, setData] = useState<UsageSummary | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api
      .get<UsageSummary>('/usage-tracking/summary')
      .then(setData)
      .catch((e) => setError((e as Error).message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="notice">Loading usage...</p>;
  if (error) return <div className="error">{error}</div>;
  if (!data) return <p className="notice">No data</p>;

  const totalDaily = data.daily.reduce((a, b) => a + b.requests, 0);
  const totalMonthly = data.monthly.reduce((a, b) => a + b.requests, 0);
  const totalCost = data.monthly.reduce((a, b) => a + b.cost, 0);

  return (
    <div>
      <h1 className="page-title">Usage & Cost Dashboard</h1>
      <p className="page-subtitle">Request volume, failures, and top contributors.</p>

      <div className="grid grid-cols-4" style={{ marginBottom: 20 }}>
        <div className="stat">
          <div className="stat-value">{totalDaily}</div>
          <div className="stat-label">Requests (today)</div>
        </div>
        <div className="stat">
          <div className="stat-value">{totalMonthly}</div>
          <div className="stat-label">Requests (month)</div>
        </div>
        <div className="stat">
          <div className="stat-value">{data.failureRate.toFixed(2)}%</div>
          <div className="stat-label">Failure rate</div>
        </div>
        <div className="stat">
          <div className="stat-value">${totalCost.toFixed(2)}</div>
          <div className="stat-label">Monthly cost</div>
        </div>
      </div>

      <div className="grid grid-cols-2">
        <div className="card">
          <h3>Top Providers</h3>
          <ul>
            {data.topProviders.map((m) => (
              <li key={m.name}>
                {m.name} — {m.value}
              </li>
            ))}
          </ul>
        </div>
        <div className="card">
          <h3>Top Models</h3>
          <ul>
            {data.topModels.map((m) => (
              <li key={m.name}>
                {m.name} — {m.value}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
