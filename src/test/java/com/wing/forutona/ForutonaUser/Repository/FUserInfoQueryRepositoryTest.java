package com.wing.forutona.ForutonaUser.Repository;

import com.google.type.LatLng;
import com.vividsolutions.jts.io.ParseException;
import com.wing.forutona.BaseTest;
import com.wing.forutona.ForutonaUser.Domain.FUserInfo;
import com.wing.forutona.ForutonaUser.Dto.FUserInfoResDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
        tempContent.forEach(x->{
            x.updatePlacePoint(geoTestLat+0.3,geoTestLong+0.3);
        });
        List<FUserInfo> content = fUserInfoDataRepository.findAll(PageRequest.of(0, 10)).getContent();
        for(int i=0;i<10;i++){
            FUserInfo fUserInfo = tempContent.get(i);
            fUserInfo.updatePlacePoint( geoTestLat,geoTestLong);
        }
        LatLng latLng = LatLng.newBuilder().setLatitude(geoTestLat).setLongitude(geoTestLong).build();
        List<FUserInfoResDto> findNearUserFromGeoLocation = fUserInfoQueryRepository.getFindNearUserFromGeoLocation(latLng, 100);
        System.out.println(findNearUserFromGeoLocation.size());
        assertTrue(findNearUserFromGeoLocation.size()>=10);
    }
}