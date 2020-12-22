package com.wing.forutona.App.FBall.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.wing.forutona.App.FBall.Dto.FBallImageUploadResDto;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BallImageUploadService {
    FBallImageUploadResDto ballImageUpload(List<MultipartFile> files) throws IOException;
}

@Service
@RequiredArgsConstructor
@Transactional
class BallImageUploadServiceImpl implements BallImageUploadService{

    final private GoogleStorgeAdmin googleStorgeAdmin;

    @Override
    public FBallImageUploadResDto ballImageUpload(List<MultipartFile> files) throws IOException {
        Storage storage = googleStorgeAdmin.GetStorageInstance();
        FBallImageUploadResDto fBallImageUploadResDto = new FBallImageUploadResDto();
        fBallImageUploadResDto.setCount(files.size());
        List<String> urls = fBallImageUploadResDto.getUrls();
        for (MultipartFile file : files) {
            String OriginalFile = file.getOriginalFilename();
            int extentIndex = OriginalFile.lastIndexOf(".");
            UUID uuid = UUID.randomUUID();
            String saveFileName = "";
            if (extentIndex > 0) {
                String extent = OriginalFile.substring(extentIndex);
                saveFileName = uuid.toString() + extent;
            } else {
                saveFileName = uuid.toString();
            }
            BlobId blobId = BlobId.of("publicforutona", "profileimage/" + saveFileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, file.getBytes());
            String imageUrl = "https://storage.googleapis.com/publicforutona/profileimage/" + saveFileName;
            urls.add(imageUrl);
        }
        return fBallImageUploadResDto;
    }
}