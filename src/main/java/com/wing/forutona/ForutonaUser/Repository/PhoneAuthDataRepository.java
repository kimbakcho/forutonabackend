package com.wing.forutona.ForutonaUser.Repository;

import com.wing.forutona.ForutonaUser.Domain.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PhoneAuthDataRepository extends JpaRepository<PhoneAuth,Long> {
    public List<PhoneAuth> findPhoneAuthByInternationalizedPhoneNumberIs(String internationalizedPhoneNumber);

}
