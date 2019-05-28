package com.ndgndg91.model.enums;


public enum LoginType {
    GOOGLE{
        @Override
        public String toString() {
            return "GOOGLE";
        }
    },
    KAKAO{
        @Override
        public String toString() {
            return "KAKAO";
        }
    },
    NAVER{
        @Override
        public String toString() { return "NAVER";}
    },
    DEFAULT{
        @Override
        public String toString() {
            return "DEFAULT";
        }
    }
}
