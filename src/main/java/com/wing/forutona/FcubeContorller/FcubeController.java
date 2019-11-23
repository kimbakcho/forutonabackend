package com.wing.forutona.FcubeContorller;

import com.google.firebase.auth.FirebaseToken;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.FcubeDao.FcubeDao;
import com.wing.forutona.FcubeDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

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
        if(fireBaseAdmin.VerifyIdToken(token) !=null){
            return  fcubeDao.MakeCube(fcube);
        }else {
            return 0;
        }
    }

    @PostMapping(value = "/api/v1/Fcube/makecubecontent")
    public int MakeCube(@RequestBody Fcubecontent fcubecontent, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        token = token.replace("Bearer ","");
        if(fireBaseAdmin.VerifyIdToken(token) !=null){
            return  fcubeDao.MakeCubeContent(fcubecontent);
        }else {
            return 0;
        }
    }

    @GetMapping(value = "/api/v1/Fcube/getusercubes")
    public List<FcubeExtender1> getusercubes(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            List<FcubeExtender1> temp1= fcubeDao.GetUserCubes(ftoken.getUid());
            return fcubeDao.GetUserCubes(ftoken.getUid());
        }else {
            return null;
        }
    }

    @PostMapping(value="/api/v1/Fcube/deletecube")
    public int deletecube(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,@RequestBody Fcube cube){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
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
        if(ftoken !=null){
            return fcubeDao.InsertCubeReply(reply);
        }else {
            return null;
        }
    }

    @GetMapping(value="/api/v1/Fcube/SelectReplyForCube")
    List<FcubereplyExtender1> SelectReplyForCube(@RequestParam String cubeuuid,@RequestParam int offset,@RequestParam int limit){
        return fcubeDao.SelectReplyForCube(cubeuuid,offset,limit);
    }
}
