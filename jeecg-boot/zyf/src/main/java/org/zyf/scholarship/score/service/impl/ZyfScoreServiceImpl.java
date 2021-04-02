package org.zyf.scholarship.score.service.impl;

import org.zyf.scholarship.score.entity.ZyfScore;
import org.zyf.scholarship.score.mapper.ZyfScoreMapper;
import org.zyf.scholarship.score.service.IZyfScoreService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 成绩表
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Service
public class ZyfScoreServiceImpl extends ServiceImpl<ZyfScoreMapper, ZyfScore> implements IZyfScoreService {
    @Resource
    private ZyfScoreMapper zyfZcScoreMapper;

    @Override
    public List<Map<String, Object>> queryZcScoreList(Map<String, String> query) {
        List<Map<String, Object>> zcScoreList = new ArrayList<>();
        zyfZcScoreMapper.initView();
        zcScoreList = zyfZcScoreMapper.queryZcScoreList(zcScoreList, query);
//        zcScoreList.forEach(row -> {
//            double zcScore = Double.parseDouble(row.get("zcScore"));
//            if (zcScore < 21) {
//                zcScore = 21;
//            } else if (zcScore > 35) {
//                zcScore = 35;
//            }
//            double realScore = Double.parseDouble(row.get("avgScore")) * 0.65 + zcScore;
//            row.put("realScore", Double.toString(realScore));
//        });

        return zcScoreList;
    }
}
