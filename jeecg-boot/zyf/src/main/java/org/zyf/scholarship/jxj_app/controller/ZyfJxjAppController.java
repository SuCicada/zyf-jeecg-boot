package org.zyf.scholarship.jxj_app.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.zyf.scholarship.jxj_app.entity.ZyfJxjApp;
import org.zyf.scholarship.jxj_app.mapper.ZyfJxjAppMapper;
import org.zyf.scholarship.jxj_app.service.IZyfJxjAppService;

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
import org.zyf.scholarship.score.mapper.ZyfScoreMapper;
import org.zyf.scholarship.score.service.IZyfScoreService;
import org.zyf.scholarship.utils.Util;
import org.zyf.scholarship.utils.service.UtilService;

/**
 * @Description: 奖学金申请
 * @Author: jeecg-boot
 * @Date: 2021-03-03
 * @Version: V1.0
 */
@Api(tags = "奖学金申请")
@RestController
@RequestMapping("/jxj_app/zyfJxjApp")
@Slf4j
public class ZyfJxjAppController extends JeecgController<ZyfJxjApp, IZyfJxjAppService> {
    @Autowired
    private IZyfJxjAppService zyfJxjAppService;
    @Resource
    private ZyfJxjAppMapper zyfZcJxjAppMapper;
    @Autowired
    private UtilService utilService;
    @Autowired
    private IZyfScoreService zyfScoreService;
    @Resource
    private ZyfScoreMapper zyfScoreMapper;

    /**
     * 分页列表查询
     *
     * @param zyfJxjApp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "奖学金申请-分页列表查询")
    @ApiOperation(value = "奖学金申请-分页列表查询", notes = "奖学金申请-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ZyfJxjApp zyfJxjApp,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
//		QueryWrapper<ZyfJxjApp> queryWrapper = QueryGenerator.initQueryWrapper(zyfJxjApp, req.getParameterMap());
//		Page<ZyfJxjApp> page = new Page<ZyfJxjApp>(pageNo, pageSize);
//		IPage<ZyfJxjApp> pageList = zyfJxjAppService.page(page, queryWrapper);
        IPage<Map<String, Object>> pageList = new Page<>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Util.Role role = utilService.who(loginUser.getId());
        if (role == Util.Role.TEACHER) {
            map.put("zyId", loginUser.getOrgCode());
        } else if (role == Util.Role.STUDENT) {
            map.put("studentId", loginUser.getId());
        }
        map.put("role", role.name());
        pageList = zyfZcJxjAppMapper.queryPageList(pageList, map);

        return Result.OK(pageList);
    }

    @AutoLog(value = "奖学金申请-分页列表查询")
    @ApiOperation(value = "奖学金申请-分页列表查询", notes = "奖学金申请-分页列表查询")
    @GetMapping(value = "/listAccepted")
    public Result listAccepted(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                               HttpServletRequest req) {
        IPage<Map<String, Object>> pageList = new Page<>(pageNo, pageSize);
        Map<String, String> map = Util.getParamMap(req);
        map.put("teacherCheck", "1");
        map.put("officeCheck", "1");
        pageList = zyfZcJxjAppMapper.queryPageList(pageList, map);
        return Result.OK(pageList);
    }

    /**
     * @return
     */
    @RequestMapping(value = "/autoInitAccepted", method = RequestMethod.GET)
    public Result autoInitAccepted() {
        /* 待分发的学生
         *   先得到每个学生的成绩
         * 然后按照奖学金类型分组: all / zy:nj,
         * 然后对每组排序, 按照奖学金的numRate 进行list分割
         * groupId:  all / zy,$zy,$nj
         *  (groupId) -> {
         *          num  // 当前这个分组总共要多少人
         *          appList: Map(appId,realScore)
         * */
        List<Map<String, Object>> acceptedList = new ArrayList<>();
        acceptedList = zyfZcJxjAppMapper.queryAccepted(acceptedList, null);
        Map<String, Map<String, Object>> acceptedGroup = new HashMap<>();
        /* 这些是制作 acceptedGroup 的*/
        acceptedList.forEach(accepted -> {
            String studentId = (String) accepted.get("studentId");
//            String jxjId = accepted.get("jxjId");
            String appId = (String) accepted.get("id");
            double realScore = getStudentRealScore(studentId);
            int sortType = (int) accepted.get("sortType");
            String groupId;
            Map query = new HashMap();
            if (sortType == 1) {
                groupId = "all";
            } else {
                String zyId = (String) accepted.get("zyId");
                String njId = (String) accepted.get("njId");
                groupId = String.format("zy,%s,%s", zyId, njId);
                query.put("zyId", zyId);
                query.put("njId", njId);
            }
            //            double rate = getStudentRank(studentId, sortType);
            if (acceptedGroup.get(groupId) == null) {
                query.put("sortType", sortType);
                int sum = getGroupSum(query);

                double numRate = (double) accepted.get("numRate");
                int num = (int) Math.ceil(numRate * sum);

                Map map = new HashMap<>();
                map.put("num", num);
                map.put("appList", new HashMap<>());
                acceptedGroup.put(groupId, map);
            }
            Map<String, Double> appList = (Map<String, Double>) acceptedGroup.get(groupId).get("appList");
            appList.put(appId, realScore);
        });

        /* 通过的申请 */
        List<String> acceptedApp = new ArrayList();
        for (Map.Entry<String, Map<String, Object>> entry : acceptedGroup.entrySet()) {
            int num = (int) entry.getValue().get("num"); /* 能发的人数 */
            Map<String, Double> appMap = (Map<String, Double>) entry.getValue().get("appList");
            //这里将map.entrySet()转换成list
            List<Map.Entry<String, Double>> appList = new ArrayList<>(appMap.entrySet());
            //然后通过比较器来实现排序
            appList.sort((o1, o2) -> (int) -(o1.getValue() - o2.getValue()));
            acceptedApp.addAll(appList.stream()
                .map(Map.Entry::getKey).collect(Collectors.toList())
                .subList(0, num));
        }

        zyfZcJxjAppMapper.autoInitAccepted(acceptedApp);

        /* 综测真实成绩 */

        /* 要查看学生排名 */

        return Result.OK();
    }

