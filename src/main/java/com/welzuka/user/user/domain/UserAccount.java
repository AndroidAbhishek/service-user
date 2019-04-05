package com.welzuka.user.user.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.welzuka.user.user.domain.enumeration.Gender;

import com.welzuka.user.user.domain.enumeration.Role;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "useraccount")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profilepic_url")
    private String profilepicUrl;

    @NotNull
    @Column(name = "dob", nullable = false)
    private String dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_role")
    private Role role;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private DoctorAccount doctorAccount;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private GeneralAccount generalAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public UserAccount userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public UserAccount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public UserAccount email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserAccount phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilepicUrl() {
        return profilepicUrl;
    }

    public UserAccount profilepicUrl(String profilepicUrl) {
        this.profilepicUrl = profilepicUrl;
        return this;
    }

    public void setProfilepicUrl(String profilepicUrl) {
        this.profilepicUrl = profilepicUrl;
    }

    public String getDob() {
        return dob;
    }

    public UserAccount dob(String dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public UserAccount gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public UserAccount role(Role role) {
        this.role = role;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public DoctorAccount getDoctorAccount() {
        return doctorAccount;
    }

    public UserAccount doctorAccount(DoctorAccount doctorAccount) {
        this.doctorAccount = doctorAccount;
        return this;
    }

    public void setDoctorAccount(DoctorAccount doctorAccount) {
        this.doctorAccount = doctorAccount;
    }

    public GeneralAccount getGeneralAccount() {
        return generalAccount;
    }

    public UserAccount generalAccount(GeneralAccount generalAccount) {
        this.generalAccount = generalAccount;
        return this;
    }

    public void setGeneralAccount(GeneralAccount generalAccount) {
        this.generalAccount = generalAccount;
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
        UserAccount userAccount = (UserAccount) o;
        if (userAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", profilepicUrl='" + getProfilepicUrl() + "'" +
            ", dob='" + getDob() + "'" +
            ", gender='" + getGender() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
