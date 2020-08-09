package com.wing.forutona.FBall.Domain;

import com.wing.forutona.FBall.Dto.FBallPlayState;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"ballUuid", "playerUid"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FBallPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballUuid")
    private FBall ballUuid;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playerUid")
    private FUserInfo playerUid;
    private Boolean hasLike;
    private Boolean hasDisLike;
    private Boolean hasGiveUp;
    private Boolean hasExit;
    private LocalDateTime startTime;
    @Enumerated(EnumType.STRING)
    private FBallPlayState playState;


}