    public double getStudentRealScore(String studentId) {
        Map<String, String> query = new HashMap<>();
        query.put("studentId", studentId);
        List<Map<String, Object>> zcScoreList = zyfScoreService.queryZcScoreList(query);
//        List<Map<String, Object>> zcScoreList = new ArrayList<>();
//        zcScoreList = zyfScoreMapper.queryZcScoreList(zcScoreList, query);
        return (Double) (zcScoreList.get(0).get("realScore"));
    }

    public int getGroupSum(Map query) {
//        List<Map<String, Object>> zcScoreList = new ArrayList<>();
//        zcScoreList = zyfScoreMapper.queryZcScoreList(zcScoreList, query);
        List<Map<String, Object>> zcScoreList = zyfScoreService.queryZcScoreList(query);
        return zcScoreList.size();
    }

    public double getStudentRank(int sortType, Map query1) {
        /* 1全校/ 2专业
         *  排名只针对当前年级 */
        Map<String, Object> res = new HashMap<>();
        if (sortType == 1) {

        } else if (sortType == 2) {
            query1.put("sortType", "2");
        }
        List<Map<String, String>> zcScoreList = zyfScoreService.queryZcScoreList(query1);
        /* 找是排名的第几个 */
        double rank = 1;
        for (Map<String, String> realScore : zcScoreList) {
//            if (realScore.get("studentId").equals(studentId)) {
//                break;
//            }
            rank++;
        }
        double all = zcScoreList.size();
        double rate = rank / all;
        res.put("rank", rank);
        res.put("all", all);
        res.put("rate", rate);
//        return res;
        return rate;
    }

    /**
     * 添加
     *
     * @param zyfJxjApp
     * @return
     */
    @AutoLog(value = "奖学金申请-添加")
    @ApiOperation(value = "奖学金申请-添加", notes = "奖学金申请-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ZyfJxjApp zyfJxjApp) {
        zyfJxjAppService.save(zyfJxjApp);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param zyfJxjApp
     * @return
     */
    @AutoLog(value = "奖学金申请-编辑")
    @ApiOperation(value = "奖学金申请-编辑", notes = "奖学金申请-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ZyfJxjApp zyfJxjApp) {
        zyfJxjAppService.updateById(zyfJxjApp);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "奖学金申请-通过id删除")
    @ApiOperation(value = "奖学金申请-通过id删除", notes = "奖学金申请-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        zyfJxjAppService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "奖学金申请-批量删除")
    @ApiOperation(value = "奖学金申请-批量删除", notes = "奖学金申请-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.zyfJxjAppService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "奖学金申请-通过id查询")
    @ApiOperation(value = "奖学金申请-通过id查询", notes = "奖学金申请-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ZyfJxjApp zyfJxjApp = zyfJxjAppService.getById(id);
        if (zyfJxjApp == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(zyfJxjApp);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param zyfJxjApp
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZyfJxjApp zyfJxjApp) {
        return super.exportXls(request, zyfJxjApp, ZyfJxjApp.class, "奖学金申请");
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
        return super.importExcel(request, response, ZyfJxjApp.class);
    }

}
