import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { MOCK_ORDERS } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';
import { Grid } from '../../../design-system/components/layout/Grid';

export const ReturnsRefundsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Form State
  const [selectedItems, setSelectedItems] = useState<Record<string, boolean>>({});
  const [reason, setReason] = useState('Contamination');
  const [notes, setNotes] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [submitted, setSubmitted] = useState(false);

  const order = MOCK_ORDERS.find((o) => o.id === id);

  if (!order) {
    return (
      <div style={{ textAlign: 'center', padding: '48px 0' }}>
        <h2>Order not found</h2>
        <button type="button" className="cw-btn cw-btn--primary" onClick={() => navigate('/dashboard/orders')}>
          Back to Orders
        </button>
      </div>
    );
  }

  const isAlreadyRefunded = order.status === 'Refunded' || order.status === 'Returned';

  const handleToggleItem = (itemId: string) => {
    setSelectedItems(prev => ({
      ...prev,
      [itemId]: !prev[itemId],
    }));
  };

  const handleSubmitReturn = (e: React.FormEvent) => {
    e.preventDefault();
    const hasSelectedItems = Object.values(selectedItems).some(val => val);
    if (!hasSelectedItems) {
      setToastMessage('Please select at least one item to return.');
      setTimeout(() => setToastMessage(null), 3000);
      return;
    }

    setSubmitting(true);
    setTimeout(() => {
      setSubmitting(false);
      setSubmitted(true);
      setToastMessage('Return request submitted successfully!');
      setTimeout(() => setToastMessage(null), 3000);
    }, 1500);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone={submitted ? "success" : "info"} onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Header back button */}
      <div>
        <button
          type="button"
          onClick={() => navigate(`/dashboard/orders/${order.id}`)}
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
          Back to Details
        </button>
      </div>

      {/* Page Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Returns & Refund Center
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Manage return requests, inspect eligibility, and track credit status for order <strong>#{order.id}</strong>
        </p>
      </div>

      <Grid columns="2fr 1.2fr" gap="24px" style={{ alignItems: 'start' }}>
        
        {/* Left Column: Form or Active Refund timeline */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          {/* Case 1: Already refunded / returned */}
          {isAlreadyRefunded && (
            <Card variant="outlined" padding="lg">
              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-md)' }}>
                Active Refund Tracker
              </h3>

              {/* Refund Milestone steps */}
              <div 
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '24px',
                  paddingLeft: '20px',
                  borderLeft: '2px solid var(--color-bg-primary-default)',
                  position: 'relative',
                  marginLeft: '10px',
                }}
              >
                {order.refundMilestones?.map((milestone, idx) => (
                  <div key={idx} style={{ position: 'relative' }}>
                    <div 
                      style={{
                        position: 'absolute',
                        left: '-29px',
                        top: '2px',
                        width: '16px',
                        height: '16px',
                        borderRadius: '50%',
                        background: 'var(--color-bg-primary-default)',
                        border: '2px solid #fff',
                      }}
                    />
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline' }}>
                      <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                        {milestone.status}
                      </h4>
                      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                        {milestone.timestamp}
                      </span>
                    </div>
                    <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '2px 0 0' }}>
                      {milestone.detail}
                    </p>
                  </div>
                ))}
              </div>

              {/* Refund Summary Info */}
              <div 
                style={{
                  marginTop: '24px',
                  background: 'var(--color-bg-success-weak, #f0fdf4)',
                  border: '1px solid #bcf0da',
                  padding: '16px',
                  borderRadius: 'var(--radius-md)',
                  fontSize: 'var(--text-body-sm)',
                  color: 'var(--color-text-success, #166534)',
                }}
              >
                <h4 style={{ margin: 0, fontWeight: 'bold' }}>Refund Credited Successfully</h4>
                <p style={{ margin: '4px 0 0', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  A refund of <strong>₹{order.total.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong> has been credited to UPI source. UPI Ref ID: <strong>UPI-REF-20265541</strong>
                </p>
              </div>
            </Card>
          )}

          {/* Case 2: Eligible & request NOT submitted yet */}
          {!isAlreadyRefunded && !submitted && (
            <Card variant="outlined" padding="lg">
              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-md)' }}>
                Request Return
              </h3>
              
              <form onSubmit={handleSubmitReturn} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                
                {/* Item selection */}
                <div>
                  <label style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Select Items to Return
                  </label>
                  
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    {order.items.map((item) => (
                      <div 
                        key={item.id}
                        onClick={() => handleToggleItem(item.id)}
                        style={{
                          display: 'flex',
                          alignItems: 'center',
                          gap: '12px',
                          border: '1px solid var(--color-border-default)',
                          borderRadius: 'var(--radius-md)',
                          padding: '12px',
                          cursor: 'pointer',
                          background: selectedItems[item.id] ? 'var(--color-bg-primary-weak)' : 'transparent',
                          transition: 'all 0.15s ease',
                        }}
                      >
                        <input
                          type="checkbox"
                          checked={!!selectedItems[item.id]}
                          onChange={() => {}} // Handled by outer click
                          style={{ cursor: 'pointer' }}
                        />
                        <span style={{ fontSize: '24px' }}>{item.image}</span>
                        <div style={{ flex: 1 }}>
                          <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block' }}>
                            {item.name}
                          </span>
                          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                            Qty: {item.quantity} · Price: ₹{item.price.toLocaleString('en-IN', { minimumFractionDigits: 2 })}
                          </span>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Reason Selection */}
                <div>
                  <label htmlFor="reason" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Reason for Return
                  </label>
                  <select
                    id="reason"
                    value={reason}
                    onChange={(e) => setReason(e.target.value)}
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
                    <option value="Contamination">Contamination on arrival (Spawn/Agar)</option>
                    <option value="Damaged">Damaged in transit / Broken packaging</option>
                    <option value="Incorrect">Incorrect item received</option>
                    <option value="Quality">Spawn quality / Moisture level issues</option>
                    <option value="Other">Other (Please specify below)</option>
                  </select>
                </div>

                {/* Additional Description Notes */}
                <div>
                  <label htmlFor="notes" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Additional Details
                  </label>
                  <textarea
                    id="notes"
                    rows={4}
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    placeholder="Please specify any details that will help our laboratory team audit this issue..."
                    style={{
                      width: '100%',
                      padding: '8px 12px',
                      borderRadius: 'var(--radius-md)',
                      border: '1px solid var(--color-border-default)',
                      fontSize: 'var(--text-body-sm)',
                      background: 'var(--color-bg-surface-default)',
                      color: 'var(--color-text-primary)',
                      outline: 'none',
                      resize: 'vertical',
                    }}
                  />
                </div>

                {/* Evidence Upload Placeholder */}
                <div>
                  <label style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                    Photo Evidence (Required for spawn contamination / damages)
                  </label>
                  
                  <div 
                    style={{
                      border: '2px dashed var(--color-border-default)',
                      borderRadius: 'var(--radius-md)',
                      padding: '24px 0',
                      textAlign: 'center',
                      background: 'var(--color-bg-surface-secondary, #fafafa)',
                      cursor: 'pointer',
                    }}
                  >
                    <Icon name="upload" size={24} color="var(--color-text-secondary)" style={{ marginBottom: '8px' }} />
                    <span style={{ display: 'block', fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>
                      Click to upload photos
                    </span>
                    <span style={{ display: 'block', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', marginTop: '4px' }}>
                      PNG, JPG up to 10MB
                    </span>
                  </div>
                </div>

                {/* Submit button */}
                <button
                  type="submit"
                  disabled={submitting}
                  className="cw-btn cw-btn--primary cw-btn--lg"
                  style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '8px' }}
                >
                  {submitting ? 'Submitting...' : 'Submit Return Request'}
                </button>
              </form>
            </Card>
          )}

          {/* Case 3: Just submitted */}
          {!isAlreadyRefunded && submitted && (
            <Card variant="outlined" padding="lg" style={{ textAlign: 'center', padding: '40px 20px' }}>
              <div 
                style={{
                  width: '56px',
                  height: '56px',
                  borderRadius: '50%',
                  background: 'var(--color-bg-success-weak, #f0fdf4)',
                  color: 'var(--color-text-success, #166534)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '28px',
                  margin: '0 auto var(--space-stack-md)',
                }}
              >
                ✓
              </div>
              <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
                Request Submitted Successfully
              </h3>
              <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '8px 0 24px', lineHeight: '1.5' }}>
                Your return request for order <strong>#{order.id}</strong> has been logged. Our laboratory QA team will review the submitted details within 24 hours.
              </p>
              <button
                type="button"
                className="cw-btn cw-btn--primary"
                onClick={() => navigate(`/dashboard/orders/${order.id}`)}
              >
                Return to Details
              </button>
            </Card>
          )}
        </div>

        {/* Right Column: Policies sidebar */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          
          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-sm)' }}>
              Return Policies
            </h4>
            <ul style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', paddingLeft: '16px', margin: 0, lineHeight: '1.6' }}>
              <li>Spawn & culture products have a strict 10-day return policy due to perishability.</li>
              <li>Contamination claims must include clear photo evidence taken immediately on receipt.</li>
              <li>Laboratory review is completed within 24-48 business hours.</li>
              <li>Approved refunds are credited directly back to the original payment source within 3-5 bank business days.</li>
            </ul>
          </Card>

          <Card variant="outlined" padding="md">
            <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
              Support Escalation
            </h4>
            <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
              Need urgent replacement instead of refund? Or want to talk to our spawn growth audit engineers?
            </p>
            <a 
              href={`/dashboard/support?subject=Return%20Escalation%20${order.id}`}
              className="cw-btn cw-btn--outlined cw-btn--sm"
              style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '6px', textDecoration: 'none', marginTop: '12px' }}
            >
              <Icon name="message-circle" size={14} color="currentColor" />
              Escalate Request
            </a>
          </Card>
        </div>
      </Grid>
    </div>
  );
};
export default ReturnsRefundsPage;
