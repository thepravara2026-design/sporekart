// ---------------------------------------------------------------------------
// Enterprise Communication Platform — Mock Rich Text Editor state
// Sprint 26 · Part 10. In-memory only. No persistence, no uploads, no backend.
// Tracks a lightweight document model and applies mock inline formatting.
// ---------------------------------------------------------------------------

import { useCallback, useMemo, useState } from 'react';

export type InlineMark = 'bold' | 'italic' | 'underline';
export type BlockKind = 'paragraph' | 'heading' | 'bullet' | 'quote';

export interface EditorSnapshot {
  text: string;
  block: BlockKind;
  marks: InlineMark[];
}

export interface UseRichTextEditorState {
  value: string;
  block: BlockKind;
  activeMarks: InlineMark[];
  setValue: (text: string) => void;
  setBlock: (block: BlockKind) => void;
  toggleMark: (mark: InlineMark) => void;
  clear: () => void;
  charCount: number;
  wordCount: number;
  previewHtml: string;
  snapshot: EditorSnapshot;
}

function escapeHtml(text: string): string {
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

// Builds a sanitized preview from plain text + block/mark state (mock only).
function buildPreview(text: string, block: BlockKind, marks: InlineMark[]): string {
  const safe = escapeHtml(text).replace(/\n/g, '<br />');
  let inner = safe;
  if (marks.includes('bold')) inner = `<strong>${inner}</strong>`;
  if (marks.includes('italic')) inner = `<em>${inner}</em>`;
  if (marks.includes('underline')) inner = `<u>${inner}</u>`;
  switch (block) {
    case 'heading':
      return `<h4>${inner}</h4>`;
    case 'bullet':
      return `<ul><li>${inner}</li></ul>`;
    case 'quote':
      return `<blockquote>${inner}</blockquote>`;
    case 'paragraph':
    default:
      return `<p>${inner}</p>`;
  }
}

export function useRichTextEditorState(initial = ''): UseRichTextEditorState {
  const [value, setValueRaw] = useState(initial);
  const [block, setBlock] = useState<BlockKind>('paragraph');
  const [activeMarks, setActiveMarks] = useState<InlineMark[]>([]);

  const setValue = useCallback((text: string) => setValueRaw(text), []);

  const toggleMark = useCallback((mark: InlineMark) => {
    setActiveMarks((prev) =>
      prev.includes(mark) ? prev.filter((m) => m !== mark) : [...prev, mark],
    );
  }, []);

  const clear = useCallback(() => {
    setValueRaw('');
    setBlock('paragraph');
    setActiveMarks([]);
  }, []);

  const charCount = value.length;
  const wordCount = useMemo(
    () => (value.trim() ? value.trim().split(/\s+/).length : 0),
    [value],
  );

  const previewHtml = useMemo(
    () => buildPreview(value, block, activeMarks),
    [value, block, activeMarks],
  );

  const snapshot = useMemo<EditorSnapshot>(
    () => ({ text: value, block, marks: activeMarks }),
    [value, block, activeMarks],
  );

  return {
    value,
    block,
    activeMarks,
    setValue,
    setBlock,
    toggleMark,
    clear,
    charCount,
    wordCount,
    previewHtml,
    snapshot,
  };
}
