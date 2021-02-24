package org.zyf.scholarship.student.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zyf.scholarship.student.entity.ZyfStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2021-02-20
 * @Version: V1.0
 */
public interface ZyfStudentMapper extends BaseMapper<ZyfStudent> {
    IPage<Map<String, Object>> queryPageList(IPage<Map<String, Object>> a, Map<String, String> query);

}
