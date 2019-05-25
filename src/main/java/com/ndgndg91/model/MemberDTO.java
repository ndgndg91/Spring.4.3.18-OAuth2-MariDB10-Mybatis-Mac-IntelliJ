package com.ndgndg91.model;

import com.ndgndg91.model.enums.LoginType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "email", "nick", "loginType", "subId"})
public class MemberDTO {
    private String id;
    private String email;
    private String pw;
    private String nick;
    private String loginType;
    private String pictureUrl;
    private String locale;
    private String subId;
    private String createTime;

    private MemberDTO(){}
    private MemberDTO(String id, String email, String pw, String nick, String loginType, String pictureUrl,
                      String locale, String subId, String createTime){
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.nick = nick;
        this.loginType = loginType;
        this.pictureUrl = pictureUrl;
        this.locale = locale;
        this.subId = subId;
        this.createTime = createTime;
    }

    public static class Builder {
        // Required parameters(필수 인자)
        private final String id;
        private final String email;

        // Optional parameters - initialized to default values(선택적 인자는 기본값으로 초기화)
        private String pw = "";
        private String nick = "";
        private String loginType = LoginType.DEFAULT.toString();
        private String pictureUrl = "";
        private String locale = "ko";
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

        public Builder loginType(String loginType) {
            this.loginType = loginType;
            return this;
        }

        public Builder pictureUrl(String pictureUrl){
            this.pictureUrl = pictureUrl;
            return this;
        }

        public Builder locale(String locale){
            this.locale = locale;
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
        this.loginType = builder.loginType;
        this.pictureUrl = builder.pictureUrl;
        this.locale = builder.locale;
        this.subId = builder.subId;
    }
}


