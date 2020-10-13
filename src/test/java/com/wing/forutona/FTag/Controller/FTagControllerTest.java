package com.wing.forutona.FTag.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.Common.RestDocsConfiguration;
import com.wing.forutona.FBall.Domain.FBallState;
import com.wing.forutona.FBall.Domain.FBallType;
import com.wing.forutona.FBall.Dto.FBallResDto;
import com.wing.forutona.FTag.Dto.*;
import com.wing.forutona.FTag.Service.FTagService;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoSimpleResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
class FTagControllerTest extends BaseTest {

    @Autowired
    FTagController fTagController;

    @MockBean
    FTagService fTagService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("fTagService 호출")
    void getFTagRankingFromBallInfluencePower() throws Exception {
        //given
        List<TagRankingResDto> tagRankingResDtos = new ArrayList<>();
        tagRankingResDtos.add(new TagRankingResDto("TESTTag1",0.03));
        tagRankingResDtos.add(new TagRankingResDto("TESTTag2",0.02));
        tagRankingResDtos.add(new TagRankingResDto("TESTTag3",0.01));
        given(fTagService.getFTagRankingFromBallInfluencePower(any(), anyInt())).willReturn(tagRankingResDtos);

        TagRankingFromBallInfluencePowerReqDto tagRankingFromBallInfluencePowerReqDto = new TagRankingFromBallInfluencePowerReqDto();
        tagRankingFromBallInfluencePowerReqDto.setMapCenterLatitude(37.5012);
        tagRankingFromBallInfluencePowerReqDto.setMapCenterLongitude(126.8976);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(tagRankingFromBallInfluencePowerReqDto,
                        new TypeReference<Map<String, String>>() {
                        });
        params.setAll(ReqDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/FTag/RankingFromBallInfluencePower")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andDo(print())
                .andDo(document("FTagRankingFromBallInfluencePower",
                        relaxedRequestParameters(
                                parameterWithName("mapCenterLatitude").description("Search 하는 MapCenter Lat"),
                                parameterWithName("mapCenterLongitude").description("Search 하는 MapCenter Long")),
                        relaxedResponseFields
                                (fieldWithPath("[].tagName").description("TagName"),
                                fieldWithPath("[].tagPower").description("ball의 BI로 부터 계산된 TagPower"))
                        )
                )
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("getTagRankingFromTextOrderBySumBI 호출")
    void getTagRankingFromTextOrderBySumBI() throws Exception {
        //given
        List<TagRankingResDto> tagRankingResDtos = new ArrayList<>();
        tagRankingResDtos.add(new TagRankingResDto("TESTTag1",0.03));
        tagRankingResDtos.add(new TagRankingResDto("TESTTag2",0.02));
        tagRankingResDtos.add(new TagRankingResDto("TESTTag3",0.01));
        given(fTagService.getTagRankingFromTextOrderBySumBI(any())).willReturn(tagRankingResDtos);

        TagRankingFromTextReqDto tagRankingFromTextReqDto = new TagRankingFromTextReqDto();
        tagRankingFromTextReqDto.setMapCenterLatitude(37.5012);
        tagRankingFromTextReqDto.setMapCenterLongitude(126.8976);
        tagRankingFromTextReqDto.setSearchTagText("TEST");


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(tagRankingFromTextReqDto,
                        new TypeReference<Map<String, String>>() {
                        });
        params.setAll(ReqDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/FTag/TagRankingFromTextOrderBySumBI")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andDo(print())
                .andDo(document("TagRankingFromTextOrderBySumBI",
                        relaxedRequestParameters(
                                parameterWithName("mapCenterLatitude").description("Search 하는 MapCenter Lat"),
                                parameterWithName("mapCenterLongitude").description("Search 하는 MapCenter Long"),
                                parameterWithName("searchTagText").description("Search 하는 Tag Name")
                                ),
                        relaxedResponseFields
                                (fieldWithPath("[].tagName").description("TagName"),
                                        fieldWithPath("[].tagPower").description("ball의 BI로 부터 계산된 TagPower"))
                        )
                )
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void getTagItem() throws Exception {
        List<FBallTagResDto> fBallTagResDtos = new ArrayList<>();
        FBallTagResDto fBallTagResDto1 = new FBallTagResDto();
        fBallTagResDto1.setBallUuid(basicBall("TESTUUID","TESTBALL"));
        fBallTagResDto1.setIdx(10L);
        fBallTagResDto1.setTagItem("TEST");
        fBallTagResDtos.add(fBallTagResDto1);

        FBallTagResDto fBallTagResDto2 = new FBallTagResDto();
        fBallTagResDto2.setBallUuid(basicBall("TESTUUID","TESTBALL"));
        fBallTagResDto2.setIdx(11L);
        fBallTagResDto2.setTagItem("TEST");
        fBallTagResDtos.add(fBallTagResDto2);


        Page<FBallTagResDto> page = new PageImpl<FBallTagResDto>(fBallTagResDtos,
                PageRequest.of(0,40, Sort.Direction.DESC,"ballPower"),fBallTagResDtos.size());

        TextMatchTagBallReqDto textMatchTagBallReqDto = new TextMatchTagBallReqDto();

        textMatchTagBallReqDto.setMapCenterLatitude(37.5012);
        textMatchTagBallReqDto.setMapCenterLongitude(126.8976);
        textMatchTagBallReqDto.setSearchText("TEST");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> ReqDto = new ObjectMapper()
                .convertValue(textMatchTagBallReqDto,
                        new TypeReference<Map<String, String>>() {
                        });
        ReqDto.put("limit", "40");
        ReqDto.put("offset", "0");
        ReqDto.put("sort","ballPower,DESC");
        params.setAll(ReqDto);


        given(fTagService.getTagItem(any(),any())).willReturn(page);

        //when then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/FTag/TagItem")
                .contentType(MediaType.APPLICATION_JSON).params(params))
                .andDo(print())
                .andDo(document("TagItem",
                        relaxedRequestParameters(
                                parameterWithName("mapCenterLatitude").description("Search 하는 MapCenter Lat"),
                                parameterWithName("mapCenterLongitude").description("Search 하는 MapCenter Long"),
                                parameterWithName("searchText").description("Search 하는 Tag Name")
                        ))
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    private FBallResDto basicBall(String ballTestUuid, String testBall1NAME) {
        FBallResDto fBallResDto = new FBallResDto();
        fBallResDto.setLatitude(37.5541);
        fBallResDto.setLongitude(127.1223);
        fBallResDto.setBallUuid(ballTestUuid);
        fBallResDto.setBallName(testBall1NAME);
        fBallResDto.setBallType(FBallType.IssueBall);
        fBallResDto.setBallState(FBallState.Play);
        fBallResDto.setPlaceAddress("대한민국 경기도");
        fBallResDto.setBallHits(100);
        fBallResDto.setBallLikes(10);
        fBallResDto.setBallDisLikes(5);
        fBallResDto.setUid(basicUser());
        fBallResDto.setCommentCount(5);
        fBallResDto.setBallPower(10);
        fBallResDto.setActivationTime(LocalDateTime.now().plusDays(7));
        fBallResDto.setMakeTime(LocalDateTime.now());
        fBallResDto.setDescription("TEST");
        return fBallResDto;
    }

    private FUserInfoSimpleResDto basicUser() {
        FUserInfoSimpleResDto fUserInfoSimpleResDto = new FUserInfoSimpleResDto();
        fUserInfoSimpleResDto.setUid("TESTUid");
        fUserInfoSimpleResDto.setNickName("UserNickName");
        fUserInfoSimpleResDto.setProfilePictureUrl("https://storage.googleapis.com/publicforutona/profileimage/6a44fed4-0ca3-47d7-b5a8-2389ad8d0145.png");
        fUserInfoSimpleResDto.setIsoCode("KR");
        fUserInfoSimpleResDto.setUserLevel(0.0);
        fUserInfoSimpleResDto.setSelfIntroduction("Hi");
        fUserInfoSimpleResDto.setCumulativeInfluence(2.0);
        fUserInfoSimpleResDto.setFollowCount(10L);
        return fUserInfoSimpleResDto;
    }
}