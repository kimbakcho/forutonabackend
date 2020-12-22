package com.wing.forutona.Manager.TermsConditions.Repository;

import com.wing.forutona.Manager.TermsConditions.Domain.TermsConditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsConditionsDataRepository  extends JpaRepository<TermsConditions,Integer> {
}
