package org.zyf.scholarship.utils;

import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public enum Role {
        OFFICE, TEACHER, STUDENT
    }

    public static final Map<String, Role> RoleMap =
        ImmutableMap.<String, Role>builder()
            .put("1359088297723342849", Role.OFFICE)
            .put("1359088351200718850", Role.TEACHER)
            .put("1361357450112102402", Role.STUDENT)
            .build();

    public static Role getRoleFromRoleId(String roleId) {
        return RoleMap.get(roleId);
    }
    public static LoginUser getLoginUser(){
        return  (LoginUser) SecurityUtils.getSubject().getPrincipal();
    }

    public static Map<String, String> getParamMap(HttpServletRequest req) {
        Map<String, String> map = new HashMap();
        for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
            map.put(entry.getKey(), entry.getValue()[0]);
        }
        return map;
    }
}
