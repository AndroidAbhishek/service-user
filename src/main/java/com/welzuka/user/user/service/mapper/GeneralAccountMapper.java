package com.welzuka.user.user.service.mapper;

import com.welzuka.user.user.domain.*;
import com.welzuka.user.user.service.dto.GeneralAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GeneralAccount and its DTO GeneralAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeneralAccountMapper extends EntityMapper<GeneralAccountDTO, GeneralAccount> {


    @Mapping(target = "users", ignore = true)
    GeneralAccount toEntity(GeneralAccountDTO generalAccountDTO);

    default GeneralAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        GeneralAccount generalAccount = new GeneralAccount();
        generalAccount.setId(id);
        return generalAccount;
    }
}
