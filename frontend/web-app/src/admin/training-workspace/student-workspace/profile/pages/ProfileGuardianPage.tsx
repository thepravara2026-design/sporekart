import { useProfile } from '../state/ProfileContext';
import { ProfileHeader } from '../components/ProfileHeader';
import { ProfileInfoCard } from '../components/ProfileInfoCard';
import { ProfileHeaderSkeleton } from '../components/ProfileSkeleton';

export function ProfileGuardianPage() {
  const { currentProfile, isLoading, error } = useProfile();

  if (isLoading) return <div><ProfileHeaderSkeleton /></div>;
  if (error) return <div className="profile-error"><p>{error}</p></div>;
  if (!currentProfile) return <div className="profile-empty"><p>No student profile selected.</p></div>;

  const { guardian, emergencyContact } = currentProfile;

  return (
    <div className="profile-guardian-page">
      <ProfileHeader profile={currentProfile} />
      <div className="profile-guardian-page__grid">
        {guardian ? (
          <ProfileInfoCard
            title="Guardian Information"
            items={[
              { label: 'Name', value: guardian.name },
              { label: 'Relationship', value: guardian.relationship },
              { label: 'Phone', value: guardian.phone },
              { label: 'Email', value: guardian.email },
              { label: 'Occupation', value: guardian.occupation },
              { label: 'Address', value: guardian.address },
            ]}
          />
        ) : (
          <div className="profile-info-card">
            <div className="profile-info-card__header">
              <h3 className="profile-info-card__title">Guardian Information</h3>
            </div>
            <p className="profile-info-card__empty">No guardian information provided</p>
          </div>
        )}

        {emergencyContact ? (
          <ProfileInfoCard
            title="Emergency Contact"
            items={[
              { label: 'Primary Name', value: emergencyContact.primaryName },
              { label: 'Primary Phone', value: emergencyContact.primaryPhone },
              { label: 'Primary Relationship', value: emergencyContact.primaryRelationship },
              { label: 'Secondary Name', value: emergencyContact.secondaryName },
              { label: 'Secondary Phone', value: emergencyContact.secondaryPhone },
              { label: 'Secondary Relationship', value: emergencyContact.secondaryRelationship },
              { label: 'Email', value: emergencyContact.email },
            ]}
          />
        ) : (
          <div className="profile-info-card">
            <div className="profile-info-card__header">
              <h3 className="profile-info-card__title">Emergency Contact</h3>
            </div>
            <p className="profile-info-card__empty">No emergency contact provided</p>
          </div>
        )}
      </div>
    </div>
  );
}
