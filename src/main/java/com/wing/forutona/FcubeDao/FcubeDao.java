package com.wing.forutona.FcubeDao;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.auth.FirebaseToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.AuthDao.UserexppointhistroyMapper;
import com.wing.forutona.AuthDao.UserinfoMapper;
import com.wing.forutona.AuthDto.Userexppointhistroy;
import com.wing.forutona.AuthDto.Userinfo;
import com.wing.forutona.FcubeDto.*;
import com.wing.forutona.FcubeScheduled;
import com.wing.forutona.GoogleStorageDao.GoogleStorgeAdmin;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Future;

@Component
public class FcubeDao {

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    @Autowired
    FireBaseAdmin fireBaseAdmin;

    @Autowired
    GoogleStorgeAdmin googlesotrageAdmin;

    @Autowired
    FcubeScheduled scheduled1;

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
        Date dt = new Date();
        //임시로 ACtivation 타임 생성일 기준 +5일로 해줌
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(5);

        Date date = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());

        fcube.setActivationtime(date);
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

    @Transactional
    public Fcubereply InsertCubeReply(Fcubereply reply) throws  Exception{
        FcubereplyExtender1Mapper mapper = sqlSession.getMapper(FcubereplyExtender1Mapper.class);
        reply.setSorts(mapper.SelectStep1ForReply(reply));
        reply.setCommnttime(new Date());
        if(reply.getBgroup() == 0){
            reply.setBgroup(mapper.SelectBgroubReplyMax(reply));
            if(mapper.insert(reply)==1){
                return reply;
            }else {
                return null;
            }
        }
        if(reply.getSorts() == 0){
            reply.setSorts(mapper.SelectStep2ForReply(reply));
            reply.setDepth(reply.getDepth()+1);
            if(mapper.insert(reply)==1){
                return reply;
            }else {
                return null;
            }
        }else {
            mapper.UpdateStep2ForReply(reply);
            if(mapper.insert(reply)==1){
                return reply;
            }else {
                return null;
            }
        }
    }

    public List<FcubereplyExtender1> SelectReplyForCube(String cubeuuid,int offset,int limit){
        FcubereplyExtender1Mapper mapper = sqlSession.getMapper(FcubereplyExtender1Mapper.class);
        return mapper.SelectReplyForCube(new FcubereplySearch(cubeuuid,offset,limit));
    }

    public int updateCubeState(Fcube cube){
        FcubeMapper mapper = sqlSession.getMapper(FcubeMapper.class);
        return mapper.updateCubeState(cube);
    };

    public List<FcubeplayerExtender1> selectPlayers(String cubeuuid,String uid){
        FcubeplayerExtender1Mapper mapper = sqlSession.getMapper(FcubeplayerExtender1Mapper.class);
        Fcubeplayer cube = new Fcubeplayer();
        cube.setCubeuuid(cubeuuid);
        cube.setUid(uid);
        return mapper.selectPlayers(cube);
    }

    public List<FcubeExtender1> findNearDistanceCube(FCubeGeoSearchUtil searchItem){
        FcubeExtend1Mapper mapper = sqlSession.getMapper(FcubeExtend1Mapper.class);
        return mapper.findNearDistanceCube(searchItem);
    }

    @Transactional
    public int insertFcubePlayer(Fcubeplayer fcubeplayer){
        FcubeMapper fcubemapper = sqlSession.getMapper(FcubeMapper.class);
        Fcube cube = fcubemapper.selectforupdate(fcubeplayer.getCubeuuid());
        FcubeplayerMapper playerMapper = sqlSession.getMapper(FcubeplayerMapper.class);
        playerMapper.insert(fcubeplayer);
        int playerCount = playerMapper.selectPlayercount(fcubeplayer);
        cube.setJoinplayer(playerCount);
        return fcubemapper.updatejoinplayerscount(cube);
    }

    public List<FcubeplayercontentExtender1> selectwithFcubeplayercontentSelector(FcubeplayercontentSelector selectitem){
        FcubeplayercontentExtender1Mapper mapper = sqlSession.getMapper(FcubeplayercontentExtender1Mapper.class);
        return mapper.selectwithFcubeplayercontentSelector(selectitem);
    }
    public int makeFcubeplayercontent(Fcubeplayercontent makeitem){
        FcubeplayercontentMapper mapper = sqlSession.getMapper(FcubeplayercontentMapper.class);
        return mapper.insert(makeitem);
    }
    public int updateFcubeplayercontent(FcubeplayercontentExtender1 makeitem){
        FcubeplayercontentExtender1Mapper mapper = sqlSession.getMapper(FcubeplayercontentExtender1Mapper.class);
        return mapper.updateFcubeplayercontent(makeitem);
    }
    public int deleteFcubeplayercontent(FcubeplayercontentExtender1 makeitem){
        FcubeplayercontentExtender1Mapper mapper = sqlSession.getMapper(FcubeplayercontentExtender1Mapper.class);
        return mapper.deleteFcubeplayercontent(makeitem);
    }

    @Async
    public void UploadAuthForImage(ResponseBodyEmitter emitter,MultipartHttpServletRequest request){
        try {
            Storage storage =  googlesotrageAdmin.GetStorageInstance();
            List<MultipartFile> getfile = request.getMultiFileMap().get("CubeAuthRelationImage");
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
            emitter.send("https://storage.googleapis.com/publicforutona/cuberelationimage/"+savefilename);
            emitter.complete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Async
    public void deleteAuthForImage (ResponseBodyEmitter emitter,String url){
        try{
            Storage storage =  googlesotrageAdmin.GetStorageInstance();
            String[] splititem = url.split("/");
            String buket = splititem[3];
            String pathname = splititem[4]+"/"+splititem[5];
            BlobId blobId = BlobId.of(buket, pathname);
            if(storage.delete(blobId)){
                emitter.send(1);
            }else {
                emitter.send(0);
            }
            emitter.complete();
        }catch (Exception ex){

        }
    }


    public int requestFcubeQuestSuccess(Fcubequestsuccess item){
        FcubequestsuccessMapper mapper = sqlSession.getMapper(FcubequestsuccessMapper.class);
        item.setJudgmenttime(new Date());
        return mapper.insert(item);
    }

    public List<FcubequestsuccessExtender1> getQuestReqList(FcubequestsuccessExtender1 item){
        FcubequestsuccessExtender1Mapper mapper = sqlSession.getMapper(FcubequestsuccessExtender1Mapper.class);
        return mapper.getQuestReqList(item);
    }

    @Async
    @Transactional
    public void updateQuestReq(ResponseBodyEmitter emitter,FcubequestsuccessExtender1 item){
        FcubecontentExtend1Mapper contentmapper = sqlSession.getMapper(FcubecontentExtend1Mapper.class);
        FcubequestsuccessExtender1Mapper cubequestsuccessExtender = sqlSession.getMapper(FcubequestsuccessExtender1Mapper.class);
        FcubeMapper fcubeMapper = sqlSession.getMapper(FcubeMapper.class);
        FcubeContentSelector contentselector = new FcubeContentSelector();
        contentselector.setCubeuuid(item.getCubeuuid());
        ArrayList<String> cubetype = new ArrayList<String>();
        cubetype.add("etcCubemode");
        contentselector.setContenttypes(cubetype);
        List<Fcubecontent> contentitems = contentmapper.selectwithFcubeContentSelector(contentselector);
        try{
            if(contentitems.size() > 0){
                var etcCubemode = contentitems.get(0);
                JsonElement etcCubemoderoot = JsonParser.parseString(etcCubemode.getContentvalue());
                var etcCubemoderootobj= etcCubemoderoot.getAsJsonObject();
                int maxScuess = etcCubemoderootobj.get("maxSuccess").getAsInt();
                List<FcubequestsuccessExtender1> successList=  cubequestsuccessExtender.getQuestSucessList(item);
                if(successList.size() >= maxScuess){
                    //실패 처리
                    item.setReadingcheck(1);
                    item.setScuesscheck(0);
                    item.setJudgmenttime(new Date());
                    int updateresult1 = cubequestsuccessExtender.updateQuestReq(item);
                    FcubeplayerExtender1Mapper fcubeplayerExtender1Mapper = sqlSession.getMapper(FcubeplayerExtender1Mapper.class);
                    Fcubeplayer player = new Fcubeplayer();
                    player.setUid(item.getFromuid());
                    player.setCubeuuid(item.getCubeuuid());
                    player.setPlaystate(2);
                    int updateresult2 = fcubeplayerExtender1Mapper.updatePlayerplaystate(player);
                    if((updateresult1>0) && (updateresult2>0)){
                        emitter.send(2);
                    }else {
                        emitter.send(0);
                    }
                }else if((successList.size()+1) == maxScuess){
                    item.setJudgmenttime(new Date());
                    item.setReadingcheck(1);
                    //큐브 완전 완료 처리 해줌.
                    var findfcube = fcubeMapper.selectforupdate(item.getCubeuuid());
                    findfcube.setCubestate(FcubeState.finish);
                    findfcube.setActivationtime(new Date());
                    //Maker 제작 경험치 줬는지 안줬는지에 따라 분기 처리
                    if(findfcube.getExpgiveflag() == 0){
                        findfcube.setExpgiveflag(1);
                        fcubeMapper.updateCubeState(findfcube);
                    }else {
                        fcubeMapper.updateCubeState(findfcube);
                    }
                    if(findfcube.getExpgiveflag() == 0){
                        Userexppointhistroy pointhistory = new Userexppointhistroy();
                        pointhistory.setCubeuuid(findfcube.getCubeuuid());
                        pointhistory.setUid(findfcube.getUid());
                        pointhistory.setGettime(new Date());
                        pointhistory.setPoints(findfcube.getMakeexp());
                        pointhistory.setExplains("MakeExp");
                        pointhistory.setFromuid("Forutona");
                        UserexppointhistroyMapper userexpmapper = sqlSession.getMapper(UserexppointhistroyMapper.class);
                        userexpmapper.insert(pointhistory);
                        UserinfoMapper userinfoMapper = sqlSession.getMapper(UserinfoMapper.class);
                        Userinfo makeruserinfo = userinfoMapper.selectforupdate(findfcube.getUid());
                        makeruserinfo.setExppoint(makeruserinfo.getExppoint() + findfcube.getMakeexp());
                        userinfoMapper.updateUserExpPoint(makeruserinfo);
                    }
                    int updateresult1 = cubequestsuccessExtender.updateQuestReq(item);
                    FcubeplayerExtender1Mapper fcubeplayerExtender1Mapper = sqlSession.getMapper(FcubeplayerExtender1Mapper.class);
                    Fcubeplayer player = new Fcubeplayer();
                    player.setUid(item.getFromuid());
                    player.setCubeuuid(item.getCubeuuid());
                    player.setPlaystate(2);
                    int updateresult2 = fcubeplayerExtender1Mapper.updatePlayerplaystate(player);
                    if((updateresult1>0) && (updateresult2>0)){
                        emitter.send(1);
                    }else {
                        emitter.send(0);
                    }

                }else {
                    //완료 처리 해줌
                    item.setJudgmenttime(new Date());
                    item.setReadingcheck(1);
                    int updateresult1 = cubequestsuccessExtender.updateQuestReq(item);
                    FcubeplayerExtender1Mapper fcubeplayerExtender1Mapper = sqlSession.getMapper(FcubeplayerExtender1Mapper.class);
                    Fcubeplayer player = new Fcubeplayer();
                    player.setUid(item.getFromuid());
                    player.setCubeuuid(item.getCubeuuid());
                    player.setPlaystate(2);
                    int updateresult2 = fcubeplayerExtender1Mapper.updatePlayerplaystate(player);
                    if((updateresult1>0) && (updateresult2>0)){
                        emitter.send(1);
                    }else {
                        emitter.send(0);
                    }
                }
                emitter.complete();
         }else {
             emitter.complete();
         }

        }catch (Exception ex){
            ex.printStackTrace();
            emitter.complete();
        }

    }

    public int updateQuesttoplayercomment(FcubequestsuccessExtender1 item) {
        FcubequestsuccessExtender1Mapper mapper = sqlSession.getMapper(FcubequestsuccessExtender1Mapper.class);
        return mapper.updateQuesttoplayercomment(item);
    }

    public int insertFcubeReview(Fcubereview item){
        //별점 포인트 해킹 방지
        if(item.getStarpoint() > 5.0) {
            item.setStarpoint(5.0);
        }
        FcubereviewMapper mapper = sqlSession.getMapper(FcubereviewMapper.class);
        return mapper.insert(item);
    }

    @Async
    @Transactional
    public void insertFcubeReviewExpPoint(ResponseBodyEmitter emitter,Fcubereview item) throws IOException {
        //별점 포인트 해킹 방지
        if(item.getStarpoint() > 5.0) {
            item.setStarpoint(5.0);
        }
        FcubereviewMapper reviewmapper = sqlSession.getMapper(FcubereviewMapper.class);
        UserexppointhistroyMapper exphistorymapper = sqlSession.getMapper(UserexppointhistroyMapper.class);
        FcubeMapper cubeMapper =  sqlSession.getMapper(FcubeMapper.class);
        UserinfoMapper userinfoMapper =  sqlSession.getMapper(UserinfoMapper.class);
        List<Fcubereview> finditem = reviewmapper.selectFcubeReview(item);
        if(finditem.size()==1){
            Fcube fcube = cubeMapper.selectByPrimaryKey(item.getCubeuuid());
            Userexppointhistroy userexppointhistroy = new Userexppointhistroy();
            userexppointhistroy.setCubeuuid(item.getCubeuuid());
            userexppointhistroy.setUid(fcube.getUid());
            userexppointhistroy.setFromuid(item.getUid());
            userexppointhistroy.setExplains("ReviewPoint");
            userexppointhistroy.setPoints(10+item.getStarpoint());
            userexppointhistroy.setGettime(new Date());
            exphistorymapper.insert(userexppointhistroy);
            Userinfo userinfo = userinfoMapper.selectforupdate(fcube.getUid());
            userinfo.setExppoint(userinfo.getExppoint() + userexppointhistroy.getPoints());
            userinfoMapper.updateUserExpPoint(userinfo);
            emitter.send(1);
            emitter.complete();
            return ;
        }
        emitter.send(1);
        emitter.complete();
    }

    public List<FcubequestsuccessExtender1> getPlayerQuestSuccessList(FcubequestsuccessExtender1 item){
        FcubequestsuccessExtender1Mapper mapper = sqlSession.getMapper(FcubequestsuccessExtender1Mapper.class);
        return mapper.getPlayerQuestSuccessList(item);
    }

    public int insertFcubeQuestSuccessCheck(Fcubequestsuccesscheck item){
        FcubequestsuccesscheckMapper mapper = sqlSession.getMapper(FcubequestsuccesscheckMapper.class);
        return mapper.insert(item);
    }

    public Fcube getFcubestate(String cubeuuid){
      FcubeMapper mapper = sqlSession.getMapper(FcubeMapper.class);
      return mapper.selectByPrimaryKey(cubeuuid);
    }

    public int updatePlayerplaystate(Fcubeplayer item){
        FcubeplayerExtender1Mapper mapper = sqlSession.getMapper(FcubeplayerExtender1Mapper.class);
        return mapper.insert(item);
    }

    @Async
    public void getPlayerJoinList(ResponseBodyEmitter emitter,FcubeplayerSearch item)  {

        FcubeplayerExtender2Mapper mapper = sqlSession.getMapper(FcubeplayerExtender2Mapper.class);
        try{
            emitter.send(mapper.selectPlayerJoinList(item));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        emitter.complete();

    }

    @Async
    public void getPlayerJoinList(ResponseBodyEmitter emitter,String cubeuuid)  {

        FcubeExtend1Mapper mapper = sqlSession.getMapper(FcubeExtend1Mapper.class);
        try{
            emitter.send(mapper.getFcubeExtender1(cubeuuid));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        emitter.complete();

    }

    @Async
    public void selectFcubeReview(ResponseBodyEmitter emitter,Fcubereview item){
        FcubereviewMapper mapper = sqlSession.getMapper(FcubereviewMapper.class);
        try{
            emitter.send(mapper.selectFcubeReview(item));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        emitter.complete();
    }

    @Async
    @Transactional
    public void updateCubeHitPoint(ResponseBodyEmitter emitter,String cubeuuid) throws IOException {
        FcubeMapper mapper = sqlSession.getMapper(FcubeMapper.class);
        Fcube cube = mapper.selectforupdate(cubeuuid);
        cube.setCubehits(cube.getCubehits() == null ? 0:cube.getCubehits());
        cube.setCubehits(cube.getCubehits() + 1 );
        mapper.updateCubeHitPoint(cube);
        emitter.send(cube.getCubehits());
        emitter.complete();
    }

    @Async
    public void getCubeuuidGetPoint(ResponseBodyEmitter emitter,Userexppointhistroy item) throws IOException{
        UserexppointhistroyMapper mapper = sqlSession.getMapper(UserexppointhistroyMapper.class);
        emitter.send(mapper.getCubeuuidGetPoint(item));
        emitter.complete();
    }
}
