package com.miniurl.enums;

import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;


public enum AppMode {

    PRODUCTION("live-mini-url"), MINIKUBE("minikube-mini-url");

    private String projectId;

    AppMode(String projectId){
        this.projectId = projectId;
    }

    public static AppMode getMode() {

        String projectId = System.getenv("PROJECT_ID");
        Preconditions.checkArgument(ObjUtil.isBlank(projectId), "Invalid projectId to run App");

        for(AppMode appMode : AppMode.values()){

            if(appMode == null)
                continue;

            if(projectId.equals(appMode.projectId))
                return appMode;

        }

        return AppMode.PRODUCTION;
    }

    public boolean isProduction(){
        return this == AppMode.PRODUCTION;
    }
}
