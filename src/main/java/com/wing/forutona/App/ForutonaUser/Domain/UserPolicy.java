package com.wing.forutona.App.ForutonaUser.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserPolicy {
    @Id
    String policyName;
    String policyContent;
    String lang;
    LocalDateTime writeDateTime;
}
