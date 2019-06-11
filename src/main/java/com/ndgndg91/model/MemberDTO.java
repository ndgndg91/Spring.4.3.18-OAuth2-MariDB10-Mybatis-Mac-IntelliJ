package com.ndgndg91.model;

import com.ndgndg91.model.enums.LoginType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;


@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "email", "nick", "realName", "pictureUrl", "thumbnailImageUrl", "gender", "age", "birth", "loginType", "subId"})
public class MemberDTO {
    private String id;
    private String email;
    private String pw;
    private String nick;
    private String realName;
    private String loginType;
    private String pictureUrl;
    private String thumbnailImageUrl;
    private String locale;
    private String gender;
    private String age;
    private String birth;
    private String subId;
    private String createTime;

    private MemberDTO(){}
    private MemberDTO(String id, String email, String pw, String nick, String realName, String loginType,
                      String pictureUrl, String thumbnailImageUrl, String locale, String subId,
                      String gender, String age, String birth, String createTime ){
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.nick = nick;
        this.realName = realName;
        this.loginType = loginType;
        this.pictureUrl = pictureUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.locale = locale;
        this.gender = gender;
        this.age = age;
        this.birth = birth;
        this.subId = subId;
        this.createTime = createTime;
    }

    public boolean isPictureDifferent(MemberDTO other){
        if (!StringUtils.equals(this.pictureUrl, other.pictureUrl))
            return true;

        return !StringUtils.equals(this.thumbnailImageUrl, other.thumbnailImageUrl);
    }

    public void makePwBlank() {
        this.pw = "";
    }

    public static class Builder {
        // Required parameters(필수 인자)
        private final String id;
        private final String email;

        // Optional parameters - initialized to default values(선택적 인자는 기본값으로 초기화)
        private String pw = "";
        private String nick = "";
        private String realName = "";
        private String loginType = LoginType.DEFAULT.toString();
        private String pictureUrl = "";
        private String thumbnailImageUrl = "";
        private String locale = "ko";
        private String gender = "";
        private String age = "";
        private String birth = "";
        private String subId = "";

        public Builder(String id, String email) {
            this.id = id;
            this.email = email;
        }

        public Builder pw(String pw) {
            this.pw = pw;
            return this;
        }

        public Builder nick(String nick) {
            this.nick = nick;
            return this;
        }

        public Builder realName(String realName) {
            this.realName = realName;
            return this;
        }

        public Builder loginType(String loginType) {
            this.loginType = loginType;
            return this;
        }

        public Builder pictureUrl(String pictureUrl){
            this.pictureUrl = pictureUrl;
            return this;
        }

        public Builder thumbnailImageUrl(String thumbnailImageUrl){
            this.thumbnailImageUrl = thumbnailImageUrl;
            return this;
        }

        public Builder locale(String locale){
            this.locale = locale;
            return this;
        }

        public Builder age(String age){
            this.age = age;
            return this;
        }

        public Builder gender(String gender){
            this.gender = gender;
            return this;
        }

        public Builder birth(String birth){
            this.birth = birth;
            return this;
        }

        public Builder subId(String subId){
            this.subId = subId;
            return this;
        }

        public MemberDTO build() {
            return new MemberDTO(this);
        }
    }

    private MemberDTO(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.pw = builder.pw;
        this.nick = builder.nick;
        this.realName = builder.realName;
        this.loginType = builder.loginType;
        this.pictureUrl = builder.pictureUrl;
        this.thumbnailImageUrl = builder.thumbnailImageUrl;
        this.locale = builder.locale;
        this.gender = builder.gender;
        this.age = builder.age;
        this.birth = builder.birth;
        this.subId = builder.subId;
    }
}


