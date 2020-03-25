package com.wing.forutona.ForutonaUser.Domain;



import com.wing.forutona.Fcube.Domain.JFcube;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "UserInfo")
public class JUserInfo {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idx;
  @Column(unique = true)
  private String uid;

  @OneToMany(mappedBy = "Fcubeuid")
  private  List<JFcube> userCubes = new ArrayList<>();

  private String nickName;
  private String profilePicktureUrl;
  private long sex;
  private LocalDateTime ageDate;
  private String email;
  private long forutonaAgree;
  private long privateAgree;
  private long positionAgree;
  private long martketingAgree;
  private long ageLimitAgree;
  private String snsService;
  private String phonenumber;
  private String isoCode;
  private double latitude;
  private double longitude;
  private LocalDateTime positionUpdateTime;
  private double userLevel;
  private double expPoint;
  private String fcMtoken;
  private LocalDateTime joinTime;
  private long followCount;
  private long backOut;
  private LocalDateTime lastBackOutTime;

}
