package com.seeker.locationtracker.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.hutool.core.date.DateUtil;
import java.util.Date;

/**
 * ResponseResult-通用返回结果信息类
 * 
 * @author Seeker Team
 * @date 2025-08-09
 */
@Data
@ApiModel("ResponseResult-通用返回结果信息")
public class ResponseResult<T> {
    public static final int STATUS_CODE_SUCCESS = 200;
    public static final int STATUS_CODE_FAIL = 501;

    public static final int OP_CODE_SUCCESS = 0;
    public static final int OP_CODE_FAIL = 1;

    @ApiModelProperty("接口操作返回码，200:成功,其他异常")
    private int statusCode;
    
    @ApiModelProperty("返回提示消息")
    private String opDesc;
    
    private int opCode = OP_CODE_SUCCESS;
    
    private String businessCode = "LOCATION_TRACKER";
    
    private String localTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

    @ApiModelProperty("返回的数据")
    private T data;

    /**
     * 判断是否是成功返回
     * 
     * @param <T> 泛型结果
     * @param retMap 返回结果结构体
     * @return boolean
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> boolean isSuccess(ResponseResult<T> retMap) {
        return retMap.getOpCode() == OP_CODE_SUCCESS ? true : false;
    }

    /**
     * 成功返回
     * 
     * @param <T> 泛型结果
     * @param msg 成功消息
     * @return ResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> ResponseResult<T> success(String msg) {
        ResponseResult<T> r = new ResponseResult<T>();
        r.setStatusCode(STATUS_CODE_SUCCESS);
        r.setOpDesc(msg);
        return r;
    }

    /**
     * 成功返回
     * 
     * @param <T> 泛型结果
     * @param data 返回数据
     * @return ResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> r = new ResponseResult<T>();
        r.setStatusCode(STATUS_CODE_SUCCESS);
        r.setOpDesc("成功");
        r.setData(data);
        return r;
    }

    /**
     * 成功返回
     * 
     * @param <T> 泛型结果
     * @param data 返回数据
     * @param msg 成功消息
     * @return ResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> ResponseResult<T> success(T data, String msg) {
        ResponseResult<T> r = new ResponseResult<T>();
        r.setStatusCode(STATUS_CODE_SUCCESS);
        r.setOpDesc(msg);
        r.setData(data);
        return r;
    }

    /**
     * 失败返回
     * 
     * @param <T> 泛型结果
     * @param msg 错误消息
     * @return ResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> ResponseResult<T> fail(String msg) {
        ResponseResult<T> r = new ResponseResult<T>();
        r.setStatusCode(STATUS_CODE_FAIL);
        r.setOpCode(OP_CODE_FAIL);
        r.setOpDesc(msg);
        return r;
    }

    /**
     * 失败返回
     * 
     * @param <T> 泛型实体
     * @param statusCode 业务状态码
     * @param msg 错误消息
     * @return ResponseResult<T>
     * @author Seeker Team
     * @date 2025-08-09
     */
    public static <T> ResponseResult<T> fail(int statusCode, String msg) {
        ResponseResult<T> r = new ResponseResult<T>();
        r.setStatusCode(statusCode);
        r.setOpCode(OP_CODE_FAIL);
        r.setOpDesc(msg);
        return r;
    }
}
