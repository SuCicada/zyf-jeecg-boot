package org.zyf.scholarship.zc_student.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zyf.scholarship.zc_student.entity.ZyfZcStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 综测学生表
 * @Author: jeecg-boot
 * @Date:   2021-02-25
 * @Version: V1.0
 */
public interface ZyfZcStudentMapper extends BaseMapper<ZyfZcStudent> {
    IPage<Map<String, Object>> queryPageList(IPage<Map<String, Object>> a, Map<String, String> query);

}
