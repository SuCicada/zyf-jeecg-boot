package org.zyf.scholarship.nj.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.zyf.scholarship.nj.entity.ZyfNj;
import org.zyf.scholarship.nj.service.IZyfNjService;

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
 * @Description: 年级表
 * @Author: jeecg-boot
 * @Date:   2021-02-14
 * @Version: V1.0
 */
@Api(tags="年级表")
@RestController
@RequestMapping("/nj/zyfNj")
@Slf4j
public class ZyfNjController extends JeecgController<ZyfNj, IZyfNjService> {
	@Autowired
	private IZyfNjService zyfNjService;

	/**
	 * 分页列表查询
	 *
	 * @param zyfNj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "年级表-分页列表查询")
	@ApiOperation(value="年级表-分页列表查询", notes="年级表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZyfNj zyfNj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZyfNj> queryWrapper = QueryGenerator.initQueryWrapper(zyfNj, req.getParameterMap());
		Page<ZyfNj> page = new Page<ZyfNj>(pageNo, pageSize);
		IPage<ZyfNj> pageList = zyfNjService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
     @RequestMapping(value = "/queryall", method = RequestMethod.GET)
     public Result<List<ZyfNj>> queryall() {
         Result<List<ZyfNj>> result = new Result<>();
         List<ZyfNj> list = zyfNjService.list();
         if(list==null||list.size()<=0) {
             result.error500("未找到信息");
         }else {
             result.setResult(list);
             result.setSuccess(true);
         }
         return result;
     }
	/**
	 *   添加
	 *
	 * @param zyfNj
	 * @return
	 */
	@AutoLog(value = "年级表-添加")
	@ApiOperation(value="年级表-添加", notes="年级表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZyfNj zyfNj) {
		zyfNjService.save(zyfNj);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param zyfNj
	 * @return
	 */
	@AutoLog(value = "年级表-编辑")
	@ApiOperation(value="年级表-编辑", notes="年级表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZyfNj zyfNj) {
		zyfNjService.updateById(zyfNj);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "年级表-通过id删除")
	@ApiOperation(value="年级表-通过id删除", notes="年级表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zyfNjService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "年级表-批量删除")
	@ApiOperation(value="年级表-批量删除", notes="年级表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zyfNjService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "年级表-通过id查询")
	@ApiOperation(value="年级表-通过id查询", notes="年级表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZyfNj zyfNj = zyfNjService.getById(id);
		if(zyfNj==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zyfNj);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zyfNj
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfNj zyfNj) {
        return super.exportXls(request, zyfNj, ZyfNj.class, "年级表");
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
        return super.importExcel(request, response, ZyfNj.class);
    }

}
