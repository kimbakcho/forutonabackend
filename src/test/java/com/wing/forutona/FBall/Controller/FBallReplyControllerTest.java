package com.wing.forutona.FBall.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Dto.*;
import com.wing.forutona.FBall.Service.FBallReply.FBallReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FBallReplyControllerTest extends BaseTest {

    @MockBean
    FBallReplyService fBallReplyService;

    @Test
    void getFBallReply() throws Exception {
        //given
        FBallReplyReqDto reqDto = new FBallReplyReqDto();
        reqDto.setReqOnlySubReply(false);
        reqDto.setBallUuid("testBallUuid");
        reqDto.setReplyNumber(0L);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(reqDto,
                        new TypeReference<Map<String, String>>() {});
        ReqDto.put("limit","20");
        ReqDto.put("offset","0");
        params.setAll(ReqDto);

        FBallReplyResWrapDto fBallReplyResWrapDto = new FBallReplyResWrapDto();
        fBallReplyResWrapDto.setCount(0);
        fBallReplyResWrapDto.setReplyTotalCount(0L);
        fBallReplyResWrapDto.setContents(new ArrayList<FBallReplyResDto>());
//        when(fBallReplyService.getFBallReply(any(),any())).thenReturn(fBallReplyResWrapDto);
        //when
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/FBallReply")
//                .contentType(MediaType.APPLICATION_JSON).params(params))
//                .andExpect(request().asyncStarted())
//                .andReturn();
//        mockMvc.perform(asyncDispatch(mvcResult))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk()).andReturn();
//        //then
//        verify(fBallReplyService).getFBallReply(any(),any());
    }
}