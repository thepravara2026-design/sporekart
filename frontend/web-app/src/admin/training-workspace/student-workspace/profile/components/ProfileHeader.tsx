import type { StudentProfile } from '../types';
import { PROFILE_STATUS_LABELS, PROFILE_STATUS_VARIANTS } from '../types';

interface ProfileHeaderProps {
  profile: StudentProfile;
}

const statusColors: Record<string, string> = {
  'default': '#6b7280',
  'success': '#16a34a',
  'warning': '#ca8a04',
  'danger': '#dc2626',
  'neutral': '#9ca3af',
  'info': '#2563eb',
  'primary': '#7c3aed',
};

export function ProfileHeader({ profile }: ProfileHeaderProps) {
  const variant = PROFILE_STATUS_VARIANTS[profile.profileStatus];
  const color = statusColors[variant] || statusColors.default;
  const initials = profile.fullName.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase();

  return (
    <div className="profile-header">
      <div className="profile-header__avatar">
        {profile.profilePhotoUrl ? (
          <img src={profile.profilePhotoUrl} alt={profile.fullName} className="profile-header__photo" />
        ) : (
          <div className="profile-header__initials">{initials}</div>
        )}
      </div>

      <div className="profile-header__info">
        <div className="profile-header__name-row">
          <h1 className="profile-header__name">{profile.fullName}</h1>
          <span className="profile-header__badge" style={{ backgroundColor: color, color: '#fff' }}>
            {PROFILE_STATUS_LABELS[profile.profileStatus]}
          </span>
        </div>
        <p className="profile-header__subtitle">
          {profile.enrollmentNumber} &middot; {profile.studentId}
        </p>
        <div className="profile-header__stats">
          <div className="profile-header__stat">
            <span className="profile-header__stat-value">{profile.age}</span>
            <span className="profile-header__stat-label">Age</span>
          </div>
          <div className="profile-header__stat">
            <span className="profile-header__stat-value">{profile.gender === 'male' ? 'M' : profile.gender === 'female' ? 'F' : 'O'}</span>
            <span className="profile-header__stat-label">Gender</span>
          </div>
          <div className="profile-header__stat">
            <span className="profile-header__stat-value">{profile.personalInfo.currentAddress.district}</span>
            <span className="profile-header__stat-label">District</span>
          </div>
          <div className="profile-header__stat">
            <span className="profile-header__stat-value">{profile.personalInfo.currentAddress.state}</span>
            <span className="profile-header__stat-label">State</span>
          </div>
        </div>
        <div className="profile-header__contact">
          <span>{profile.email}</span>
          <span className="profile-header__dot">&middot;</span>
          <span>{profile.phone}</span>
        </div>
      </div>
    </div>
  );
}
