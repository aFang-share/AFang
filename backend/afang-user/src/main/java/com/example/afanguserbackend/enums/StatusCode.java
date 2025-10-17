package com.example.afanguserbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS("20000", "success"),//成功对象
    SYSTEM_ERROR("50000", "system error"), //失败对象
    //运行时异常错误代码
    RUN_ERROR("99999", "请稍后再试一试!");
    //如果缺少状态可以继续添加
    private final String code;
    private final String message;
}
