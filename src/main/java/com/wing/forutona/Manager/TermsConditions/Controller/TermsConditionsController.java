package com.wing.forutona.Manager.TermsConditions.Controller;

import com.wing.forutona.Manager.TermsConditions.Dto.TermsConditionsResDto;
import com.wing.forutona.Manager.TermsConditions.Service.TermsConditionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/termsConditions")
@RequiredArgsConstructor
public class TermsConditionsController {

    final TermsConditionsService termsConditionsService;

    @GetMapping
    public TermsConditionsResDto getTermsConditions(@RequestParam Integer idx){
        return termsConditionsService.getTerms(idx);
    }
}
