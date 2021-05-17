package com.wing.forutona.App.FBall.Domain;

import com.google.firebase.auth.UserInfo;
import com.wing.forutona.App.FBall.Value.QuestBallParticipateState;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "QuestBallParticipant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class QuestBallParticipant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idx;

    @JoinColumn(name = "ballUuid")
    @ManyToOne()
    FBall ballUuid;

    @JoinColumn(name = "uid")
    @ManyToOne()
    FUserInfo uid;

    LocalDateTime participationTime;

    String photoShotForCertificationImage;

    Double checkInPositionLat;

    Double checkInPositionLng;

    Double startPositionLat;

    Double startPositionLng;

    Integer likePoint;

    Integer dislikePoint;

    @Enumerated(EnumType.STRING)
    QuestBallParticipateState currentState;

    LocalDateTime acceptTime;

}
