package com.wing.forutona.FBallReply.Repositroy;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBallReply.Domain.FBallReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FBallReplyDataRepository extends JpaRepository<FBallReply,String> {
    FBallReply findTop1ByReplyBallUuidIsOrderByReplyNumberDesc(FBall replyBallUuid);

    List<FBallReply> findByReplyBallUuidIsAndReplyNumberIsOrderByReplyUploadDateTimeDesc(FBall replyBallUuid,Long replyNumber);

    void deleteByReplyBallUuid(FBall fBall);

    FBallReply findByReplyBallUuidAndReplyNumberAndReplySort(FBall ballUuid,Long replyNumber,Long replySort);

    Long countByReplyBallUuid(FBall fball);
}
