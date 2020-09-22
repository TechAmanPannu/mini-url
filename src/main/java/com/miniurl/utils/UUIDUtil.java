package com.miniurl.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public final class UUIDUtil {

    public static class LongNumber {
        public static long getRandomNumber() {
            return System.currentTimeMillis() + ((UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE) / 100000);
        }

        public static long getServerId() {

            final String  podId = System.getenv("POD_ID");
            Preconditions.checkArgument(ObjUtil.isBlank(podId), "Invalid PodId to generate serverId");
            UUID serverUUID = UUID.fromString(podId);
            return serverUUID.getMostSignificantBits() & Long.MAX_VALUE;
        }
    }


}
