package com.welzuka.user.user.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DoctorAccount entity.
 */
public class DoctorAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private Long accoundId;

    @NotNull
    private String licenseNumber;

    private String experience;

    @NotNull
    private String speciality;

    private String qualification;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccoundId() {
        return accoundId;
    }

    public void setAccoundId(Long accoundId) {
        this.accoundId = accoundId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DoctorAccountDTO doctorAccountDTO = (DoctorAccountDTO) o;
        if (doctorAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctorAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DoctorAccountDTO{" +
            "id=" + getId() +
            ", accoundId=" + getAccoundId() +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            ", experience='" + getExperience() + "'" +
            ", speciality='" + getSpeciality() + "'" +
            ", qualification='" + getQualification() + "'" +
            "}";
    }
}
