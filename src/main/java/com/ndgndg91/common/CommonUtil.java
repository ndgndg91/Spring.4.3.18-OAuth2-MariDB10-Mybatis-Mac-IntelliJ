package com.ndgndg91.common;


import com.sun.istack.internal.NotNull;

public class CommonUtil {
    public static int IntFromAssumedToBeIntegerObject(@NotNull Object obj){
        return obj instanceof Integer ? (int) obj : 0;
    }
}
