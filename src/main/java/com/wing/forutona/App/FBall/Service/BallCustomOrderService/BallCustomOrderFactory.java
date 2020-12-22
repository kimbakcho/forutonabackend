package com.wing.forutona.App.FBall.Service.BallCustomOrderService;

import com.google.type.LatLng;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class BallCustomOrderFactory {

    public BallCustomOrderPathService makeOrder(LatLng position,Sort.Order sort ){
        if(BallCustomOrderDistance.orderKey.equals(sort.getProperty())){
            return  new BallCustomOrderDistance(position,sort);
        }else if(BallCustomOrderMakeTimeDESCAliveDESC.orderKey.equals(sort.getProperty())) {
            return  new BallCustomOrderMakeTimeDESCAliveDESC(sort);
        }else {
            return new BallNoCustomOrder(sort);
        }
    };

}
