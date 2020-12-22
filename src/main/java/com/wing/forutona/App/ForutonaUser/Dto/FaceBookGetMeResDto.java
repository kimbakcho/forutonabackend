package com.wing.forutona.App.ForutonaUser.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public  class FaceBookGetMeResDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("picture")
    private Picture picture;
    @JsonProperty("email")
    private String email;
    @JsonProperty("last_name")
    private String last_name;
    @JsonProperty("first_name")
    private String first_name;
    @JsonProperty("name")
    private String name;


    public static class Picture {
        @JsonProperty("data")
        private Data data;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public static class Data {
        @JsonProperty("width")
        private int width;
        @JsonProperty("url")
        private String url;
        @JsonProperty("is_silhouette")
        private boolean is_silhouette;
        @JsonProperty("height")
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isIs_silhouette() {
            return is_silhouette;
        }

        public void setIs_silhouette(boolean is_silhouette) {
            this.is_silhouette = is_silhouette;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
