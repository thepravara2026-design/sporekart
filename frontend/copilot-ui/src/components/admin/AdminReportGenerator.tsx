import React from 'react';
import { PerformanceReport } from '../types/admin';

interface AdminReportGeneratorProps {
  onGenerate: (config: ReportConfig) => void;
  report: PerformanceReport | null;
  onDownload: (url: string) => void;
}

export interface ReportConfig {
  title: string;
  type: string;
  metrics: string[];
  periodFrom: string;
  periodTo: string;
  format: string;
}

const availableMetrics = [
  { id: 'revenue', label: 'Revenue' },
  { id: 'orders', label: 'Orders' },
  { id: 'customers', label: 'Customers' },
  { id: 'products', label: 'Products' },
  { id: 'inventory', label: 'Inventory' },
  { id: 'growth', label: 'Growth Rate' },
  { id: 'alerts', label: 'Alerts' },
  { id: 'forecast', label: 'Forecast' }
];

const formatOptions = ['PDF', 'Excel', 'CSV'];

const AdminReportGenerator: React.FC<AdminReportGeneratorProps> = ({ onGenerate, report, onDownload }) => {
  const [title, setTitle] = React.useState('');
  const [type, setType] = React.useState('summary');
  const [format, setFormat] = React.useState('PDF');
  const [selectedMetrics, setSelectedMetrics] = React.useState<string[]>(['revenue', 'orders']);
  const [periodFrom, setPeriodFrom] = React.useState(() => {
    const d = new Date();
    d.setDate(d.getDate() - 30);
    return d.toISOString().split('T')[0];
  });
  const [periodTo, setPeriodTo] = React.useState(() => new Date().toISOString().split('T')[0]);
  const [generating, setGenerating] = React.useState(false);

  const toggleMetric = (id: string) => {
    setSelectedMetrics((prev) =>
      prev.includes(id) ? prev.filter((m) => m !== id) : [...prev, id]
    );
  };

  const handleGenerate = () => {
    setGenerating(true);
    onGenerate({
      title: title || `${type.charAt(0).toUpperCase() + type.slice(1)} Report`,
      type,
      metrics: selectedMetrics,
      periodFrom,
      periodTo,
      format
    });
    setTimeout(() => setGenerating(false), 800);
  };

  return (
    <div style={{
      backgroundColor: '#ffffff',
      borderRadius: '12px',
      padding: '24px',
      border: '1px solid #e5e7eb',
      display: 'flex',
      flexDirection: 'column',
      gap: '20px'
    }}>
      <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: 0 }}>Generate Report</h3>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
        <div>
          <label style={{ display: 'block', fontSize: '13px', fontWeight: 500, color: '#374151', marginBottom: '4px' }}>
            Report Title
          </label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="e.g., Monthly Business Summary"
            style={{
              width: '100%',
              padding: '8px 12px',
              border: '1px solid #d1d5db',
              borderRadius: '8px',
              fontSize: '13px',
              boxSizing: 'border-box'
            }}
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '13px', fontWeight: 500, color: '#374151', marginBottom: '4px' }}>
            Report Type
          </label>
          <select
            value={type}
            onChange={(e) => setType(e.target.value)}
            style={{
              width: '100%',
              padding: '8px 12px',
              border: '1px solid #d1d5db',
              borderRadius: '8px',
              fontSize: '13px',
              backgroundColor: '#ffffff'
            }}
          >
            <option value="summary">Summary</option>
            <option value="detailed">Detailed</option>
            <option value="analytics">Analytics</option>
            <option value="executive">Executive</option>
          </select>
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '13px', fontWeight: 500, color: '#374151', marginBottom: '4px' }}>
            From Date
          </label>
          <input
            type="date"
            value={periodFrom}
            onChange={(e) => setPeriodFrom(e.target.value)}
            style={{
              width: '100%',
              padding: '8px 12px',
              border: '1px solid #d1d5db',
              borderRadius: '8px',
              fontSize: '13px',
              boxSizing: 'border-box'
            }}
          />
        </div>

        <div>
          <label style={{ display: 'block', fontSize: '13px', fontWeight: 500, color: '#374151', marginBottom: '4px' }}>
            To Date
          </label>
          <input
            type="date"
            value={periodTo}
            onChange={(e) => setPeriodTo(e.target.value)}
            style={{
              width: '100%',
              padding: '8px 12px',
              border: '1px solid #d1d5db',
              borderRadius: '8px',
              fontSize: '13px',
              boxSizing: 'border-box'
            }}
          />
        </div>
      </div>

      <div>
        <label style={{ display: 'block', fontSize: '13px', fontWeight: 500, color: '#374151', marginBottom: '6px' }}>
          Metrics to Include
        </label>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
          {availableMetrics.map((metric) => (
            <button
              key={metric.id}
              onClick={() => toggleMetric(metric.id)}
              style={{
                padding: '5px 12px',
                borderRadius: '6px',
                border: `1px solid ${selectedMetrics.includes(metric.id) ? '#2563eb' : '#d1d5db'}`,
                backgroundColor: selectedMetrics.includes(metric.id) ? '#eff6ff' : '#ffffff',
                color: selectedMetrics.includes(metric.id) ? '#2563eb' : '#374151',
                fontSize: '12px',
                fontWeight: 500,
                cursor: 'pointer'
              }}
            >
              {metric.label}
            </button>
          ))}
        </div>
      </div>

      <div>
        <label style={{ display: 'block', fontSize: '13px', fontWeight: 500, color: '#374151', marginBottom: '6px' }}>
          Export Format
        </label>
        <div style={{ display: 'flex', gap: '8px' }}>
          {formatOptions.map((f) => (
            <button
              key={f}
              onClick={() => setFormat(f)}
              style={{
                padding: '7px 20px',
                borderRadius: '8px',
                border: `1px solid ${format === f ? '#2563eb' : '#d1d5db'}`,
                backgroundColor: format === f ? '#2563eb' : '#ffffff',
                color: format === f ? '#ffffff' : '#374151',
                fontSize: '13px',
                fontWeight: 500,
                cursor: 'pointer'
              }}
            >
              {f}
            </button>
          ))}
        </div>
      </div>

      <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
        <button
          onClick={handleGenerate}
          disabled={generating || selectedMetrics.length === 0}
          style={{
            padding: '10px 28px',
            backgroundColor: generating ? '#93c5fd' : '#2563eb',
            color: '#ffffff',
            border: 'none',
            borderRadius: '8px',
            fontWeight: 600,
            fontSize: '14px',
            cursor: generating ? 'not-allowed' : 'pointer'
          }}
        >
          {generating ? 'Generating...' : 'Generate Report'}
        </button>

        {report && (
          <button
            onClick={() => onDownload(report.downloadUrl)}
            style={{
              padding: '10px 20px',
              backgroundColor: '#f9fafb',
              border: '1px solid #d1d5db',
              borderRadius: '8px',
              fontWeight: 500,
              fontSize: '13px',
              color: '#2563eb',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              gap: '6px'
            }}
          >
            Download {report.title}
          </button>
        )}
      </div>

      {report && (
        <div style={{
          padding: '12px 16px',
          backgroundColor: '#f0fdf4',
          borderRadius: '8px',
          border: '1px solid #bbf7d0',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center'
        }}>
          <div>
            <span style={{ fontWeight: 600, fontSize: '14px', color: '#047857' }}>Report Generated</span>
            <span style={{ fontSize: '13px', color: '#6b7280', marginLeft: '8px' }}>
              {report.title} ({report.type}) - {new Date(report.generatedAt).toLocaleString()}
            </span>
          </div>
          <a
            href={report.downloadUrl}
            onClick={(e) => { e.preventDefault(); onDownload(report.downloadUrl); }}
            style={{ color: '#2563eb', fontWeight: 500, fontSize: '13px', cursor: 'pointer' }}
          >
            Download
          </a>
        </div>
      )}
    </div>
  );
};

export default AdminReportGenerator;
