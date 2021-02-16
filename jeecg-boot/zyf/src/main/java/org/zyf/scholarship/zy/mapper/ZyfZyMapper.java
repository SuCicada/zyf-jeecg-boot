package org.zyf.scholarship.zy.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zyf.scholarship.zy.entity.ZyfZy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 专业表
 * @Author: jeecg-boot
 * @Date: 2021-02-14
 * @Version: V1.0
 */
public interface ZyfZyMapper extends BaseMapper<ZyfZy> {

//    @Select(
//        " select zyf_zy.id as id, zy_name as zyName, xy_name as xyName " +
//            " from zyf_zy,zyf_xy " +
//            " where zyf_zy.xy_id=zyf_xy.id " +
//            " and '${query.zyName}' is not null and zyf_zy.zy_name like '%${query.zyName}%' " +
//            " and '${query.xyName}' is not null and zyf_xy.xy_name like '%${query.xyName}%' ")
    IPage<Map<String, String>> queryPageList(IPage<Map<String, String>> a,
                                             @Param("query") Map<String, String> query);

}
