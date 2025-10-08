package com.example.afanguserbackend.exception.service;
import com.example.afanguserbackend.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常注解
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    //处理异常
    public BaseResponse<String> handleException(Exception e) {
        //返回错误信息
        log.error("系统异常",e);
        return new BaseResponse<>("500","系统内部异常", e.getMessage());
    }
}
//网络IO
//数据库
//业务逻辑
//其他
//磁盘
