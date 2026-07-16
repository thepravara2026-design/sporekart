import { useProfile } from '../state/ProfileContext';
import { ProfileHeader } from '../components/ProfileHeader';
import { ProfileInfoCard } from '../components/ProfileInfoCard';
import { ProfileHeaderSkeleton, ProfileInfoCardSkeleton } from '../components/ProfileSkeleton';

export function ProfileProfessionalPage() {
  const { currentProfile, isLoading, error } = useProfile();

  if (isLoading) return <div><ProfileHeaderSkeleton /><ProfileInfoCardSkeleton /></div>;
  if (error) return <div className="profile-error"><p>{error}</p></div>;
  if (!currentProfile) return <div className="profile-empty"><p>No student profile selected.</p></div>;

  const { professionalInfo, personalInfo } = currentProfile;

  return (
    <div className="profile-professional-page">
      <ProfileHeader profile={currentProfile} />
      <div className="profile-professional-page__grid">
        <ProfileInfoCard
          title="Current Employment"
          items={[
            { label: 'Current Profession', value: professionalInfo.currentProfession },
            { label: 'Profession Type', value: professionalInfo.professionType === 'business-owner' ? 'Business Owner' : professionalInfo.professionType === 'farmer' ? 'Farmer' : professionalInfo.professionType === 'employee' ? 'Employee' : professionalInfo.professionType === 'entrepreneur' ? 'Entrepreneur' : professionalInfo.professionType === 'unemployed' ? 'Unemployed' : professionalInfo.professionType === 'other' ? 'Other' : professionalInfo.professionType },
            { label: 'Industry', value: professionalInfo.industry },
            { label: 'Years of Experience', value: professionalInfo.yearsOfExperience },
            { label: 'Employer', value: personalInfo.employer },
            { label: 'Business Name', value: personalInfo.businessName },
          ]}
        />
        <ProfileInfoCard
          title="Skills & Certifications"
          items={[
            { label: 'Skills', value: professionalInfo.skills.length > 0 ? professionalInfo.skills.join(', ') : 'None' },
            { label: 'Certifications', value: professionalInfo.certifications.length > 0 ? professionalInfo.certifications.join(', ') : 'None' },
          ]}
        />
        <ProfileInfoCard
          title="Professional Status"
          items={[
            { label: 'Is Business Owner', value: professionalInfo.isBusinessOwner ? 'Yes' : 'No' },
            { label: 'Is Farmer', value: professionalInfo.isFarmer ? 'Yes' : 'No' },
            { label: 'Is Student', value: professionalInfo.isStudent ? 'Yes' : 'No' },
          ]}
        />
      </div>
    </div>
  );
}
