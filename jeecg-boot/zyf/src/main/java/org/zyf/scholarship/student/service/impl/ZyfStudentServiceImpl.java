package org.zyf.scholarship.student.service.impl;

import org.zyf.scholarship.student.entity.ZyfStudent;
import org.zyf.scholarship.student.mapper.ZyfStudentMapper;
import org.zyf.scholarship.student.service.IZyfStudentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2021-02-20
 * @Version: V1.0
 */
@Service
public class ZyfStudentServiceImpl extends ServiceImpl<ZyfStudentMapper, ZyfStudent> implements IZyfStudentService {

}
