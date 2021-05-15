package org.zyf.scholarship.score.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.zyf.scholarship.score.entity.ZyfScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 成绩表
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
public interface ZyfScoreMapper extends BaseMapper<ZyfScore> {

    void initView();

    IPage<Map<String, Object>>  queryPageList(IPage<Map<String, Object>> a, Map<String, String> query);

    /**
     * 综测班级成绩排名图表数据
     */
    List<Map<String, String>> queryBjScoreSortChart(List<Map<String, String>> a, Map<String, String> query);

    List<Map<String, Object>> queryBjKcScoreSortChart(List<Map<String, Object>> a,Map<String, String> query, List<String> bjIdList);

    List<Map<String, Object>> queryZcScoreList(List<Map<String, Object>> a, Map<String, String> query);

    IPage<Map<String, Object>> queryZcScorePageList(IPage<Map<String, Object>> a, Map<String, String> query);
}
