package com.wing.forutona.Manager.MaliciousBall.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaliciousBallReqDto {
    private String ballUuid;
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
}
