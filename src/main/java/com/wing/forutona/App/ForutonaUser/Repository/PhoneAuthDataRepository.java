package com.wing.forutona.App.ForutonaUser.Repository;

import com.wing.forutona.App.ForutonaUser.Domain.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PhoneAuthDataRepository extends JpaRepository<PhoneAuth,Long> {
    List<PhoneAuth> findPhoneAuthByInternationalizedPhoneNumberIs(String internationalizedPhoneNumber);

}
