import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { KB_ARTICLES, KbArticle } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const KnowledgeBasePage: React.FC = () => {
  const navigate = useNavigate();
  const [articles, setArticles] = useState<KbArticle[]>(KB_ARTICLES);
  const [selectedArticleId, setSelectedArticleId] = useState<string | null>(null);

  // Filters
  const [searchQuery, setSearchQuery] = useState('');
  const [activeCategory, setActiveCategory] = useState<'all' | 'Lab Work' | 'Cultivation' | 'Orders & Billing'>('all');
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Feedback states
  const [ratedArticles, setRatedArticles] = useState<Record<string, boolean>>({});

  const handleRateHelpful = (id: string, helpful: boolean) => {
    if (ratedArticles[id]) return;

    setRatedArticles(prev => ({ ...prev, [id]: true }));
    if (helpful) {
      setArticles(prev =>
        prev.map(a => (a.id === id ? { ...a, likes: a.likes + 1 } : a))
      );
      setToastMessage("Thank you for your feedback! Glad we could help.");
    } else {
      setToastMessage("Feedback registered. We will update this article soon.");
    }
    setTimeout(() => setToastMessage(null), 3000);
  };

  const selectedArticle = articles.find((a) => a.id === selectedArticleId);

  // Filter articles
  const filteredArticles = articles.filter(a => {
    const matchesSearch = a.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                          a.description.toLowerCase().includes(searchQuery.toLowerCase()) || 
                          a.body.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = activeCategory === 'all' || a.category === activeCategory;
    return matchesSearch && matchesCategory;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="info" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Case 1: Article Viewer */}
      {selectedArticle ? (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Back button */}
          <div>
            <button
              type="button"
              onClick={() => setSelectedArticleId(null)}
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
              Back to Articles List
            </button>
          </div>

          <Grid columns="2fr 1.1fr" gap="24px" style={{ alignItems: 'start' }}>
            {/* Main Article Content */}
            <Card variant="outlined" padding="lg">
              <span style={{ fontSize: '10px', color: 'var(--color-primary)', fontWeight: 'bold', textTransform: 'uppercase' }}>
                {selectedArticle.category} · {selectedArticle.readTime} read
              </span>
              
              <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '8px 0 16px' }}>
                {selectedArticle.title}
              </h2>

              <p 
                style={{ 
                  fontSize: 'var(--text-body-sm)', 
                  color: 'var(--color-text-primary)', 
                  lineHeight: '1.6', 
                  whiteSpace: 'pre-line',
                  margin: '0 0 24px'
                }}
              >
                {selectedArticle.body}
              </p>

              {/* Helpfulness check */}
              <div 
                style={{ 
                  borderTop: '1px solid var(--color-border-default)', 
                  paddingTop: '16px', 
                  display: 'flex', 
                  justifyContent: 'space-between', 
                  alignItems: 'center',
                  flexWrap: 'wrap',
                  gap: '12px'
                }}
              >
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>
                  Was this article helpful? · <strong>{selectedArticle.likes} people found this useful</strong>
                </span>

                <div style={{ display: 'flex', gap: '12px' }}>
                  <button
                    type="button"
                    disabled={!!ratedArticles[selectedArticle.id]}
                    onClick={() => handleRateHelpful(selectedArticle.id, true)}
                    className="cw-btn cw-btn--outlined cw-btn--sm"
                    style={{ display: 'flex', alignItems: 'center', gap: '6px' }}
                  >
                    👍 Yes
                  </button>
                  <button
                    type="button"
                    disabled={!!ratedArticles[selectedArticle.id]}
                    onClick={() => handleRateHelpful(selectedArticle.id, false)}
                    className="cw-btn cw-btn--outlined cw-btn--sm"
                    style={{ display: 'flex', alignItems: 'center', gap: '6px' }}
                  >
                    👎 No
                  </button>
                </div>
              </div>
            </Card>

            {/* Sidebar: Related Articles */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
              <Card variant="outlined" padding="md">
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
                  Related Articles
                </h4>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                  {articles.filter(a => a.id !== selectedArticle.id).map(art => (
                    <div 
                      key={art.id}
                      onClick={() => {
                        setSelectedArticleId(art.id);
                        window.scrollTo(0,0);
                      }}
                      style={{ cursor: 'pointer', fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}
                    >
                      <strong style={{ color: 'var(--color-primary)', display: 'block' }}>{art.title}</strong>
                      {art.description}
                    </div>
                  ))}
                </div>
              </Card>

              <Card variant="outlined" padding="md">
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
                  Need more help?
                </h4>
                <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
                  Talk to our lead mycology research team directly by opening a support ticket.
                </p>
                <button
                  type="button"
                  className="cw-btn cw-btn--primary cw-btn--sm"
                  onClick={() => navigate('/dashboard/support/contact')}
                  style={{ width: '100%', marginTop: '12px' }}
                >
                  Contact Expert
                </button>
              </Card>
            </div>
          </Grid>
        </div>
      ) : (
        /* Case 2: Articles List View */
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          
          {/* Header */}
          <div>
            <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
              Knowledge Base
            </h2>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
              Search sterile technique procedures, substrate recipes, and billing FAQs.
            </p>
          </div>

          {/* Search and category filters controls */}
          <div 
            style={{ 
              display: 'flex', 
              justifyContent: 'space-between', 
              alignItems: 'center', 
              gap: '16px', 
              flexWrap: 'wrap',
              background: '#f8fafc',
              padding: '16px',
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--color-border-default)'
            }}
          >
            <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', flex: 1, minWidth: '280px' }}>
              {/* Search */}
              <div style={{ position: 'relative', flex: 1, minWidth: '180px' }}>
                <span style={{ position: 'absolute', left: '10px', top: '10px', color: 'var(--color-text-secondary)', display: 'flex' }}>
                  <Icon name="search" size={14} color="currentColor" />
                </span>
                <input
                  type="text"
                  placeholder="Search articles..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  style={{
                    width: '100%',
                    padding: '6px 12px 6px 32px',
                    border: '1px solid var(--color-border-default)',
                    borderRadius: 'var(--radius-md)',
                    fontSize: 'var(--text-body-sm)',
                    outline: 'none',
                    background: 'var(--color-bg-surface-default)',
                    color: 'var(--color-text-primary)'
                  }}
                />
              </div>

              {/* Categories tabs/select */}
              <select
                value={activeCategory}
                onChange={(e) => setActiveCategory(e.target.value as any)}
                style={{
                  padding: '6px 12px',
                  borderRadius: 'var(--radius-md)',
                  border: '1px solid var(--color-border-default)',
                  fontSize: 'var(--text-body-sm)',
                  background: 'var(--color-bg-surface-default)',
                  color: 'var(--color-text-primary)',
                  outline: 'none',
                }}
              >
                <option value="all">All Categories</option>
                <option value="Lab Work">Lab Work</option>
                <option value="Cultivation">Cultivation</option>
                <option value="Orders & Billing">Orders & Billing</option>
              </select>
            </div>
          </div>

          {/* Articles list */}
          {filteredArticles.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
              <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>📖</div>
              <div>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>No articles match filters</h3>
                <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Try using different keywords.</p>
              </div>
            </div>
          ) : (
            <Grid columns="repeat(auto-fill, minmax(280px, 1fr))" gap="20px">
              {filteredArticles.map((art) => (
                <Card 
                  key={art.id}
                  variant="outlined"
                  padding="md"
                  onClick={() => setSelectedArticleId(art.id)}
                  style={{ cursor: 'pointer', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', height: '100%' }}
                >
                  <div>
                    <span style={{ fontSize: '9px', color: 'var(--color-primary)', fontWeight: 'bold', display: 'block' }}>
                      {art.category.toUpperCase()} · {art.readTime}
                    </span>
                    <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '8px 0 4px' }}>
                      {art.title}
                    </h4>
                    <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: 0, lineHeight: '1.4' }}>
                      {art.description}
                    </p>
                  </div>
                  
                  <div style={{ borderTop: '1px solid var(--color-border-default)', paddingTop: '10px', marginTop: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontSize: '9px', color: 'var(--color-text-secondary)' }}>
                      👍 {art.likes} likes
                    </span>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-primary)', fontWeight: 'bold' }}>
                      Read Article &rarr;
                    </span>
                  </div>
                </Card>
              ))}
            </Grid>
          )}

        </div>
      )}
    </div>
  );
};
export default KnowledgeBasePage;
