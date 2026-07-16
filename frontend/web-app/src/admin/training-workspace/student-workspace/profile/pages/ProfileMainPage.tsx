import { useProfile } from '../state/ProfileContext';
import { ProfileHeader } from '../components/ProfileHeader';
import { ProfileCompletenessCard } from '../components/ProfileCompletenessCard';
import { ProfileInfoCard } from '../components/ProfileInfoCard';
import { ProfileTimeline } from '../components/ProfileTimeline';
import { DocumentStatusCard } from '../components/DocumentStatusCard';
import { ProfileHeaderSkeleton, ProfileCompletenessSkeleton, ProfileInfoCardSkeleton } from '../components/ProfileSkeleton';

export function ProfileMainPage() {
  const { currentProfile, isLoading, error } = useProfile();

  if (isLoading) {
    return (
      <div className="profile-main-page">
        <ProfileHeaderSkeleton />
        <div className="profile-main-page__grid">
          <div><ProfileCompletenessSkeleton /></div>
          <div><ProfileInfoCardSkeleton /></div>
          <div><ProfileInfoCardSkeleton /></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="profile-main-page">
        <div className="profile-error">
          <p>{error}</p>
        </div>
      </div>
    );
  }

  if (!currentProfile) {
    return (
      <div className="profile-main-page">
        <div className="profile-empty">
          <p>No student profile selected. Please select a student from the registry.</p>
        </div>
      </div>
    );
  }

  const { completeness, personalInfo, academicInfo, professionalInfo, learningPreferences, guardian, emergencyContact, documents, timeline } = currentProfile;

  const personalItems = [
    { label: 'Blood Group', value: personalInfo.bloodGroup },
    { label: 'Marital Status', value: personalInfo.maritalStatus !== 'not-specified' ? personalInfo.maritalStatus : 'Not Specified' },
    { label: 'Alternate Phone', value: personalInfo.alternatePhone },
    { label: 'Government ID', value: personalInfo.governmentId },
    { label: 'Employer', value: personalInfo.employer },
    { label: 'Business', value: personalInfo.businessName },
    { label: 'Experience', value: personalInfo.annualExperience },
  ];

  const academicItems = [
    { label: 'Highest Qualification', value: academicInfo.highestQualification },
    { label: 'Specialization', value: academicInfo.specialization },
    { label: 'Institution', value: academicInfo.institution },
    { label: 'Graduation Year', value: academicInfo.graduationYear },
    { label: 'Current Education', value: academicInfo.currentEducation },
    { label: 'Previous Courses', value: academicInfo.previousCourses.length > 0 ? academicInfo.previousCourses.join(', ') : 'None' },
  ];

  const professionalItems = [
    { label: 'Current Profession', value: professionalInfo.currentProfession },
    { label: 'Professional Type', value: professionalInfo.professionType },
    { label: 'Industry', value: professionalInfo.industry },
    { label: 'Years of Experience', value: professionalInfo.yearsOfExperience },
    { label: 'Skills', value: professionalInfo.skills.length > 0 ? professionalInfo.skills.join(', ') : 'None' },
    { label: 'Certifications', value: professionalInfo.certifications.length > 0 ? professionalInfo.certifications.join(', ') : 'None' },
  ];

  return (
    <div className="profile-main-page">
      <ProfileHeader profile={currentProfile} />

      <div className="profile-main-page__grid">
        <div className="profile-main-page__sidebar">
          <ProfileCompletenessCard completeness={completeness} />

          <ProfileInfoCard
            title="Learning Preferences"
            variant="compact"
            items={[
              { label: 'Preferred Language', value: learningPreferences.preferredLanguage },
              { label: 'Mode', value: learningPreferences.preferredMode },
              { label: 'Time', value: learningPreferences.timePreferences.join(', ') },
              { label: 'Training Category', value: learningPreferences.preferredTrainingCategory },
            ]}
          />

          <DocumentStatusCard documents={documents} />

          {guardian && (
            <ProfileInfoCard
              title="Guardian"
              variant="compact"
              items={[
                { label: 'Name', value: guardian.name },
                { label: 'Relationship', value: guardian.relationship },
                { label: 'Phone', value: guardian.phone },
                { label: 'Email', value: guardian.email },
                { label: 'Occupation', value: guardian.occupation },
              ]}
            />
          )}

          {emergencyContact && (
            <ProfileInfoCard
              title="Emergency Contact"
              variant="compact"
              items={[
                { label: 'Primary', value: `${emergencyContact.primaryName} (${emergencyContact.primaryRelationship})` },
                { label: 'Primary Phone', value: emergencyContact.primaryPhone },
                { label: 'Secondary', value: emergencyContact.secondaryName ? `${emergencyContact.secondaryName} (${emergencyContact.secondaryRelationship})` : 'None' },
                { label: 'Secondary Phone', value: emergencyContact.secondaryPhone },
              ]}
            />
          )}
        </div>

        <div className="profile-main-page__main">
          <ProfileInfoCard title="Personal Information" items={personalItems} />
          <ProfileInfoCard title="Academic Information" items={academicItems} />
          <ProfileInfoCard title="Professional Information" items={professionalItems} />
          <ProfileTimeline events={timeline} max={5} />
        </div>
      </div>
    </div>
  );
}
