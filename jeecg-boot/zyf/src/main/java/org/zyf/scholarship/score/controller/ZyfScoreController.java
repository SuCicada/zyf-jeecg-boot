package org.zyf.scholarship.score.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.zyf.scholarship.bj.entity.ZyfBj;
import org.zyf.scholarship.bj.service.IZyfBjService;
import org.zyf.scholarship.score.entity.ZyfScore;
import org.zyf.scholarship.score.mapper.ZyfScoreMapper;
import org.zyf.scholarship.score.service.IZyfScoreService;

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
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.utils.service.UtilService;

/**
 * @Description: 成绩表
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Api(tags = "成绩表")
@RestController
@RequestMapping("/score/zyfScore")
@Slf4j
public class ZyfScoreController extends JeecgController<ZyfScore, IZyfScoreService> {
    @Autowired
    private IZyfScoreService zyfScoreService;
    @Resource
    private ZyfScoreMapper zyfZcScoreMapper;
    @Autowired
    private UtilService utilService;
    @Autowired
    private IZyfBjService zyfBjService;

    /**
     * 分页列表查询
     *
     * @param zyfScore
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "成绩表-分页列表查询")
    @ApiOperation(value = "成绩表-分页列表查询", notes = "成绩表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ZyfScore zyfScore,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//		QueryWrapper<ZyfScore> queryWrapper = QueryGenerator.initQueryWrapper(zyfScore, req.getParameterMap());
//		Page<ZyfScore> page = new Page<ZyfScore>(pageNo, pageSize);
//		IPage<ZyfScore> pageList = zyfScoreService.page(page, queryWrapper);
        IPage<Map<String, Object>> pageList = new Page<>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Util.Role role = utilService.who(loginUser.getId());
        if (role == Util.Role.TEACHER) {
            map.put("zyId", loginUser.getOrgCode());
        } else if (role == Util.Role.STUDENT) {
            map.put("bjId", loginUser.getOrgCode());
            map.put("studentId", loginUser.getId());
        }
        pageList = zyfZcScoreMapper.queryPageList(pageList, map);

        return Result.OK(pageList);
    }

    @AutoLog(value = "综测成绩表-分页列表查询")
    @ApiOperation(value = "综测成绩表-分页列表查询", notes = "成绩表-分页列表查询")
    @GetMapping(value = "/queryZcScorePageList")
    public Result<?> queryZcScorePageList(ZyfScore zyfScore,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {
//		QueryWrapper<ZyfScore> queryWrapper = QueryGenerator.initQueryWrapper(zyfScore, req.getParameterMap());
//		Page<ZyfScore> page = new Page<ZyfScore>(pageNo, pageSize);
//		IPage<ZyfScore> pageList = zyfScoreService.page(page, queryWrapper);
        IPage<Map<String, Object>> pageList = new Page<>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Util.Role role = utilService.who(loginUser.getId());
        if (role == Util.Role.TEACHER) {
            map.put("zyId", loginUser.getOrgCode());
        } else if (role == Util.Role.STUDENT) {
            String bjId = loginUser.getOrgCode();
            ZyfBj zyfBj = zyfBjService.getById(bjId);
            map.put("zyId", zyfBj.getZyId());
            map.put("njId", zyfBj.getNjId());
        }
        pageList = zyfZcScoreMapper.queryZcScorePageList(pageList, map);

        return Result.OK(pageList);
    }

    /**
     * 班级成绩排名图表数据
     */
    @RequestMapping(value = "/queryBjScoreSortChart", method = RequestMethod.GET)
    public Result<List<Map<String, String>>> queryBjScoreSortChart(HttpServletRequest req) {

        Result<List<Map<String, String>>> result = new Result<>();
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = Util.getParamMap(req);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Util.Role role = utilService.who(loginUser.getId());
        if (role == Util.Role.TEACHER) {
            map.put("zyId", loginUser.getOrgCode());
        }
        zyfZcScoreMapper.initView();
        list = zyfZcScoreMapper.queryBjScoreSortChart(list, map);

        if (list == null || list.size() <= 0) {
            result.error500("未找到信息");
        } else {
            result.setResult(list);
            result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping(value = "/queryBjKcScoreSortChart", method = RequestMethod.GET)
    public Result queryBjKcScoreSortChart(HttpServletRequest req) {

        Result<Map<String, Object>> result = new Result<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, String> map = Util.getParamMap(req);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Util.Role role = utilService.who(loginUser.getId());
        if (role == Util.Role.TEACHER) {
            map.put("zyId", loginUser.getOrgCode());
        }
        zyfZcScoreMapper.initView();
        List<String> bjIdList =
            map.entrySet().stream()
                .filter(a -> a.getKey().startsWith("bjId"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        list = zyfZcScoreMapper.queryBjKcScoreSortChart(list, map, bjIdList);

        Set<String> fields = new HashSet();
        Map<String, Map<String, Object>> temp = new HashMap();
        list.forEach(aa -> {
            String bjName = (String) aa.get("bjName");
            Map<String, Object> m = temp.computeIfAbsent(bjName, k -> new HashMap<>());
            String kcName = (String) aa.get("kcName");
            double a = (double) aa.get("score");
            m.put("type", bjName);
            m.put(kcName, a);
            fields.add(kcName);
        });
        List<Map<String, Object>> dataSource = new ArrayList<>(temp.values());
        Map<String, Object> res = new HashMap<>();
        res.put("dataSource", dataSource);
        res.put("fields", new ArrayList<>(fields));

        if (list == null || list.size() <= 0) {
            result.error500("未找到信息");
        } else {
            result.setResult(res);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 添加
     *
     * @param zyfScore
     * @return
     */
    @AutoLog(value = "成绩表-添加")
    @ApiOperation(value = "成绩表-添加", notes = "成绩表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ZyfScore zyfScore) {
        zyfScoreService.save(zyfScore);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param zyfScore
     * @return
     */
    @AutoLog(value = "成绩表-编辑")
    @ApiOperation(value = "成绩表-编辑", notes = "成绩表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ZyfScore zyfScore) {
        zyfScoreService.updateById(zyfScore);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "成绩表-通过id删除")
    @ApiOperation(value = "成绩表-通过id删除", notes = "成绩表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        zyfScoreService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "成绩表-批量删除")
    @ApiOperation(value = "成绩表-批量删除", notes = "成绩表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zyfScoreService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "成绩表-通过id查询")
    @ApiOperation(value = "成绩表-通过id查询", notes = "成绩表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ZyfScore zyfScore = zyfScoreService.getById(id);
        if (zyfScore == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zyfScore);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zyfScore
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfScore zyfScore) {
        return super.exportXls(request, zyfScore, ZyfScore.class, "成绩表");
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
        return super.importExcel(request, response, ZyfScore.class);
    }

}
