# Upload Components

## FileUpload

Complete file upload control with validation, drag-and-drop, and preview.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `name` | `string` | — | Field name |
| `accept` | `string` | — | Accepted MIME types |
| `multiple` | `boolean` | `false` | Allow multiple files |
| `maxSize` | `number` | `5 * 1024 * 1024` | Max file size in bytes |
| `maxCount` | `number` | `1` | Max files (when multiple) |
| `disabled` | `boolean` | `false` | Disable upload |

### Usage

```tsx
<FileUpload
  name="documents"
  accept=".pdf,.doc,.docx"
  multiple
  maxSize={10 * 1024 * 1024}
  maxCount={5}
/>
```

## DropZone

A standalone drag-and-drop target with keyboard support.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `onDrop` | `(files: File[]) => void` | — | Drop handler |
| `accept` | `string` | — | Accepted MIME types |
| `multiple` | `boolean` | `true` | Allow multiple files |
| `maxSize` | `number` | — | Max file size |
| `disabled` | `boolean` | `false` | Disable drops |
| `children` | `ReactNode` | — | Custom content |

### Usage

```tsx
<DropZone onDrop={handleDrop} accept="image/*" maxSize={5 * 1024 * 1024}>
  <p>Drag and drop images here, or click to browse</p>
</DropZone>
```

Features:
- Drag-over visual feedback
- Click to open file picker
- Keyboard Enter/Space to activate
- File size and type validation on drop
- `aria-label` for screen readers

## FilePreview

Displays individual file information with progress and actions.

### Props

| Prop | Type | Default | Description |
|---|---|---|---|
| `file` | `File` | — | File object |
| `name` | `string` | file.name | Display name |
| `size` | `number` | file.size | File size in bytes |
| `progress` | `number` | — | Upload progress (0-100) |
| `error` | `string` | — | Error message |
| `onRemove` | `() => void` | — | Remove handler |
| `type` | `'list' \| 'grid' \| 'compact'` | `'list'` | Display variant |

### Usage

```tsx
{files.map((file) => (
  <FilePreview
    key={file.name}
    file={file}
    progress={uploadProgress[file.name]}
    onRemove={() => removeFile(file.name)}
  />
))}
```

### File Size Limits

| Context | Default | Configurable |
|---|---|---|
| Single file | 5 MB | `maxSize` prop |
| Total (multiple) | 25 MB | — |
| Images | 10 MB | — |
| Documents (PDF/DOC) | 10 MB | — |

### Type Restrictions

| accept value | Allowed types |
|---|---|
| `image/*` | PNG, JPG, GIF, WEBP, SVG |
| `application/pdf` | PDF |
| `.pdf,.doc,.docx` | PDF, Word documents |
| `.xls,.xlsx` | Excel spreadsheets |
| `.csv` | CSV files |

### Progress Simulation

```tsx
const [progress, setProgress] = useState(0);

useEffect(() => {
  if (isUploading) {
    const timer = setInterval(() => {
      setProgress((p) => Math.min(p + 10, 90));
    }, 200);
    return () => clearInterval(timer);
  }
}, [isUploading]);
```

## Accessibility

- DropZone is keyboard operable (Enter/Space to browse)
- `role="button"` with `tabIndex={0}` on DropZone
- FilePreview has remove button with `aria-label="Remove {filename}"`
- Progress indicators have `role="progressbar"` with `aria-valuenow`
- Error messages linked via `aria-describedby`
- Focus moves to next file after removal
- Live region announces upload completion
