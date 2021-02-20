package org.zyf.scholarship.kc.controller;

import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.kc.entity.ZyfKc;
import org.zyf.scholarship.kc.mapper.ZyfKcMapper;
import org.zyf.scholarship.kc.service.IZyfKcService;

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
 * @Description: 课程表
 * @Author: jeecg-boot
 * @Date:   2021-02-15
 * @Version: V1.0
 */
@Api(tags="课程表")
@RestController
@RequestMapping("/kc/zyfKc")
@Slf4j
public class ZyfKcController extends JeecgController<ZyfKc, IZyfKcService> {
	@Autowired
	private IZyfKcService zyfKcService;
     @Resource
     private ZyfKcMapper zyfKcMapper;
	/**
	 * 分页列表查询
	 *
	 * @param zyfKc
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "课程表-分页列表查询")
	@ApiOperation(value="课程表-分页列表查询", notes="课程表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZyfKc zyfKc,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<ZyfKc> queryWrapper = QueryGenerator.initQueryWrapper(zyfKc, req.getParameterMap());
//		Page<ZyfKc> page = new Page<ZyfKc>(pageNo, pageSize);
//		IPage<ZyfKc> pageList = zyfKcService.page(page, queryWrapper);
        IPage<Map<String, String>> pageList = new Page<Map<String, String>>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);
        pageList = zyfKcMapper.queryPageList(pageList,map);
        return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param zyfKc
	 * @return
	 */
	@AutoLog(value = "课程表-添加")
	@ApiOperation(value="课程表-添加", notes="课程表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZyfKc zyfKc) {
		zyfKcService.save(zyfKc);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param zyfKc
	 * @return
	 */
	@AutoLog(value = "课程表-编辑")
	@ApiOperation(value="课程表-编辑", notes="课程表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZyfKc zyfKc) {
		zyfKcService.updateById(zyfKc);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "课程表-通过id删除")
	@ApiOperation(value="课程表-通过id删除", notes="课程表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zyfKcService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "课程表-批量删除")
	@ApiOperation(value="课程表-批量删除", notes="课程表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zyfKcService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "课程表-通过id查询")
	@ApiOperation(value="课程表-通过id查询", notes="课程表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZyfKc zyfKc = zyfKcService.getById(id);
		if(zyfKc==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zyfKc);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zyfKc
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfKc zyfKc) {
        return super.exportXls(request, zyfKc, ZyfKc.class, "课程表");
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
        return super.importExcel(request, response, ZyfKc.class);
    }

}
