package com.wing.forutona.FBall.Service;

import com.wing.forutona.FBall.Domain.Contributors;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.ContributorReqDto;
import com.wing.forutona.FBall.Repository.Contributors.ContributorsDataRepository;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributorsControllerService {

    final private ContributorsDataRepository contributorsDataRepository;
    final private FBallService fBallService;

    @Transactional
    public void ifNotExistsInsert(ContributorReqDto reqDto) {
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        FUserInfo fUserInfo = new FUserInfo();
        fUserInfo.setUid(reqDto.getUid());
        List<Contributors> contributorsByUidIsAndBallUuidIs = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo, fBall);
        if(contributorsByUidIsAndBallUuidIs.size()  == 0){
            Contributors contributors = new Contributors();
            contributors.setUid(fUserInfo);
            contributors.setBallUuid(fBall);
            contributorsDataRepository.saveAndFlush(contributors);
            fBallService.increaseContributor(contributors.getBallUuid().getBallUuid(),1L);
        }
    }

    @Transactional
    public void deleteContributorsByUidIsAndBallUuidIs(ContributorReqDto reqDto) {
        FBall fBall = new FBall();
        fBall.setBallUuid(reqDto.getBallUuid());
        FUserInfo fUserInfo = new FUserInfo();
        fUserInfo.setUid(reqDto.getUid());
        contributorsDataRepository.deleteContributorsByUidIsAndBallUuidIs(fUserInfo,fBall);
        fBallService.decreaseContributor(fBall.getBallUuid(),1L);
    }


}
