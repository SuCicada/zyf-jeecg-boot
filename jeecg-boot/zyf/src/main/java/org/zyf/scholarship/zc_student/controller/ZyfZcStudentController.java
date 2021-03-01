package org.zyf.scholarship.zc_student.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import org.zyf.scholarship.student.mapper.ZyfStudentMapper;
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.utils.service.UtilService;
import org.zyf.scholarship.zc_student.entity.ZyfZcStudent;
import org.zyf.scholarship.zc_student.mapper.ZyfZcStudentMapper;
import org.zyf.scholarship.zc_student.service.IZyfZcStudentService;

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

 /**
 * @Description: 综测学生表
 * @Author: jeecg-boot
 * @Date:   2021-02-25
 * @Version: V1.0
 */
@Api(tags="综测学生表")
@RestController
@RequestMapping("/zc_student/zyfZcStudent")
@Slf4j
public class ZyfZcStudentController extends JeecgController<ZyfZcStudent, IZyfZcStudentService> {
	@Autowired
	private IZyfZcStudentService zyfZcStudentService;
     @Resource
     private ZyfZcStudentMapper zyfZcStudentMapper;
     @Autowired
     private UtilService utilService;

     /**
	 * 分页列表查询
	 *
	 * @param zyfZcStudent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "综测学生表-分页列表查询")
	@ApiOperation(value="综测学生表-分页列表查询", notes="综测学生表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZyfZcStudent zyfZcStudent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<ZyfZcStudent> queryWrapper = QueryGenerator.initQueryWrapper(zyfZcStudent, req.getParameterMap());
//		Page<ZyfZcStudent> page = new Page<ZyfZcStudent>(pageNo, pageSize);
//		IPage<ZyfZcStudent> pageList = zyfZcStudentService.page(page, queryWrapper);
		IPage<Map<String, Object>> pageList = new Page<>(pageNo, pageSize);
		Map<String, String> map = Util.getParamMap(req);

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Util.Role role = utilService.who(loginUser.getId());
		if (role == Util.Role.TEACHER) {
			map.put("zyId", loginUser.getOrgCode());
		}
		pageList = zyfZcStudentMapper.queryPageList(pageList, map);

		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param zyfZcStudent
	 * @return
	 */
	@AutoLog(value = "综测学生表-添加")
	@ApiOperation(value="综测学生表-添加", notes="综测学生表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZyfZcStudent zyfZcStudent) {
		zyfZcStudentService.save(zyfZcStudent);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param zyfZcStudent
	 * @return
	 */
	@AutoLog(value = "综测学生表-编辑")
	@ApiOperation(value="综测学生表-编辑", notes="综测学生表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZyfZcStudent zyfZcStudent) {
		zyfZcStudentService.updateById(zyfZcStudent);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "综测学生表-通过id删除")
	@ApiOperation(value="综测学生表-通过id删除", notes="综测学生表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zyfZcStudentService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "综测学生表-批量删除")
	@ApiOperation(value="综测学生表-批量删除", notes="综测学生表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zyfZcStudentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "综测学生表-通过id查询")
	@ApiOperation(value="综测学生表-通过id查询", notes="综测学生表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZyfZcStudent zyfZcStudent = zyfZcStudentService.getById(id);
		if(zyfZcStudent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zyfZcStudent);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zyfZcStudent
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfZcStudent zyfZcStudent) {
        return super.exportXls(request, zyfZcStudent, ZyfZcStudent.class, "综测学生表");
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
        return super.importExcel(request, response, ZyfZcStudent.class);
    }

}
