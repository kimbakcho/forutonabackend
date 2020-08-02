package com.wing.forutona.ForutonaUser.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "UserInfo")
public class FUserInfoSimple {

    @Id
    @Column(unique = true)
    private String uid;
    private String nickName;
    private String profilePictureUrl;
    private String isoCode;
    private Double userLevel;
    private String selfIntroduction;
    private Double cumulativeInfluence;

    @Builder
    FUserInfoSimple(String uid,String nickName){
        this.uid = uid;
        this.nickName = nickName;
    }

}
