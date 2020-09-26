package com.miniurl.constants;


import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.zookeeper.leader.model.ServerNode;


public final class AppConstants {

    private AppConstants() {
    }

    public final static String APP_URL = "";
    public final static String APP_USER = "MINI_URL_APP_USER";
    public final static AppMode APP_MODE;

    public final static ServerNode SERVER;

    static {

        APP_MODE = AppMode.getMode();
        SERVER = getServerFromEnv();
        switch (APP_MODE) {

            case PRODUCTION:
                break;
            case MINIKUBE:
                break;
        }

    }


    private static ServerNode getServerFromEnv() {

        final String podIp = System.getenv("POD_IP");
        final String  podId = System.getenv("POD_ID");

        Preconditions.checkArgument(ObjUtil.isBlank(podIp), "Invalid podId to start server");
        Preconditions.checkArgument(ObjUtil.isBlank(podId), "Invalid podId for running server");

        return new ServerNode(podId, podIp);
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
