export function ProfileHeaderSkeleton() {
  return (
    <div className="profile-header-skeleton">
      <div className="skeleton-circle" style={{ width: 80, height: 80, borderRadius: '50%', background: '#e5e7eb' }} />
      <div style={{ flex: 1 }}>
        <div className="skeleton-line" style={{ width: '60%', height: 24, marginBottom: 8, background: '#e5e7eb', borderRadius: 4 }} />
        <div className="skeleton-line" style={{ width: '40%', height: 16, marginBottom: 12, background: '#e5e7eb', borderRadius: 4 }} />
        <div className="skeleton-line" style={{ width: '80%', height: 16, background: '#e5e7eb', borderRadius: 4 }} />
      </div>
    </div>
  );
}

export function ProfileCompletenessSkeleton() {
  return (
    <div className="profile-completeness-skeleton">
      <div className="skeleton-line" style={{ width: '50%', height: 20, marginBottom: 12, background: '#e5e7eb', borderRadius: 4 }} />
      <div className="skeleton-line" style={{ width: '100%', height: 12, marginBottom: 16, background: '#e5e7eb', borderRadius: 4 }} />
      {Array.from({ length: 4 }).map((_, i) => (
        <div key={i} style={{ marginBottom: 8 }}>
          <div className="skeleton-line" style={{ width: '30%', height: 14, marginBottom: 4, background: '#e5e7eb', borderRadius: 4 }} />
          <div className="skeleton-line" style={{ width: '100%', height: 8, background: '#e5e7eb', borderRadius: 4 }} />
        </div>
      ))}
    </div>
  );
}

export function ProfileInfoCardSkeleton() {
  return (
    <div className="profile-info-card-skeleton">
      <div className="skeleton-line" style={{ width: '40%', height: 20, marginBottom: 16, background: '#e5e7eb', borderRadius: 4 }} />
      {Array.from({ length: 4 }).map((_, i) => (
        <div key={i} className="skeleton-row" style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
          <div className="skeleton-line" style={{ width: '30%', height: 14, background: '#e5e7eb', borderRadius: 4 }} />
          <div className="skeleton-line" style={{ width: '50%', height: 14, background: '#e5e7eb', borderRadius: 4 }} />
        </div>
      ))}
    </div>
  );
}
