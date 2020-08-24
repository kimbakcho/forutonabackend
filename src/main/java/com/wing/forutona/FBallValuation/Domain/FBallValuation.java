package com.wing.forutona.FBallValuation.Domain;

import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FBallValuation {
    @Id
    String valueUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballUuid")
    FBall ballUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    FUserInfo uid;

    Long ballLike = 0L;
    Long ballDislike = 0L;
    Long point = 0L;

    @Builder
    public FBallValuation(String valueUuid,FBall ballUuid,FUserInfo uid,Long ballLike,Long ballDislike,Long point){
        this.valueUuid = valueUuid;
        this.ballUuid = ballUuid;
        this.uid =uid;
        this.point = point;
        this.ballLike = ballLike;
        this.ballDislike = ballDislike;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public void setBallLike(Long like) {
        this.ballLike = like;
    }

    public void setBallDislike(Long dislike) {
        this.ballDislike = dislike;
    }


}
