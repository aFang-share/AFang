package com.example.afanguserbackend.common;

import lombok.Data;

/**
 * 分页请求参数封装类
 * 用于接收前端传来的分页查询参数，支持排序功能
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     * 从1开始计数，使用Integer类型避免空指针异常
     */
    private Integer pageNum;

    /**
     * 页面大小
     * 每页显示的记录数量
     */
    private Integer pageSize;

    /**
     * 排序字段
     * 指定用于排序的字段名
     */
    private String sortField;

    /**
     * 排序顺序
     * ascend: 升序，descend: 降序，默认为降序
     */
    private String sortOrder = "descend";
}
