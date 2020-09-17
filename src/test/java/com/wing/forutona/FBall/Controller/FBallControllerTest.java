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
import com.wing.forutona.FBall.Service.BallSelectService;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class FBallControllerTest extends BaseTest {


    @MockBean
    BallListUpService ballListUpService;

    @MockBean
    BallSelectService ballSelectService;
    ;

    @Test
    @DisplayName("fBallListUpService 영향력 순으로 ListUp 호출")
    void listUpBallInfluencePower() throws Exception {
        List<FBallResDto> fBallResDtos = new ArrayList<>();
        FBallResDto fBallResDto = new FBallResDto();
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
        fBallResDto.setInfluencePower(9.0);
        fBallResDto.setContributor(10);
        fBallResDto.setBallDeleteFlag(false);

        FBallResDto clone1 = (FBallResDto) fBallResDto.clone();
        fBallResDtos.add(clone1);
        FBallResDto clone2 = (FBallResDto) fBallResDto.clone();
        clone2.setBallUuid("TESTBALL2UUID");
        clone2.setBallUuid("TESTBall2NAME");
        clone2.setLatitude(36.5531);
        clone2.setLongitude(127.1323);
        fBallResDtos.add(clone2);
        FBallResDto clone3 = (FBallResDto) fBallResDto.clone();
        clone3.setBallUuid("TESTBALL3UUID");
        clone3.setBallUuid("TESTBall3NAME");
        clone3.setLatitude(36.5131);
        clone3.setLongitude(127.2323);
        fBallResDtos.add(clone3);

        //given
        Pageable pageable = PageRequest.of(0, 40);
        PageImpl<FBallResDto> testData = new PageImpl<FBallResDto>(fBallResDtos, pageable, 3);

        when(ballListUpService.searchBallListUpOrderByBI(any(), any())).thenReturn(testData);

        FBallListUpFromBallInfluencePowerReqDto fBallListUpFromBallInfluencePowerReqDto =
                new FBallListUpFromBallInfluencePowerReqDto();

        fBallListUpFromBallInfluencePowerReqDto.setMapCenterLongitude(37.5012);
        fBallListUpFromBallInfluencePowerReqDto.setMapCenterLatitude(126.8976);
        fBallListUpFromBallInfluencePowerReqDto.setUserLongitude(126.9203);
        fBallListUpFromBallInfluencePowerReqDto.setUserLatitude(37.5012);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(fBallListUpFromBallInfluencePowerReqDto,
                        new TypeReference<Map<String, String>>() {});

        ReqDto.put("limit", "40");
        ReqDto.put("offset", "0");
        params.setAll(ReqDto);

        //when then
        mockMvc.perform(get("/v1/FBall/ListUpBallListUpOrderByBI")
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, super.getTestUserToken())
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("ListUpBallListUpOrderByBI",
                        relaxedRequestParameters(
                                parameterWithName("mapCenterLongitude").description("find 하는 Map 중앙 위치"),
                                parameterWithName("mapCenterLatitude").description("find 하는 Map 중앙 위치"),
                                parameterWithName("userLongitude").description("user 위치"),
                                parameterWithName("userLatitude").description("user 위치")
                        ),
                        relaxedResponseFields(fieldWithPath("content").description("ball 들"),
                                fieldWithPath("content[].latitude").description("위도"),
                                fieldWithPath("content[].longitude").description("경도"),
                                fieldWithPath("content[].ballUuid").description("Ball Id")
                        )
                ));


    }

    @Test
    void selectBall() throws Exception {
        //given
        String ballTestUuid = "TESTBALL1UUID";
        FBallResDto fBallResDto = new FBallResDto();
        fBallResDto.setLatitude(37.5541);
        fBallResDto.setLongitude(127.1223);
        fBallResDto.setBallUuid(ballTestUuid);
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
        fBallResDto.setInfluencePower(9.0);
        fBallResDto.setContributor(10);
        fBallResDto.setBallDeleteFlag(false);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ballUuid", ballTestUuid);

        when(ballSelectService.selectBall(ballTestUuid)).thenReturn(fBallResDto);
        //when then
        mockMvc.perform(get("/v1/FBall").params(params)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, super.getTestUserToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ballUuid").value(ballTestUuid))
                .andDo(print())
                .andDo(document("FBallSelect",
                        requestParameters(parameterWithName("ballUuid").description("Ball Uuid")),
                        relaxedResponseFields(fieldWithPath("latitude").description("latitude"),
                                fieldWithPath("longitude").description("longitude"),
                                fieldWithPath("ballUuid").description("ball Uuid"),
                                fieldWithPath("ballName").description("ballName"),
                                fieldWithPath("ballType").type(FBallType.class).description("ballType"),
                                fieldWithPath("ballState").type(FBallState.class).description("ballState"),
                                fieldWithPath("placeAddress").description("Ball 주소"),
                                fieldWithPath("ballHits").description("ballHits"),
                                fieldWithPath("ballLikes").description("Ball Like"),
                                fieldWithPath("ballDisLikes").description("ballDisLikes"),
                                fieldWithPath("commentCount").description("commentCount"),
                                fieldWithPath("ballPower").description("ballPower(BP)"),
                                fieldWithPath("activationTime").description("Ball 이 살아 있을수 있는 시간"),
                                fieldWithPath("makeTime").description("ball 생성 시간"),
                                fieldWithPath("description").description("설명 JSON"),
                                fieldWithPath("uid").type(FUserInfoSimpleResDto.class).description("작성자"),
                                fieldWithPath("influencePower").description("ball Maker Level"),
                                fieldWithPath("contributor").description("기여자 숫자"),
                                fieldWithPath("ballDeleteFlag").description("볼 삭제 유무")
                        )));
    }

}