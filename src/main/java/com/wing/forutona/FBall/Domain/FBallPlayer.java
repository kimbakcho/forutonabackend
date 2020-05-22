package com.wing.forutona.FBall.Domain;

import com.wing.forutona.FBall.Dto.FBallPlayState;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"ballUuid", "playerUid"})
)
@NoArgsConstructor()
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
  private boolean hasLike;
  private boolean hasDisLike;
  private boolean hasGiveUp;
  private boolean hasExit;
  private LocalDateTime startTime;
  @Enumerated(EnumType.STRING)
  private FBallPlayState playState;

}
