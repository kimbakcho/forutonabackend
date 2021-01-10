package com.wing.forutona.Manager.Notice.Domain;

import com.wing.forutona.Manager.MUserInfo.Domain.MUserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idx;
    String title;
    String content;
    String openFlag;

    @JoinColumn(name = "writerUid")
    @ManyToOne
    MUserInfo writerUid;

    LocalDateTime modifyDate;
}
