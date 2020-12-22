package com.wing.forutona.App.ForutonaUser.Service;


import com.wing.forutona.App.ForutonaUser.Domain.PersonaSettingNotice;
import com.wing.forutona.App.ForutonaUser.Dto.PersonaSettingNoticeInsertReqDto;
import com.wing.forutona.App.ForutonaUser.Dto.PersonaSettingNoticeResDto;
import com.wing.forutona.App.ForutonaUser.Dto.PersonaSettingNoticeUpdateReqDto;
import com.wing.forutona.App.ForutonaUser.Repository.PersonaSettingNoticeDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
