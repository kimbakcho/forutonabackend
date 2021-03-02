package com.wing.forutona.Manager.MaliciousBall.Dto;

import com.wing.forutona.Manager.MUserInfo.Dto.MUserInfoResDto;
import com.wing.forutona.Manager.MaliciousBall.Domain.MaliciousBall;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaliciousBallResDto {
    Integer idx;
    String ballUuid;
    Integer totalNumberReports;
    Integer crime;
    Integer abuse;
    Integer privacy;
    Integer sexual;
    Integer advertising;
    Integer etc;
    Boolean maliciousContentFlag;
    Boolean falseReportFlag;
    LocalDateTime judgmentTime;
    MUserInfoResDto judgmentUid;

    public static MaliciousBallResDto fromMaliciousBall(MaliciousBall maliciousBall) {
        MaliciousBallResDto maliciousBallResDto = new MaliciousBallResDto();
        maliciousBallResDto.idx = maliciousBall.getIdx();
        maliciousBallResDto.ballUuid = maliciousBall.getBallUuid();
        maliciousBallResDto.totalNumberReports = maliciousBall.getTotalNumberReports();
        maliciousBallResDto.crime = maliciousBall.getCrime();
        maliciousBallResDto.abuse = maliciousBall.getAbuse();
        maliciousBallResDto.privacy = maliciousBall.getPrivacy();
        maliciousBallResDto.sexual = maliciousBall.getSexual();
        maliciousBallResDto.advertising = maliciousBall.getAdvertising();
        maliciousBallResDto.etc = maliciousBall.getEtc();
        maliciousBallResDto.maliciousContentFlag = maliciousBall.getMaliciousContentFlag();
        maliciousBallResDto.falseReportFlag = maliciousBall.getFalseReportFlag();
        maliciousBallResDto.judgmentTime = maliciousBall.getJudgmentTime();
        if(maliciousBall.getJudgmentUid() != null){
            maliciousBallResDto.judgmentUid = MUserInfoResDto.fromUserInfoResDto(maliciousBall.getJudgmentUid());
        }
        return maliciousBallResDto;
    }
}
