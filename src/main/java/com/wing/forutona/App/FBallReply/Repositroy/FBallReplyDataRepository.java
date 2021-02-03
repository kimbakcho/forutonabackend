package com.wing.forutona.App.FBallReply.Repositroy;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.FBallReply.Domain.FBallReply;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FBallReplyDataRepository extends JpaRepository<FBallReply,String> {
    FBallReply findTop1ByReplyBallUuidIsOrderByReplyNumberDesc(FBall replyBallUuid);

    List<FBallReply> findByReplyBallUuidIsAndReplyNumberIsOrderByReplyUploadDateTimeDesc(FBall replyBallUuid,Long replyNumber);

    void deleteByReplyBallUuid(FBall fBall);

    FBallReply findByReplyBallUuidAndReplyNumberAndReplySort(FBall ballUuid,Long replyNumber,Long replySort);

    Long countByReplyBallUuid(FBall fball);

}
