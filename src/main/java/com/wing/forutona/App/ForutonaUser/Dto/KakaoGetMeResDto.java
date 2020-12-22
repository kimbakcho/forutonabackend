package com.wing.forutona.App.ForutonaUser.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoGetMeResDto {

    @JsonProperty("kakao_account")
    private Kakao_account kakao_account;
    @JsonProperty("properties")
    private Properties properties;
    @JsonProperty("connected_at")
    private String connected_at;
    @JsonProperty("id")
    private int id;


    public static class Kakao_account {
        @JsonProperty("gender")
        private String gender;
        @JsonProperty("gender_needs_agreement")
        private boolean gender_needs_agreement;
        @JsonProperty("has_gender")
        private boolean has_gender;
        @JsonProperty("birthday_type")
        private String birthday_type;
        @JsonProperty("birthday")
        private String birthday;
        @JsonProperty("birthday_needs_agreement")
        private boolean birthday_needs_agreement;
        @JsonProperty("has_birthday")
        private boolean has_birthday;
        @JsonProperty("age_range")
        private String age_range;
        @JsonProperty("age_range_needs_agreement")
        private boolean age_range_needs_agreement;
        @JsonProperty("has_age_range")
        private boolean has_age_range;
        @JsonProperty("email")
        private String email;
        @JsonProperty("is_email_verified")
        private boolean is_email_verified;
        @JsonProperty("is_email_valid")
        private boolean is_email_valid;
        @JsonProperty("email_needs_agreement")
        private boolean email_needs_agreement;
        @JsonProperty("has_email")
        private boolean has_email;
        @JsonProperty("profile")
        private Profile profile;
        @JsonProperty("profile_needs_agreement")
        private boolean profile_needs_agreement;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public boolean isGender_needs_agreement() {
            return gender_needs_agreement;
        }

        public void setGender_needs_agreement(boolean gender_needs_agreement) {
            this.gender_needs_agreement = gender_needs_agreement;
        }

        public boolean isHas_gender() {
            return has_gender;
        }

        public void setHas_gender(boolean has_gender) {
            this.has_gender = has_gender;
        }

        public String getBirthday_type() {
            return birthday_type;
        }

        public void setBirthday_type(String birthday_type) {
            this.birthday_type = birthday_type;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public boolean isBirthday_needs_agreement() {
            return birthday_needs_agreement;
        }

        public void setBirthday_needs_agreement(boolean birthday_needs_agreement) {
            this.birthday_needs_agreement = birthday_needs_agreement;
        }

        public boolean isHas_birthday() {
            return has_birthday;
        }

        public void setHas_birthday(boolean has_birthday) {
            this.has_birthday = has_birthday;
        }

        public String getAge_range() {
            return age_range;
        }

        public void setAge_range(String age_range) {
            this.age_range = age_range;
        }

        public boolean isAge_range_needs_agreement() {
            return age_range_needs_agreement;
        }

        public void setAge_range_needs_agreement(boolean age_range_needs_agreement) {
            this.age_range_needs_agreement = age_range_needs_agreement;
        }

        public boolean isHas_age_range() {
            return has_age_range;
        }

        public void setHas_age_range(boolean has_age_range) {
            this.has_age_range = has_age_range;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isIs_email_verified() {
            return is_email_verified;
        }

        public void setIs_email_verified(boolean is_email_verified) {
            this.is_email_verified = is_email_verified;
        }

        public boolean isIs_email_valid() {
            return is_email_valid;
        }

        public void setIs_email_valid(boolean is_email_valid) {
            this.is_email_valid = is_email_valid;
        }

        public boolean isEmail_needs_agreement() {
            return email_needs_agreement;
        }

        public void setEmail_needs_agreement(boolean email_needs_agreement) {
            this.email_needs_agreement = email_needs_agreement;
        }

        public boolean isHas_email() {
            return has_email;
        }

        public void setHas_email(boolean has_email) {
            this.has_email = has_email;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public boolean isProfile_needs_agreement() {
            return profile_needs_agreement;
        }

        public void setProfile_needs_agreement(boolean profile_needs_agreement) {
            this.profile_needs_agreement = profile_needs_agreement;
        }
    }

    public static class Profile {
        @JsonProperty("profile_image_url")
        private String profile_image_url;
        @JsonProperty("thumbnail_image_url")
        private String thumbnail_image_url;
        @JsonProperty("nickname")
        private String nickname;

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getThumbnail_image_url() {
            return thumbnail_image_url;
        }

        public void setThumbnail_image_url(String thumbnail_image_url) {
            this.thumbnail_image_url = thumbnail_image_url;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public static class Properties {
        @JsonProperty("thumbnail_image")
        private String thumbnail_image;
        @JsonProperty("profile_image")
        private String profile_image;
        @JsonProperty("nickname")
        private String nickname;

        public String getThumbnail_image() {
            return thumbnail_image;
        }

        public void setThumbnail_image(String thumbnail_image) {
            this.thumbnail_image = thumbnail_image;
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
    }
}
