package org.zyf.scholarship.utils.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.utils.mapper.UtilMapper;
import org.zyf.scholarship.utils.service.UtilService;
import org.zyf.scholarship.zy.entity.ZyfZy;
import org.zyf.scholarship.zy.mapper.ZyfZyMapper;
import org.zyf.scholarship.zy.service.IZyfZyService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UtilServiceImpl
    extends ServiceImpl<UtilMapper, Map>
    implements UtilService {
    @Resource
    private UtilMapper utilMapper;

    @Override
    public Util.Role who(String userId) {
        String roleId = utilMapper.getRoleId(userId);
        return Util.getRoleFromRoleId(roleId);
    }
}

