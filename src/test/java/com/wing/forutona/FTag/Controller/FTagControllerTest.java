package com.wing.forutona.FTag.Controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wing.forutona.BaseTest;
import com.wing.forutona.Common.RestDocsConfiguration;
import com.wing.forutona.FTag.Dto.TagRankingFromBallInfluencePowerReqDto;
import com.wing.forutona.FTag.Dto.TagRankingResDto;
import com.wing.forutona.FTag.Service.FTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
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
}