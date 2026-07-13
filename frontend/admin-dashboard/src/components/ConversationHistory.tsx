import { useState } from 'react';
import { Conversation } from '../types';
import FeedbackDialog from './FeedbackDialog';

const CONVERSATIONS: Conversation[] = Array.from({ length: 25 }, (_, i) => ({
  id: String(i + 1),
  title: `Conversation ${i + 1}`,
  intent: ['Sales Query', 'Report Gen', 'Data Sync', 'Alert Config', 'Backup'][i % 5],
  copilotUsed: ['Sales Copilot', 'Report Copilot', 'Sync Copilot', 'Alert Copilot', 'Backup Copilot'][i % 5],
  messageCount: Math.floor(Math.random() * 20) + 3,
  createdAt: new Date(Date.now() - i * 3600000).toISOString(),
}));

const PAGE_SIZE = 8;

function ConversationHistory() {
  const [page, setPage] = useState(1);
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const totalPages = Math.ceil(CONVERSATIONS.length / PAGE_SIZE);
  const start = (page - 1) * PAGE_SIZE;
  const pageItems = CONVERSATIONS.slice(start, start + PAGE_SIZE);

  return (
    <div className="conversation-history-page">
      <h2>Conversation History</h2>
      <div className="history-list">
        {pageItems.map((c) => (
          <div key={c.id} className="history-item">
            <div className="history-item-main">
              <div className="history-item-info">
                <span className="history-title">{c.title}</span>
                <span className="history-intent">{c.intent}</span>
                <span className="history-copilot">{c.copilotUsed}</span>
                <span className="history-date">
                  {new Date(c.createdAt).toLocaleDateString()}
                </span>
                <span className="history-messages">{c.messageCount} msgs</span>
              </div>
              <button className="btn btn-secondary" onClick={() => setSelectedId(c.id)}>
                Feedback
              </button>
            </div>
          </div>
        ))}
      </div>
      <div className="pagination">
        <button disabled={page <= 1} onClick={() => setPage(page - 1)}>
          Previous
        </button>
        <span>
          Page {page} of {totalPages}
        </span>
        <button disabled={page >= totalPages} onClick={() => setPage(page + 1)}>
          Next
        </button>
      </div>
      {selectedId && (
        <FeedbackDialog
          conversationId={selectedId}
          onClose={() => setSelectedId(null)}
          onSubmit={(fb) => {
            console.log('Feedback submitted:', fb);
            setSelectedId(null);
          }}
        />
      )}
    </div>
  );
}

export default ConversationHistory;
