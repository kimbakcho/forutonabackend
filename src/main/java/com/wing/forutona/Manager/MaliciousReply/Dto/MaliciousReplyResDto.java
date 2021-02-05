package com.wing.forutona.Manager.MaliciousReply.Dto;

import com.wing.forutona.Manager.MUserInfo.Dto.MUserInfoResDto;
import com.wing.forutona.Manager.MaliciousReply.Domain.MaliciousReply;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaliciousReplyResDto {

    private Integer idx;
    private String replyUuid;
    private Integer crime;
    private Integer abuse;
    private Integer privacy;
    private Integer sexual;
    private Integer advertising;
    private Integer etc;
    private Integer totalNumberReports;
    private Boolean maliciousContentFlag;
    private Boolean falseReportFlag;
    private LocalDateTime judgmentTime;
    private MUserInfoResDto judgmentUid;

    public static MaliciousReplyResDto fromMaliciousReply(MaliciousReply maliciousReply){
        MaliciousReplyResDto maliciousReplyResDto = new MaliciousReplyResDto();
        maliciousReplyResDto.idx = maliciousReply.getIdx();
        maliciousReplyResDto.replyUuid = maliciousReply.getReplyUuid();
        maliciousReplyResDto.crime = maliciousReply.getCrime();
        maliciousReplyResDto.abuse = maliciousReply.getAbuse();
        maliciousReplyResDto.privacy = maliciousReply.getPrivacy();
        maliciousReplyResDto.sexual = maliciousReply.getSexual();
        maliciousReplyResDto.advertising = maliciousReply.getAdvertising();
        maliciousReplyResDto.etc = maliciousReply.getEtc();
        maliciousReplyResDto.totalNumberReports = maliciousReply.getTotalNumberReports();
        maliciousReplyResDto.maliciousContentFlag = maliciousReply.getMaliciousContentFlag();
        maliciousReplyResDto.falseReportFlag = maliciousReply.getFalseReportFlag();
        if(maliciousReply.getJudgmentTime() != null){
            maliciousReplyResDto.judgmentTime = maliciousReply.getJudgmentTime();
        }
        if(maliciousReply.getJudgmentUid() != null){
            maliciousReplyResDto.judgmentUid = MUserInfoResDto.fromUserInfoResDto(maliciousReply.getJudgmentUid());
        }

        return maliciousReplyResDto;
    }
}
