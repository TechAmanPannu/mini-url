package com.miniurl.constants;


import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;


public final class AppConstants {

    private AppConstants() {
    }

    public final static String APP_URL = "";
    public final static String APP_USER = "MINI_URL_APP_USER";
    public final static AppMode APP_MODE;
    public final static String SERVER_ID;
    public final static String SERVER_IP;

    static {

        APP_MODE = AppMode.getMode();
        SERVER_ID = getServerId();
        SERVER_IP = getServerIP();
        switch (APP_MODE) {

            case PRODUCTION:
                break;
            case MINIKUBE:
                break;
        }

    }

    private static String getServerIP() {
        final String podIp = System.getenv("POD_IP");
        Preconditions.checkArgument(ObjUtil.isBlank(podIp), "Invalid podId to start server");
        return podIp;
    }

    public static String getServerId() {
        final String  podId = System.getenv("POD_ID");
        Preconditions.checkArgument(ObjUtil.isBlank(podId), "Invalid podId for running server");
        return podId;
    }

    enum AppMode {

        PRODUCTION("live-mini-url"), MINIKUBE("minikube-mini-url");

        private String projectId;

        AppMode(String projectId) {
            this.projectId = projectId;
        }

        public static AppMode getMode() {

            String projectId = System.getenv("PROJECT_ID");
            Preconditions.checkArgument(ObjUtil.isBlank(projectId), "Invalid projectId to run App");

            for (AppMode appMode : AppMode.values()) {

                if (appMode == null)
                    continue;
                if (projectId.equals(appMode.projectId))
                    return appMode;
            }
            return AppMode.PRODUCTION;
        }

    }

    public static boolean isProduction() {
        return APP_MODE == AppMode.PRODUCTION;
    }
}
