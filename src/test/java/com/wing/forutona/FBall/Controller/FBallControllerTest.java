package com.wing.forutona.FBall.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wing.forutona.BaseTest;
import com.wing.forutona.Common.RestDocsConfiguration;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Domain.FBallType;
import com.wing.forutona.FBall.Dto.FBallListUpFromBallInfluencePowerReqDto;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FBall.Service.BallListUpService;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class FBallControllerTest extends BaseTest {


    @MockBean
    BallListUpService ballListUpService;


    @Test
    @DisplayName("fBallListUpService 영향력 순으로 ListUp 호출")
    void listUpBallInfluencePower() throws Exception {

        List<FBallResDto> fBallResDtos = new ArrayList<>();
        FBallResDto fBallResDto =  new FBallResDto();
        fBallResDto.setLatitude(37.5541);
        fBallResDto.setLongitude(127.1223);
        fBallResDto.setBallUuid("TESTBALL1UUID");
        fBallResDto.setBallName("TESTBall1NAME");
        fBallResDto.setBallType(FBallType.IssueBall);
        fBallResDto.setBallState(FBallState.Play);
        fBallResDto.setPlaceAddress("대한민국 경기도");
        fBallResDto.setBallHits(100);
        fBallResDto.setBallLikes(10);
        fBallResDto.setBallDisLikes(5);
        fBallResDto.setCommentCount(5);
        fBallResDto.setBallPower(10);
        fBallResDto.setActivationTime(LocalDateTime.now().plusDays(7));
        fBallResDto.setMakeTime(LocalDateTime.now());
        fBallResDto.setDescription("TEST");

        FUserInfoSimpleResDto fUserInfoSimpleResDto = new FUserInfoSimpleResDto();
        fUserInfoSimpleResDto.setUid("TESTUid");
        fUserInfoSimpleResDto.setNickName("UserNickName");
        fUserInfoSimpleResDto.setProfilePictureUrl("https://storage.googleapis.com/publicforutona/profileimage/6a44fed4-0ca3-47d7-b5a8-2389ad8d0145.png");
        fUserInfoSimpleResDto.setIsoCode("KR");
        fUserInfoSimpleResDto.setUserLevel(0.0);
        fUserInfoSimpleResDto.setSelfIntroduction("Hi");
        fUserInfoSimpleResDto.setCumulativeInfluence(2.0);
        fUserInfoSimpleResDto.setFollowCount(10L);

        fBallResDto.setUid(fUserInfoSimpleResDto);
        fBallResDto.setUserLevel(0.0);
        fBallResDto.setInfluencePower(9.0);
        fBallResDto.setContributor(10);
        fBallResDto.setBallDeleteFlag(false);

        FBallResDto clone1 = (FBallResDto)fBallResDto.clone();
        fBallResDtos.add(clone1);
        FBallResDto clone2 = (FBallResDto)fBallResDto.clone();
        clone2.setBallUuid("TESTBALL2UUID");
        clone2.setBallUuid("TESTBall2NAME");
        clone2.setLatitude(36.5531);
        clone2.setLongitude(127.1323);
        fBallResDtos.add(clone2);
        FBallResDto clone3 = (FBallResDto)fBallResDto.clone();
        clone3.setBallUuid("TESTBALL3UUID");
        clone3.setBallUuid("TESTBall3NAME");
        clone3.setLatitude(36.5131);
        clone3.setLongitude(127.2323);
        fBallResDtos.add(clone3);
        //given
        Pageable pageable =  PageRequest.of(0,20);
        PageImpl<FBallResDto> testData = new PageImpl<FBallResDto>(fBallResDtos,pageable,3);

        when(ballListUpService.searchBallListUpInfluencePower(any(),any())).thenReturn(testData);

        FBallListUpFromBallInfluencePowerReqDto fBallListUpFromBallInfluencePowerReqDto =
                new FBallListUpFromBallInfluencePowerReqDto();

        fBallListUpFromBallInfluencePowerReqDto.setLatitude(127.0);
        fBallListUpFromBallInfluencePowerReqDto.setLongitude(37.0);
        fBallListUpFromBallInfluencePowerReqDto.setBallLimit(1000);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(fBallListUpFromBallInfluencePowerReqDto,
                        new TypeReference<Map<String, String>>() {});

        ReqDto.put("limit","20");
        ReqDto.put("offset","0");
        params.setAll(ReqDto);

        //when then
        mockMvc.perform(get("/v1/FBall/ListUpFromBallInfluencePower")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,super.getTestUserToken())
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("FBallListUp",
                        relaxedRequestParameters(
                                parameterWithName("latitude").description("경도"),
                                parameterWithName("longitude").description("위도"),
                                parameterWithName("ballLimit").description("볼 검색 범위 최대 갯수")
                        ),
                        relaxedResponseFields(fieldWithPath("content").description("ball 들"))
                        ));


    }
}