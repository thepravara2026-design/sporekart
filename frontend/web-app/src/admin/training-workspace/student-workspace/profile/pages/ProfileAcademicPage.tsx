import { useProfile } from '../state/ProfileContext';
import { ProfileHeader } from '../components/ProfileHeader';
import { ProfileInfoCard } from '../components/ProfileInfoCard';
import { ProfileHeaderSkeleton, ProfileInfoCardSkeleton } from '../components/ProfileSkeleton';

export function ProfileAcademicPage() {
  const { currentProfile, isLoading, error } = useProfile();

  if (isLoading) return <div><ProfileHeaderSkeleton /><ProfileInfoCardSkeleton /></div>;
  if (error) return <div className="profile-error"><p>{error}</p></div>;
  if (!currentProfile) return <div className="profile-empty"><p>No student profile selected.</p></div>;

  const { academicInfo, personalInfo } = currentProfile;

  return (
    <div className="profile-academic-page">
      <ProfileHeader profile={currentProfile} />
      <div className="profile-academic-page__grid">
        <ProfileInfoCard
          title="Academic Qualifications"
          items={[
            { label: 'Highest Qualification', value: academicInfo.highestQualification },
            { label: 'Specialization', value: academicInfo.specialization },
            { label: 'Institution', value: academicInfo.institution },
            { label: 'Graduation Year', value: academicInfo.graduationYear },
            { label: 'Current Education', value: academicInfo.currentEducation },
          ]}
        />
        <ProfileInfoCard
          title="Course History"
          items={[
            { label: 'Previous Courses', value: academicInfo.previousCourses.length > 0 ? academicInfo.previousCourses.join(', ') : 'None' },
            { label: 'Learning Interests', value: academicInfo.learningInterests.length > 0 ? academicInfo.learningInterests.join(', ') : 'None' },
          ]}
        />
        {academicInfo.academicNotes && (
          <ProfileInfoCard
            title="Notes"
            items={[
              { label: 'Academic Notes', value: academicInfo.academicNotes },
            ]}
          />
        )}
        <ProfileInfoCard
          title="Contact Information"
          items={[
            { label: 'Email', value: currentProfile.email },
            { label: 'Phone', value: currentProfile.phone },
            { label: 'Current Address', value: `${personalInfo.currentAddress.village}, ${personalInfo.currentAddress.district}, ${personalInfo.currentAddress.state} - ${personalInfo.currentAddress.postalCode}` },
          ]}
        />
      </div>
    </div>
  );
}
