interface InfoItem {
  label: string;
  value: string | number | null | undefined;
}

interface ProfileInfoCardProps {
  title: string;
  items: InfoItem[];
  variant?: 'default' | 'compact';
}

export function ProfileInfoCard({ title, items, variant = 'default' }: ProfileInfoCardProps) {
  const visible = items.filter((i) => i.value !== null && i.value !== undefined && i.value !== '');

  if (visible.length === 0) {
    return (
      <div className="profile-info-card">
        <div className="profile-info-card__header">
          <h3 className="profile-info-card__title">{title}</h3>
        </div>
        <p className="profile-info-card__empty">No information provided</p>
      </div>
    );
  }

  return (
    <div className={`profile-info-card profile-info-card--${variant}`}>
      <div className="profile-info-card__header">
        <h3 className="profile-info-card__title">{title}</h3>
      </div>
      <div className="profile-info-card__body">
        {visible.map((item) => (
          <div key={item.label} className="profile-info-card__item">
            <span className="profile-info-card__label">{item.label}</span>
            <span className="profile-info-card__value">{item.value}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
