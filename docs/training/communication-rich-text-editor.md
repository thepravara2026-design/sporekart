# Enterprise Communication Platform — Rich Text Editor

**Sprint 26 · Part 10.** Design of the mock rich text editor
(`components/RichTextEditor.tsx` + `state/useRichTextEditorState.ts`).

## 1. Purpose and Non-Goals

The editor is a **mock authoring surface**. It demonstrates inline formatting and block
structure and renders a sanitized HTML preview. It deliberately does NOT:

- persist content to any store or backend,
- support file/image uploads,
- call any API,
- guarantee the preview reflects a real deliverable.

Formatting is illustrative only.

## 2. State Model (`useRichTextEditorState`)

```ts
type InlineMark = 'bold' | 'italic' | 'underline';
type BlockKind   = 'paragraph' | 'heading' | 'bullet' | 'quote';

interface EditorSnapshot {
  text: string;
  block: BlockKind;
  marks: InlineMark[];
}

interface UseRichTextEditorState {
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
```

State is held in three `useState` values (`value`, `block`, `activeMarks`). All setters are
`useCallback`d. Derived values are memoised:

- `charCount = value.length` (cheap, direct).
- `wordCount` via `useMemo` (whitespace split).
- `previewHtml` via `useMemo` calling `buildPreview`.
- `snapshot` via `useMemo` for callers needing a serialisable view.

## 3. Sanitization (`buildPreview`)

```ts
function escapeHtml(text: string): string {
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function buildPreview(text: string, block: BlockKind, marks: InlineMark[]): string {
  const safe = escapeHtml(text).replace(/\n/g, '<br />');
  let inner = safe;
  if (marks.includes('bold'))      inner = `<strong>${inner}</strong>`;
  if (marks.includes('italic'))    inner = `<em>${inner}</em>`;
  if (marks.includes('underline'))  inner = `<u>${inner}</u>`;
  switch (block) {
    case 'heading': return `<h4>${inner}</h4>`;
    case 'bullet':  return `<ul><li>${inner}</li></ul>`;
    case 'quote':   return `<blockquote>${inner}</blockquote>`;
    case 'paragraph':
    default:        return `<p>${inner}</p>`;
  }
}
```

The text is **HTML-escaped first**, then mark wrappers are applied. This guarantees user input
can never inject raw tags — the only HTML in the preview is the fixed, safe wrapper markup.

## 4. Supported Marks and Blocks

| Kind | Values | Toolbar buttons |
| --- | --- | --- |
| Inline marks | `bold`, `italic`, `underline` | toggle buttons (`aria-pressed`) |
| Block kinds | `paragraph`, `heading`, `bullet`, `quote` | toggle buttons (`aria-pressed`) |

Marks can be combined (bold + italic + underline). Only one block kind is active at a time.

## 5. Toolbar and Preview

- Toolbar uses `role="toolbar"` with `aria-label="Formatting"` and wraps
  (`flexWrap: 'wrap'`).
- Mark buttons and block buttons render with `aria-pressed` reflecting active state; a clear
  button resets value/block/marks.
- The textarea is labeled (`htmlFor="rte-textarea"`), `rows={5}`, resizable.
- The preview region is `aria-live="polite"` and renders `previewHtml` via
  `dangerouslySetInnerHTML`. Safe because `previewHtml` is escaped before formatting.
- A word/char counter is shown inline; a helper line states formatting is illustrative and not
  persisted.

## 6. Usage

Rendered inside the template preview `Dialog` (`CommunicationTemplatesPage`) with
`initialValue` seeded from the template body (HTML stripped). Editing there is purely local and
disappears when the dialog closes.

## 7. Explicit Non-Goals (recap)

| Non-goal | Reason |
| --- | --- |
| No persistence | Mock mode; nothing is saved. |
| No uploads | No attachment pipeline exists. |
| No backend/API | No network calls in feature tree. |
| Formatting illustrative | Preview is a demo, not a deliverable. |
