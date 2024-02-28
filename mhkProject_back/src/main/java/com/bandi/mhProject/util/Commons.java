package com.bandi.mhProject.util;

import java.util.Map;

public class Commons {
    public static Map<String, Object> putMessage(Map<String, Object> map, Integer code, String description){
        map.put("result", code);
        map.put("result_description", description);

        return map;
    }
}
