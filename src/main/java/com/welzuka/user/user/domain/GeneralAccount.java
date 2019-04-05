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
 * A GeneralAccount.
 */
@Entity
@Table(name = "general_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "generalaccount")
public class GeneralAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "personal_info")
    private String personalInfo;

    /**
     * A relationship
     */
    @OneToMany(mappedBy = "generalAccount")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserAccount> users = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public GeneralAccount accountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public GeneralAccount personalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
        return this;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Set<UserAccount> getUsers() {
        return users;
    }

    public GeneralAccount users(Set<UserAccount> userAccounts) {
        this.users = userAccounts;
        return this;
    }

    public GeneralAccount addUsers(UserAccount userAccount) {
        this.users.add(userAccount);
        userAccount.setGeneralAccount(this);
        return this;
    }

    public GeneralAccount removeUsers(UserAccount userAccount) {
        this.users.remove(userAccount);
        userAccount.setGeneralAccount(null);
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
        GeneralAccount generalAccount = (GeneralAccount) o;
        if (generalAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), generalAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeneralAccount{" +
            "id=" + getId() +
            ", accountId=" + getAccountId() +
            ", personalInfo='" + getPersonalInfo() + "'" +
            "}";
    }
}
