package com.wing.forutona.Manager.MaliciousBall.Domain;


import com.wing.forutona.Manager.MUserInfo.Domain.MUserInfo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class MaliciousBall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
