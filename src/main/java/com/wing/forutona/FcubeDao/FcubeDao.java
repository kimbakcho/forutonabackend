package com.wing.forutona.FcubeDao;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.auth.FirebaseToken;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.FcubeDto.Fcube;
import com.wing.forutona.FcubeDto.FcubeContentSelector;
import com.wing.forutona.FcubeDto.FcubeExtender1;
import com.wing.forutona.FcubeDto.Fcubecontent;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class FcubeDao {

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    @Autowired
    FireBaseAdmin fireBaseAdmin;

    @Autowired
    GoogleStorgeAdmin googlesotrageAdmin;

    public List<Fcube> getFcubeMains(){
        FcubeMapper mapper =  sqlSession.getMapper(FcubeMapper.class);
        return mapper.selectAll();
    }

    public int MakeCube(Fcube fcube){
        FcubeMapper mapper =  sqlSession.getMapper(FcubeMapper.class);
        if(fcube.getInfluence() == null){
            fcube.setInfluence(0.0);
        }
        fcube.setMaketime(new Date());
        return mapper.insert(fcube);
    };

    public int MakeCubeContent(Fcubecontent fcubecontent){
        fcubecontent.setContentupdatetime(new Date());
        FcubecontentExtend1Mapper mapper =  sqlSession.getMapper(FcubecontentExtend1Mapper.class);
        return mapper.insert(fcubecontent);
    }

    public List<FcubeExtender1> GetUserCubes(String uid){
        Fcube cube = new Fcube();
        cube.setUid(uid);
        FcubeExtend1Mapper mapper =  sqlSession.getMapper(FcubeExtend1Mapper.class);
        return  mapper.selectUserBoxAll(cube);
    }

    public int deletecube(Fcube cube){
        FcubeMapper mapper =  sqlSession.getMapper(FcubeMapper.class);
        return mapper.deleteByPrimaryKey(cube.getCubeuuid());
    }
    public int CubeRelationImageDelete(String url){
        Storage storage =  googlesotrageAdmin.GetStorageInstance();
        String[] splititem = url.split("/");
        String buket = splititem[3];
        String pathname = splititem[4]+"/"+splititem[5];
        BlobId blobId = BlobId.of(buket, pathname);
        if(storage.delete(blobId)){
            return 1;
        }else {
            return 0;
        }
    }

    public String CubeRelationImageUpload(MultipartHttpServletRequest request) throws IOException {
        System.out.println(request.getFileMap());
        Storage storage =  googlesotrageAdmin.GetStorageInstance();
        List<MultipartFile> getfile = request.getMultiFileMap().get("CubeRelationImage");
        String OriginalFile = getfile.get(0).getOriginalFilename();
        int extentionindex = OriginalFile.lastIndexOf(".");
        UUID uuid = UUID.randomUUID();
        String savefilename = "";
        if(extentionindex > 0){
            String extention = OriginalFile.substring(extentionindex);
            savefilename = uuid.toString() + extention;
        }else {
            savefilename = uuid.toString();
        }
        BlobId blobId = BlobId.of("publicforutona", "cuberelationimage/"+savefilename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
        storage.create(blobInfo, getfile.get(0).getBytes());
        return "https://storage.googleapis.com/publicforutona/cuberelationimage/"+savefilename;
    }

    public List<Fcubecontent> selectwithFcubeContentSelector(FcubeContentSelector selectitem){
        FcubecontentExtend1Mapper mapper = sqlSession.getMapper(FcubecontentExtend1Mapper.class);
        return mapper.selectwithFcubeContentSelector(selectitem);
    }
}
