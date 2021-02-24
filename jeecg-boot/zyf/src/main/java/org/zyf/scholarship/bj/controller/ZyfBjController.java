package org.zyf.scholarship.bj.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.bj.entity.ZyfBj;
import org.zyf.scholarship.bj.mapper.ZyfBjMapper;
import org.zyf.scholarship.bj.service.IZyfBjService;

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
import org.zyf.scholarship.utils.service.UtilService;
import org.zyf.scholarship.zy.entity.ZyfZy;

/**
 * @Description: 班级表
 * @Author: jeecg-boot
 * @Date:   2021-02-14
 * @Version: V1.0
 */
@Api(tags="班级表")
@RestController
@RequestMapping("/bj/zyfBj")
@Slf4j
public class ZyfBjController extends JeecgController<ZyfBj, IZyfBjService> {
	@Autowired
	private IZyfBjService zyfBjService;
    @Resource
    private ZyfBjMapper zyfBjMapper;
    @Autowired
    private UtilService utilService;

    /**
	 * 分页列表查询
	 *
	 * @param zyfBj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "班级表-分页列表查询")
	@ApiOperation(value="班级表-分页列表查询", notes="班级表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZyfBj zyfBj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<ZyfBj> queryWrapper = QueryGenerator.initQueryWrapper(zyfBj, req.getParameterMap());
//		Page<ZyfBj> page = new Page<ZyfBj>(pageNo, pageSize);
//		IPage<ZyfBj> pageList = zyfBjService.page(page, queryWrapper);
        IPage<Map<String, String>> pageList = new Page<Map<String, String>>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);

        LoginUser loginUser = Util.getLoginUser();
        Util.Role role = utilService.who(loginUser.getId());
        // 这里很重要, 这里是老师权限必须要判断的专业
        if (role == Util.Role.TEACHER) {
            map.put("zyId", loginUser.getOrgCode());
        }

        pageList = zyfBjMapper.queryPageList(pageList,map);
		return Result.OK(pageList);
	}
    @RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public Result<List<ZyfBj>> queryall(ZyfBj zyfBj,
                                        HttpServletRequest req) {
        Result<List<ZyfBj>> result = new Result<>();

        LoginUser loginUser = Util.getLoginUser();

        QueryWrapper<ZyfBj> queryWrapper = QueryGenerator.initQueryWrapper(zyfBj, req.getParameterMap());
        Util.Role role = utilService.who(loginUser.getId());
        // 这里很重要, 这里是老师权限必须要判断的专业
        if (role == Util.Role.TEACHER) {
            queryWrapper.eq("zy_id", loginUser.getOrgCode());
        }

        List<ZyfBj> list = zyfBjService.list(queryWrapper);
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
	 * @param zyfBj
	 * @return
	 */
	@AutoLog(value = "班级表-添加")
	@ApiOperation(value="班级表-添加", notes="班级表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZyfBj zyfBj) {
		zyfBjService.save(zyfBj);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param zyfBj
	 * @return
	 */
	@AutoLog(value = "班级表-编辑")
	@ApiOperation(value="班级表-编辑", notes="班级表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZyfBj zyfBj) {
		zyfBjService.updateById(zyfBj);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班级表-通过id删除")
	@ApiOperation(value="班级表-通过id删除", notes="班级表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zyfBjService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "班级表-批量删除")
	@ApiOperation(value="班级表-批量删除", notes="班级表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zyfBjService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班级表-通过id查询")
	@ApiOperation(value="班级表-通过id查询", notes="班级表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZyfBj zyfBj = zyfBjService.getById(id);
		if(zyfBj==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zyfBj);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zyfBj
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfBj zyfBj) {
        return super.exportXls(request, zyfBj, ZyfBj.class, "班级表");
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
        return super.importExcel(request, response, ZyfBj.class);
    }

}
