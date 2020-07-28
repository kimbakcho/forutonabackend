package com.wing.forutona.FBall.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Dto.FBallListUpFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Dto.FBallListUpWrapDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FBallListUpControllerTest extends BaseTest {


    @BeforeEach
    void setUp() throws ParseException {

    }

    @Test
    @DisplayName("fBallListUpService 영향력 순으로 ListUp 호출")
    void listUpBallInfluencePower() throws Exception {
        //given
        FBallListUpFromBallInfluencePowerReqDto fBallListUpFromBallInfluencePowerReqDto =
                new FBallListUpFromBallInfluencePowerReqDto();
        fBallListUpFromBallInfluencePowerReqDto.setLatitude(127.0);
        fBallListUpFromBallInfluencePowerReqDto.setLongitude(37.0);
        fBallListUpFromBallInfluencePowerReqDto.setBallLimit(10);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(fBallListUpFromBallInfluencePowerReqDto,
                        new TypeReference<Map<String, String>>() {});
        ReqDto.put("limit","20");
        ReqDto.put("offset","0");
        params.setAll(ReqDto);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/FBall/ListUpFromBallInfluencePower")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();

        //then
//        then(fBallListUpService).should().ListUpBallInfluencePower(any(),any());
    }
}