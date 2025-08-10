package com.seeker.locationtracker.config;

import com.seeker.locationtracker.model.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 全局异常处理配置类
 * 
 * @author Seeker Team
 * @date 2025-08-09
 */
@RestControllerAdvice
@Slf4j
@ResponseBody
public class ErrorHandleConfig {

    /**
     * 运行时异常处理
     * 
     * @param ex 运行时异常
     * @return 错误消息
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<String> runtimeExceptionHandler(RuntimeException ex) {
        return resultFormat(ErrorCode.RuntimeException.getCode(), ex);
    }

    /**
     * 空指针异常处理
     * 
     * @param ex 空指针异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseResult<String> nullPointerExceptionHandler(NullPointerException ex) {
        return resultFormat(ErrorCode.NullPointerException.getCode(), ex);
    }

    /**
     * 类型转换异常处理
     * 
     * @param ex 类型转换异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler(ClassCastException.class)
    public ResponseResult<String> classCastExceptionHandler(ClassCastException ex) {
        return resultFormat(ErrorCode.ClassCastException.getCode(), ex);
    }

    /**
     * IO异常处理
     * 
     * @param ex IO异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler(IOException.class)
    public ResponseResult<String> iOExceptionHandler(IOException ex) {
        return resultFormat(ErrorCode.IOException.getCode(), ex);
    }

    /**
     * 未知方法异常处理
     * 
     * @param ex 未知方法异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler(NoSuchMethodException.class)
    public ResponseResult<String> noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return resultFormat(ErrorCode.NoSuchMethodException.getCode(), ex);
    }

    /**
     * 数组越界异常处理
     * 
     * @param ex 数组越界异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseResult<String> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return resultFormat(ErrorCode.IndexOutOfBoundsException.getCode(), ex);
    }

    /**
     * 400错误处理
     * 
     * @param ex 请求不可读异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseResult<String> requestNotReadable(HttpMessageNotReadableException ex) {
        return resultFormat(ErrorCode.HttpMessageNotReadableException.getCode(), ex);
    }

    /**
     * 400错误处理
     * 
     * @param ex 类型不匹配异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({TypeMismatchException.class})
    public ResponseResult<String> requestTypeMismatch(TypeMismatchException ex) {
        return resultFormat(ErrorCode.TypeMismatchException.getCode(), ex);
    }

    /**
     * 400错误处理
     * 
     * @param ex 缺少请求参数异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseResult<String> requestMissingServletRequest(MissingServletRequestParameterException ex) {
        return resultFormat(ErrorCode.MissingServletRequestParameterException.getCode(), ex);
    }

    /**
     * 405错误处理
     * 
     * @param ex 请求方法不支持异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseResult<String> request405(HttpRequestMethodNotSupportedException ex) {
        return resultFormat(ErrorCode.HttpRequestMethodNotSupportedException.getCode(), ex);
    }

    /**
     * 406错误处理
     * 
     * @param ex 媒体类型不接受异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public ResponseResult<String> request406(HttpMediaTypeNotAcceptableException ex) {
        return resultFormat(ErrorCode.HttpMediaTypeNotAcceptableException.getCode(), ex);
    }

    /**
     * 500错误处理
     * 
     * @param ex 服务器内部错误
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    public ResponseResult<String> server500(RuntimeException ex) {
        return resultFormat(ErrorCode.ConversionNotSupportedException.getCode(), ex);
    }

    /**
     * 栈溢出错误处理
     * 
     * @param ex 栈溢出错误
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({StackOverflowError.class})
    public ResponseResult<String> requestStackOverflow(StackOverflowError ex) {
        return resultFormat(ErrorCode.StackOverflowError.getCode(), ex);
    }

    /**
     * 算术异常处理
     * 
     * @param ex 算术异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({ArithmeticException.class})
    public ResponseResult<String> arithmeticException(ArithmeticException ex) {
        return resultFormat(ErrorCode.ArithmeticException.getCode(), ex);
    }

    /**
     * 参数验证错误处理
     * 
     * @param ex 参数验证错误异常
     * @return ResponseResult<String>
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseResult<String> handleBindException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder("");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            if (sb.length() == 0) {
                sb.append(error.getDefaultMessage());
            } else {
                sb.append(", ").append(error.getDefaultMessage());
            }
        }
        ResponseResult<String> ret = ResponseResult.fail(sb.toString());
        ret.setStatusCode(ErrorCode.MethodArgumentNotValidException.getCode());
        return ret;
    }

    /**
     * 其他异常处理
     * 
     * @param ex 其他异常
     * @return ResponseResult<String> 错误结果
     * @author Seeker Team
     * @date 2025-08-09
     */
    @ExceptionHandler({Exception.class})
    public ResponseResult<String> exception(Exception ex) {
        return resultFormat(ErrorCode.Exception.getCode(), ex);
    }

    /**
     * 异常封装
     * 
     * @param <T> 异常类型
     * @param code 异常编码
     * @param ex 异常类
     * @return ResponseResult<String>
     * @author Seeker Team
     * @date 2025-08-09
     */
    private <T extends Throwable> ResponseResult<String> resultFormat(Integer code, T ex) {
        ex.printStackTrace();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            log.debug(stackTrace[i].getClassName() + "." + stackTrace[i].getMethodName() + ":"
                    + stackTrace[i].getLineNumber());
        }
        ResponseResult<String> ret = ResponseResult.fail(ex.getMessage());
        ret.setStatusCode(code);
        return ret;
    }

    /**
     * 异常码定义
     * 
     * @author Seeker Team
     * @date 2025-08-09
     */
    enum ErrorCode {
        RuntimeException(502), 
        NullPointerException(503), 
        ClassCastException(504), 
        IOException(505),
        NoSuchMethodException(506), 
        IndexOutOfBoundsException(507), 
        HttpMessageNotReadableException(508),
        TypeMismatchException(509), 
        MissingServletRequestParameterException(510),
        HttpRequestMethodNotSupportedException(511), 
        HttpMediaTypeNotAcceptableException(512),
        ConversionNotSupportedException(513), 
        StackOverflowError(514), 
        ArithmeticException(515),
        MethodArgumentNotValidException(516), 
        Exception(517);

        int code;

        ErrorCode(int code) {
            this.code = code;
        }

        int getCode() {
            return code;
        }
    }
}
