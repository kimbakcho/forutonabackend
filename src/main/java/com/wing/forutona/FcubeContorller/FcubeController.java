package com.wing.forutona.FcubeContorller;

import com.google.firebase.auth.FirebaseToken;
import com.wing.forutona.AuthDao.FireBaseAdmin;
import com.wing.forutona.FcubeDao.FcubeDao;
import com.wing.forutona.FcubeDto.Fcube;
import com.wing.forutona.FcubeDto.Fcubecontent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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
    public List<Fcube> getusercubes(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token){
        token = token.replace("Bearer ","");
        FirebaseToken ftoken = fireBaseAdmin.VerifyIdToken(token);
        if(ftoken !=null){
            return fcubeDao.GetUserCubes(ftoken.getUid());
        }else {
            return null;
        }
    }

}
