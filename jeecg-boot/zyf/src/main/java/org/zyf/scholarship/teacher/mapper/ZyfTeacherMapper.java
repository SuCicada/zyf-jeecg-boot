package org.zyf.scholarship.teacher.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zyf.scholarship.teacher.entity.ZyfTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 老师表
 * @Author: jeecg-boot
 * @Date:   2021-02-16
 * @Version: V1.0
 */
public interface ZyfTeacherMapper extends BaseMapper<ZyfTeacher> {
    IPage<Map<String, Object>> queryPageList(IPage<Map<String, Object>> a, Map<String, String> query);

}
