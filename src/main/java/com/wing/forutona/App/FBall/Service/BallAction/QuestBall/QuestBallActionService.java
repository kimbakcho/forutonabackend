package com.wing.forutona.App.FBall.Service.BallAction.QuestBall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Domain.FBallState;
import com.wing.forutona.App.FBall.Domain.QuestBallParticipant;
import com.wing.forutona.App.FBall.Dto.*;
import com.wing.forutona.App.FBall.Repository.FBallDataRepository;
import com.wing.forutona.App.FBall.Repository.FBallQueryRepository;
import com.wing.forutona.App.FBall.Repository.QuestBallParticipantDataRepository;
import com.wing.forutona.App.FBall.Value.QuestBallParticipateState;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoDataRepository;
import com.wing.forutona.App.ForutonaUser.Repository.FUserInfoQueryRepository;
import com.wing.forutona.SpringSecurity.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestBallActionService {

    final FBallDataRepository fBallDataRepository;

    final QuestBallParticipantDataRepository questBallParticipantDataRepository;

    final FUserInfoDataRepository fUserInfoDataRepository;

    public FBallResDto recruitParticipantService(RecruitParticipantReqDto reqDto) throws JsonProcessingException {
        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        String description = fBall.getDescription();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        QuestBallDescriptionDto questBallDescriptionDto = mapper.readValue(description, QuestBallDescriptionDto.class);
        questBallDescriptionDto.setQualifyingForQuestText(reqDto.getQualifyingForQuestText());
        description = mapper.writeValueAsString(questBallDescriptionDto);
        fBall.setDescription(description);
        fBall.setBallState(FBallState.Play);
        return new FBallResDto(fBall);
    }

    public ParticipantResDto participate(ParticipantReqDto reqDto, UserAdapter userAdapter) {

        FBall fBall = fBallDataRepository.findById(reqDto.getBallUuid()).get();
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userAdapter.getfUserInfo().getUid()).get();
        QuestBallParticipant questBallParticipant = QuestBallParticipant.builder()
                .ballUuid(fBall)
                .uid(fUserInfo)
                .checkInPositionLat(null)
                .checkInPositionLng(null)
                .dislikePoint(0)
                .likePoint(0)
                .participationTime(LocalDateTime.now())
                .photoShotForCertificationImage(null)
                .startPositionLat(null)
                .startPositionLng(null)
                .currentState(QuestBallParticipateState.Wait)
                .build();

        QuestBallParticipant save = questBallParticipantDataRepository.save(questBallParticipant);

        ParticipantResDto participantResDto = new ParticipantResDto();
        participantResDto.setBallUuid(save.getBallUuid().getBallUuid());
        participantResDto.setMakerUid(save.getBallUuid().getMakerUid());
        participantResDto.setSuccess(true);
        return participantResDto;
    }


    public QuestBallParticipantResDto getParticipate(String ballUuid, UserAdapter userAdapter) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        FUserInfo fUserInfo = fUserInfoDataRepository.findById(userAdapter.getfUserInfo().getUid()).get();
        Optional<QuestBallParticipant> byBallUuidAndUid = questBallParticipantDataRepository.findByBallUuidAndUid(fBall, fUserInfo);
        if(byBallUuidAndUid.isPresent()){
            QuestBallParticipant questBallParticipant = byBallUuidAndUid.get();
            return QuestBallParticipantResDto.fromQuestBallParticipant(questBallParticipant);
        }else {
            QuestBallParticipantResDto questBallParticipantResDto = new QuestBallParticipantResDto();
            questBallParticipantResDto.setBallUuid(null);
            questBallParticipantResDto.setUid(null);
            return questBallParticipantResDto;
        }

    }

    public List<QuestBallParticipantResDto> getParticipates(String ballUuid, QuestBallParticipateState state) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        List<QuestBallParticipant> byBallUuidAndCurrentState = questBallParticipantDataRepository.findByBallUuidAndCurrentState(fBall, state);
        List<QuestBallParticipantResDto> collect = byBallUuidAndCurrentState.stream().map(x -> QuestBallParticipantResDto.fromQuestBallParticipant(x)).collect(Collectors.toList());
        return collect;
    }


    public void participateAccept(QuestParticipateAcceptReqDto reqDto, UserAdapter userAdapter) throws AuthException {
        FBall fBall = checkAuth(userAdapter, reqDto.getBallUuid());
        FUserInfo user = fUserInfoDataRepository.findById(reqDto.getUid()).get();
        QuestBallParticipant questBallParticipant = questBallParticipantDataRepository.findByBallUuidAndUid(fBall, user).get();
        questBallParticipant.setCurrentState(QuestBallParticipateState.Accept);
        questBallParticipant.setAcceptTime(LocalDateTime.now());
    }

    public void participateDenied(QuestParticipateDeniedReqDto reqDto, UserAdapter userAdapter) throws AuthException {
        FBall fBall = checkAuth(userAdapter, reqDto.getBallUuid());
        FUserInfo user = fUserInfoDataRepository.findById(reqDto.getUid()).get();
        QuestBallParticipant questBallParticipant = questBallParticipantDataRepository.findByBallUuidAndUid(fBall, user).get();
        questBallParticipant.setCurrentState(QuestBallParticipateState.ForceOut);

    }

    public FBall checkAuth(UserAdapter userAdapter, String ballUuid) throws AuthException {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        FUserInfo maker = fUserInfoDataRepository.findById(userAdapter.getfUserInfo().getUid()).get();
        if (!fBall.getUid().getUid().equals(maker.getUid())) {
            throw new AuthException("don't auth");
        }
        return fBall;
    }

    public int getStateParticipatesCount(String ballUuid, QuestBallParticipateState state) {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        int count  =
                questBallParticipantDataRepository.countByBallUuidAndCurrentState(fBall, state);
        return count;
    }
}
