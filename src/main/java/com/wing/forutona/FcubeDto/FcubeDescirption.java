package com.wing.forutona.FcubeDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
public  class FcubeDescirption {
    String cubeuuid;
    @JsonProperty("youtubeVideoid")
    private String youtubeVideoid;
    @JsonProperty("havemodify")
    private boolean havemodify;
    @JsonProperty("writetime")
    private String writetime;
    @JsonProperty("desimages")
    private List<Desimages> desimages;
    @JsonProperty("text")
    private String text;
    @JsonProperty("tags")
    private List<String> tags;

    @Data
    public static class Desimages {
        @JsonProperty("index")
        private int index;
        @JsonProperty("src")
        private String src;
    }
}
