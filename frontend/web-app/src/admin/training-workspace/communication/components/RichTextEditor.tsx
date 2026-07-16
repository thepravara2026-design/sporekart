import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { useRichTextEditorState } from '../state/useRichTextEditorState';
import type { BlockKind, InlineMark } from '../state/useRichTextEditorState';

export interface RichTextEditorProps {
  initialValue?: string;
  label?: string;
  helperText?: string;
}

const MARK_BUTTONS: Array<{ mark: InlineMark; icon: string; label: string }> = [
  { mark: 'bold', icon: 'type', label: 'Bold' },
  { mark: 'italic', icon: 'type', label: 'Italic' },
  { mark: 'underline', icon: 'type', label: 'Underline' },
];

const BLOCK_BUTTONS: Array<{ block: BlockKind; icon: string; label: string }> = [
  { block: 'paragraph', icon: 'file', label: 'Paragraph' },
  { block: 'heading', icon: 'type', label: 'Heading' },
  { block: 'bullet', icon: 'list', label: 'Bullet list' },
  { block: 'quote', icon: 'message-square', label: 'Quote' },
];

const toolbarBtn = (active: boolean): React.CSSProperties => ({
  display: 'inline-flex',
  alignItems: 'center',
  gap: 4,
  padding: '4px 8px',
  borderRadius: 'var(--radius-sm)',
  border: `1px solid ${active ? 'var(--color-primary)' : 'var(--color-border-default)'}`,
  background: active ? 'var(--color-bg-primary-weak)' : 'var(--color-bg-surface-default)',
  color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
  fontSize: 'var(--text-caption)',
  fontWeight: 500,
  cursor: 'pointer',
});

/**
 * Mock rich text editor. Formatting is illustrative only — it toggles inline
 * marks and block kind that drive a sanitized HTML preview. No content is
 * persisted and no uploads are supported (Mock Mode).
 */
const RichTextEditor = memo(function RichTextEditor({
  initialValue = '',
  label = 'Message body',
  helperText = 'Mock editor — formatting is illustrative and not persisted.',
}: RichTextEditorProps) {
  const editor = useRichTextEditorState(initialValue);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-2)' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-2)' }}>
        <label htmlFor="rte-textarea" style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
          {label}
        </label>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
          {editor.wordCount} words · {editor.charCount} chars
        </span>
      </div>

      <div
        role="toolbar"
        aria-label="Formatting"
        style={{
          display: 'flex', flexWrap: 'wrap', gap: 'var(--space-1)',
          padding: 'var(--space-2)', borderRadius: 'var(--radius-md) var(--radius-md) 0 0',
          border: '1px solid var(--color-border-default)', borderBottom: 'none',
          background: 'var(--color-bg-surface-muted)',
        }}
      >
        {MARK_BUTTONS.map((b) => (
          <button
            key={b.mark}
            type="button"
            aria-pressed={editor.activeMarks.includes(b.mark)}
            title={b.label}
            onClick={() => editor.toggleMark(b.mark)}
            style={toolbarBtn(editor.activeMarks.includes(b.mark))}
          >
            <Icon name={b.icon} size={14} />{b.label[0]}
          </button>
        ))}
        <span aria-hidden style={{ width: 1, background: 'var(--color-border-default)', margin: '0 var(--space-1)' }} />
        {BLOCK_BUTTONS.map((b) => (
          <button
            key={b.block}
            type="button"
            aria-pressed={editor.block === b.block}
            title={b.label}
            onClick={() => editor.setBlock(b.block)}
            style={toolbarBtn(editor.block === b.block)}
          >
            <Icon name={b.icon} size={14} />{b.label}
          </button>
        ))}
        <button
          type="button"
          title="Clear"
          onClick={editor.clear}
          style={{ ...toolbarBtn(false), marginLeft: 'auto' }}
        >
          <Icon name="trash" size={14} />Clear
        </button>
      </div>

      <textarea
        id="rte-textarea"
        value={editor.value}
        onChange={(e) => editor.setValue(e.target.value)}
        rows={5}
        placeholder="Write your message…"
        style={{
          width: '100%', resize: 'vertical', padding: 'var(--space-3)',
          border: '1px solid var(--color-border-default)', borderRadius: '0 0 var(--radius-md) var(--radius-md)',
          background: 'var(--color-bg-surface-default)', color: 'var(--color-text-primary)',
          fontSize: 'var(--text-body-sm)', fontFamily: 'inherit', lineHeight: 1.5,
        }}
      />

      <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-1)' }}>
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>Preview</span>
        <div
          aria-live="polite"
          style={{
            padding: 'var(--space-3)', minHeight: 48,
            border: '1px dashed var(--color-border-default)', borderRadius: 'var(--radius-md)',
            background: 'var(--color-bg-surface-muted)', color: 'var(--color-text-secondary)',
            fontSize: 'var(--text-body-sm)',
          }}
          dangerouslySetInnerHTML={{ __html: editor.previewHtml }}
        />
      </div>

      <p style={{ margin: 0, fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>{helperText}</p>
    </div>
  );
});

export default RichTextEditor;
