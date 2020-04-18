package com.wing.forutona.ForutonaUser.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserPolicy {
    @Id
    String policyName;
    String policyContent;
    String lang;
    LocalDateTime writeDateTime;
}
