package com.wing.forutona.FBall.Repository.FBallReply;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FBallReplyDataRepository extends JpaRepository<FBallReply,Long> {
    FBallReply findTop1ByReplyBallUuidIsOrderByReplyNumberDesc(FBall replyBallUuid);
}
