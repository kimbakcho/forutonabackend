package com.wing.forutona.FBall.Domain;

import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class FBallValuation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballUuid")
    FBall ballUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    FUserInfo uid;
    Long upAndDown;


}
