import { useProfile } from '../state/ProfileContext';
import { ProfileTimeline } from '../components/ProfileTimeline';
import { ProfileHeader } from '../components/ProfileHeader';
import { ProfileHeaderSkeleton } from '../components/ProfileSkeleton';

export function ProfileTimelinePage() {
  const { currentProfile, isLoading, error } = useProfile();

  if (isLoading) return <div><ProfileHeaderSkeleton /></div>;
  if (error) return <div className="profile-error"><p>{error}</p></div>;
  if (!currentProfile) return <div className="profile-empty"><p>No student profile selected.</p></div>;

  return (
    <div className="profile-timeline-page">
      <ProfileHeader profile={currentProfile} />
      <div className="profile-timeline-page__content">
        <ProfileTimeline events={currentProfile.timeline} />
      </div>
    </div>
  );
}
