package com.wing.forutona.ForutonaUser.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverGetMeResDto {

    @JsonProperty("response")
    private Response response;
    @JsonProperty("message")
    private String message;
    @JsonProperty("resultcode")
    private String resultcode;

    public static class Response {
        @JsonProperty("birthday")
        private String birthday;
        @JsonProperty("email")
        private String email;
        @JsonProperty("gender")
        private String gender;
        @JsonProperty("age")
        private String age;
        @JsonProperty("profile_image")
        private String profile_image;
        @JsonProperty("nickname")
        private String nickname;
        @JsonProperty("id")
        private String id;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
