package com.wing.forutona.FBall.Domain;

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

    Integer point;

    @Builder
    public FBallValuation(String valueUuid,FBall ballUuid,FUserInfo uid,Integer point){
        this.valueUuid = valueUuid;
        this.ballUuid = ballUuid;
        this.uid =uid;
        this.point = point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }


}
