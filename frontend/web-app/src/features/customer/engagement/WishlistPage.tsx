import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { WISHLIST_ITEMS, SAVED_ITEMS, RECENTLY_VIEWED } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Icon } from '../../../design-system/icons/Icon';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const WishlistPage: React.FC = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState<'wishlist' | 'saved' | 'recent'>('wishlist');
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // States
  const [wishlist, setWishlist] = useState(WISHLIST_ITEMS);
  const [savedItems, setSavedItems] = useState(SAVED_ITEMS);
  const [recentItems, setRecentItems] = useState(RECENTLY_VIEWED);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortBy, setSortBy] = useState('name');

  const triggerToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const handleMoveToCart = (item: any) => {
    triggerToast(`"${item.name}" moved to shopping cart successfully!`);
  };

  const handleShareWishlist = () => {
    triggerToast('Wishlist share link copied to clipboard!');
  };

  // Filter & Sort Wishlist
  const sortedWishlist = [...wishlist]
    .filter(item => item.name.toLowerCase().includes(searchTerm.toLowerCase()))
    .sort((a, b) => {
      if (sortBy === 'price-asc') return a.price - b.price;
      if (sortBy === 'price-desc') return b.price - a.price;
      if (sortBy === 'rating') return b.rating - a.rating;
      return a.name.localeCompare(b.name);
    });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast Notification */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Main tab switching row */}
      <div 
        style={{ 
          display: 'flex', 
          justifyContent: 'space-between',
          alignItems: 'center',
          borderBottom: '1px solid var(--color-border-default)',
          paddingBottom: '8px',
          flexWrap: 'wrap',
          gap: '16px'
        }}
      >
        <div 
          style={{ display: 'flex', gap: '24px' }}
          role="tablist"
          aria-label="Wishlist and History navigation"
        >
          {[
            { id: 'wishlist', label: `My Wishlist (${wishlist.length})` },
            { id: 'saved', label: `Saved for Later (${savedItems.length})` },
            { id: 'recent', label: 'Recently Viewed' },
          ].map((tab) => (
            <button
              key={tab.id}
              role="tab"
              aria-selected={activeTab === tab.id}
              type="button"
              onClick={() => setActiveTab(tab.id as any)}
              style={{
                border: 'none',
                background: 'none',
                padding: '8px 0',
                fontSize: 'var(--text-body-lg)',
                fontWeight: activeTab === tab.id ? 'var(--weight-bold)' : 'var(--weight-medium)',
                color: activeTab === tab.id ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                borderBottom: activeTab === tab.id ? '3px solid var(--color-primary)' : '3px solid transparent',
                cursor: 'pointer',
                transition: 'all 0.15s ease',
              }}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {activeTab === 'wishlist' && wishlist.length > 0 && (
          <button
            type="button"
            className="cw-btn cw-btn--outlined cw-btn--sm"
            onClick={handleShareWishlist}
            style={{ display: 'flex', alignItems: 'center', gap: '6px' }}
          >
            <Icon name="share" size={14} color="currentColor" />
            Share Wishlist
          </button>
        )}
      </div>

      {/* TAB 1: WISHLIST WORKSPACE */}
      {activeTab === 'wishlist' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Controls Bar */}
          {wishlist.length > 0 && (
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
              {/* Search input */}
              <div style={{ position: 'relative', width: '100%', maxWidth: '280px' }}>
                <span style={{ position: 'absolute', left: '12px', top: '10px', color: 'var(--color-text-secondary)', display: 'flex' }}>
                  <Icon name="search" size={16} color="currentColor" />
                </span>
                <input
                  type="text"
                  placeholder="Search wishlist..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
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

              {/* Sort by */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Sort by</span>
                <select
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value)}
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
                  <option value="name">Name (A-Z)</option>
                  <option value="price-asc">Price (Low to High)</option>
                  <option value="price-desc">Price (High to Low)</option>
                  <option value="rating">Rating</option>
                </select>
              </div>
            </div>
          )}

          {sortedWishlist.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
              <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>❤️</div>
              <div>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>Wishlist is empty</h3>
                <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
                  {searchTerm ? 'No items match your search term.' : 'Explore SporeKart cultivars and save your favorites here.'}
                </p>
              </div>
              <button type="button" className="cw-btn cw-btn--primary" onClick={() => navigate('/dashboard/products')}>Browse Products</button>
            </div>
          ) : (
            <Grid columns="repeat(auto-fill, minmax(280px, 1fr))" gap="20px">
              {sortedWishlist.map((item) => (
                <Card 
                  key={item.id} 
                  variant="outlined" 
                  padding="md"
                  style={{ display: 'flex', flexDirection: 'column', height: '100%', justifyContent: 'space-between' }}
                >
                  <div>
                    {/* Visual Card Image */}
                    <div style={{ height: '140px', background: 'var(--color-bg-primary-weak)', borderRadius: 'var(--radius-md)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '48px', position: 'relative', border: '1px solid var(--color-border-default)' }}>
                      {item.image}
                      <button 
                        type="button"
                        aria-label="Remove item"
                        onClick={() => setWishlist(prev => prev.filter(w => w.id !== item.id))}
                        style={{ position: 'absolute', top: '10px', right: '10px', border: 'none', background: 'var(--color-bg-surface-default)', width: '28px', height: '28px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', cursor: 'pointer', boxShadow: 'var(--shadow-1)' }}
                      >
                        <Icon name="x" size={14} color="var(--color-text-secondary)" />
                      </button>
                    </div>

                    {/* Metadata */}
                    <div style={{ marginTop: '12px' }}>
                      <span style={{ 
                        fontSize: '10px', 
                        fontWeight: 'bold', 
                        color: item.availability === 'Out of Stock' ? 'var(--color-text-danger, #dc2626)' : item.availability === 'Low Stock' ? 'var(--color-text-warning, #d97706)' : 'var(--color-text-success, #166534)',
                        display: 'block' 
                      }}>
                        {item.availability.toUpperCase()}
                      </span>
                      <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '4px 0 0', minHeight: '40px', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                        {item.name}
                      </h4>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '4px', marginTop: '6px' }}>
                        <span style={{ color: 'var(--color-text-warning, #eab308)', display: 'flex' }}><Icon name="star" size={14} color="currentColor" /></span>
                        <span style={{ fontSize: 'var(--text-caption)', fontWeight: 'bold', color: 'var(--color-text-primary)' }}>{item.rating}</span>
                        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>({item.reviewsCount} reviews)</span>
                      </div>
                    </div>
                  </div>

                  {/* Buy info and buttons */}
                  <div style={{ marginTop: '16px', borderTop: '1px solid var(--color-border-default)', paddingTop: '12px' }}>
                    <div style={{ display: 'flex', alignItems: 'baseline', gap: '8px', marginBottom: '12px' }}>
                      <strong style={{ fontSize: 'var(--text-body-lg)', color: 'var(--color-text-primary)' }}>₹{item.price.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong>
                      {item.originalPrice && (
                        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', textDecoration: 'line-through' }}>
                          ₹{item.originalPrice.toLocaleString('en-IN', { minimumFractionDigits: 2 })}
                        </span>
                      )}
                    </div>
                    
                    <button
                      type="button"
                      disabled={item.availability === 'Out of Stock'}
                      onClick={() => handleMoveToCart(item)}
                      className="cw-btn cw-btn--primary cw-btn--sm"
                      style={{ width: '100%', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '8px' }}
                    >
                      <Icon name="shopping-cart" size={14} color="currentColor" />
                      Move to Cart
                    </button>
                  </div>
                </Card>
              ))}
            </Grid>
          )}
        </div>
      )}

      {/* TAB 2: SAVED FOR LATER */}
      {activeTab === 'saved' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {savedItems.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
              <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>🛍️</div>
              <div>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>No saved items</h3>
                <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Items saved during checkout appear here.</p>
              </div>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              {savedItems.map((item) => (
                <Card 
                  key={item.id}
                  variant="outlined"
                  padding="md"
                  style={{ display: 'flex', gap: '16px', flexWrap: 'wrap', alignItems: 'center' }}
                >
                  <div style={{ width: '72px', height: '72px', background: 'var(--color-bg-primary-weak)', borderRadius: 'var(--radius-md)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '32px', border: '1px solid var(--color-border-default)' }}>
                    {item.image}
                  </div>
                  
                  <div style={{ flex: 1, minWidth: '200px' }}>
                    <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                      {item.name}
                    </h4>
                    <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '2px' }}>
                      Status: <strong>{item.availability}</strong>
                    </span>
                    {item.alertMessage && (
                      <div style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', background: 'var(--color-bg-success-weak, #f0fdf4)', padding: '2px 8px', borderRadius: 'var(--radius-sm)', marginTop: '6px', fontSize: 'var(--text-caption)', color: 'var(--color-text-success, #166534)' }}>
                        <Icon name="sparkles" size={10} color="currentColor" />
                        {item.alertMessage}
                      </div>
                    )}
                  </div>

                  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '8px' }}>
                    <div style={{ display: 'flex', gap: '8px', alignItems: 'baseline' }}>
                      <strong style={{ fontSize: 'var(--text-body-lg)', color: 'var(--color-text-primary)' }}>₹{item.price.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong>
                      {item.originalPrice && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', textDecoration: 'line-through' }}>₹{item.originalPrice}</span>}
                    </div>
                    <div style={{ display: 'flex', gap: '8px' }}>
                      <button
                        type="button"
                        className="cw-btn cw-btn--outlined cw-btn--sm"
                        onClick={() => setSavedItems(prev => prev.filter(s => s.id !== item.id))}
                      >
                        Delete
                      </button>
                      <button
                        type="button"
                        className="cw-btn cw-btn--primary cw-btn--sm"
                        onClick={() => handleMoveToCart(item)}
                      >
                        Move to Cart
                      </button>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          )}
        </div>
      )}

      {/* TAB 3: RECENTLY VIEWED */}
      {activeTab === 'recent' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {recentItems.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '64px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '12px' }}>
              <div style={{ width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-bg-primary-weak)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '28px' }}>👁️</div>
              <div>
                <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>Browsing history is empty</h3>
                <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Products you browse will be saved here for easy access.</p>
              </div>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                <button
                  type="button"
                  className="cw-btn cw-btn--outlined cw-btn--sm"
                  onClick={() => {
                    setRecentItems([]);
                    triggerToast('Browsing history cleared!');
                  }}
                  style={{ display: 'flex', alignItems: 'center', gap: '6px' }}
                >
                  <Icon name="trash" size={14} color="currentColor" />
                  Clear History
                </button>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                {recentItems.map((item) => (
                  <div 
                    key={item.id}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between',
                      borderBottom: '1px solid var(--color-border-default)',
                      paddingBottom: '12px',
                      flexWrap: 'wrap',
                      gap: '12px',
                    }}
                  >
                    <div style={{ display: 'flex', gap: '16px', alignItems: 'center' }}>
                      <div style={{ width: '56px', height: '56px', background: 'var(--color-bg-primary-weak)', borderRadius: 'var(--radius-md)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '24px', border: '1px solid var(--color-border-default)', flexShrink: 0 }}>
                        {item.image}
                      </div>
                      <div>
                        <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: 0 }}>
                          {item.name}
                        </h4>
                        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', display: 'block', marginTop: '2px' }}>
                          Category: <strong>{item.category}</strong> · Viewed {item.lastViewed}
                        </span>
                      </div>
                    </div>

                    <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                      <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>₹{item.price.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong>
                      <button
                        type="button"
                        className="cw-btn cw-btn--primary cw-btn--sm"
                        onClick={() => navigate('/dashboard/products')}
                      >
                        View Product
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
export default WishlistPage;
