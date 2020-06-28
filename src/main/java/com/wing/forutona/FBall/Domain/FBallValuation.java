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
    Long upAndDown;

    @Builder
    public FBallValuation(String valueUuid,FBall ballUuid,FUserInfo uid){
        this.valueUuid = valueUuid;
        this.ballUuid = ballUuid;
        this.uid =uid;
    }

    public void setUpAndDown(Long upAndDown) {
        this.upAndDown = upAndDown;
    }
}
