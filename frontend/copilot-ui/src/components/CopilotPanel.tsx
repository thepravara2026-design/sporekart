import { useRef, useEffect, useState, useCallback, type FormEvent, type KeyboardEvent } from 'react';
import { type CopilotMessage, type Suggestion, type CopilotPersona, type CopilotStatus } from '../types';
import { CopilotHeader } from './CopilotHeader';
import { CopilotMessageBubble } from './CopilotMessageBubble';
import { CopilotStreamingRenderer } from './CopilotStreamingRenderer';
import { CopilotSuggestionChips } from './CopilotSuggestionChips';

interface CopilotPanelProps {
  messages: CopilotMessage[];
  onSend: (content: string) => void;
  onClose: () => void;
  onMinimize?: () => void;
  isStreaming: boolean;
  streamingContent?: string;
  suggestions: Suggestion[];
  persona: CopilotPersona;
  status: CopilotStatus;
  copilotName: string;
  onSuggestionClick: (suggestion: Suggestion) => void;
}

export function CopilotPanel({
  messages,
  onSend,
  onClose,
  onMinimize,
  isStreaming,
  streamingContent,
  suggestions,
  persona,
  status,
  copilotName,
  onSuggestionClick,
}: CopilotPanelProps) {
  const [input, setInput] = useState('');
  const listRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLTextAreaElement>(null);

  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTop = listRef.current.scrollHeight;
    }
  }, [messages, streamingContent]);

  useEffect(() => {
    if (!isStreaming && inputRef.current) {
      inputRef.current.focus();
    }
  }, [isStreaming]);

  const handleSubmit = useCallback(
    (e?: FormEvent) => {
      e?.preventDefault();
      const trimmed = input.trim();
      if (!trimmed) return;
      onSend(trimmed);
      setInput('');
    },
    [input, onSend],
  );

  const handleKeyDown = useCallback(
    (e: KeyboardEvent<HTMLTextAreaElement>) => {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        handleSubmit();
      }
    },
    [handleSubmit],
  );

  return (
    <div
      id="copilot-panel"
      className="cp-panel"
      role="dialog"
      aria-label={`${copilotName} copilot`}
      aria-hidden={false}
    >
      <CopilotHeader
        name={copilotName}
        persona={persona}
        status={status}
        onClose={onClose}
        onMinimize={onMinimize}
      />

      <div className="cp-panel__messages" ref={listRef} role="list" aria-label="Conversation">
        {messages.map((msg) => (
          <CopilotMessageBubble key={msg.id} message={msg} />
        ))}
        {isStreaming && streamingContent != null && (
          <CopilotStreamingRenderer content={streamingContent} isStreaming={isStreaming} />
        )}
        {messages.length === 0 && !isStreaming && (
          <div className="cp-panel__empty">
            <span className="cp-panel__empty-icon" aria-hidden="true">{'\uD83E\uDD16'}</span>
            <p>How can I help you?</p>
          </div>
        )}
      </div>

      <div className="cp-panel__input-area">
        <CopilotSuggestionChips suggestions={suggestions} onSelect={onSuggestionClick} />
        <form className="cp-panel__form" onSubmit={handleSubmit}>
          <textarea
            ref={inputRef}
            className="cp-panel__input"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="Type a message..."
            rows={1}
            aria-label="Message input"
          />
          <button
            type="submit"
            className="cp-panel__send-btn"
            disabled={!input.trim() || isStreaming}
            aria-label="Send message"
          >
            Send
          </button>
        </form>
      </div>
    </div>
  );
}
