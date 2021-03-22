package org.zyf.scholarship.score.service;

import org.zyf.scholarship.score.entity.ZyfScore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 成绩表
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
public interface IZyfScoreService extends IService<ZyfScore> {
    List<Map<String, Object>> queryZcScoreList(Map<String, String> query);
}
