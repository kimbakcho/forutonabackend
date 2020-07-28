package com.wing.forutona.FBall.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.wing.forutona.FBall.Domain.FBall;
import com.wing.forutona.FBall.Dto.FBallDesImagesDto;
import com.wing.forutona.FBall.Dto.IssueBallDescriptionDto;
import com.wing.forutona.FBall.Repository.FBall.FBallDataRepository;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface BallDeleteService {
    String deleteBall(String ballUuid,String userUid) throws Exception;
}
@Service
@Transactional
@RequiredArgsConstructor
class BallDeleteServiceImpl implements BallDeleteService {

    final FBallDataRepository fBallDataRepository;

    final GoogleStorgeAdmin googleStorgeAdmin;

    @Override
    public String deleteBall(String ballUuid, String userUid) throws Exception {
        FBall fBall = fBallDataRepository.findById(ballUuid).get();
        if (!fBall.getUid().getUid().equals(userUid)) {
            throw new Exception("don't Have deleteBall Permisstion");
        }
        fBall.delete();

        ObjectMapper objectMapper = new ObjectMapper();
        IssueBallDescriptionDto issueBallDescriptionDto = objectMapper.readValue(fBall.getDescription(), IssueBallDescriptionDto.class);
        if (issueBallDescriptionDto.getDesimages() != null) {
            deleteImageFile(issueBallDescriptionDto);
        }

        fBall.setActivationTime(LocalDateTime.now());
        fBall.getTags().clear();
        return ballUuid;
    }

    void deleteImageFile(IssueBallDescriptionDto issueBallDescriptionDto) {
        for (FBallDesImagesDto item : issueBallDescriptionDto.getDesimages()
        ) {
            int index = item.getSrc().lastIndexOf("/");
            String saveFileName = item.getSrc().substring(index);
            if (saveFileName.length() > 1) {
                BlobId blobId = BlobId.of("publicforutona", "profileimage/" + saveFileName);
                googleStorgeAdmin.GetStorageInstance().delete(blobId);
            }
        }
    }
}
