package com.wing.forutona.FBall.Domain;

import com.google.firebase.auth.UserInfo;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class FBallReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;
    @ManyToOne
    @JoinColumn(name = "ballUuid")
    FBall replyBallUuid;
    @ManyToOne
    @JoinColumn(name = "uid")
    FUserInfo replyUid;
    Long replyNumber;
    Long replySort;
    Long replyDepth;
    String replyText;
    LocalDateTime replyUploadDateTime;
    LocalDateTime replyUpdateDateTime;
    Boolean deleteFlag;

}
