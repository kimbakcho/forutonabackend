package com.wing.forutona.FcubeDao;

import com.wing.forutona.FcubeDto.FcubetagSearch;
import com.wing.forutona.FcubeDto.Fcubetagextender1;
import java.util.List;

public interface Fcubetagextender1Mapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FcubetagExtender1
     *
     * @mbg.generated
     */
    List<Fcubetagextender1> selectAll();

    List<Fcubetagextender1> selectFcubetagSearch(FcubetagSearch search);
}