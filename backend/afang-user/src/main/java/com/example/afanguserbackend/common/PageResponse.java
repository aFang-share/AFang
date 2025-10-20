package com.example.afanguserbackend.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应封装类
 * 用于封装分页查询的响应结果，包含数据列表和分页信息
 *
 * @author AFang Team
 * @version 1.0
 * @since 2024-01-01
 * @param <T> 响应数据列表的泛型类型
 */
@Data
public class PageResponse<T> implements Serializable {

    /**
     * 数据列表
     * 当前页的数据记录集合
     */
    private List<T> records;

    /**
     * 总记录数
     * 符合查询条件的记录总数
     */
    private long total;

    /**
     * 当前页号
     * 当前页的页码，从1开始
     */
    private long pageNum;

    /**
     * 每页大小
     * 每页显示的记录数量
     */
    private long pageSize;

    /**
     * 总页数
     * 根据总记录数和每页大小计算得出的总页数
     */
    private long pages;
}
