import { useState, useCallback } from 'react';
import { type CopilotMessage } from '../types';

interface CopilotMessageBubbleProps {
  message: CopilotMessage;
}

function formatTimestamp(ts: string): string {
  try {
    const d = new Date(ts);
    return d.toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit' });
  } catch {
    return '';
  }
}

function SimpleMarkdown({ content }: { content: string }) {
  const html = content
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/```(\w*)\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    .replace(/\n/g, '<br>');

  return <span className="cp-bubble__md" dangerouslySetInnerHTML={{ __html: html }} />;
}

export function CopilotMessageBubble({ message }: CopilotMessageBubbleProps) {
  const [copied, setCopied] = useState(false);

  const handleCopy = useCallback(() => {
    navigator.clipboard.writeText(message.content).then(() => {
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    });
  }, [message.content]);

  const isUser = message.role === 'user';
  const isSystem = message.role === 'system';

  return (
    <div
      className={`cp-bubble cp-bubble--${message.role}`}
      role="listitem"
      aria-label={`${message.role} message`}
    >
      {!isUser && (
        <div className="cp-bubble__avatar" aria-hidden="true">
          {isSystem ? '\u2699' : '\uD83E\uDD16'}
        </div>
      )}
      <div className="cp-bubble__body">
        <div className="cp-bubble__content">
          {isUser ? (
            <span>{message.content}</span>
          ) : (
            <SimpleMarkdown content={message.content} />
          )}
        </div>
        <div className="cp-bubble__footer">
          <span className="cp-bubble__time">{formatTimestamp(message.timestamp)}</span>
          {!isUser && (
            <button
              type="button"
              className="cp-bubble__copy-btn"
              onClick={handleCopy}
              aria-label={copied ? 'Copied' : 'Copy message'}
            >
              {copied ? '\u2713' : '\uD83D\uDCCB'}
            </button>
          )}
        </div>
      </div>
      {isUser && (
        <div className="cp-bubble__avatar cp-bubble__avatar--user" aria-hidden="true">
          {'\uD83D\uDC64'}
        </div>
      )}
    </div>
  );
}
