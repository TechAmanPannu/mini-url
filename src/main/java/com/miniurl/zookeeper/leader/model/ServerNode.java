package com.miniurl.zookeeper.leader.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ServerNode implements Serializable {

    private static final long serialVersionUID = -9078861953401781166L;

    private String id;
    private String host;
    private boolean leader;

    public ServerNode(String id, String host) {
        this.id = id;
        this.host = host;
    }

    public static List<ServerNode> toList(String serverListJson) {

        Preconditions.checkArgument(ObjUtil.isBlank(serverListJson), "invalid server list json");
        try {
            return ObjUtil.getJacksonMapper().convertValue(serverListJson, new TypeReference<List<ServerNode>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
