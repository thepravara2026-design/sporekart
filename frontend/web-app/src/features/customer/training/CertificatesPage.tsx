import React, { useState } from 'react';
import { MOCK_CERTIFICATES } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const CertificatesPage: React.FC = () => {
  const [certificates] = useState(MOCK_CERTIFICATES);
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  
  // Modal popups for verification simulation
  const [activeCertId, setActiveCertId] = useState<string | null>(null);

  const handleDownload = (certTitle: string) => {
    setToastMessage(`Generating high-resolution PDF for "${certTitle}"...`);
    setTimeout(() => setToastMessage(null), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="info" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          My Certifications
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Verifiable credentials generated upon successful completion of SporeKart syllabus modules.
        </p>
      </div>

      {certificates.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>🎗️</div>
          <div>
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>No certificates earned yet</h3>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Finish study modules at 100% to generate credentials.</p>
          </div>
        </div>
      ) : (
        <Grid columns="repeat(auto-fill, minmax(340px, 1fr))" gap="24px">
          {certificates.map((cert) => (
            <Card 
              key={cert.id}
              variant="outlined"
              padding="lg"
              style={{
                background: 'linear-gradient(to bottom, #fff, #f8fafc)',
                border: '2px solid var(--color-border-default)',
                borderRadius: 'var(--radius-lg)',
                display: 'flex',
                flexDirection: 'column',
                gap: '16px',
                position: 'relative',
              }}
            >
              {/* Certificate Border Details */}
              <div 
                style={{ 
                  display: 'flex', 
                  justifyContent: 'space-between', 
                  alignItems: 'center', 
                  borderBottom: '1px dashed var(--color-border-default)', 
                  paddingBottom: '12px' 
                }}
              >
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-primary)', fontWeight: 'bold' }}>
                  SPOREKART ACADEMY
                </span>
                <div style={{ color: 'var(--color-text-warning, #d97706)' }}>
                  <Icon name="award" size={24} color="currentColor" />
                </div>
              </div>

              <div>
                <span style={{ fontSize: '10px', color: 'var(--color-text-secondary)', textTransform: 'uppercase', display: 'block' }}>
                  CERTIFICATE OF COMPLETION
                </span>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '4px 0' }}>
                  {cert.title}
                </h3>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '6px 0 0' }}>
                  Completed Course: <strong>{cert.courseTitle}</strong>
                </p>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '4px' }}>
                  Issue Date: {cert.completionDate}
                </span>
              </div>

              <div 
                style={{ 
                  borderTop: '1px solid var(--color-border-default)', 
                  paddingTop: '12px',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  flexWrap: 'wrap',
                  gap: '8px'
                }}
              >
                <div>
                  <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)', display: 'block' }}>VERIFICATION ID</span>
                  <strong style={{ fontSize: '10px', fontFamily: 'monospace', color: 'var(--color-text-primary)' }}>{cert.verificationCode}</strong>
                </div>

                <div style={{ display: 'flex', gap: '8px' }}>
                  <button
                    type="button"
                    className="cw-btn cw-btn--outlined cw-btn--xs"
                    onClick={() => setActiveCertId(cert.id)}
                    style={{ fontSize: '10px', display: 'flex', alignItems: 'center', gap: '4px' }}
                  >
                    <Icon name="qr-code" size={12} color="currentColor" />
                    Verify
                  </button>
                  <button
                    type="button"
                    className="cw-btn cw-btn--primary cw-btn--xs"
                    onClick={() => handleDownload(cert.title)}
                    style={{ fontSize: '10px' }}
                  >
                    Download PDF
                  </button>
                </div>
              </div>

              {/* QR Verification simulator Modal Popup overlay */}
              {activeCertId === cert.id && (
                <div 
                  style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    background: 'rgba(255, 255, 255, 0.95)',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    padding: '20px',
                    borderRadius: 'var(--radius-lg)',
                    zIndex: 10
                  }}
                >
                  {/* Mock QR SVG */}
                  <svg width="100" height="100" viewBox="0 0 100 100" style={{ background: '#000', padding: 6, borderRadius: 4 }}>
                    {/* Stylized QR dots */}
                    <rect x="0" y="0" width="30" height="30" fill="#fff" />
                    <rect x="5" y="5" width="20" height="20" fill="#000" />
                    <rect x="70" y="0" width="30" height="30" fill="#fff" />
                    <rect x="75" y="5" width="20" height="20" fill="#000" />
                    <rect x="0" y="70" width="30" height="30" fill="#fff" />
                    <rect x="5" y="75" width="20" height="20" fill="#000" />
                    <rect x="40" y="40" width="20" height="20" fill="#fff" />
                    {/* random patterns */}
                    <rect x="45" y="10" width="10" height="10" fill="#fff" />
                    <rect x="15" y="45" width="10" height="10" fill="#fff" />
                    <rect x="75" y="45" width="15" height="15" fill="#fff" />
                    <rect x="45" y="75" width="10" height="15" fill="#fff" />
                  </svg>
                  <strong style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-primary)', marginTop: '12px' }}>
                    Verifiable Credentials Logged
                  </strong>
                  <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)', textAlign: 'center', margin: '4px 0 12px' }}>
                    Scan QR to verify signature on sporekart.com/verify/{cert.verificationCode}
                  </span>
                  <button 
                    type="button" 
                    className="cw-btn cw-btn--outlined cw-btn--xs"
                    onClick={() => setActiveCertId(null)}
                  >
                    Close
                  </button>
                </div>
              )}
            </Card>
          ))}
        </Grid>
      )}

    </div>
  );
};
export default CertificatesPage;
