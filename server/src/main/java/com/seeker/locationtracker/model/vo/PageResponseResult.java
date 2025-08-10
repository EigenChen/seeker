package com.seeker.locationtracker.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.hutool.core.date.DateUtil;
import java.util.Date;
import java.util.List;

/**
 * PageResponseResult - 分页接口响应实体类
 * 
 * @author Seeker Team
 * @date 2025-08-09
 */
@Data
public class PageResponseResult<T> {

    @ApiModelProperty("接口状态码，200：正常")
    @JsonProperty(value = "StatusCode")
    private Integer statusCode;

    @ApiModelProperty("模块代码")
    @JsonProperty(value = "BusinessCode")
    private String businessCode;

    @ApiModelProperty("业务操作代码，0:成功,1:失败,>1:异常")
    @JsonProperty(value = "OpCode")
    private int opCode;

    @ApiModelProperty("业务操作描述")
    @JsonProperty(value = "OpDesc")
    private String opDesc;

    @ApiModelProperty("本次搜索中的页码")
    @JsonProperty(value = "PageNum")
    private Integer pageNum;

    @ApiModelProperty("本次搜索每页大小")
    @JsonProperty(value = "PageSize")
    private Integer pageSize;

    @ApiModelProperty("本次搜索中的总页数")
    @JsonProperty(value = "Pages")
    private Integer pages;

    @ApiModelProperty("总记录数大小")
    @JsonProperty(value = "Total")
    private Long total;

    @ApiModelProperty("时间戳")
    @JsonProperty(value = "LocalTime")
    private String localTime;

    @ApiModelProperty("分页查询当前页数据列表")
    @JsonProperty(value = "Data")
    private List<T> data;

    @ApiModelProperty("用于分页查询的标识id")
    private String scrollId;

    /**
     * 成功返回
     * 
     * @param <T> 泛型结果
     * @param msg 成功消息
     * @return PageResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> PageResponseResult<T> success(String msg) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(ResponseResult.STATUS_CODE_SUCCESS);
        r.setOpCode(ResponseResult.OP_CODE_SUCCESS);
        r.setOpDesc(msg);
        r.setBusinessCode("LOCATION_TRACKER");
        r.setLocalTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return r;
    }

    /**
     * 成功返回
     * 
     * @param <T> 泛型结果
     * @param data 返回数据
     * @return PageResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> PageResponseResult<T> success(List<T> data) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(ResponseResult.STATUS_CODE_SUCCESS);
        r.setOpCode(ResponseResult.OP_CODE_SUCCESS);
        r.setOpDesc("成功");
        r.setData(data);
        r.setBusinessCode("LOCATION_TRACKER");
        r.setLocalTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return r;
    }

    /**
     * 成功返回 - MyBatis-Plus IPage
     * 
     * @param <T> 泛型结果
     * @param data 分页数据
     * @return PageResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> PageResponseResult<T> success(IPage<T> data) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setTotal(data.getTotal());
        r.setData(data.getRecords());
        r.setPages((int) data.getPages());
        r.setPageNum((int) data.getCurrent());
        r.setPageSize((int) data.getSize());
        r.setStatusCode(ResponseResult.STATUS_CODE_SUCCESS);
        r.setOpCode(ResponseResult.OP_CODE_SUCCESS);
        r.setOpDesc("成功");
        r.setBusinessCode("LOCATION_TRACKER");
        r.setLocalTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return r;
    }

    /**
     * 失败返回
     * 
     * @param <T> 泛型结果
     * @param msg 错误消息
     * @return PageResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> PageResponseResult<T> fail(String msg) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(ResponseResult.STATUS_CODE_FAIL);
        r.setOpCode(ResponseResult.OP_CODE_FAIL);
        r.setOpDesc(msg);
        r.setBusinessCode("LOCATION_TRACKER");
        r.setLocalTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return r;
    }

    /**
     * 失败返回
     * 
     * @param <T> 泛型实体
     * @param statusCode 业务状态码
     * @param msg 错误消息
     * @return PageResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> PageResponseResult<T> fail(int statusCode, String msg) {
        PageResponseResult<T> r = new PageResponseResult<T>();
        r.setStatusCode(statusCode);
        r.setOpCode(ResponseResult.OP_CODE_FAIL);
        r.setOpDesc(msg);
        r.setBusinessCode("LOCATION_TRACKER");
        r.setLocalTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return r;
    }
}
