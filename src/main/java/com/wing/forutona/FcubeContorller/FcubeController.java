package com.wing.forutona.FcubeContorller;

import com.google.firebase.auth.FirebaseToken;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.AuthDto.Userexppointhistroy;
import com.wing.forutona.FcubeDao.FcubeDao;
import com.wing.forutona.FcubeDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

@RestController
public class FcubeController {

    @Autowired
    FcubeDao fcubeDao;

    @Autowired
    FireBaseAdmin fireBaseAdmin;


//    @GetMapping(value = "/api/v1/Cube/getFcubeMains")
//    public List<Fcube> getCubeMain(){
//        return  fcubeDao.getFcubeMains();
//    }

    @PostMapping(value = "/api/v1/Fcube/makecube")
    public int MakeCube(@RequestBody Fcube fcube, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(fcube.getUid())){
            return  fcubeDao.MakeCube(fcube);
        }else {
            return 0;
        }
    }

    @PostMapping(value = "/api/v1/Fcube/makecubecontent")
    public int MakeCube(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody Fcubecontent fcubecontent ){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null ){
            return  fcubeDao.MakeCubeContent(fcubecontent);
        }else {
            return 0;
        }
    }

    @GetMapping(value = "/api/v1/Fcube/getusercubes")
    public List<FcubeExtender1> getusercubes(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                             FcubeSearch search){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            search.setUid(ftoken.getUid());
            return fcubeDao.GetUserCubes(search);
        }else {
            return null;
        }
    }

    @PostMapping(value="/api/v1/Fcube/deletecube")
    public int deletecube(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody Fcube cube){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(cube.getUid()) ){
            return fcubeDao.deletecube(cube);
        }else {
            return 0;
        }
    }
    @PostMapping(value="/api/v1/Fcube/cuberelationimageupload")
    String CubeRelationImageUpload(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,MultipartHttpServletRequest request) throws IOException {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            return fcubeDao.CubeRelationImageUpload(request);
        }else {
            return "";
        }
    }

    @PostMapping(value="/api/v1/Fcube/cuberelationimagedelete")
    int CubeRelationImageDelete(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestParam String url) throws IOException {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            return fcubeDao.CubeRelationImageDelete(url);
        }else {
            return 0;
        }
    }

    @PostMapping(value="/api/v1/Fcube/getFcubecontent")
    List<Fcubecontent> getFcubecontent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody
    FcubeContentSelector selectitem) {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            return fcubeDao.selectwithFcubeContentSelector(selectitem);
        }else {
            return null;
        }
    }

    @PostMapping(value = "/api/v1/Fcube/InsertCubeReply")
    Fcubereply InsertCubeReply(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody
            Fcubereply reply) throws Exception {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(reply.getUid())){
            return fcubeDao.InsertCubeReply(reply);
        }else {
            return null;
        }
    }

    @GetMapping(value="/api/v1/Fcube/SelectReplyForCube")
    List<FcubereplyExtender1> SelectReplyForCube(@RequestParam String cubeuuid,@RequestParam int offset,@RequestParam int limit){
        return fcubeDao.SelectReplyForCube(cubeuuid,offset,limit);
    }

    @PostMapping(value="/api/v1/Fcube/updateCubeState")
    int updateCubeState(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody Fcube cube){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(cube.getUid())){
            return fcubeDao.updateCubeState(cube);
        }else {
            return 0;
        }
    }

    @GetMapping(value = "/api/v1/Fcube/SelectPlayers")
    List<FcubeplayerExtender1> SelectPlayers(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                             @RequestParam String cubeuuid,@RequestParam(required = false) String uid){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            return fcubeDao.selectPlayers(cubeuuid,uid);
        }else {
            return null;
        }
    }

    @PostMapping(value="/api/v1/Fcube/findNearDistanceCube")
    List<FcubeExtender1> findNearDistanceCube(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody FCubeGeoSearchUtil searchItem){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            return fcubeDao.findNearDistanceCube(searchItem);
        }else {
            return null;
        }
    }

    @PostMapping(value = "/api/v1/Fcube/insertFcubePlayer")
    int insertFcubePlayer(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody Fcubeplayer fcubeplayer){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(fcubeplayer.getUid())) {
            return fcubeDao.insertFcubePlayer(fcubeplayer);
        }else {
            return 0;
        }
    }

    @PostMapping(value="/api/v1/Fcube/getFcubeplayercontent")
    List<FcubeplayercontentExtender1> getFcubeplayercontent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                                            @RequestBody FcubeplayercontentSelector selectitem){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(selectitem.getUid())) {
            return fcubeDao.selectwithFcubeplayercontentSelector(selectitem);
        }else {
            return null;
        }
    }

    @PostMapping(value="/api/v1/Fcube/makeFcubeplayercontent")
    int makeFcubeplayercontent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                                            @RequestBody Fcubeplayercontent makeitem){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(makeitem.getUid())) {
            return fcubeDao.makeFcubeplayercontent(makeitem);
        }else {
            return 0;
        }
    }

    @PostMapping(value="/api/v1/Fcube/updateFcubeplayercontent")
    int updateFcubeplayercontent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                               @RequestBody FcubeplayercontentExtender1 updateitem){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(updateitem.getUid())) {
            return fcubeDao.updateFcubeplayercontent(updateitem);
        }else {
            return 0;
        }
    }

    @PostMapping(value="/api/v1/Fcube/deleteFcubeplayercontent")
    int deleteFcubeplayercontent(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                 @RequestBody FcubeplayercontentExtender1 deleteitem){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(deleteitem.getUid())) {
            return fcubeDao.deleteFcubeplayercontent(deleteitem);
        }else {
            return 0;
        }
    }

    @PostMapping(value="/api/v1/Fcube/uploadAuthForImage")
    ResponseBodyEmitter UploadAuthForImage(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,MultipartHttpServletRequest request) throws IOException {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        System.out.println(request.getParameter("uid"));
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(ftoken !=null && ftoken.getUid().equals(request.getParameter("uid"))) {
            fcubeDao.UploadAuthForImage(emitter,request);
            return emitter;
        }
        emitter.complete();
        return emitter;
    }


    @PostMapping(value="/api/v1/Fcube/deleteAuthForImage")
    ResponseBodyEmitter deleteAuthForImage(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                           @RequestParam String url,@RequestParam String uid) throws IOException {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(ftoken !=null && ftoken.getUid().equals(uid)) {
            fcubeDao.deleteAuthForImage(emitter,url);
            return emitter;
        }
        emitter.send(0);
        emitter.complete();
        return emitter;
    }

    @PostMapping(value="/api/v1/Fcube/requestFcubeQuestSuccess")
    int requestFcubeQuestSuccess(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                           @RequestBody Fcubequestsuccess item) throws IOException {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(item.getFromuid())) {
            return  fcubeDao.requestFcubeQuestSuccess(item);
        }
        return 0;
    }

    @PostMapping(value="/api/v1/Fcube/getQuestReqList")
    List<FcubequestsuccessExtender1> getQuestReqList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                                     @RequestBody FcubequestsuccessExtender1 item){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(item.getFromuid())) {
            return fcubeDao.getQuestReqList(item);
        }
        return null;
    }

    @PostMapping(value="/api/v1/Fcube/updateQuestReq")
    ResponseBodyEmitter updateQuestReq(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                                     @RequestBody FcubequestsuccessExtender1 item){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(ftoken !=null && ftoken.getUid().equals(item.getUid())) {
            fcubeDao.updateQuestReq(emitter,item);
        }
        return emitter;
    }
    @PostMapping(value="/api/v1/Fcube/updateQuesttoplayercomment")
    int updateQuesttoplayercomment(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                       @RequestBody FcubequestsuccessExtender1 item){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);

        if(ftoken !=null && ftoken.getUid().equals(item.getUid())) {
            return fcubeDao.updateQuesttoplayercomment(item);
        }
        return 0;
    }

    @PostMapping(value="/api/v1/Fcube/insertFcubeReview")
    int insertFcubeReview(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                   @RequestBody Fcubereview item){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(item.getUid())) {
            return fcubeDao.insertFcubeReview(item);
        }
        return 0;
    }

    @PostMapping(value = "/api/v1/Fcube/insertFcubeReviewExpPoint")
    ResponseBodyEmitter insertFcubeReviewExpPoint(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                  @RequestBody Fcubereview item){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(ftoken !=null && ftoken.getUid().equals(item.getUid())) {
           try {
               fcubeDao.insertFcubeReviewExpPoint(emitter,item);
           }catch (Exception ex){
               ex.printStackTrace();
               emitter.complete();
           }
        }
        return emitter;
    }


    @PostMapping(value="/api/v1/Fcube/getPlayerQuestSuccessList")
    List<FcubequestsuccessExtender1> getPlayerQuestSuccessList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                   @RequestBody FcubequestsuccessExtender1 item){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(item.getReaduid())) {
            return fcubeDao.getPlayerQuestSuccessList(item);
        }
        return null;
    }

    @PostMapping(value="/api/v1/Fcube/insertFcubeQuestSuccessCheck")
    int insertFcubeQuestSuccessCheck(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                     @RequestBody Fcubequestsuccesscheck item){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null && ftoken.getUid().equals(item.getReaduid())) {
            return fcubeDao.insertFcubeQuestSuccessCheck(item);
        }
        return 0;
    }

    @GetMapping(value = "/api/v1/Fcube/getFcubestate")
    Fcube getFcubestate(@RequestParam String cubeuuid){
        return fcubeDao.getFcubestate(cubeuuid);
    }

    @GetMapping(value = "/api/v1/Fcube/getPlayerJoinList")
    ResponseBodyEmitter getPlayerJoinList(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                                HttpServletResponse response,
                                                 @RequestParam String uid,@RequestParam int offset,@RequestParam int limit){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(ftoken !=null && ftoken.getUid().equals(uid)) {
            response.addHeader("content-type","application/json;charset=UTF-8");
            FcubeplayerSearch item = new FcubeplayerSearch();
            item.setUid(uid);
            item.setOffset(offset);
            item.setLimit(limit);
            fcubeDao.getPlayerJoinList(emitter,item);
        }
        return emitter;
    }

    @GetMapping(value = "/api/v1/Fcube/getFcubeExtender1")
    ResponseBodyEmitter getFcubeExtender1(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                          HttpServletResponse response,
                                          @RequestParam String uid,@RequestParam String cubeuuid){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(ftoken !=null && ftoken.getUid().equals(uid)) {
            response.addHeader("content-type","application/json;charset=UTF-8");
            fcubeDao.getPlayerJoinList(emitter,cubeuuid);
        }else {
            emitter.complete();
        }
        return emitter;
    }

    @GetMapping(value = "/api/v1/Fcube/getFcubeReview")
    ResponseBodyEmitter getFcubeReview(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                                          HttpServletResponse response,
                                          @RequestParam String cubeuuid,@RequestParam String uid)
    {
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        if(ftoken !=null && ftoken.getUid().equals(uid)) {
            response.addHeader("content-type","application/json;charset=UTF-8");
            Fcubereview item = new Fcubereview();
            item.setCubeuuid(cubeuuid);
            item.setUid(uid);
            fcubeDao.selectFcubeReview(emitter,item);
        }else {
            emitter.complete();
        }
        return emitter;
    }

    @PostMapping(value = "/api/v1/Fcube/updateCubeHitPoint")
    ResponseBodyEmitter updateCubeHitPoint(@RequestParam String cubeuuid){
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        try {
            fcubeDao.updateCubeHitPoint(emitter,cubeuuid);
        } catch (IOException e) {
            emitter.complete();
            e.printStackTrace();
        }
        return emitter;
    }

    @GetMapping(value = "/api/v1/Fcube/getCubeuuidGetPoint")
    ResponseBodyEmitter getCubeuuidGetPoint(Userexppointhistroy item) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        try {
            fcubeDao.getCubeuuidGetPoint(emitter,item);
        } catch (IOException e) {
            emitter.complete();
            e.printStackTrace();
        }
        return emitter;
    }
}
