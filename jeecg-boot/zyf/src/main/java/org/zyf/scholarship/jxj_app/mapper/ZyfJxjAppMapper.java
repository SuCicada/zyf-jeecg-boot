package org.zyf.scholarship.jxj_app.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zyf.scholarship.jxj_app.entity.ZyfJxjApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 奖学金申请
 * @Author: jeecg-boot
 * @Date: 2021-03-03
 * @Version: V1.0
 */
public interface ZyfJxjAppMapper extends BaseMapper<ZyfJxjApp> {
    IPage<Map<String, Object>> queryPageList(IPage<Map<String, Object>> a, Map<String, String> query);

    /**
     * 得到待分发的学生信息
     */
    List<Map<String, Object>> queryAccepted(List<Map<String, Object>>a,Map<String, String> query);

    void autoInitAccepted(@Param("appIdList") List<String> appIdList);
}
