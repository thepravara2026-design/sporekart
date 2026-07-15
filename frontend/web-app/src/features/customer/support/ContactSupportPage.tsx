import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';
import { Grid } from '../../../design-system/components/layout/Grid';

export const ContactSupportPage: React.FC = () => {
  const navigate = useNavigate();
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Form states
  const [subject, setSubject] = useState('');
  const [category, setCategory] = useState('Cultivation Help');
  const [priority, setPriority] = useState('Medium');
  const [description, setDescription] = useState('');
  const [channel, setChannel] = useState('Email');
  const [submitting, setSubmitting] = useState(false);
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!subject.trim() || !description.trim()) {
      setToastMessage('Please fill out the subject and description fields.');
      setTimeout(() => setToastMessage(null), 3000);
      return;
    }

    setSubmitting(true);
    setTimeout(() => {
      setSubmitting(false);
      setSubmitted(true);
      setToastMessage('Support ticket created successfully!');
      setTimeout(() => setToastMessage(null), 3000);
    }, 1200);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone={submitted ? "success" : "info"} onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Back to dashboard */}
      <div>
        <button
          type="button"
          onClick={() => navigate('/dashboard/support')}
          style={{
            background: 'none',
            border: 'none',
            padding: 0,
            fontSize: 'var(--text-body-sm)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-secondary)',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
          }}
        >
          <Icon name="arrow-left" size={16} color="currentColor" />
          Back to Support Dashboard
        </button>
      </div>

      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Contact Support Expert
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Open a new ticket. Our laboratory scientists and shipping experts respond within 24 business hours.
        </p>
      </div>

      <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Form area */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {!submitted ? (
            <Card variant="outlined" padding="lg">
              <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                
                {/* Subject */}
                <div>
                  <label htmlFor="subject" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Issue Subject
                  </label>
                  <input
                    id="subject"
                    type="text"
                    placeholder="e.g. Delayed delivery or mold growth assistance request"
                    value={subject}
                    onChange={(e) => setSubject(e.target.value)}
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-md)',
                      border: '1px solid var(--color-border-default)',
                      fontSize: 'var(--text-body-sm)',
                      background: 'var(--color-bg-surface-default)',
                      color: 'var(--color-text-primary)',
                      outline: 'none',
                    }}
                  />
                </div>

                {/* Grid Category and Priority */}
                <Grid columns="1fr 1fr" gap="16px">
                  <div>
                    <label htmlFor="category" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                      Category
                    </label>
                    <select
                      id="category"
                      value={category}
                      onChange={(e) => setCategory(e.target.value)}
                      style={{
                        width: '100%',
                        padding: '8px 12px',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--color-border-default)',
                        fontSize: 'var(--text-body-sm)',
                        background: 'var(--color-bg-surface-default)',
                        color: 'var(--color-text-primary)',
                        outline: 'none',
                      }}
                    >
                      <option value="Order & Shipping">Order & Shipping</option>
                      <option value="Cultivation Help">Cultivation Help</option>
                      <option value="Lab tech">Lab tech</option>
                      <option value="Billing & Account">Billing & Account</option>
                    </select>
                  </div>

                  <div>
                    <label htmlFor="priority" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                      Priority
                    </label>
                    <select
                      id="priority"
                      value={priority}
                      onChange={(e) => setPriority(e.target.value)}
                      style={{
                        width: '100%',
                        padding: '8px 12px',
                        borderRadius: 'var(--radius-md)',
                        border: '1px solid var(--color-border-default)',
                        fontSize: 'var(--text-body-sm)',
                        background: 'var(--color-bg-surface-default)',
                        color: 'var(--color-text-primary)',
                        outline: 'none',
                      }}
                    >
                      <option value="Low">Low (General Query)</option>
                      <option value="Medium">Medium (Help Needed)</option>
                      <option value="High">High (Urgent / Blocked)</option>
                    </select>
                  </div>
                </Grid>

                {/* Description */}
                <div>
                  <label htmlFor="description" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Describe your issue
                  </label>
                  <textarea
                    id="description"
                    rows={5}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    placeholder="Specify batch codes, autoclave run times, or inoculation environments for faster support audits..."
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-md)',
                      border: '1px solid var(--color-border-default)',
                      fontSize: 'var(--text-body-sm)',
                      background: 'var(--color-bg-surface-default)',
                      color: 'var(--color-text-primary)',
                      outline: 'none',
                      resize: 'vertical'
                    }}
                  />
                </div>

                {/* Contact Preferred Channel */}
                <div>
                  <label style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Preferred Contact Channel
                  </label>
                  <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
                    {['Email', 'Phone Callback', 'WhatsApp'].map((ch) => (
                      <label key={ch} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}>
                        <input
                          type="radio"
                          name="channel"
                          value={ch}
                          checked={channel === ch}
                          onChange={(e) => setChannel(e.target.value)}
                          style={{ cursor: 'pointer' }}
                        />
                        {ch}
                      </label>
                    ))}
                  </div>
                </div>

                {/* Upload Placeholder */}
                <div>
                  <label style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Attach Screenshots or Laboratory Photos
                  </label>
                  <div style={{ border: '2px dashed var(--color-border-default)', borderRadius: 'var(--radius-md)', padding: '24px 0', textAlign: 'center', background: '#fafafa', cursor: 'pointer' }}>
                    <Icon name="upload" size={24} color="var(--color-text-secondary)" style={{ marginBottom: '8px' }} />
                    <span style={{ display: 'block', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 'bold' }}>
                      Drag files here or click to browse
                    </span>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '4px' }}>
                      PNG, JPG, PDF up to 15MB
                    </span>
                  </div>
                </div>

                <button
                  type="submit"
                  disabled={submitting}
                  className="cw-btn cw-btn--primary cw-btn--lg"
                  style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
                >
                  {submitting ? 'Submitting ticket...' : 'Open Support Ticket'}
                </button>

              </form>
            </Card>
          ) : (
            <Card variant="outlined" padding="lg" style={{ textAlign: 'center', padding: '40px 20px' }}>
              <div 
                style={{ 
                  width: '56px', 
                  height: '56px', 
                  borderRadius: '50%', 
                  background: 'var(--color-bg-primary-weak)', 
                  color: 'var(--color-primary)', 
                  display: 'flex', 
                  alignItems: 'center', 
                  justifyContent: 'center', 
                  fontSize: '28px',
                  margin: '0 auto 16px'
                }}
              >
                ✓
              </div>
              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                Ticket Created Successfully
              </h3>
              <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '8px 0 24px', lineHeight: '1.5' }}>
                Your request has been logged under ID <strong>TKT-2026-9912</strong>. We have assigned Dr. Anita Rao to audit your cultivation query.
              </p>
              <button
                type="button"
                className="cw-btn cw-btn--primary"
                onClick={() => navigate('/dashboard/support/tickets')}
              >
                View Support History
              </button>
            </Card>
          )}
        </div>

        {/* Right Area: policies */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              Response Estimates
            </h4>
            <ul style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', paddingLeft: '16px', margin: 0, lineHeight: '1.6' }}>
              <li>High priority: 2-4 business hours.</li>
              <li>Medium/Low priority: 12-24 business hours.</li>
              <li>Lab technical audits are handled directly by Lead Mycologists.</li>
            </ul>
          </Card>

          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
              Live WhatsApp Support
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              Prefer instant messaging? Connect with our shipping assistance team via WhatsApp.
            </p>
            <button
              type="button"
              className="cw-btn cw-btn--outlined cw-btn--sm"
              onClick={() => {
                setToastMessage('WhatsApp redirect simulator triggered!');
                setTimeout(() => setToastMessage(null), 3000);
              }}
              style={{ width: '100%', marginTop: '12px', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '6px' }}
            >
              <Icon name="message-circle" size={14} color="currentColor" />
              WhatsApp Helpdesk
            </button>
          </Card>
        </div>

      </Grid>

    </div>
  );
};
export default ContactSupportPage;
