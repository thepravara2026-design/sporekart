import { memo, useMemo, useState } from 'react';
import { Dialog } from '../../../../design-system/components/feedback/Dialog';
import { MOCK_TEMPLATES } from '../data/communicationMockData';
import { TEMPLATE_KIND_LABELS } from '../data/communicationTypes';
import type { MessageTemplate, TemplateKind } from '../data/communicationTypes';
import type { SelectOption } from '../data/communicationOptions';
import { stripHtml } from '../data/communicationFormatters';
import { CommEmptyState, CommToolbar, RichTextEditor, TemplateCard } from '../components';

const KIND_OPTIONS: SelectOption<TemplateKind | 'active' | 'future'>[] = [
  { value: 'active', label: 'Active only' },
  { value: 'future', label: 'Future only' },
  ...(Object.keys(TEMPLATE_KIND_LABELS) as TemplateKind[]).map((k) => ({ value: k, label: TEMPLATE_KIND_LABELS[k] })),
];

const CommunicationTemplatesPage = memo(function CommunicationTemplatesPage() {
  const [search, setSearch] = useState('');
  const [kind, setKind] = useState('');
  const [preview, setPreview] = useState<MessageTemplate | null>(null);

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    return MOCK_TEMPLATES.filter((t) => {
      if (q && !`${t.name} ${t.subject}`.toLowerCase().includes(q)) return false;
      if (kind === 'active') return !t.future;
      if (kind === 'future') return t.future;
      if (kind && t.kind !== kind) return false;
      return true;
    });
  }, [search, kind]);

  return (
    <section id="panel-templates" aria-labelledby="tab-templates" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <CommToolbar
        search={search}
        searchPlaceholder="Search templates…"
        onSearchChange={setSearch}
        activeFilterCount={kind ? 1 : 0}
        onReset={() => { setSearch(''); setKind(''); }}
        selects={[{ id: 'kind', label: 'Type', value: kind, options: KIND_OPTIONS, onChange: setKind }]}
      />

      {filtered.length === 0 ? (
        <CommEmptyState icon="file" title="No templates found" description="Adjust filters to see reusable message templates." />
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 'var(--space-4)' }}>
          {filtered.map((t) => <TemplateCard key={t.id} template={t} onPreview={setPreview} />)}
        </div>
      )}

      {preview && (
        <Dialog open={!!preview} onClose={() => setPreview(null)} title={`Preview · ${preview.name}`} size="lg">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
            {preview.subject && preview.subject !== '—' && (
              <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                <strong style={{ color: 'var(--color-text-primary)' }}>Subject:</strong> {preview.subject}
              </p>
            )}
            <div
              style={{
                padding: 'var(--space-3)', borderRadius: 'var(--radius-md)',
                background: 'var(--color-bg-surface-muted)', border: '1px solid var(--color-border-default)',
                fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 1.6,
              }}
            >
              {stripHtml(preview.body)}
            </div>
            <RichTextEditor
              initialValue={stripHtml(preview.body)}
              label="Edit copy (mock)"
              helperText="Editing is illustrative only and is not saved (Mock Mode)."
            />
          </div>
        </Dialog>
      )}
    </section>
  );
});

export default CommunicationTemplatesPage;
