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
    DEFAULT{
        @Override
        public String toString() {
            return "DEFAULT";
        }
    }
}
