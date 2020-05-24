package com.wing.forutona.FBall.Repository.FBallReply;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Domain.FBallReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FBallReplyDataRepository extends JpaRepository<FBallReply,String> {
    FBallReply findTop1ByReplyBallUuidIsOrderByReplyNumberDesc(FBall replyBallUuid);

    List<FBallReply> findByReplyBallUuidIsAndReplyNumberIsOrderByReplyUploadDateTimeDesc(FBall replyBallUuid,Long replyNumber);
}
