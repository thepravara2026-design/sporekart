package com.sporekart.training.infrastructure.persistence;

import com.sporekart.training.domain.model.GrowerProfile;
import jakarta.persistence.*;

@Entity
@Table(name = "grower_profiles")
public class GrowerProfileEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "village", length = 100)
    private String village;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "experience", length = 500)
    private String experience;

    protected GrowerProfileEntity() {}

    public GrowerProfileEntity(String id, String fullName, String village, String district,
                               String state, String experience) {
        this.id = id;
        this.fullName = fullName;
        this.village = village;
        this.district = district;
        this.state = state;
        this.experience = experience;
    }

    public static GrowerProfileEntity fromDomain(GrowerProfile profile) {
        return new GrowerProfileEntity(
            profile.getId(), profile.getFullName(), profile.getVillage(),
            profile.getDistrict(), profile.getState(), profile.getExperience());
    }

    public GrowerProfile toDomain() {
        return new GrowerProfile(id, fullName, village, district, state, experience);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
}
