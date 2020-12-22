package com.wing.forutona.App.ForutonaUser.Controller;

import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.App.ForutonaUser.Dto.PersonaSettingNoticeResDto;
import com.wing.forutona.App.ForutonaUser.Service.PersonaSettingNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PersonaSettingNoticeController {

    final PersonaSettingNoticeService personaSettingNoticeService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/PersonaSettingNotice")
    Page<PersonaSettingNoticeResDto> getPersonaSettingNotice(Pageable pageable){
        return personaSettingNoticeService.getPersonaSettingNotice(pageable);
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/PersonaSettingNotice/{idx}")
    PersonaSettingNoticeResDto getPersonaSettingNoticePage(@PathVariable("idx") long idx){
        return personaSettingNoticeService.getPersonaSettingNoticePage(idx);
    }

    /** 여기는 추후 관리자 에서 DB에 입력 할것 이므로 혹시 몰라 샘플 코드만 작성 해둠.
    @PostMapping(value = "/v1/PersonaSettingNotice")
    ResponseBodyEmitter insertPersonaSettingNotice(PersonaSettingNoticeInsertReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        personaSettingNoticeService.insertPersonaSettingNotice(emitter,reqDto);
        return emitter;
    }

    @PutMapping(value = "/v1/PersonaSettingNotice")
    ResponseBodyEmitter updatePersonaSettingNotice(PersonaSettingNoticeInsertReqDto reqDto){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        personaSettingNoticeService.updatePersonaSettingNotice(emitter,reqDto);
        return emitter;
    }

    @DeleteMapping(value ="/v1/PersonaSettingNotice/{idx}")
    ResponseBodyEmitter deletePersonaSettingNotice(@PathVariable("idx") long idx){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        personaSettingNoticeService.deletePersonaSettingNotice(emitter,idx);
        return emitter;
    }
    **/

}
