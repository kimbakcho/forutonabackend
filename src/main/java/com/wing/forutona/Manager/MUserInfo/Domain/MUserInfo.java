package com.wing.forutona.Manager.MUserInfo.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MUserInfo {
    @Id
    String uid;
    String userUuid;
    String userName;
    String groupName;
    String hasRole;
    String password;

    @Builder
    public MUserInfo(String uid,String userUuid, String userName, String groupName, String hasRole, String password) {
        this.uid = uid;
        this.userUuid  = userUuid;
        this.userName = userName;
        this.groupName = groupName;
        this.hasRole = hasRole;
        this.password = password;
    }
}
