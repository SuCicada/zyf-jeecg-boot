package org.zyf.scholarship;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Map<String, String>  getParamMap(HttpServletRequest req){
        Map<String, String> map = new HashMap();
        for(Map.Entry<String, String[]> entry: req.getParameterMap().entrySet()){
            map.put(entry.getKey(),entry.getValue()[0]);
        }
        return map;
    }
}
