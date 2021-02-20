package org.zyf.scholarship.utils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zyf.scholarship.zy.entity.ZyfZy;

import java.util.Map;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zyf.scholarship.zy.entity.ZyfZy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UtilMapper extends BaseMapper<Map> {
    @Select("select role_id from sys_user_role \n" +
        "    where sys_user_role.user_id=#{userId} \n" +
        "    limit 1 ")
    String getRoleId(String userId);
}
