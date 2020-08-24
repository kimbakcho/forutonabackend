package com.wing.forutona.Contributors.Domain;


import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"uid", "ballUuid"})
)
public class Contributors {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    FUserInfo uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ballUuid")
    FBall ballUuid;

    @Builder
    public Contributors(FUserInfo uid,FBall ballUuid){
        this.uid = uid;
        this.ballUuid = ballUuid;

    }


}
