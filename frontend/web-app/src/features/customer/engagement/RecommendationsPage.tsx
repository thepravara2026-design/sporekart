import React, { useState } from 'react';
import { RECOMMENDATIONS } from './mockData';
import { Grid } from '../../../design-system/components/layout/Grid';
import { Card } from '../../../design-system/components/composite/Card';
import { Toast } from '../../../design-system/components/feedback/Toast';

export const RecommendationsPage: React.FC = () => {
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  
  // Interactive personalization simulator state
  const [setup, setSetup] = useState<'monotub' | 'lab' | 'outdoor'>('monotub');

  // Personalized dynamically-rendered recommendations based on choice
  const getAiRecommendations = () => {
    if (setup === 'lab') {
      return [
        { id: 'ai-1', name: 'MEA Pre-poured Agar Plates (Pack of 20)', price: 1100.00, image: '🧫', desc: 'Required for spore inoculation & strain purification.' },
        { id: 'ai-2', name: 'Sterile Scalpel Blades (Pack of 10)', price: 250.00, image: '🔪', desc: 'Grade-A surgical carbon steel for sterile cutting.' },
        { id: 'ai-3', name: 'Liquid Culture Lids (Pack of 4)', price: 450.00, image: '🔩', desc: 'Pre-fitted with syringe injection ports and syringe filters.' },
      ];
    }
    if (setup === 'outdoor') {
      return [
        { id: 'ai-4', name: 'Golden Oyster Spawn Plug Logs (Pack of 100)', price: 950.00, image: '🪵', desc: 'Ideal logs inoculants for shaded outdoor beds.' },
        { id: 'ai-5', name: 'Shiitake Sawdust Plug Spawn (500g)', price: 1200.00, image: '🍄', desc: 'Premium sawdust spawn optimized for hardwood logs.' },
        { id: 'ai-6', name: 'Wax Applicator Brush Kit', price: 150.00, image: '🖌️', desc: 'Seal plug spawn nodes to prevent moisture loss.' },
      ];
    }
    // monotub
    return [
      { id: 'ai-7', name: 'Pink Oyster Mushroom Spore Syringe (10ml)', price: 800.00, image: '💉', desc: 'Fast-colonizing substrate syringe optimized for coco coir.' },
      { id: 'ai-8', name: 'Coco Coir Block Grow Medium', price: 350.00, image: '🥥', desc: 'Organic moisture-retaining casing base.' },
      { id: 'ai-9', name: 'High-Fine Spray Mister (500ml)', price: 600.00, image: '💦', desc: 'Ultra-fine water misting to prevent mold pooling.' },
    ];
  };

  const handleAddToCart = (item: any) => {
    setToastMessage(`"${item.name}" added to shopping cart!`);
    setTimeout(() => setToastMessage(null), 3000);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      {/* Toast */}
      {toastMessage && (
        <div style={{ position: 'fixed', bottom: '24px', right: '24px', zIndex: 1000 }}>
          <Toast message={toastMessage} tone="success" onClose={() => setToastMessage(null)} />
        </div>
      )}

      {/* Page Title */}
      <div>
        <h2 style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
          Personalized Recommendations
        </h2>
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>
          Tailored spawn collections, sterile equipment suggestions, and seasonal cultivars based on your grow history.
        </p>
      </div>

      {/* Simulator Banner */}
      <Card 
        variant="outlined" 
        padding="lg" 
        style={{ 
          background: 'linear-gradient(135deg, var(--color-bg-primary-weak) 0%, #fcfdf9 100%)', 
          border: '1px solid var(--color-border-default)' 
        }}
      >
        <div style={{ display: 'flex', gap: '16px', alignItems: 'flex-start', flexWrap: 'wrap' }}>
          <div style={{ fontSize: '32px' }}>🤖</div>
          <div style={{ flex: 1, minWidth: '240px' }}>
            <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: 0 }}>
              AI Recommendation Assistant
            </h3>
            <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '6px 0 16px', lineHeight: '1.5' }}>
              Select your cultivation setup to simulate how SporeKart customizes recommended spawn cultivars and sterile lab equipment for your specific environment.
            </p>

            {/* Selection Switchers */}
            <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
              {[
                { id: 'monotub', label: 'Monotub Growing Bag Setup', icon: '🎪' },
                { id: 'lab', label: 'Sterile cleanroom lab agar inoculating', icon: '🧫' },
                { id: 'outdoor', label: 'Outdoor log bedding & plugs', icon: '🪵' },
              ].map((opt) => (
                <button
                  key={opt.id}
                  type="button"
                  onClick={() => setSetup(opt.id as any)}
                  className={`cw-btn cw-btn--sm ${setup === opt.id ? 'cw-btn--primary' : 'cw-btn--outlined'}`}
                  style={{ display: 'flex', alignItems: 'center', gap: '8px' }}
                >
                  <span>{opt.icon}</span>
                  {opt.label}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Dynamic AI Results Grid */}
        <div style={{ marginTop: '24px', borderTop: '1px dashed var(--color-border-default)', paddingTop: '20px' }}>
          <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '0 0 12px' }}>
            Tailored for you (AI suggestions):
          </h4>
          
          <Grid columns="repeat(auto-fit, minmax(250px, 1fr))" gap="16px">
            {getAiRecommendations().map((item) => (
              <div 
                key={item.id} 
                style={{ 
                  background: 'var(--color-bg-surface-default)', 
                  border: '1px solid var(--color-border-default)',
                  borderRadius: 'var(--radius-md)',
                  padding: '12px',
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'space-between',
                  boxShadow: 'var(--shadow-1)'
                }}
              >
                <div>
                  <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                    <span style={{ fontSize: '28px' }}>{item.image}</span>
                    <strong style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{item.name}</strong>
                  </div>
                  <p style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', margin: '8px 0 0', lineHeight: '1.4' }}>
                    {item.desc}
                  </p>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '16px', borderTop: '1px solid var(--color-border-default)', paddingTop: '8px' }}>
                  <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'bold' }}>₹{item.price.toFixed(2)}</span>
                  <button
                    type="button"
                    onClick={() => handleAddToCart(item)}
                    className="cw-btn cw-btn--primary cw-btn--sm"
                    style={{ padding: '4px 8px', fontSize: '11px' }}
                  >
                    Add to Cart
                  </button>
                </div>
              </div>
            ))}
          </Grid>
        </div>
      </Card>

      {/* Grid of other recommendation categories */}
      <div>
        <h3 style={{ fontSize: 'var(--text-body-lg)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', margin: '0 0 16px' }}>
          Trending & Popular Products
        </h3>
        
        <Grid columns="repeat(auto-fit, minmax(280px, 1fr))" gap="20px">
          {RECOMMENDATIONS.map((item) => (
            <Card 
              key={item.id}
              variant="outlined"
              padding="md"
              style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}
            >
              <div>
                <div style={{ height: '120px', background: 'var(--color-bg-primary-weak)', borderRadius: 'var(--radius-sm)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '40px', border: '1px solid var(--color-border-default)', position: 'relative' }}>
                  {item.image}
                  <span 
                    style={{ 
                      position: 'absolute', 
                      top: '8px', 
                      left: '8px', 
                      background: 'var(--color-bg-primary-default)', 
                      color: '#fff', 
                      padding: '2px 8px', 
                      borderRadius: '10px', 
                      fontSize: '9px', 
                      fontWeight: 'bold' 
                    }}
                  >
                    {item.tag.toUpperCase()}
                  </span>
                </div>
                <h4 style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', margin: '12px 0 0' }}>
                  {item.name}
                </h4>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '16px', borderTop: '1px solid var(--color-border-default)', paddingTop: '12px' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'bold' }}>₹{item.price.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</span>
                <button
                  type="button"
                  className="cw-btn cw-btn--primary cw-btn--sm"
                  onClick={() => handleAddToCart(item)}
                >
                  Add to Cart
                </button>
              </div>
            </Card>
          ))}
        </Grid>
      </div>
    </div>
  );
};
export default RecommendationsPage;
