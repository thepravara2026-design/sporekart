export default function PlaceholderPanel({
  title,
  hint,
  actionLabel,
}: {
  title: string;
  hint: string;
  actionLabel?: string;
}) {
  return (
    <section className="sk-panel" aria-label={title}>
      <div className="sk-placeholder">
        <div className="sk-placeholder__icon" aria-hidden="true">◌</div>
        <p className="sk-placeholder__title">{title}</p>
        <p className="sk-placeholder__hint">{hint}</p>
        {actionLabel && (
          <button className="sk-primary-action" type="button">
            {actionLabel}
          </button>
        )}
      </div>
    </section>
  );
}
