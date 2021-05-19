package com.wing.forutona.App.FBall.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wing.forutona.App.FBall.Dto.*;
import com.wing.forutona.App.FBall.Service.BallAction.QuestBall.QuestBallActionService;
import com.wing.forutona.App.FBall.Value.QuestBallParticipateState;
import com.wing.forutona.SpringSecurity.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.List;

@RestController
@RequestMapping("/QuestBall")
@RequiredArgsConstructor
public class QuestBallActionController {

    final QuestBallActionService questBallActionService;


    @PostMapping("/RecruitParticipant")
    FBallResDto recruitParticipants(@RequestBody RecruitParticipantReqDto reqDto) throws JsonProcessingException {
        return questBallActionService.recruitParticipantService(reqDto);
    }

    @PostMapping("/Participate")
    ParticipantResDto participate(@RequestBody ParticipantReqDto reqDto,@AuthenticationPrincipal UserAdapter userAdapter){
        return questBallActionService.participate(reqDto,userAdapter);
    }

    @GetMapping("/Participate")
    QuestBallParticipantResDto getParticipate(String ballUuid, @AuthenticationPrincipal UserAdapter userAdapter){
        return questBallActionService.getParticipate(ballUuid,userAdapter);
    }

    @GetMapping("/Participates")
    List<QuestBallParticipantResDto> getParticipates(String ballUuid, QuestBallParticipateState state){
        return questBallActionService.getParticipates(ballUuid,state);
    }

    @GetMapping("/StateParticipatesCount")
    int getStateParticipatesCount(String ballUuid, QuestBallParticipateState state){
        return questBallActionService.getStateParticipatesCount(ballUuid,state);
    }

    @PostMapping("/ParticipateAccept")
    void participateAccept(@RequestBody QuestParticipateAcceptReqDto reqDto,@AuthenticationPrincipal UserAdapter userAdapter) throws AuthException {
        questBallActionService.participateAccept(reqDto,userAdapter);
    }

    @PostMapping("/ParticipateDenied")
    void participateDenied(@RequestBody QuestParticipateDeniedReqDto reqDto,@AuthenticationPrincipal UserAdapter userAdapter) throws AuthException {
        questBallActionService.participateDenied(reqDto,userAdapter);
    }
}
