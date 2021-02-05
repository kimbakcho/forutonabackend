package com.wing.forutona.Manager.MaliciousReply.Domain;

import com.wing.forutona.Manager.MUserInfo.Domain.MUserInfo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class MaliciousReply {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idx;
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

    @JoinColumn(name = "judgmentUid")
    @ManyToOne
    private MUserInfo judgmentUid;

    public void setMaliciousContentFlag(Boolean maliciousContentFlag) {
        this.maliciousContentFlag = maliciousContentFlag;
    }

    public void setJudgmentTime(LocalDateTime judgmentTime) {
        this.judgmentTime = judgmentTime;
    }

    public void setJudgmentUid(MUserInfo judgmentUid) {
        this.judgmentUid = judgmentUid;
    }

    public void setTotalNumberReports(Integer totalNumberReports) {
        this.totalNumberReports = totalNumberReports;
    }


    public void reportPointCalcAndResetReportFlag(int point) {
        totalNumberReports += point;
        maliciousContentFlag = true;
        judgmentTime = null;
    }

    public void addCrime(int point){
        crime += point;
        reportPointCalcAndResetReportFlag(point);
    }

    public void addAbuse(int point){
        abuse += point;
        reportPointCalcAndResetReportFlag(point);
    }

    public void addPrivacy(int point){
        privacy += point;
        reportPointCalcAndResetReportFlag(point);
    }

    public void addSexual(int point){
        sexual += point;
        reportPointCalcAndResetReportFlag(point);
    }

    public void addAdvertising(int point){
        advertising += point;
        reportPointCalcAndResetReportFlag(point);
    }

    public void addEtc(int point){
        etc += point;
        reportPointCalcAndResetReportFlag(point);
    }

}
