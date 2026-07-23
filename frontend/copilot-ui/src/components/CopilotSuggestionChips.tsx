import { useState, useRef, useCallback } from 'react';
import { type Suggestion } from '../types';

interface CopilotSuggestionChipsProps {
  suggestions: Suggestion[];
  onSelect: (suggestion: Suggestion) => void;
  maxVisible?: number;
}

export function CopilotSuggestionChips({ suggestions, onSelect, maxVisible = 5 }: CopilotSuggestionChipsProps) {
  const [showAll, setShowAll] = useState(false);
  const scrollRef = useRef<HTMLDivElement>(null);

  const visible = showAll ? suggestions : suggestions.slice(0, maxVisible);
  const hasMore = suggestions.length > maxVisible;

  const handleKeyDown = useCallback(
    (suggestion: Suggestion) => (e: React.KeyboardEvent) => {
      if (e.key === 'Enter' || e.key === ' ') {
        e.preventDefault();
        onSelect(suggestion);
      }
    },
    [onSelect],
  );

  if (suggestions.length === 0) return null;

  return (
    <div className="cp-chips" role="region" aria-label="Suggestions">
      <div className="cp-chips__scroll" ref={scrollRef} role="list">
        {visible.map((s, i) => (
          <button
            key={`${s.action}-${i}`}
            type="button"
            className="cp-chips__chip"
            onClick={() => onSelect(s)}
            onKeyDown={handleKeyDown(s)}
            role="listitem"
            aria-label={s.label}
          >
            {s.label}
          </button>
        ))}
      </div>
      {hasMore && !showAll && (
        <button
          type="button"
          className="cp-chips__more"
          onClick={() => setShowAll(true)}
          aria-label={`Show ${suggestions.length - maxVisible} more suggestions`}
        >
          +{suggestions.length - maxVisible} more
        </button>
      )}
    </div>
  );
}
