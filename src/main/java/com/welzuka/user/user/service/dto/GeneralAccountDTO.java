package com.welzuka.user.user.service.dto;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the GeneralAccount entity.
 */
public class GeneralAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private Long accountId;

    private String personalInfo;

    /**
     * A relationship
     */
    @ApiModelProperty(value = "A relationship")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeneralAccountDTO generalAccountDTO = (GeneralAccountDTO) o;
        if (generalAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), generalAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeneralAccountDTO{" +
            "id=" + getId() +
            ", accountId=" + getAccountId() +
            ", personalInfo='" + getPersonalInfo() + "'" +
            "}";
    }
}
