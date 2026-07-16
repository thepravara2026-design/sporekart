import type { DocumentMetadata } from '../types';
import { DOCUMENT_TYPES } from '../types';

interface DocumentStatusCardProps {
  documents: DocumentMetadata[];
}

const statusMap: Record<string, { label: string; color: string }> = {
  'not-uploaded': { label: 'Not Uploaded', color: '#d1d5db' },
  'uploaded': { label: 'Uploaded', color: '#f59e0b' },
  'verified': { label: 'Verified', color: '#16a34a' },
  'rejected': { label: 'Rejected', color: '#dc2626' },
};

export function DocumentStatusCard({ documents }: DocumentStatusCardProps) {
  return (
    <div className="document-status-card">
      <div className="document-status-card__header">
        <h3 className="document-status-card__title">Documents</h3>
        <span className="document-status-card__count">
          {documents.filter((d) => d.status !== 'not-uploaded').length}/{documents.length}
        </span>
      </div>

      <div className="document-status-card__list">
        {DOCUMENT_TYPES.map((docType) => {
          const doc = documents.find((d) => d.type === docType.value);
          const status = doc ? statusMap[doc.status] : statusMap['not-uploaded'];
          return (
            <div key={docType.value} className="document-status-card__item">
              <div className="document-status-card__item-icon">{docType.label[0]}</div>
              <div className="document-status-card__item-info">
                <span className="document-status-card__item-label">{docType.label}</span>
                {doc?.uploadedAt && (
                  <span className="document-status-card__item-date">
                    Uploaded: {doc.uploadedAt}
                  </span>
                )}
              </div>
              <div
                className="document-status-card__item-status"
                style={{ backgroundColor: status.color, color: '#fff' }}
              >
                {status.label}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
