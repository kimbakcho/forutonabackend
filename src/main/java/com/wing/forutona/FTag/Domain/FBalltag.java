package com.wing.forutona.FTag.Domain;


import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FTag.Dto.TagInsertReqDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Setter
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


  public FBalltag(FBall fball, TagInsertReqDto reqDto){
    this.ballUuid = fball;
    this.tagItem = reqDto.getTagItem();
  }
}
