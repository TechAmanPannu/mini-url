package com.miniurl.utils;


import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public final class HashUtil {

    private HashUtil(){}
    public static String sha256(String base) {

        if (ObjUtil.isBlank(base))
            return null;
        return Hashing.sha256().hashString(base, Charsets.UTF_8).toString();
    }
}
