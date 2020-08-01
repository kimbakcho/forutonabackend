package com.wing.forutona.ForutonaUser.Service;


import com.wing.forutona.ForutonaUser.Domain.PersonaSettingNotice;
import com.wing.forutona.ForutonaUser.Dto.PersonaSettingNoticeInsertReqDto;
import com.wing.forutona.ForutonaUser.Dto.PersonaSettingNoticeResDto;
import com.wing.forutona.ForutonaUser.Dto.PersonaSettingNoticeUpdateReqDto;
import com.wing.forutona.ForutonaUser.Repository.PersonaSettingNoticeDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonaSettingNoticeService {

    final PersonaSettingNoticeDataRepository personaSettingNoticeDataRepository;

    public Page<PersonaSettingNoticeResDto> getPersonaSettingNotice(Pageable pageable) {
        Page<PersonaSettingNotice> findAll = personaSettingNoticeDataRepository.findAll(pageable);
        Page<PersonaSettingNoticeResDto> result = findAll.map(x -> new PersonaSettingNoticeResDto(x));
        return result;
    }

    public PersonaSettingNoticeResDto getPersonaSettingNoticePage(long idx) {
        PersonaSettingNotice personaSettingNotice = personaSettingNoticeDataRepository.findById(idx).get();
        return new PersonaSettingNoticeResDto(personaSettingNotice);
    }


    public PersonaSettingNoticeResDto insertPersonaSettingNotice(PersonaSettingNoticeInsertReqDto reqDto) {
        PersonaSettingNotice personaSettingNotice =  PersonaSettingNotice.fromPersonaSettingNoticeInsertReqDto(reqDto);
        PersonaSettingNotice save = personaSettingNoticeDataRepository.save(personaSettingNotice);
        return new PersonaSettingNoticeResDto(save);
    }

    public PersonaSettingNoticeResDto updatePersonaSettingNotice(PersonaSettingNoticeUpdateReqDto reqDto) {
        PersonaSettingNotice personaSettingNotice = personaSettingNoticeDataRepository.findById(reqDto.getIdx()).get();
        personaSettingNotice.updatePersonaSettingNotice(reqDto);
        return new PersonaSettingNoticeResDto(personaSettingNotice);
    }

    public Long deletePersonaSettingNotice(long idx) {
        personaSettingNoticeDataRepository.deleteById(idx);
        return idx;
    }
}
