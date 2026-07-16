import { memo, useState } from 'react';
import { Dialog } from '../../../../design-system/components/feedback/Dialog';
import { Icon } from '../../../../design-system/icons/Icon';
import { useAnnouncementListState } from '../state/useAnnouncementListState';
import {
  ANNOUNCEMENT_SORT_OPTIONS,
  CATEGORY_OPTIONS,
  PRIORITY_OPTIONS,
  STATUS_OPTIONS,
} from '../data/communicationOptions';
import type { Announcement } from '../data/communicationTypes';
import { CATEGORY_LABELS } from '../data/communicationTypes';
import { formatAudience, formatDateTime, stripHtml } from '../data/communicationFormatters';
import {
  AnnouncementCard,
  ChannelChip,
  CommEmptyState,
  CommPagination,
  CommTimeline,
  CommToolbar,
  PriorityBadge,
  StatusBadge,
} from '../components';

const CommunicationAnnouncementsPage = memo(function CommunicationAnnouncementsPage() {
  const state = useAnnouncementListState();
  const [selected, setSelected] = useState<Announcement | null>(null);

  return (
    <section id="panel-announcements" aria-labelledby="tab-announcements" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
      <CommToolbar
        search={state.filters.search}
        searchPlaceholder="Search announcements…"
        onSearchChange={(v) => state.setFilter('search', v)}
        activeFilterCount={state.activeFilterCount}
        onReset={state.resetFilters}
        selects={[
          { id: 'status', label: 'Status', value: state.filters.status, options: STATUS_OPTIONS, onChange: (v) => state.setFilter('status', v as never) },
          { id: 'category', label: 'Category', value: state.filters.category, options: CATEGORY_OPTIONS, onChange: (v) => state.setFilter('category', v as never) },
          { id: 'priority', label: 'Priority', value: state.filters.priority, options: PRIORITY_OPTIONS, onChange: (v) => state.setFilter('priority', v as never) },
          { id: 'sort', label: 'Sort', value: state.filters.sort, options: ANNOUNCEMENT_SORT_OPTIONS, onChange: (v) => state.setFilter('sort', v as never) },
        ]}
        actions={
          <label style={{ display: 'inline-flex', alignItems: 'center', gap: 'var(--space-1)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', cursor: 'pointer' }}>
            <input type="checkbox" checked={state.filters.pinnedOnly} onChange={(e) => state.setFilter('pinnedOnly', e.target.checked)} />
            Pinned only
          </label>
        }
      />

      {state.paged.length === 0 ? (
        <CommEmptyState icon="speaker" title="No announcements found" description="Try adjusting your filters or search terms." />
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(360px, 1fr))', gap: 'var(--space-4)' }}>
          {state.paged.map((a) => (
            <AnnouncementCard key={a.id} announcement={a} onOpen={setSelected} />
          ))}
        </div>
      )}

      <CommPagination
        page={state.page}
        pageCount={state.pageCount}
        pageSize={state.pageSize}
        total={state.total}
        onPageChange={state.setPage}
        onPageSizeChange={state.setPageSize}
      />

      {selected && (
        <Dialog
          open={!!selected}
          onClose={() => setSelected(null)}
          title={selected.title}
          size="lg"
        >
          <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-4)' }}>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)', alignItems: 'center' }}>
              <StatusBadge status={selected.status} />
              <PriorityBadge priority={selected.priority} />
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
                {CATEGORY_LABELS[selected.category]}
              </span>
            </div>

            <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', lineHeight: 1.6 }}>
              {stripHtml(selected.body)}
            </p>

            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-3)', fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
              <span><Icon name="user" size={13} /> {selected.author}</span>
              <span><Icon name="users" size={13} /> {formatAudience(selected.audience)}</span>
              <span><Icon name="calendar" size={13} /> {formatDateTime(selected.publishedAt ?? selected.createdAt)}</span>
            </div>

            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-2)' }}>
              {selected.channels.map((ch) => <ChannelChip key={ch} channel={ch} />)}
            </div>

            {selected.attachments.length > 0 && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Attachments (placeholder)</span>
                {selected.attachments.map((att) => (
                  <div key={att.id} style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
                    <Icon name="paperclip" size={14} /> {att.name} · {att.sizeLabel}
                  </div>
                ))}
              </div>
            )}

            <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
              <span style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>Activity timeline</span>
              <CommTimeline events={selected.timeline} />
            </div>
          </div>
        </Dialog>
      )}
    </section>
  );
});

export default CommunicationAnnouncementsPage;
