package com.wing.forutona.FTag.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Dto.TagRankingWrapdto;
import com.wing.forutona.FTag.Service.FTagService;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FTagControllerTest extends BaseTest {

    @Autowired
    FTagController fTagController;

    @MockBean
    FTagService fTagService;

    @BeforeEach
    void setUp(){

    }


    @Test
    @DisplayName("fTagService 호출")
    void getFTagRankingFromBallInfluencePower() throws Exception {
        //given
        TagRankingWrapdto tagRankingWrapdto = new TagRankingWrapdto();
        given(fTagService.getFTagRankingFromBallInfluencePower(any())).willReturn(tagRankingWrapdto);

        TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto = new TagRankingFromBallInfluencePowerReqDto();
        tagRankingFromBallInfluencePowerReqDto.setLatitude(127.0);
        tagRankingFromBallInfluencePowerReqDto.setLongitude(31.0);
        tagRankingFromBallInfluencePowerReqDto.setLimit(1000);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(tagRankingFromBallInfluencePowerReqDto,
                        new TypeReference<Map<String, String>>() {});
        params.setAll(ReqDto);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/FTag/RankingFromBallInfluencePower")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
        //then
        then(fTagService).should().getFTagRankingFromBallInfluencePower(any());
    }
}