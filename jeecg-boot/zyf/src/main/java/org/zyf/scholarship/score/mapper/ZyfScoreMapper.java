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
    IPage<Map<String, Object>> queryPageList(IPage<Map<String, Object>> a, Map<String, String> query);

    /**
     * 班级成绩排名图表数据
     */
    List<Map<String, String>> queryBjScoreSortChart(List<Map<String, String>> a, Map<String, String> query);
}
