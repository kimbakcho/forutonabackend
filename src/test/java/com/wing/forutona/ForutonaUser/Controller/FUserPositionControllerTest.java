package com.wing.forutona.ForutonaUser.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wing.forutona.BaseTest;
import com.wing.forutona.CustomUtil.FFireBaseToken;
import com.wing.forutona.ForutonaUser.Dto.UserPositionUpdateReqDto;
import com.wing.forutona.ForutonaUser.Service.UserPositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FUserPositionControllerTest extends BaseTest {

    @MockBean
    UserPositionService userPositionService;

    @MockBean
    FFireBaseToken fFireBaseToken;

    @BeforeEach
    void setUp() throws Exception {
        given(userPositionService.updateUserPosition(any(),any())).willReturn(1);
    }

    @Test
    @DisplayName("updateUserPosition call service ")
    public void updateUserPosition() throws Exception {
        //given
        UserPositionUpdateReqDto userPositionUpdateReqDto = new UserPositionUpdateReqDto(127.0, 18.0);

        String ReqDtoJson = new ObjectMapper().writeValueAsString(userPositionUpdateReqDto);

        //when
        MvcResult mvcResult = mockMvc.perform(put("/v1/ForutonaUser/UserPosition")
                .contentType(MediaType.APPLICATION_JSON).content(ReqDtoJson))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();

        //then
        then(userPositionService).should().updateUserPosition(any(),any());
    }
}