package org.zyf.scholarship.zy.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.zyf.scholarship.zy.entity.ZyfZy;
import org.zyf.scholarship.zy.mapper.ZyfZyMapper;
import org.zyf.scholarship.zy.service.IZyfZyService;

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
 * @Description: 专业表
 * @Author: jeecg-boot
 * @Date:   2021-02-14
 * @Version: V1.0
 */
@Api(tags="专业表")
@RestController
@RequestMapping("/zy/zyfZy")
@Slf4j
public class ZyfZyController extends JeecgController<ZyfZy, IZyfZyService> {
	@Autowired
	private IZyfZyService zyfZyService;
     @Resource
     private ZyfZyMapper zyfZyMapper;

	/**
	 * 分页列表查询
	 *
	 * @param zyfZy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "专业表-分页列表查询")
	@ApiOperation(value="专业表-分页列表查询", notes="专业表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZyfZy zyfZy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZyfZy> queryWrapper = QueryGenerator.initQueryWrapper(zyfZy, req.getParameterMap());
//		Page<ZyfZy> page = new Page<ZyfZy>(pageNo, pageSize);
//		IPage<ZyfZy> pageList = zyfZyService.page(page, queryWrapper);
        QueryWrapper<Map<String, String>> qryWrapper = new QueryWrapper<>();
//            .select("zyf_zy.id as id"," zy_name as zyName"," xy_name as xyName")
        IPage<Map<String, String>> pageList = new Page<Map<String, String>>(pageNo, pageSize);
        Map<String, String> map = new HashMap();
        for(Map.Entry<String, String[]> entry: req.getParameterMap().entrySet()){
            map.put(entry.getKey(),entry.getValue()[0]);
        }
        pageList = zyfZyMapper.queryPageList(pageList,map);
		return Result.OK(pageList);
	}
     @RequestMapping(value = "/queryall", method = RequestMethod.GET)
     public Result<List<ZyfZy>> queryall() {
         Result<List<ZyfZy>> result = new Result<>();
         List<ZyfZy> list = zyfZyService.list();
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
	 * @param zyfZy
	 * @return
	 */
	@AutoLog(value = "专业表-添加")
	@ApiOperation(value="专业表-添加", notes="专业表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZyfZy zyfZy) {
		zyfZyService.save(zyfZy);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param zyfZy
	 * @return
	 */
	@AutoLog(value = "专业表-编辑")
	@ApiOperation(value="专业表-编辑", notes="专业表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZyfZy zyfZy) {
		zyfZyService.updateById(zyfZy);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专业表-通过id删除")
	@ApiOperation(value="专业表-通过id删除", notes="专业表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zyfZyService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "专业表-批量删除")
	@ApiOperation(value="专业表-批量删除", notes="专业表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zyfZyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "专业表-通过id查询")
	@ApiOperation(value="专业表-通过id查询", notes="专业表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZyfZy zyfZy = zyfZyService.getById(id);
		if(zyfZy==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zyfZy);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zyfZy
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfZy zyfZy) {
        return super.exportXls(request, zyfZy, ZyfZy.class, "专业表");
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
        return super.importExcel(request, response, ZyfZy.class);
    }

}
