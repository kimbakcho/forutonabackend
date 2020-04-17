package com.wing.forutona.PersonaSettingNotice.Controller;

import com.wing.forutona.CustomUtil.ResponseAddJsonHeader;
import com.wing.forutona.PersonaSettingNotice.Dto.PersonaSettingNoticeInsertReqDto;
import com.wing.forutona.PersonaSettingNotice.Service.PersonaSettingNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;



@RestController
public class PersonaSettingNoticeController {

    @Autowired
    PersonaSettingNoticeService personaSettingNoticeService;

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/PersonaSettingNotice")
    ResponseBodyEmitter getPersonaSettingNotice(Pageable pageable){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        personaSettingNoticeService.getPersonaSettingNotice(emitter,pageable);
        return emitter;
    }

    @ResponseAddJsonHeader
    @GetMapping(value = "/v1/PersonaSettingNotice/{idx}")
    ResponseBodyEmitter getPersonaSettingNoticePage(@PathVariable("idx") long idx){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        personaSettingNoticeService.getPersonaSettingNoticePage(emitter,idx);
        return emitter;
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
