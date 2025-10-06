package com.example.afanguserbackend.common;

import lombok.Data;

/**
 * 请求封装类，请求参数类（入参），用于接收前端传来的分页参数
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */

    private Integer pageNum;  // 使用 Integer 而不是 int


    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";




}
