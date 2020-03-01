package com.wing.forutona;

import com.wing.forutona.FcubeDto.Fcube;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FcubeScheduledSupport1In {
    void fcubeupdateandhistorysave(Fcube item);
}
