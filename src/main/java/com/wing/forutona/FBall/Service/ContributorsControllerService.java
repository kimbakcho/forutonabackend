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
        FBall fBall =  FBall.builder().ballUuid(reqDto.getBallUuid()).build();
        FUserInfo fUserInfo = FUserInfo.builder().uid(reqDto.getUid()).build();

        List<Contributors> contributorsByUidIsAndBallUuidIs = contributorsDataRepository.findContributorsByUidIsAndBallUuidIs(fUserInfo, fBall);
        if (contributorsByUidIsAndBallUuidIs.size() == 0) {
            Contributors contributors =  Contributors.builder().uid(fUserInfo).ballUuid(fBall).build();

            contributorsDataRepository.saveAndFlush(contributors);
            fBallService.increaseContributor(contributors.getBallUuid().getBallUuid(), 1L);
        }
    }

    @Transactional
    public void deleteContributorsByUidIsAndBallUuidIs(ContributorReqDto reqDto) {
        FBall fBall =  FBall.builder().ballUuid(reqDto.getBallUuid()).build();

        FUserInfo fUserInfo = FUserInfo.builder().uid(reqDto.getUid()).build();

        contributorsDataRepository.deleteContributorsByUidIsAndBallUuidIs(fUserInfo, fBall);
        fBallService.decreaseContributor(fBall.getBallUuid(), 1L);
    }


}
