package org.zyf.scholarship.student.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.zyf.scholarship.student.entity.ZyfStudent;
import org.zyf.scholarship.student.mapper.ZyfStudentMapper;
import org.zyf.scholarship.student.service.IZyfStudentService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.zyf.scholarship.teacher.mapper.ZyfTeacherMapper;
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.utils.service.UtilService;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date: 2021-02-20
 * @Version: V1.0
 */
@Api(tags = "学生表")
@RestController
@RequestMapping("/student/zyfStudent")
@Slf4j
public class ZyfStudentController extends JeecgController<ZyfStudent, IZyfStudentService> {
    @Autowired
    private IZyfStudentService zyfStudentService;
    @Resource
    private ZyfStudentMapper zyfStudentMapper;
    @Autowired
    private UtilService utilService;

    /**
     * 分页列表查询
     *
     * @param zyfStudent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "学生表-分页列表查询")
    @ApiOperation(value = "学生表-分页列表查询", notes = "学生表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ZyfStudent zyfStudent,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//		QueryWrapper<ZyfStudent> queryWrapper = QueryGenerator.initQueryWrapper(zyfStudent, req.getParameterMap());
//		Page<ZyfStudent> page = new Page<ZyfStudent>(pageNo, pageSize);
//		IPage<ZyfStudent> pageList = zyfStudentService.page(page, queryWrapper);
        IPage<Map<String, Object>> pageList = new Page<>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Util.Role role = utilService.who(loginUser.getId());
        if (role == Util.Role.TEACHER) {
            map.put("zyId", loginUser.getOrgCode());
        }
        pageList = zyfStudentMapper.queryPageList(pageList, map);
        pageList.getRecords().forEach(res -> {
            String sex = "";
            switch ((Integer) res.get("sex")) {
                case 1:
                    sex = "男";
                    break;
                case 2:
                    sex = "女";
                    break;
                default:
            }
            res.put("birthday", res.get("birthday").toString().split(" ")[0]);
            res.put("sexStr", sex);
        });
        return Result.OK(pageList);
    }

    @RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public Result<List<Map<String, Object>>> queryall(ZyfStudent zyfStudent,
                                             HttpServletRequest req) {
        Result<List<Map<String, Object>>> result = new Result<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//
        Map<String, String> map = new HashMap<>();
//        QueryWrapper<ZyfStudent> queryWrapper = QueryGenerator
//            .initQueryWrapper(zyfStudent, req.getParameterMap());
        Util.Role role = utilService.who(loginUser.getId());
        if (role == Util.Role.TEACHER) {
//            queryWrapper.eq("id", loginUser.getOrgCode());
            map.put("zyId", loginUser.getOrgCode());
        }
        map.put("studentRuleId", Util.STUDENT_ID);
        List<Map<String, Object>> list = new ArrayList<>();
            list = zyfStudentMapper.queryAllList(list,map);

//        List<ZyfStudent> list = zyfStudentService.list(queryWrapper);
        if (list == null || list.size() <= 0) {
            result.error500("未找到信息");
        } else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 添加
     *
     * @param zyfStudent
     * @return
     */
    @AutoLog(value = "学生表-添加")
    @ApiOperation(value = "学生表-添加", notes = "学生表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ZyfStudent zyfStudent) {
        zyfStudentService.save(zyfStudent);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param zyfStudent
     * @return
     */
    @AutoLog(value = "学生表-编辑")
    @ApiOperation(value = "学生表-编辑", notes = "学生表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ZyfStudent zyfStudent) {
        zyfStudentService.updateById(zyfStudent);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "学生表-通过id删除")
    @ApiOperation(value = "学生表-通过id删除", notes = "学生表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        zyfStudentService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "学生表-批量删除")
    @ApiOperation(value = "学生表-批量删除", notes = "学生表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zyfStudentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "学生表-通过id查询")
    @ApiOperation(value = "学生表-通过id查询", notes = "学生表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ZyfStudent zyfStudent = zyfStudentService.getById(id);
        if (zyfStudent == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zyfStudent);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zyfStudent
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfStudent zyfStudent) {
        return super.exportXls(request, zyfStudent, ZyfStudent.class, "学生表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ZyfStudent.class);
    }

}
