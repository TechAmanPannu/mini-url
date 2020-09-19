package com.miniurl.impl;


import com.miniurl.redis.KeyCounterService;
import com.miniurl.utils.EncodeUtil;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

class UrlDaoImplTest {

    public static void main(String[] arg) {


        System.out.println(EncodeUtil.Base62.encode(new BigInteger("100000000000")));

    }
}
