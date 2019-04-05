package com.welzuka.user.user.repository;

import com.welzuka.user.user.domain.GeneralAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GeneralAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeneralAccountRepository extends JpaRepository<GeneralAccount, Long> {

}
