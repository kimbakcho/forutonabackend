package com.wing.forutona.Manager.TermsConditions.Domain;

import com.wing.forutona.Manager.MUserInfo.Domain.MUserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TermsConditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idx;
    String termsName;
    String termsContent;
    LocalDateTime modifyDate;

    @JoinColumn(name = "modifyUser")
    @ManyToOne()
    MUserInfo modifyUser;
}

