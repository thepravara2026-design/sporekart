interface CopilotStreamingRendererProps {
  content: string;
  isStreaming: boolean;
}

export function CopilotStreamingRenderer({ content, isStreaming }: CopilotStreamingRendererProps) {
  const html = content
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/```(\w*)\n([\s\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    .replace(/\*([^*]+)\*/g, '<em>$1</em>')
    .replace(/\n/g, '<br>');

  return (
    <div className="cp-streaming" role="status" aria-live="polite">
      <div className="cp-bubble cp-bubble--assistant">
        <div className="cp-bubble__avatar" aria-hidden="true">{'\uD83E\uDD16'}</div>
        <div className="cp-bubble__body">
          <div className="cp-bubble__content">
            <span className="cp-streaming__text" dangerouslySetInnerHTML={{ __html: html }} />
            {isStreaming && <span className="cp-streaming__cursor" aria-hidden="true">|</span>}
          </div>
        </div>
      </div>
    </div>
  );
}
