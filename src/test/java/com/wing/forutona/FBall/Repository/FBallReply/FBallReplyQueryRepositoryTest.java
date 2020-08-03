package com.wing.forutona.FBall.Repository.FBallReply;

import com.wing.forutona.BaseTest;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import com.wing.forutona.FBall.Dto.FBallReplyReqDto;
import com.wing.forutona.FBall.Dto.FBallReplyResDto;
import com.wing.forutona.FBall.Dto.FBallReplyResWrapDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Domain.FUserInfoSimple;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.ForutonaUser.Repository.FUserInfoSimpleDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
class FBallReplyQueryRepositoryTest extends BaseTest {

    @Autowired
    EntityManager em;

    @Autowired
    FBallDataRepository fBallDataRepository;

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    FUserInfoSimpleDataRepository fUserInfoSimpleDataRepository;

    @Autowired
    FBallReplyQueryRepository fBallReplyQueryRepository;

    @Autowired
    FBallReplyDataRepository fBallReplyDataRepository;

    @BeforeEach
    void BeforeEach(){
        em.clear();
    }

    @Test
    @DisplayName("메인 댓글만 가져오기 테스트")
    void getFBallReplyReqOnlyMain() {
        //given
        List<FBall> fBalls = fBallDataRepository.findAll(PageRequest.of(0, 1)).getContent();
        List<FUserInfoSimple> users = fUserInfoSimpleDataRepository.findAll(PageRequest.of(0, 1)).getContent();
        fBallReplyDataRepository.deleteByReplyBallUuid(fBalls.get(0));
        Long replyNumber = 999990L;
        for(int j=0;j<10;j++){
            for(int i=0;i<2;i++){
                FBallReply fBallReply = FBallReply.builder()
                        .replyUuid(UUID.randomUUID().toString())
                        .replyBallUuid(fBalls.get(0))
                        .replyNumber(replyNumber+j)
                        .replySort((long)i)
                        .replyDepth(0L)
                        .replyText("test")
                        .replyUid(users.get(0))
                        .replyUpdateDateTime(LocalDateTime.now().minusHours(1))
                        .build();
                em.persist(fBallReply);
            }
        }
        FBallReplyReqDto reqDto = new FBallReplyReqDto();
        reqDto.setBallUuid(fBalls.get(0).getBallUuid());
        reqDto.setReqOnlySubReply(false);
        //when
        Page<FBallReplyResDto> pageItem = fBallReplyQueryRepository.getFBallRootNodeReply(reqDto, PageRequest.of(0, 20));
        //then
        assertEquals(10,pageItem.getSize());
    }

    @Test
    @DisplayName("대댓글만 가져오기 테스트")
    void getFBallReplyReqOnlySubReply() {
        //given
        List<FBall> fBalls = fBallDataRepository.findAll(PageRequest.of(0, 1)).getContent();
        List<FUserInfoSimple> users = fUserInfoSimpleDataRepository.findAll(PageRequest.of(0, 1)).getContent();
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
                        .replyUid(users.get(0))
                        .replyUpdateDateTime(LocalDateTime.now().minusHours(1))
                        .build();
                em.persist(fBallReply);
            }
        }
        FBallReplyReqDto reqDto = new FBallReplyReqDto();
        reqDto.setBallUuid(fBalls.get(0).getBallUuid());
        reqDto.setReplyNumber(replyNumber);
        reqDto.setReqOnlySubReply(true);
        //when
        Page<FBallReplyResDto> fBallReply = fBallReplyQueryRepository.getFBallSubNodeReply(reqDto, PageRequest.of(0, 20));
        //then
        assertEquals(9,fBallReply.getSize());
    }


}