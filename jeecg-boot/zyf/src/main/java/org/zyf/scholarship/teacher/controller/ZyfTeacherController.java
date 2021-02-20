package org.zyf.scholarship.teacher.controller;

import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
//import org.jeecg.modules.system.entity.SysUser;
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.teacher.entity.ZyfTeacher;
import org.zyf.scholarship.teacher.mapper.ZyfTeacherMapper;
import org.zyf.scholarship.teacher.service.IZyfTeacherService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 老师表
 * @Author: jeecg-boot
 * @Date: 2021-02-16
 * @Version: V1.0
 */
@Api(tags = "老师表")
@RestController
@RequestMapping("/teacher/zyfTeacher")
@Slf4j
public class ZyfTeacherController extends JeecgController<ZyfTeacher, IZyfTeacherService> {
    @Autowired
    private IZyfTeacherService zyfTeacherService;
    @Resource
    private ZyfTeacherMapper zyfTeacherMapper;

    /**
     * 分页列表查询
     *
     * @param zyfTeacher
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "老师表-分页列表查询")
    @ApiOperation(value = "老师表-分页列表查询", notes = "老师表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ZyfTeacher zyfTeacher,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//		QueryWrapper<ZyfTeacher> queryWrapper = QueryGenerator.initQueryWrapper(zyfTeacher, req.getParameterMap());
//		Page<ZyfTeacher> page = new Page<ZyfTeacher>(pageNo, pageSize);
//		IPage<ZyfTeacher> pageList = zyfTeacherService.page(page, queryWrapper);
        IPage<Map<String, Object>> pageList = new Page<>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);
        pageList = zyfTeacherMapper.queryPageList(pageList, map);
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

    /**
     * 添加
     *
     * @param zyfTeacher
     * @return
     */
    @AutoLog(value = "老师表-添加")
    @ApiOperation(value = "老师表-添加", notes = "老师表-添加")
    @PostMapping(value = "/add")
//    public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
    public Result<?> add(@RequestBody ZyfTeacher zyfTeacher) {
//		zyfTeacherService.save(zyfTeacher);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param zyfTeacher
     * @return
     */
    @AutoLog(value = "老师表-编辑")
    @ApiOperation(value = "老师表-编辑", notes = "老师表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ZyfTeacher zyfTeacher) {
        zyfTeacherService.updateById(zyfTeacher);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "老师表-通过id删除")
    @ApiOperation(value = "老师表-通过id删除", notes = "老师表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        zyfTeacherService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "老师表-批量删除")
    @ApiOperation(value = "老师表-批量删除", notes = "老师表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zyfTeacherService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "老师表-通过id查询")
    @ApiOperation(value = "老师表-通过id查询", notes = "老师表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ZyfTeacher zyfTeacher = zyfTeacherService.getById(id);
        if (zyfTeacher == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zyfTeacher);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zyfTeacher
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfTeacher zyfTeacher) {
        return super.exportXls(request, zyfTeacher, ZyfTeacher.class, "老师表");
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
        return super.importExcel(request, response, ZyfTeacher.class);
    }

}
