import React, { useState } from 'react';
import { MOCK_FAQS } from './mockData';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';

export const FaqCenterPage: React.FC = () => {
  const [faqs] = useState(MOCK_FAQS);
  const [searchQuery, setSearchQuery] = useState('');
  
  // Track open FAQ question index
  const [openFaqIdx, setOpenFaqIdx] = useState<number | null>(0);

  const filteredFaqs = faqs.filter(f => 
    f.q.toLowerCase().includes(searchQuery.toLowerCase()) || 
    f.a.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      
      {/* Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Frequently Asked Questions
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Quick answers to common questions about spawn handling, ordering limits, and lab sterility.
        </p>
      </div>

      {/* Search Input */}
      <div style={{ position: 'relative', width: '100%', maxWidth: '400px' }}>
        <span style={{ position: 'absolute', left: '12px', top: '10px', color: 'var(--color-text-secondary)', display: 'flex' }}>
          <Icon name="search" size={16} color="currentColor" />
        </span>
        <input
          type="text"
          placeholder="Search FAQs..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          style={{
            width: '100%',
            padding: '8px 12px 8px 36px',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-md)',
            fontSize: 'var(--text-body-sm)',
            outline: 'none',
            background: 'var(--color-bg-surface-default)',
            color: 'var(--color-text-primary)'
          }}
        />
      </div>

      {/* FAQs list accordion */}
      <Card variant="outlined" padding="lg">
        {filteredFaqs.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '32px 0', color: 'var(--color-text-secondary)' }}>
            No matching questions found.
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {filteredFaqs.map((faq, idx) => {
              const isOpen = openFaqIdx === idx;
              return (
                <div 
                  key={idx}
                  style={{
                    borderBottom: '1px solid var(--color-border-default)',
                    paddingBottom: '12px',
                  }}
                >
                  <div 
                    onClick={() => setOpenFaqIdx(isOpen ? null : idx)}
                    style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      cursor: 'pointer',
                      userSelect: 'none',
                      padding: '8px 0'
                    }}
                  >
                    <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>
                      [{faq.category.toUpperCase()}] {faq.q}
                    </strong>
                    <Icon name={isOpen ? 'chevron-up' : 'chevron-down'} size={16} color="var(--color-text-secondary)" />
                  </div>

                  {isOpen && (
                    <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '8px 0 0', lineHeight: '1.5', paddingLeft: '8px' }}>
                      {faq.a}
                    </p>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </Card>
      
    </div>
  );
};
export default FaqCenterPage;
