package com.wing.forutona.Manager.MaliciousBall.Service;

import com.wing.forutona.Manager.MaliciousBall.Domain.MaliciousBall;
import com.wing.forutona.Manager.MaliciousBall.Dto.MaliciousBallReqDto;
import com.wing.forutona.Manager.MaliciousBall.Dto.MaliciousBallResDto;
import com.wing.forutona.Manager.MaliciousBall.Repository.MaliciousBallDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MaliciousBallService {

    final MaliciousBallDataRepository maliciousBallDataRepository;

    public MaliciousBallResDto reportMaliciousBall(MaliciousBallReqDto reqDto) {

        MaliciousBallResDto result;

        Optional<MaliciousBall> byBallUuid = maliciousBallDataRepository.findByBallUuid(reqDto.getBallUuid());

        if (byBallUuid.isPresent()) {
            MaliciousBall maliciousBall = byBallUuid.get();
            maliciousBall.addAbuse(reqDto.getAbuse());
            maliciousBall.addAdvertising(reqDto.getAdvertising());
            maliciousBall.addCrime(reqDto.getCrime());
            maliciousBall.addPrivacy(reqDto.getPrivacy());
            maliciousBall.addEtc(reqDto.getEtc());
            maliciousBall.addSexual(reqDto.getSexual());
            result = MaliciousBallResDto.fromMaliciousBall(maliciousBall);
        } else {
            MaliciousBall saveItem = MaliciousBall.builder()
                    .abuse(reqDto.getAbuse())
                    .advertising(reqDto.getAdvertising())
                    .crime(reqDto.getCrime())
                    .etc(reqDto.getEtc())
                    .falseReportFlag(false)
                    .privacy(reqDto.getPrivacy())
                    .maliciousContentFlag(true)
                    .ballUuid(reqDto.getBallUuid())
                    .sexual(reqDto.getSexual())
                    .totalNumberReports(1)
                    .build();
            MaliciousBall save = maliciousBallDataRepository.save(saveItem);
            result = MaliciousBallResDto.fromMaliciousBall(save);
        }
        return result;
    }
}
