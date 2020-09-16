package com.miniurl.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.miniurl.utils.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Text implements Serializable {
    private static final long serialVersionUID = -4801264900920801628L;

    private String value;

    public static List<Text> getList(String json) {

        if (ObjUtil.isBlank(json))
            return new ArrayList<>();
        try {
            return ObjUtil.getJacksonMapper().readValue(json,
                    new TypeReference<ArrayList<Text>>() {
                    });
        } catch (IOException e) {
            return null;
        }
    }
}
