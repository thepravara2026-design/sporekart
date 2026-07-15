import React, { useState } from 'react';
import Icon from '../../../../design-system/icons/Icon';
import type { Asset } from '../types';

interface ImageGalleryProps {
  assets: Asset[];
  onPreview: (id: string) => void;
}

export const ImageGallery = React.memo(function ImageGallery({ assets, onPreview }: ImageGalleryProps) {
  const [selectedIndex, setSelectedIndex] = useState(0);

  const images = assets.filter((a) => a.type === 'image');

  if (images.length === 0) {
    return (
      <div
        style={{
          display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
          gap: 'var(--space-stack-sm)', padding: 'var(--space-12) var(--space-4)',
          background: 'var(--color-bg-surface-raised)', borderRadius: 'var(--radius-card)',
          textAlign: 'center', color: 'var(--color-text-tertiary)',
        }}
      >
        <Icon name="image" size={48} />
        <span style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-secondary)' }}>
          No images in this collection
        </span>
      </div>
    );
  }

  const current = images[selectedIndex] ?? images[0];

  const goPrev = () => setSelectedIndex((i) => (i > 0 ? i - 1 : images.length - 1));
  const goNext = () => setSelectedIndex((i) => (i < images.length - 1 ? i + 1 : 0));

  const navBtnStyle: React.CSSProperties = {
    position: 'absolute', top: '50%', transform: 'translateY(-50%)',
    display: 'flex', alignItems: 'center', justifyContent: 'center',
    width: 40, height: 40, borderRadius: 'var(--radius-full)',
    border: 'none', background: 'rgba(0,0,0,0.5)', color: '#fff',
    cursor: 'pointer', zIndex: 2,
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-md)' }}>
      <div
        style={{
          position: 'relative', borderRadius: 'var(--radius-card)',
          overflow: 'hidden', background: 'var(--color-bg-background)',
          aspectRatio: '16 / 9', display: 'flex', alignItems: 'center',
          justifyContent: 'center',
        }}
      >
        <img
          src={current.thumbnailUrl || current.url}
          alt={current.alt || current.name}
          style={{ maxWidth: '100%', maxHeight: '100%', objectFit: 'contain' }}
        />

        {images.length > 1 && (
          <>
            <button type="button" style={{ ...navBtnStyle, left: 12 }} onClick={goPrev} aria-label="Previous image">
              <Icon name="chevron-left" size={20} />
            </button>
            <button type="button" style={{ ...navBtnStyle, right: 12 }} onClick={goNext} aria-label="Next image">
              <Icon name="chevron-right" size={20} />
            </button>
          </>
        )}

        <div
          style={{
            position: 'absolute', bottom: 12, left: '50%', transform: 'translateX(-50%)',
            display: 'flex', gap: 6,
          }}
        >
          {images.map((_, i) => (
            <button
              key={i}
              type="button"
              onClick={() => setSelectedIndex(i)}
              style={{
                width: i === selectedIndex ? 20 : 8, height: 8, borderRadius: 'var(--radius-full)',
                border: 'none', cursor: 'pointer',
                background: i === selectedIndex ? 'var(--color-primary)' : 'rgba(255,255,255,0.5)',
                transition: 'all var(--duration-fast) var(--easing-standard)',
              }}
              aria-label={`Go to image ${i + 1}`}
            />
          ))}
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(80px, 1fr))', gap: 8 }}>
        {images.map((img, i) => (
          <button
            key={img.id}
            type="button"
            onClick={() => { setSelectedIndex(i); onPreview(img.id); }}
            style={{
              aspectRatio: '1', borderRadius: 'var(--radius-sm)', overflow: 'hidden',
              border: i === selectedIndex ? '2px solid var(--color-primary)' : '2px solid transparent',
              cursor: 'pointer', padding: 0, background: 'var(--color-bg-surface-raised)',
            }}
            aria-label={`View ${img.name}`}
          >
            <img
              src={img.thumbnailUrl || img.url}
              alt={img.alt || img.name}
              style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }}
              loading="lazy"
            />
          </button>
        ))}
      </div>

      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-medium)', color: 'var(--color-text-primary)' }}>
            {current.name}
          </span>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginLeft: 'var(--space-inline-xs)' }}>
            {selectedIndex + 1} of {images.length}
          </span>
        </div>
        {current.width && current.height && (
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
            {current.width} × {current.height} px
          </span>
        )}
      </div>
    </div>
  );
});

export default ImageGallery;
