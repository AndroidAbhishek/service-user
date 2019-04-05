package com.welzuka.user.user.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DoctorAccount.
 */
@Entity
@Table(name = "doctor_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "doctoraccount")
public class DoctorAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "accound_id", nullable = false)
    private Long accoundId;

    @NotNull
    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "experience")
    private String experience;

    @NotNull
    @Column(name = "speciality", nullable = false)
    private String speciality;

    @Column(name = "qualification")
    private String qualification;

    @OneToMany(mappedBy = "doctorAccount")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserAccount> users = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccoundId() {
        return accoundId;
    }

    public DoctorAccount accoundId(Long accoundId) {
        this.accoundId = accoundId;
        return this;
    }

    public void setAccoundId(Long accoundId) {
        this.accoundId = accoundId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public DoctorAccount licenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
        return this;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getExperience() {
        return experience;
    }

    public DoctorAccount experience(String experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSpeciality() {
        return speciality;
    }

    public DoctorAccount speciality(String speciality) {
        this.speciality = speciality;
        return this;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getQualification() {
        return qualification;
    }

    public DoctorAccount qualification(String qualification) {
        this.qualification = qualification;
        return this;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Set<UserAccount> getUsers() {
        return users;
    }

    public DoctorAccount users(Set<UserAccount> userAccounts) {
        this.users = userAccounts;
        return this;
    }

    public DoctorAccount addUsers(UserAccount userAccount) {
        this.users.add(userAccount);
        userAccount.setDoctorAccount(this);
        return this;
    }

    public DoctorAccount removeUsers(UserAccount userAccount) {
        this.users.remove(userAccount);
        userAccount.setDoctorAccount(null);
        return this;
    }

    public void setUsers(Set<UserAccount> userAccounts) {
        this.users = userAccounts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DoctorAccount doctorAccount = (DoctorAccount) o;
        if (doctorAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctorAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DoctorAccount{" +
            "id=" + getId() +
            ", accoundId=" + getAccoundId() +
            ", licenseNumber='" + getLicenseNumber() + "'" +
            ", experience='" + getExperience() + "'" +
            ", speciality='" + getSpeciality() + "'" +
            ", qualification='" + getQualification() + "'" +
            "}";
    }
}
