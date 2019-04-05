package com.welzuka.user.user.repository;

import com.welzuka.user.user.domain.DoctorAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DoctorAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorAccountRepository extends JpaRepository<DoctorAccount, Long> {

}
