package com.example.afanguserbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS("0", "success"),//成功对象
    SYSTEM_ERROR("500", "system error"); //失败对象
//如果缺少状态可以继续添加
    private String code;
    private String message;


}
