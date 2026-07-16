import { useProfile } from '../state/ProfileContext';
import { ProfileHeader } from '../components/ProfileHeader';
import { DocumentStatusCard } from '../components/DocumentStatusCard';
import { ProfileHeaderSkeleton } from '../components/ProfileSkeleton';

export function ProfileDocumentsPage() {
  const { currentProfile, isLoading, error } = useProfile();

  if (isLoading) return <div><ProfileHeaderSkeleton /></div>;
  if (error) return <div className="profile-error"><p>{error}</p></div>;
  if (!currentProfile) return <div className="profile-empty"><p>No student profile selected.</p></div>;

  const { documents } = currentProfile;
  const uploaded = documents.filter((d) => d.status !== 'not-uploaded');
  const verified = documents.filter((d) => d.status === 'verified');

  return (
    <div className="profile-documents-page">
      <ProfileHeader profile={currentProfile} />
      <div className="profile-documents-page__summary">
        <div className="profile-documents-page__stat">
          <span className="profile-documents-page__stat-value">{documents.length}</span>
          <span className="profile-documents-page__stat-label">Total Required</span>
        </div>
        <div className="profile-documents-page__stat">
          <span className="profile-documents-page__stat-value">{uploaded.length}</span>
          <span className="profile-documents-page__stat-label">Uploaded</span>
        </div>
        <div className="profile-documents-page__stat">
          <span className="profile-documents-page__stat-value">{verified.length}</span>
          <span className="profile-documents-page__stat-label">Verified</span>
        </div>
      </div>
      <DocumentStatusCard documents={documents} />
    </div>
  );
}
