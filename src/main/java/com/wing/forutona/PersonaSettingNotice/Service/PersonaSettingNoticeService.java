package com.wing.forutona.PersonaSettingNotice.Service;

import com.wing.forutona.PersonaSettingNotice.Domain.PersonaSettingNotice;
import com.wing.forutona.PersonaSettingNotice.Dto.PersonaSettingNoticeInsertReqDto;
import com.wing.forutona.PersonaSettingNotice.Dto.PersonaSettingNoticeResDto;
import com.wing.forutona.PersonaSettingNotice.Repository.PersonaSettingNoticeDataRepository;
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
public class PersonaSettingNoticeService {

    @Autowired
    private PersonaSettingNoticeDataRepository personaSettingNoticeDataRepository;

    @Async
    @Transactional
    public void getPersonaSettingNotice(ResponseBodyEmitter emitter, Pageable pageable) {
        Slice<PersonaSettingNotice> findAll = personaSettingNoticeDataRepository.findAll(pageable);

        Slice<PersonaSettingNoticeResDto> result = findAll.map(x -> new PersonaSettingNoticeResDto(x));
        try {
            emitter.send(result);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }

    }

    @Async
    @Transactional
    public void getPersonaSettingNoticePage(ResponseBodyEmitter emitter, long idx) {
        PersonaSettingNotice personaSettingNotice = personaSettingNoticeDataRepository.findById(idx).get();
        PersonaSettingNoticeResDto personaSettingNoticeResDto = new PersonaSettingNoticeResDto(personaSettingNotice);
        try {
            emitter.send(personaSettingNoticeResDto);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void insertPersonaSettingNotice(ResponseBodyEmitter emitter, PersonaSettingNoticeInsertReqDto reqDto) {
        PersonaSettingNotice personaSettingNotice = new PersonaSettingNotice(reqDto);
        personaSettingNoticeDataRepository.save(personaSettingNotice);
        try {
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }

    @Async
    @Transactional
    public void updatePersonaSettingNotice(ResponseBodyEmitter emitter, PersonaSettingNoticeInsertReqDto reqDto) {
        PersonaSettingNotice personaSettingNotice = personaSettingNoticeDataRepository.findById(reqDto.getIdx()).get();
        personaSettingNotice.setLang(reqDto.getLang());
        personaSettingNotice.setNoticeContent(reqDto.getNoticeContent());
        personaSettingNotice.setNoticeName(reqDto.getNoticeName());
        personaSettingNotice.setNoticeWriteDateTime(reqDto.getNoticeWriteDateTime());
        try {
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
    @Async
    @Transactional
    public void deletePersonaSettingNotice(ResponseBodyEmitter emitter, long idx) {
        personaSettingNoticeDataRepository.deleteById(idx);
        try {
            emitter.send(1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            emitter.complete();
        }
    }
}
