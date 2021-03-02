package com.wing.forutona.Manager.MaliciousReply.Service;

import com.wing.forutona.Manager.MaliciousReply.Domain.MaliciousReply;
import com.wing.forutona.Manager.MaliciousReply.Dto.MaliciousReplyReqDto;
import com.wing.forutona.Manager.MaliciousReply.Dto.MaliciousReplyResDto;
import com.wing.forutona.Manager.MaliciousReply.Repository.MaliciousReplyDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(transactionManager = "managerTransactionManager")
@RequiredArgsConstructor
public class MaliciousReplyService {

    final MaliciousReplyDataRepository maliciousReplyDataRepository;

    public MaliciousReplyResDto reportMaliciousReply(MaliciousReplyReqDto reqDto) {

        Optional<MaliciousReply> byReplyUuid = maliciousReplyDataRepository.findByReplyUuid(reqDto.getReplyUuid());

        MaliciousReplyResDto result;

        if(byReplyUuid.isPresent()){
            MaliciousReply maliciousReply = byReplyUuid.get();
            maliciousReply.addAbuse(reqDto.getAbuse());
            maliciousReply.addAdvertising(reqDto.getAdvertising());
            maliciousReply.addCrime(reqDto.getCrime());
            maliciousReply.addPrivacy(reqDto.getPrivacy());
            maliciousReply.addEtc(reqDto.getEtc());
            maliciousReply.addSexual(reqDto.getSexual());
            result = MaliciousReplyResDto.fromMaliciousReply(maliciousReply);
        } else {
            MaliciousReply saveItem = MaliciousReply.builder()
                    .abuse(reqDto.getAbuse())
                    .advertising(reqDto.getAdvertising())
                    .crime(reqDto.getCrime())
                    .etc(reqDto.getEtc())
                    .falseReportFlag(false)
                    .privacy(reqDto.getPrivacy())
                    .maliciousContentFlag(true)
                    .replyUuid(reqDto.getReplyUuid())
                    .sexual(reqDto.getSexual())
                    .totalNumberReports(1)
                    .build();
            MaliciousReply save = maliciousReplyDataRepository.save(saveItem);
            result = MaliciousReplyResDto.fromMaliciousReply(save);
        }
        return result;
    }

}
