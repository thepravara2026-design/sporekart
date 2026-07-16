import { useProfile } from '../state/ProfileContext';
import { ProfileHeader } from '../components/ProfileHeader';
import { ProfileInfoCard } from '../components/ProfileInfoCard';
import { ProfileHeaderSkeleton, ProfileInfoCardSkeleton } from '../components/ProfileSkeleton';

export function ProfileLearningPage() {
  const { currentProfile, isLoading, error } = useProfile();

  if (isLoading) return <div><ProfileHeaderSkeleton /><ProfileInfoCardSkeleton /></div>;
  if (error) return <div className="profile-error"><p>{error}</p></div>;
  if (!currentProfile) return <div className="profile-empty"><p>No student profile selected.</p></div>;

  const { learningPreferences } = currentProfile;

  return (
    <div className="profile-learning-page">
      <ProfileHeader profile={currentProfile} />
      <div className="profile-learning-page__grid">
        <ProfileInfoCard
          title="Language Preferences"
          items={[
            { label: 'Preferred Language', value: learningPreferences.preferredLanguage },
            { label: 'Secondary Languages', value: learningPreferences.secondaryLanguages.length > 0 ? learningPreferences.secondaryLanguages.join(', ') : 'None' },
          ]}
        />
        <ProfileInfoCard
          title="Training Preferences"
          items={[
            { label: 'Preferred Mode', value: learningPreferences.preferredMode === 'online' ? 'Online' : learningPreferences.preferredMode === 'offline' ? 'Offline' : 'Hybrid' },
            { label: 'Time Preferences', value: learningPreferences.timePreferences.join(', ') },
            { label: 'Training Category', value: learningPreferences.preferredTrainingCategory },
          ]}
        />
        <ProfileInfoCard
          title="Learning Goals"
          items={[
            { label: 'Goals', value: learningPreferences.learningGoals.length > 0 ? learningPreferences.learningGoals.join(', ') : 'None' },
            { label: 'Interests', value: learningPreferences.learningInterests.length > 0 ? learningPreferences.learningInterests.join(', ') : 'None' },
          ]}
        />
      </div>
    </div>
  );
}
