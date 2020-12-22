package com.wing.forutona.Manager.TermsConditions.Service;

import com.wing.forutona.Manager.TermsConditions.Dto.TermsConditionsResDto;
import com.wing.forutona.Manager.TermsConditions.Repository.TermsConditionsDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TermsConditionsService {

    final TermsConditionsDataRepository termsConditionsDataRepository;

    public TermsConditionsResDto getTerms(Integer idx) {
        return TermsConditionsResDto.fromTermsConditions(
                termsConditionsDataRepository.findById(idx).get()
        );
    }
}
