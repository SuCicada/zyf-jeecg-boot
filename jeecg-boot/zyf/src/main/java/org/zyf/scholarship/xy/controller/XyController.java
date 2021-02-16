package org.zyf.scholarship.xy.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.zyf.scholarship.xy.entity.Xy;
import org.zyf.scholarship.xy.service.IXyService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @Description: 学院表
 * @Author: jeecg-boot
 * @Date:   2021-02-12
 * @Version: V1.0
 */
@Api(tags="学院表")
@RestController
@RequestMapping("/xy/zyfXy")
@Slf4j
public class XyController extends JeecgController<Xy, IXyService> {
	@Autowired
	private IXyService xyService;

	/**
	 * 分页列表查询
	 *
	 * @param xy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "学院表-分页列表查询")
	@ApiOperation(value="学院表-分页列表查询", notes="学院表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Xy xy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Xy> queryWrapper = QueryGenerator.initQueryWrapper(xy, req.getParameterMap());
		Page<Xy> page = new Page<Xy>(pageNo, pageSize);
		IPage<Xy> pageList = xyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

     @RequestMapping(value = "/queryall", method = RequestMethod.GET)
     public Result<List<Xy>> queryall() {
         Result<List<Xy>> result = new Result<>();
         List<Xy> list = xyService.list();
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
	 * @param xy
	 * @return
	 */
	@AutoLog(value = "学院表-添加")
	@ApiOperation(value="学院表-添加", notes="学院表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Xy xy) {
		xyService.save(xy);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param xy
	 * @return
	 */
	@AutoLog(value = "学院表-编辑")
	@ApiOperation(value="学院表-编辑", notes="学院表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Xy xy) {
		xyService.updateById(xy);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学院表-通过id删除")
	@ApiOperation(value="学院表-通过id删除", notes="学院表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		xyService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学院表-批量删除")
	@ApiOperation(value="学院表-批量删除", notes="学院表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.xyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学院表-通过id查询")
	@ApiOperation(value="学院表-通过id查询", notes="学院表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Xy xy = xyService.getById(id);
		if(xy==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(xy);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param xy
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Xy xy) {
        return super.exportXls(request, xy, Xy.class, "学院表");
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
        return super.importExcel(request, response, Xy.class);
    }

}
