package com.wing.forutona.FBall.Domain;


import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
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

}
