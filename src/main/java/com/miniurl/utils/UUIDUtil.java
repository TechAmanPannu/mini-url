package com.miniurl.utils;

import java.util.UUID;

public final class UUIDUtil {

    public static class LongNumber {
        public static long getRandomNumber(){
           return System.currentTimeMillis() + ((UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE) / 100000);
        }
    }
}
