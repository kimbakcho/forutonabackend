package com.wing.forutona.App.FBall.Repository;


import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBall.Domain.QuestBallParticipant;
import com.wing.forutona.App.FBall.Value.QuestBallParticipateState;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestBallParticipantDataRepository extends JpaRepository<QuestBallParticipant, Integer> {
    Optional<QuestBallParticipant> findByBallUuidAndUid(FBall fBall, FUserInfo userInfo);

    int countByBallUuidAndCurrentState(FBall fBall,QuestBallParticipateState participateState);

    List<QuestBallParticipant> findByBallUuidAndCurrentState(FBall fBall, QuestBallParticipateState state);
}
