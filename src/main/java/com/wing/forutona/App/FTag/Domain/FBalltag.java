package com.wing.forutona.App.FTag.Domain;


import com.wing.forutona.App.FBall.Domain.FBall;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FBalltag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long idx;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ballUuid")
  private FBall ballUuid;
  //ALTER TABLE `FBalltag` ADD FULLTEXT INDEX `tagindex` (`tagItem`) WITH PARSER ngram;  ngram index 사용
  private String tagItem;

  @Builder
  public FBalltag(FBall ballUuid, String tagItem){
    this.ballUuid = ballUuid;
    this.tagItem = tagItem;
  }
}
