package com.example.afanguserbackend.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应类
 *
 * @param <T>
 */
@Data
public class PageResponse<T> implements Serializable {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页号
     */
    private long pageNum;

    /**
     * 每页大小
     */
    private long pageSize;

    /**
     * 总页数
     */
    private long pages;
}
