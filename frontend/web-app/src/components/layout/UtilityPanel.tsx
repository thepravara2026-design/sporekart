export default function UtilityPanel({ open }: { open: boolean }) {
  if (!open) return null;
  return (
    <aside className="sk-utility" aria-label="Utilities">
      <div className="sk-utility__tabs" role="tablist">
        <button className="sk-utility__tab sk-utility__tab--active" role="tab">Notifications</button>
        <button className="sk-utility__tab" role="tab">Assistant</button>
        <button className="sk-utility__tab" role="tab">Help</button>
      </div>
      <div className="sk-utility__body">
        <div className="sk-placeholder sk-placeholder--sm">
          <p className="sk-placeholder__title">Notifications</p>
          <p className="sk-placeholder__hint">Real-time alerts will appear here. (Placeholder)</p>
        </div>
      </div>
    </aside>
  );
}
