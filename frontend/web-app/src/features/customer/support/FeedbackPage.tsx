import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const FeedbackPage: React.FC = () => {
  const navigate = useNavigate();
  const [rating, setRating] = useState<number>(5);
  const [hoverRating, setHoverRating] = useState<number | null>(null);
  const [category, setCategory] = useState<'bug' | 'feature' | 'experience'>('experience');
  const [suggestions, setSuggestions] = useState('');
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!suggestions.trim()) {
      setToastMessage('Please enter your suggestions before submitting.');
      setTimeout(() => setToastMessage(null), 3000);
      return;
    }

    setSubmitted(true);
    setToastMessage('Feedback submitted successfully. Thank you!');
    setTimeout(() => {
      setToastMessage(null);
      navigate('/dashboard/support');
    }, 2000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', maxWidth: '600px', margin: '0 auto' }}>
      
      {/* Toast */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Header back */}
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
          Back to Support
        </button>
      </div>

      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Feedback & NPS Center
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Rate your platform experience. Your reviews guide SporeKart feature developments.
        </p>
      </div>

      <Card variant="outlined" padding="lg">
        {!submitted ? (
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
            
            {/* Star Rating */}
            <div style={{ textAlign: 'center' }}>
              <label style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '12px' }}>
                How would you rate your overall experience?
              </label>

              <div style={{ display: 'flex', justifyContent: 'center', gap: '8px' }}>
                {[1, 2, 3, 4, 5].map((star) => {
                  const filled = hoverRating !== null ? star <= hoverRating : star <= rating;
                  return (
                    <button
                      key={star}
                      type="button"
                      onClick={() => setRating(star)}
                      onMouseEnter={() => setHoverRating(star)}
                      onMouseLeave={() => setHoverRating(null)}
                      style={{
                        background: 'none',
                        border: 'none',
                        cursor: 'pointer',
                        fontSize: '32px',
                        color: filled ? 'var(--color-text-warning, #eab308)' : '#cbd5e1',
                        transition: 'transform 0.1s ease',
                        padding: 0
                      }}
                    >
                      ★
                    </button>
                  );
                })}
              </div>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '8px' }}>
                {rating === 5 ? 'Excellent!' : rating === 4 ? 'Very Good' : rating === 3 ? 'Good' : rating === 2 ? 'Fair' : 'Needs Improvement'}
              </span>
            </div>

            {/* Category */}
            <div>
              <label style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                Feedback Category
              </label>
              <div style={{ display: 'flex', gap: '16px', flexWrap: 'wrap' }}>
                {[
                  { id: 'experience', label: 'Platform Experience' },
                  { id: 'bug', label: 'Report a Bug' },
                  { id: 'feature', label: 'Feature Request' },
                ].map((opt) => (
                  <label key={opt.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer', fontSize: 'var(--text-body-sm)' }}>
                    <input
                      type="radio"
                      name="feedback-category"
                      checked={category === opt.id}
                      onChange={() => setCategory(opt.id as any)}
                      style={{ cursor: 'pointer' }}
                    />
                    {opt.label}
                  </label>
                ))}
              </div>
            </div>

            {/* Suggestions textarea */}
            <div>
              <label htmlFor="suggestions" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', display: 'block', marginBottom: '8px' }}>
                {category === 'bug' ? 'Describe the bug in detail' : category === 'feature' ? 'What features would you like to see?' : 'Suggestions or comments'}
              </label>
              <textarea
                id="suggestions"
                rows={4}
                value={suggestions}
                onChange={(e) => setSuggestions(e.target.value)}
                placeholder={category === 'bug' ? "Specify steps to reproduce the error or describe what broke..." : "Share your ideas..."}
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

            <button
              type="submit"
              className="cw-btn cw-btn--primary cw-btn--lg"
              style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
            >
              Submit Feedback
            </button>

          </form>
        ) : (
          <div style={{ textAlign: 'center', padding: '24px 0' }}>
            <span style={{ fontSize: '48px', display: 'block', marginBottom: '16px' }}>🎉</span>
            <strong style={{ fontSize: 'var(--text-body-lg)', color: 'var(--color-text-primary)' }}>
              Feedback Logged
            </strong>
          </div>
        )}
      </Card>

    </div>
  );
};
export default FeedbackPage;
