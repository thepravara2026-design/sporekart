import React, { useState } from 'react';

const REPORT_TYPES = [
  { value: 'revenue_summary', label: 'Revenue Summary' },
  { value: 'customer_analysis', label: 'Customer Analysis' },
  { value: 'training_performance', label: 'Training Performance' },
  { value: 'cultivation_yield', label: 'Cultivation Yield' },
  { value: 'full_business', label: 'Full Business Review' },
];

const FORMATS = [
  { value: 'pdf', label: 'PDF' },
  { value: 'csv', label: 'CSV' },
  { value: 'xlsx', label: 'Excel' },
  { value: 'html', label: 'HTML' },
];

const METRICS = [
  { value: 'revenue', label: 'Revenue' },
  { value: 'orders', label: 'Orders' },
  { value: 'customers', label: 'Customers' },
  { value: 'students', label: 'Students' },
  { value: 'yield', label: 'Yield' },
  { value: 'churn', label: 'Churn Rate' },
];

interface BiReportGeneratorProps {
  onGenerate?: (config: ReportConfig) => Promise<Blob | null>;
  loading?: boolean;
}

export interface ReportConfig {
  reportType: string;
  format: string;
  schedule: string;
  metrics: string[];
  dimensions: string[];
}

export const BiReportGenerator: React.FC<BiReportGeneratorProps> = ({ onGenerate, loading = false }) => {
  const [config, setConfig] = useState<ReportConfig>({
    reportType: 'revenue_summary',
    format: 'pdf',
    schedule: 'none',
    metrics: ['revenue'],
    dimensions: [],
  });
  const [blob, setBlob] = useState<Blob | null>(null);

  const updateConfig = (key: keyof ReportConfig, value: string | string[]) => {
    setConfig(prev => ({ ...prev, [key]: value }));
  };

  const toggleMetric = (metric: string) => {
    setConfig(prev => ({
      ...prev,
      metrics: prev.metrics.includes(metric)
        ? prev.metrics.filter(m => m !== metric)
        : [...prev.metrics, metric],
    }));
  };

  const toggleDimension = (dim: string) => {
    setConfig(prev => ({
      ...prev,
      dimensions: prev.dimensions.includes(dim)
        ? prev.dimensions.filter(d => d !== dim)
        : [...prev.dimensions, dim],
    }));
  };

  const handleGenerate = async () => {
    if (!onGenerate) return;
    const result = await onGenerate(config);
    if (result) setBlob(result);
  };

  return (
    <div style={{ background: '#fff', borderRadius: 8, padding: 20, border: '1px solid #e5e7eb' }}>
      <h3 style={{ margin: '0 0 20px 0', fontSize: 16, fontWeight: 600, color: '#111827' }}>Report Generator</h3>

      {/* Report Type */}
      <div style={{ marginBottom: 16 }}>
        <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: '#374151', marginBottom: 6 }}>Report Type</label>
        <select
          value={config.reportType}
          onChange={e => updateConfig('reportType', e.target.value)}
          style={{ width: '100%', padding: '8px 10px', borderRadius: 6, border: '1px solid #e5e7eb', fontSize: 13, color: '#111827' }}
        >
          {REPORT_TYPES.map(t => <option key={t.value} value={t.value}>{t.label}</option>)}
        </select>
      </div>

      {/* Format */}
      <div style={{ marginBottom: 16 }}>
        <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: '#374151', marginBottom: 6 }}>Format</label>
        <div style={{ display: 'flex', gap: 8 }}>
          {FORMATS.map(f => (
            <button
              key={f.value}
              onClick={() => updateConfig('format', f.value)}
              style={{
                padding: '6px 14px',
                borderRadius: 6,
                border: '1px solid #e5e7eb',
                background: config.format === f.value ? '#3b82f6' : '#fff',
                color: config.format === f.value ? '#fff' : '#374151',
                fontSize: 12,
                fontWeight: 500,
                cursor: 'pointer',
              }}
            >
              {f.label}
            </button>
          ))}
        </div>
      </div>

      {/* Schedule */}
      <div style={{ marginBottom: 16 }}>
        <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: '#374151', marginBottom: 6 }}>Schedule</label>
        <select
          value={config.schedule}
          onChange={e => updateConfig('schedule', e.target.value)}
          style={{ width: '100%', padding: '8px 10px', borderRadius: 6, border: '1px solid #e5e7eb', fontSize: 13, color: '#111827' }}
        >
          <option value="none">No Schedule (Generate Now)</option>
          <option value="daily">Daily</option>
          <option value="weekly">Weekly</option>
          <option value="monthly">Monthly</option>
          <option value="quarterly">Quarterly</option>
        </select>
      </div>

      {/* Metrics */}
      <div style={{ marginBottom: 16 }}>
        <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: '#374151', marginBottom: 6 }}>Metrics</label>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
          {METRICS.map(m => (
            <button
              key={m.value}
              onClick={() => toggleMetric(m.value)}
              style={{
                padding: '4px 10px',
                borderRadius: 12,
                border: '1px solid #e5e7eb',
                background: config.metrics.includes(m.value) ? '#3b82f6' : '#fff',
                color: config.metrics.includes(m.value) ? '#fff' : '#374151',
                fontSize: 11,
                cursor: 'pointer',
              }}
            >
              {m.label}
            </button>
          ))}
        </div>
      </div>

      {/* Dimensions */}
      <div style={{ marginBottom: 20 }}>
        <label style={{ display: 'block', fontSize: 12, fontWeight: 600, color: '#374151', marginBottom: 6 }}>Dimensions</label>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6 }}>
          {['product', 'category', 'region', 'segment', 'period'].map(dim => (
            <button
              key={dim}
              onClick={() => toggleDimension(dim)}
              style={{
                padding: '4px 10px',
                borderRadius: 12,
                border: '1px solid #e5e7eb',
                background: config.dimensions.includes(dim) ? '#3b82f6' : '#fff',
                color: config.dimensions.includes(dim) ? '#fff' : '#374151',
                fontSize: 11,
                cursor: 'pointer',
              }}
            >
              {dim.charAt(0).toUpperCase() + dim.slice(1)}
            </button>
          ))}
        </div>
      </div>

      {/* Generate */}
      <button
        onClick={handleGenerate}
        disabled={loading}
        style={{
          width: '100%',
          padding: '10px',
          borderRadius: 6,
          border: 'none',
          background: loading ? '#9ca3af' : '#3b82f6',
          color: '#fff',
          fontSize: 14,
          fontWeight: 600,
          cursor: loading ? 'not-allowed' : 'pointer',
          marginBottom: blob ? 12 : 0,
        }}
      >
        {loading ? 'Generating...' : 'Generate Report'}
      </button>

      {/* Download */}
      {blob && (
        <div style={{ textAlign: 'center' }}>
          <a
            href={URL.createObjectURL(blob)}
            download={`report.${config.format}`}
            style={{
              color: '#3b82f6',
              fontSize: 13,
              fontWeight: 600,
              textDecoration: 'none',
            }}
            onClick={() => setTimeout(() => URL.revokeObjectURL(URL.createObjectURL(blob)), 60000)}
          >
            Download Report ({config.format.toUpperCase()})
          </a>
        </div>
      )}
    </div>
  );
};
