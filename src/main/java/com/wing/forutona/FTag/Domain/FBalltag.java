package com.wing.forutona.FTag.Domain;


import com.wing.forutona.FBall.Domain.FBall;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

  public FBall getBallUuid() {
    return ballUuid;
  }

  public void setBallUuid(FBall ballUuid) {
    this.ballUuid = ballUuid;
  }

  public Long getIdx() {
    return idx;
  }

  public void setIdx(Long idx) {
    this.idx = idx;
  }

  public String getTagItem() {
    return tagItem;
  }

  public void setTagItem(String tagItem) {
    this.tagItem = tagItem;
  }
}
