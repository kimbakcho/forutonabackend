package com.wing.forutona.App.FBall.Repository.FBallReply;

import com.wing.forutona.BaseTest;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallReply.Domain.FBallReply;
import com.wing.forutona.App.FBall.Domain.FBallState;
import com.wing.forutona.App.FBall.Domain.FBallType;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyReqDto;
import com.wing.forutona.App.FBallReply.Dto.FBallReplyResDto;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBallReply.Repositroy.FBallReplyDataRepository;
import com.wing.forutona.App.FBallReply.Repositroy.FBallReplyQueryRepository;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
class FBallReplyQueryRepositoryTest extends BaseTest {

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    FBallReplyQueryRepository fBallReplyQueryRepository;

    @Autowired
    FBallReplyDataRepository fBallReplyDataRepository;


    FBall testBall;

    @BeforeEach
    void BeforeEach(){
        testBall = FBall.builder()
                .ballUuid("TESTBBallUuid")
                .ballName("TESTBall")
                .ballState(FBallState.Play)
                .ballType(FBallType.IssueBall)
                .activationTime(LocalDateTime.now().plusDays(7))
                .description("{}")
                .latitude(37.4402052)
                .longitude(126.79369789999998)
                .uid(testUser)
                .ballPower(0)
                .makeTime(LocalDateTime.now())
                .build();
        fBallDataRepository.save(testBall);
    }

    @Test
    @DisplayName("메인 댓글만 가져오기 테스트")
    void getFBallReplyReqOnlyMain() {
        //given
        Long replyNumber = 999990L;
        for(int j=0;j<10;j++){
            for(int i=0;i<2;i++){
                FBallReply fBallReply = FBallReply.builder()
                        .replyUuid(UUID.randomUUID().toString())
                        .replyBallUuid(testBall)
                        .replyNumber(replyNumber+j)
                        .replySort((long)i)
                        .replyDepth(0L)
                        .replyText("test")
                        .replyUid(testUser)
                        .replyUpdateDateTime(LocalDateTime.now().minusHours(1))
                        .build();
                fBallReplyDataRepository.save(fBallReply);
            }
        }
        FBallReplyReqDto reqDto = new FBallReplyReqDto();
        reqDto.setBallUuid(testBall.getBallUuid());
        reqDto.setReqOnlySubReply(false);
        //when
        Page<FBallReplyResDto> pageItem = fBallReplyQueryRepository.getFBallRootNodeReply(testBall, PageRequest.of(0, 20));
        //then
        assertEquals(10,pageItem.getContent().size());
        assertEquals(1,pageItem.getContent().get(0).getChildCount());
    }

    @Test
    @DisplayName("대댓글만 가져오기 테스트")
    void getFBallReplyReqOnlySubReply() {
        //given
        List<FBall> fBalls = fBallDataRepository.findAll(PageRequest.of(0, 1)).getContent();
        fBallReplyDataRepository.deleteByReplyBallUuid(fBalls.get(0));
        Long replyNumber = 999990L;
        for(int j=0;j<2;j++){
            for(int i=0;i<10;i++){
                FBallReply fBallReply = FBallReply.builder()
                        .replyUuid(UUID.randomUUID().toString())
                        .replyBallUuid(fBalls.get(0))
                        .replyNumber(replyNumber+j)
                        .replySort((long)i)
                        .replyDepth(0L)
                        .replyText("test")
                        .replyUid(testUser)
                        .replyUpdateDateTime(LocalDateTime.now().minusHours(1))
                        .build();
                fBallReplyDataRepository.save(fBallReply);
            }
        }
        FBallReplyReqDto reqDto = new FBallReplyReqDto();
        reqDto.setBallUuid(fBalls.get(0).getBallUuid());
        reqDto.setReplyNumber(replyNumber);
        reqDto.setReqOnlySubReply(true);
        //when
        Page<FBallReplyResDto> fBallReply = fBallReplyQueryRepository.getFBallSubNodeReply(fBalls.get(0),reqDto.getReplyNumber(), PageRequest.of(0, 20));
        //then
        assertEquals(9,fBallReply.getContent().size());
    }


    @Test
    void getMaxReplyNumber() {

        Long maxReplyNumber = fBallReplyQueryRepository.getMaxReplyNumber(testBall.getBallUuid());

        assertEquals(-1,maxReplyNumber);

        FBallReply testReply = FBallReply.builder()
                .replyUid(testUser)
                .replyUploadDateTime(LocalDateTime.now())
                .replySort(0L)
                .replyText("TEST")
                .replyNumber(0L)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyUuid(UUID.randomUUID().toString())
                .replyDepth(0L)
                .replyBallUuid(testBall)
                .build();

        fBallReplyDataRepository.save(testReply);

        Long maxReplyNumber1 = fBallReplyQueryRepository.getMaxReplyNumber(testBall.getBallUuid());

        assertEquals(0,maxReplyNumber1);

    }

    @Test
    void getMaxSortNumber() {
        String replyUuid = UUID.randomUUID().toString();
        FBallReply testReply = FBallReply.builder()
                .replyUid(testUser)
                .replyUploadDateTime(LocalDateTime.now())
                .replySort(0L)
                .replyText("TEST")
                .replyNumber(0L)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyUuid(replyUuid)
                .replyDepth(0L)
                .replyBallUuid(testBall)
                .build();

        fBallReplyDataRepository.save(testReply);

        Long maxSortNumber = fBallReplyQueryRepository.getMaxSortNumber(replyUuid);
        assertEquals(0,maxSortNumber);


        FBallReply testSubReply = FBallReply.builder()
                .replyUid(testUser)
                .replyUploadDateTime(LocalDateTime.now())
                .replySort(1L)
                .replyText("TEST")
                .replyNumber(0L)
                .replyUpdateDateTime(LocalDateTime.now())
                .replyUuid(replyUuid)
                .replyDepth(0L)
                .replyBallUuid(testBall)
                .build();

        fBallReplyDataRepository.save(testSubReply);

        Long maxSortNumber1 = fBallReplyQueryRepository.getMaxSortNumber(replyUuid);
        assertEquals(1,maxSortNumber1);
    }
}