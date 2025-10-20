package com.example.afanguserbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求封装类
 * 用于封装删除操作的请求参数，统一删除接口的输入格式
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * 要删除的记录ID
     * 支持Long类型的自增主键
     */
    private Long id;

    /**
     * 序列化版本号
     * 用于Serializable接口的版本控制
     */
    private static final long serialVersionUID = 1L;
}