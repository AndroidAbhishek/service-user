package com.welzuka.user.user.service.mapper;

import com.welzuka.user.user.domain.*;
import com.welzuka.user.user.service.dto.DoctorAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DoctorAccount and its DTO DoctorAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DoctorAccountMapper extends EntityMapper<DoctorAccountDTO, DoctorAccount> {


    @Mapping(target = "users", ignore = true)
    DoctorAccount toEntity(DoctorAccountDTO doctorAccountDTO);

    default DoctorAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        DoctorAccount doctorAccount = new DoctorAccount();
        doctorAccount.setId(id);
        return doctorAccount;
    }
}
