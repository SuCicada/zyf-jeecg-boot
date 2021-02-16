package org.zyf.scholarship.bj.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zyf.scholarship.bj.entity.ZyfBj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 班级表
 * @Author: jeecg-boot
 * @Date: 2021-02-14
 * @Version: V1.0
 */
public interface ZyfBjMapper extends BaseMapper<ZyfBj> {
    IPage<Map<String, String>> queryPageList(IPage<Map<String, String>> a, Map<String, String> query);
}
