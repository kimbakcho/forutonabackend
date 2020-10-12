package com.wing.forutona.ForutonaUser.Controller;

import com.wing.forutona.BaseTest;
import com.wing.forutona.Common.RestDocsConfiguration;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import com.wing.forutona.ForutonaUser.Service.FUserInfoService;
import org.apache.coyote.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.relaxedRequestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class FUserInfoControllerTest extends BaseTest {

    @MockBean
    FUserInfoService fUserInfoService;

    @Test
    void getUserNickNameWithFullTextMatchIndex() throws Exception {

        //gvien
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("searchNickName","테스트");
        params.add("limit","40");
        params.add("offset","0");

        List<FUserInfoSimpleResDto> contents = new ArrayList<>();
        FUserInfoSimpleResDto user1 = FUserInfoSimpleResDto.builder()
                .uid("testuid")
                .nickName("테스트")
                .userLevel(0.0)
                .cumulativeInfluence(0.0)
                .followCount(10L)
                .isoCode("KR")
                .playerPower(0.0)
                .profilePictureUrl("https://test.com/test.png")
                .selfIntroduction("테스트")
                .build();

        contents.add(user1);
        FUserInfoSimpleResDto user2 = (FUserInfoSimpleResDto)user1.clone();
        user2.setNickName("테스트1");
        contents.add(user2);
        FUserInfoSimpleResDto user3 = (FUserInfoSimpleResDto)user1.clone();
        user2.setNickName("테스트2");
        contents.add(user3);
        FUserInfoSimpleResDto user4 = (FUserInfoSimpleResDto)user1.clone();
        user2.setNickName("aa 테스트 3");
        contents.add(user4);

        Page<FUserInfoSimpleResDto> pageResult =
                new PageImpl<FUserInfoSimpleResDto>(contents,PageRequest.of(0,40),contents.size());

        given(fUserInfoService.getUserNickNameWithFullTextMatchIndex(anyString(),any()))
                .willReturn(pageResult);

        //when then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/FUserInfo/UserNickNameWithFullTextMatchIndex")
                .contentType(MediaType.APPLICATION_JSON)
                .params(params))
                .andDo(print())
                .andDo(document("UserNickNameWithFullTextMatchIndex",
                        relaxedRequestParameters(
                                parameterWithName("searchNickName").description("Search 하는 NickName")),
                        relaxedResponseFields
                                (fieldWithPath("content[].uid").description("uid"),
                                        fieldWithPath("content[].nickName").description("NickName"),
                                        fieldWithPath("content[].profilePictureUrl").description("Profile Image"),
                                        fieldWithPath("content[].isoCode").description("국적 코드"),
                                        fieldWithPath("content[].userLevel").description("레벨"),
                                        fieldWithPath("content[].selfIntroduction").description("selfIntroduction"),
                                        fieldWithPath("content[].cumulativeInfluence").description("유저 영향력"),
                                        fieldWithPath("content[].followCount").description("follow 숫자"),
                                        fieldWithPath("content[].playerPower").description("playerPower")
                                )
                        )
                )
                .andExpect(status().isOk())
                .andReturn();
    }
}