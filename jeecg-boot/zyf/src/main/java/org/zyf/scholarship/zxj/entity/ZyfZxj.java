package org.zyf.scholarship.zxj.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 助学金
 * @Author: jeecg-boot
 * @Date:   2021-02-23
 * @Version: V1.0
 */
@Data
@TableName("zyf_zxj")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zyf_zxj对象", description="助学金")
public class ZyfZxj implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String zxjName;
	/**排名方式*/
	@Excel(name = "排名方式", width = 15)
    @ApiModelProperty(value = "排名方式")
    @Dict(dicCode = "sortType")
    private Integer sortType;
	/**人数百分比*/
	@Excel(name = "人数百分比", width = 15)
    @ApiModelProperty(value = "人数百分比")
    private Double numRate;
	/**详情文件*/
	@Excel(name = "详情文件", width = 15)
    @ApiModelProperty(value = "详情文件")
    private String fileList;
}
