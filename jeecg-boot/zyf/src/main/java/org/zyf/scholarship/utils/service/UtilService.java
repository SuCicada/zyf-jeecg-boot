package org.zyf.scholarship.utils.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zyf.scholarship.utils.Util;

import java.util.Map;

public interface UtilService extends IService<Map> {
    Util.Role who(String userId);

}
