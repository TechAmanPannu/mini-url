package com.miniurl.utils;


import java.util.UUID;


public final class UUIDUtil {

    private UUIDUtil(){}
    public static class LongNumber {

        private LongNumber(){}
        public static long getRandomNumber() {
            return System.currentTimeMillis() + ((UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE) / 100000);
        }

    }


}
