package com.wing.forutona.App.ForutonaUser.Repository;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.App.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoResDto;
import com.wing.forutona.App.ForutonaUser.Dto.FUserInfoSimpleResDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class FUserInfoQueryRepositoryTest extends BaseTest {

    @Autowired
    FUserInfoDataRepository fUserInfoDataRepository;

    @Autowired
    FUserInfoQueryRepository fUserInfoQueryRepository;

    @Test
    void getFindNearUserFromGeoLocation() throws ParseException {
        double geoTestLat = 36.767149;
        double geoTestLong = 127.371234;
        List<FUserInfo> tempContent = fUserInfoDataRepository.findAll();
        tempContent.forEach(x -> {
            x.updatePlacePoint(geoTestLat + 0.3, geoTestLong + 0.3);
        });
        List<FUserInfo> content = fUserInfoDataRepository.findAll(PageRequest.of(0, 10)).getContent();
        for (int i = 0; i < 10; i++) {
            FUserInfo fUserInfo = tempContent.get(i);
            fUserInfo.updatePlacePoint(geoTestLat, geoTestLong);
        }
        LatLng latLng = LatLng.newBuilder().setLatitude(geoTestLat).setLongitude(geoTestLong).build();
        List<FUserInfoResDto> findNearUserFromGeoLocation = fUserInfoQueryRepository.getFindNearUserFromGeoLocationWithOutUser(latLng, 1000,testUser);
        System.out.println(findNearUserFromGeoLocation.size());
        assertTrue(findNearUserFromGeoLocation.size() >= 10);
    }

    @Test
    void findByUserNickNameWithFullTextMatchIndex() {
        //given

        //when
        Page<FUserInfoSimpleResDto> playerPowers = fUserInfoQueryRepository.findByUserNickNameWithFullTextMatchIndex("te",
                PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, "playerPower")));

        Page<FUserInfoSimpleResDto> nickNames = fUserInfoQueryRepository.findByUserNickNameWithFullTextMatchIndex("te",
                PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, "nickName")));

        Page<FUserInfoSimpleResDto> followerCount = fUserInfoQueryRepository.findByUserNickNameWithFullTextMatchIndex("te",
                PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, "followerCount")));

        //then

    }
}