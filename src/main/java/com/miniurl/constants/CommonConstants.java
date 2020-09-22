package com.miniurl.constants;

import com.miniurl.enums.AppMode;
import com.miniurl.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final  class CommonConstants {

    public final static String APP_URL = "";

    public final static String APP_USER = "MINI_URL_APP_USER";

    public final static AppMode APP_MODE ;

    public final static Long SERVER_ID;

    static {

        APP_MODE = AppMode.getMode();
        SERVER_ID = UUIDUtil.LongNumber.getServerId();

        switch (APP_MODE){

            case PRODUCTION:
                break;
            case MINIKUBE:
                break;
        }

    }
}
