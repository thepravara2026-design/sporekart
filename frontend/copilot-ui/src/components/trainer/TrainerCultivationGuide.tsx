import { useState } from 'react';
import type { PracticalGuide } from './types/trainer';

const categories = ['All', 'Mushroom', 'Vegetable', 'Fruit', 'Herb', 'Soil Prep'];

export function TrainerCultivationGuide({ guides }: { guides: PracticalGuide[] }) {
  const [activeCategory, setActiveCategory] = useState('All');
  const [selectedGuide, setSelectedGuide] = useState<PracticalGuide | null>(null);
  const [checkedMaterials, setCheckedMaterials] = useState<Set<string>>(new Set());

  const filtered = activeCategory === 'All'
    ? guides
    : guides.filter(g => g.category === activeCategory);

  const toggleMaterial = (item: string) => {
    setCheckedMaterials(prev => {
      const next = new Set(prev);
      if (next.has(item)) next.delete(item); else next.add(item);
      return next;
    });
  };

  return (
    <div style={{ border: '1px solid var(--cp-color-border)', borderRadius: 8, background: 'var(--cp-color-surface)', overflow: 'hidden' }}>
      <div style={{ padding: '12px 16px', borderBottom: '1px solid var(--cp-color-border)' }}>
        <div style={{ display: 'flex', gap: 6, flexWrap: 'wrap' }}>
          {categories.map(cat => (
            <button key={cat} onClick={() => setActiveCategory(cat)}
              style={{
                background: activeCategory === cat ? 'var(--cp-color-primary)' : 'transparent',
                color: activeCategory === cat ? '#fff' : 'var(--cp-color-text-secondary)',
                border: '1px solid var(--cp-color-border)', borderRadius: 4,
                padding: '4px 12px', fontSize: 12, cursor: 'pointer', fontWeight: 500,
              }}>
              {cat}
            </button>
          ))}
        </div>
      </div>

      <div style={{ display: 'flex', gap: 16, padding: 16 }}>
        <div style={{ flex: '0 0 240px', maxHeight: 320, overflowY: 'auto' }}>
          {filtered.length === 0 && (
            <div style={{ fontSize: 13, color: 'var(--cp-color-text-muted)', padding: 8 }}>No guides found</div>
          )}
          {filtered.map(guide => (
            <div key={guide.guideId} onClick={() => setSelectedGuide(guide)}
              style={{
                padding: '8px 12px', borderRadius: 6, cursor: 'pointer', marginBottom: 4,
                background: selectedGuide?.guideId === guide.guideId ? 'var(--cp-color-primary-weak)' : 'transparent',
                color: 'var(--cp-color-text)', fontSize: 13, fontWeight: 500,
                border: selectedGuide?.guideId === guide.guideId ? '1px solid var(--cp-color-primary)' : '1px solid transparent',
              }}>
              {guide.title}
              <div style={{ fontSize: 11, color: 'var(--cp-color-text-muted)', fontWeight: 400 }}>{guide.difficulty}</div>
            </div>
          ))}
        </div>

        <div style={{ flex: 1, minWidth: 0 }}>
          {selectedGuide ? (
            <>
              <h3 style={{ margin: '0 0 8px', fontSize: 16, color: 'var(--cp-color-text)' }}>{selectedGuide.title}</h3>
              <div style={{ fontSize: 12, color: 'var(--cp-color-text-secondary)', marginBottom: 12 }}>
                Difficulty: <strong>{selectedGuide.difficulty}</strong> | Category: {selectedGuide.category}
              </div>

              {selectedGuide.safetyPrecautions.length > 0 && (
                <div style={{ background: '#fff8e1', border: '1px solid #ffe082', borderRadius: 6, padding: '8px 12px', marginBottom: 12 }}>
                  <div style={{ fontSize: 12, fontWeight: 700, color: '#f57f17', marginBottom: 4 }}>⚠ Safety Precautions</div>
                  <ul style={{ margin: 0, paddingLeft: 16, fontSize: 12, color: '#795548' }}>
                    {selectedGuide.safetyPrecautions.map((s, i) => <li key={i}>{s}</li>)}
                  </ul>
                </div>
              )}

              {selectedGuide.requiredMaterials.length > 0 && (
                <div style={{ marginBottom: 12 }}>
                  <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-text)', marginBottom: 4 }}>Materials Checklist</div>
                  {selectedGuide.requiredMaterials.map((m, i) => (
                    <label key={i} style={{ display: 'flex', alignItems: 'center', gap: 6, padding: '3px 0', fontSize: 12, color: 'var(--cp-color-text-secondary)', cursor: 'pointer' }}>
                      <input type="checkbox" checked={checkedMaterials.has(m)} onChange={() => toggleMaterial(m)}
                        style={{ accentColor: 'var(--cp-color-primary)' }} />
                      <span style={{ textDecoration: checkedMaterials.has(m) ? 'line-through' : 'none', color: checkedMaterials.has(m) ? 'var(--cp-color-text-muted)' : 'var(--cp-color-text-secondary)' }}>
                        {m}
                      </span>
                    </label>
                  ))}
                </div>
              )}

              <div style={{ marginBottom: 12 }}>
                <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-text)', marginBottom: 4 }}>Steps</div>
                <ol style={{ margin: 0, paddingLeft: 20, fontSize: 12, color: 'var(--cp-color-text-secondary)', lineHeight: 1.8 }}>
                  {selectedGuide.steps.map((step, i) => <li key={i}>{step}</li>)}
                </ol>
              </div>

              {selectedGuide.commonMistakes.length > 0 && (
                <div>
                  <div style={{ fontSize: 12, fontWeight: 600, color: 'var(--cp-color-danger)', marginBottom: 4 }}>Common Mistakes</div>
                  <ul style={{ margin: 0, paddingLeft: 16, fontSize: 12, color: 'var(--cp-color-text-secondary)' }}>
                    {selectedGuide.commonMistakes.map((m, i) => <li key={i}>{m}</li>)}
                  </ul>
                </div>
              )}
            </>
          ) : (
            <div style={{ fontSize: 13, color: 'var(--cp-color-text-muted)', textAlign: 'center', padding: 40 }}>
              Select a guide from the list
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
