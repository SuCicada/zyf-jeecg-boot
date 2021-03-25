package org.zyf.scholarship.zxj_app.entity;

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
 * @Description: 助学金申请
 * @Author: jeecg-boot
 * @Date:   2021-03-03
 * @Version: V1.0
 */
@Data
@TableName("zyf_zxj_app")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="zyf_zxj_app对象", description="助学金申请")
public class ZyfZxjApp implements Serializable {
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
	/**学生id*/
	@Excel(name = "学生id", width = 15)
    @ApiModelProperty(value = "学生id")
    private String studentId;
	/**助学金id*/
	@Excel(name = "助学金id", width = 15)
    @ApiModelProperty(value = "助学金id")
    private String zxjId;
	/**辅导员审核*/
	@Excel(name = "辅导员审核", width = 15)
    @ApiModelProperty(value = "辅导员审核")
    private Integer teacherCheck;
	/**教务处审核*/
	@Excel(name = "教务处审核", width = 15)
    @ApiModelProperty(value = "教务处审核")
    private Integer officeCheck;
	/**是否发放*/
	@Excel(name = "是否发放", width = 15)
    @ApiModelProperty(value = "是否发放")
    private Integer accepted;
}
