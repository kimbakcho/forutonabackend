package com.wing.forutona.App.FBallValuation.Domain;

import com.wing.forutona.App.FBall.Domain.FBall;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class FBallValuation {
    @Id
    String valueUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballUuid")
    FBall ballUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    FUserInfo uid;

    Integer ballLike = 0;
    Integer ballDislike = 0;
    Integer point = 0;


    public void setBallLike(Integer ballLike) {
        this.ballLike = ballLike;
    }

    public void setBallDislike(Integer ballDislike) {
        this.ballDislike = ballDislike;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
